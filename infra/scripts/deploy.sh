#!/bin/bash

# KeysBank Deploy Script
# Suporta deploy local (Docker Compose) ou AWS (Terraform + EC2)
# Uso: ./deploy.sh local|aws

set -e

# Cores para output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m'

log_info() { echo -e "${BLUE}[INFO]${NC} $1"; }
log_success() { echo -e "${GREEN}[SUCCESS]${NC} $1"; }
log_error() { echo -e "${RED}[ERROR]${NC} $1"; }
log_warning() { echo -e "${YELLOW}[WARNING]${NC} $1"; }

# Detectar diretório raiz do projeto
PROJECT_ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/../.." && pwd)"
DEPLOY_TYPE=${1:-}

# Validar argumento
if [ -z "$DEPLOY_TYPE" ]; then
    echo ""
    echo "╔════════════════════════════════════════╗"
    echo "║     KeysBank - Deploy Script          ║"
    echo "╚════════════════════════════════════════╝"
    echo ""
    echo "Uso: $0 <tipo>"
    echo ""
    echo "Tipos de deploy:"
    echo "  local    - Deploy local com Docker Compose"
    echo "  aws      - Deploy na AWS (Terraform + EC2)"
    echo ""
    echo "Exemplo:"
    echo "  $0 local"
    echo "  $0 aws"
    echo ""
    exit 1
fi

# ============================================
# DEPLOY LOCAL (Docker Compose)
# ============================================
if [ "$DEPLOY_TYPE" = "local" ]; then
    log_info "════════════════════════════════════════════════════════════"
    log_info "   KeysBank - Deploy Local (Docker Compose)"
    log_info "════════════════════════════════════════════════════════════"

    # ============================================
    # 1. BUILD BACKEND
    # ============================================
    log_info "📦 [1/2] Building Backend..."
    cd "$PROJECT_ROOT/back-end"

    log_info "Compilando aplicação..."
    export JAVA_HOME=/opt/homebrew/opt/openjdk@17
    mvn clean package -DskipTests -q || {
        log_error "Build do backend falhou!"
        exit 1
    }

    if [ ! -f "target/keysbankapi-0.0.1-SNAPSHOT.jar" ]; then
        log_error "JAR do backend não foi gerado!"
        exit 1
    fi

    log_success "Backend compilado: $(du -h target/keysbankapi-0.0.1-SNAPSHOT.jar | cut -f1)"

    # ============================================
    # 2. START DOCKER COMPOSE
    # ============================================
    log_info "📦 [2/2] Iniciando Docker Compose..."
    cd "$PROJECT_ROOT/infra"

    # Verificar Docker
    if ! command -v docker-compose &> /dev/null; then
        log_error "Docker Compose não está instalado!"
        exit 1
    fi

    log_info "Parando containers antigos..."
    docker-compose down || true
    
    log_info "Iniciando containers..."
    docker-compose up -d --build

    if [ $? -eq 0 ]; then
        log_success "Deploy local concluído!"
        echo ""
        echo "Serviços disponíveis:"
        echo "  • Backend:   http://localhost:8080"
        echo "  • Swagger:   http://localhost:8080/swagger-ui.html"
        echo "  • Frontend:  http://localhost:3000"
        echo "  • Database:  localhost:5432"
        echo ""
        echo "Comando para acompanhar logs:"
        echo "  docker-compose logs -f"
        echo ""
        echo "Comando para parar:"
        echo "  docker-compose down"
        echo ""
    else
        log_error "Deploy local falhou!"
        exit 1
    fi

# ============================================
# DEPLOY AWS (Terraform + EC2)
# ============================================
elif [ "$DEPLOY_TYPE" = "aws" ]; then
    log_info "════════════════════════════════════════════════════════════"
    log_info "   KeysBank - Deploy AWS"
    log_info "════════════════════════════════════════════════════════════"

    # ============================================
    # 1. BUILD BACKEND
    # ============================================
    log_info "📦 [1/5] Building Backend..."
    cd "$PROJECT_ROOT/back-end"

    log_info "Executando testes..."
    mvn clean test -q || {
        log_error "Testes do backend falharam!"
        exit 1
    }

    log_info "Compilando aplicação..."
    export JAVA_HOME=/opt/homebrew/opt/openjdk@17
    mvn package -DskipTests -q || {
        log_error "Build do backend falhou!"
        exit 1
    }

    if [ ! -f "target/keysbankapi-0.0.1-SNAPSHOT.jar" ]; then
        log_error "JAR do backend não foi gerado!"
        exit 1
    fi

    log_success "Backend compilado: $(du -h target/keysbankapi-0.0.1-SNAPSHOT.jar | cut -f1)"

    # ============================================
    # 2. PREPARE FRONTEND (deps + tests)
    # ============================================
    log_info "📦 [2/5] Preparando Frontend..."
    cd "$PROJECT_ROOT/front-end"

    log_info "Instalando dependências..."
    npm ci --silent || {
        log_error "Instalação de dependências do frontend falhou!"
        exit 1
    }

    log_info "Executando testes..."
    npm run test:ci --silent 2>/dev/null || log_warning "Alguns testes do frontend falharam (continuando...)"

    # ============================================
    # 3. TERRAFORM APPLY
    # ============================================
    log_info "🏗️  [3/5] Aplicando Infraestrutura com Terraform..."
    cd "$PROJECT_ROOT/infra/terraform/environments/dev"

    if [ ! -f "terraform.tfvars" ]; then
        log_error "Arquivo terraform.tfvars não encontrado!"
        log_info "Execute: cp terraform.tfvars.example terraform.tfvars"
        exit 1
    fi

    log_info "Inicializando Terraform..."
    terraform init -reconfigure > /dev/null || {
        log_error "Terraform init falhou!"
        exit 1
    }

    log_info "Aplicando mudanças na infraestrutura..."
    terraform apply -auto-approve || {
        log_error "Terraform apply falhou!"
        exit 1
    }

    # Obter outputs
    ALB_DNS=$(terraform output -raw alb_dns_name 2>/dev/null || echo "unknown")
    EC2_IPS=$(terraform output -json backend_instance_ips 2>/dev/null | jq -r '.[]' || echo "")
    RDS_ENDPOINT=$(terraform output -raw rds_address 2>/dev/null || echo "unknown")

    log_success "Infraestrutura aplicada!"
    log_info "ALB DNS: $ALB_DNS"
    log_info "RDS Endpoint: $RDS_ENDPOINT"

    # ============================================
    # 4. DEPLOY BACKEND
    # ============================================
    log_info "🚀 [4/5] Deploy do Backend nas Instâncias EC2..."

    BACKEND_JAR="$PROJECT_ROOT/back-end/target/keysbankapi-0.0.1-SNAPSHOT.jar"
    KEY_PATH="$HOME/.ssh/keysbank-dev-key.pem"

    if [ ! -f "$KEY_PATH" ]; then
        log_error "Chave SSH não encontrada: $KEY_PATH"
        log_info "Crie a chave com:"
        log_info "  aws ec2 create-key-pair --key-name keysbank-dev-key --region sa-east-1 --query 'KeyMaterial' --output text > $KEY_PATH"
        log_info "  chmod 400 $KEY_PATH"
        exit 1
    fi

    if [ -z "$EC2_IPS" ]; then
        log_error "Nenhuma instância EC2 encontrada!"
        exit 1
    fi

    for EC2_IP in $EC2_IPS; do
        log_info "Enviando JAR para $EC2_IP..."
        
        scp -i "$KEY_PATH" -o StrictHostKeyChecking=no \
            "$BACKEND_JAR" \
            "ec2-user@$EC2_IP:/opt/keysbank/backend/app.jar" || {
            log_error "Falha ao enviar JAR para $EC2_IP"
            continue
        }
        
        log_info "Reiniciando serviço no $EC2_IP..."
        ssh -i "$KEY_PATH" -o StrictHostKeyChecking=no \
            "ec2-user@$EC2_IP" \
            "sudo systemctl restart keysbank-backend" || {
            log_error "Falha ao reiniciar serviço em $EC2_IP"
            continue
        }
        
        log_success "Backend deployado em $EC2_IP"
    done

    # ============================================
    # 5. BUILD + DEPLOY FRONTEND
    # ============================================
    log_info "🚀 [5/5] Build + Deploy do Frontend..."

    log_info "Compilando aplicação com VITE_API_BASE_URL..."
    cd "$PROJECT_ROOT/front-end"
    if ! VITE_API_BASE_URL="http://${ALB_DNS}/api" npm run build --silent; then
        log_error "Build do frontend falhou!"
        exit 1
    fi

    if [ ! -d "dist" ]; then
        log_error "Diretório dist do frontend não foi gerado!"
        exit 1
    fi

    log_success "Frontend compilado: $(du -sh dist | cut -f1)"

    log_info "Configurando Nginx em cada instância..."
    
    # Nginx configuration file
    read -r -d '' NGINX_CONF << 'NGINX_EOF' || true
server {
    listen 80 default_server;
    listen [::]:80 default_server;
    server_name _;

    root /var/www/keysbank;
    index index.html index.htm index.nginx-debian.html;

    location / {
        try_files $uri $uri/ /index.html;
    }

    location /api/ {
        proxy_pass http://localhost:8080;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
        proxy_http_version 1.1;
        proxy_set_header Connection "";
        proxy_buffering off;
    }

    location /actuator/ {
        proxy_pass http://localhost:8080;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }

    location /swagger-ui/ {
        proxy_pass http://localhost:8080;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }

    location /v3/ {
        proxy_pass http://localhost:8080;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }
}
NGINX_EOF

    log_info "Enviando assets para as instâncias EC2..."

    for EC2_IP in $EC2_IPS; do
        log_info "Enviando assets para $EC2_IP..."

        ssh -i "$KEY_PATH" -o StrictHostKeyChecking=no \
            "ec2-user@$EC2_IP" \
            "sudo mkdir -p /var/www/keysbank && sudo chown -R ec2-user:ec2-user /var/www/keysbank" || {
            log_error "Falha ao preparar diretório /var/www/keysbank em $EC2_IP"
            continue
        }
        
        # Configure Nginx
        ssh -i "$KEY_PATH" -o StrictHostKeyChecking=no \
            "ec2-user@$EC2_IP" \
            "echo '$NGINX_CONF' | sudo tee /etc/nginx/conf.d/keysbank.conf > /dev/null && sudo nginx -t && sudo systemctl reload nginx" || {
            log_error "Falha ao configurar Nginx em $EC2_IP"
            continue
        }
        
        scp -i "$KEY_PATH" -o StrictHostKeyChecking=no \
            -r "$PROJECT_ROOT/front-end/dist/." \
            "ec2-user@$EC2_IP:/var/www/keysbank/" || {
            log_error "Falha ao enviar frontend para $EC2_IP"
            continue
        }
        
        log_success "Frontend deployado em $EC2_IP"
    done

    log_success "════════════════════════════════════════════════════════════"
    log_success "Deploy concluído com sucesso!"
    log_success "════════════════════════════════════════════════════════════"
    echo ""
    echo "Aplicação disponível em:"
    echo "  • Frontend/API: http://$ALB_DNS"
    echo "  • Swagger UI:   http://$ALB_DNS/swagger-ui.html"
    echo ""
    echo "Dados da infraestrutura:"
    echo "  • Instâncias EC2: $EC2_IPS"
    echo "  • RDS Database:   $RDS_ENDPOINT"
    echo ""

else
    log_error "Tipo de deploy inválido: '$DEPLOY_TYPE'"
    log_info "Use: local ou aws"
    exit 1
fi
