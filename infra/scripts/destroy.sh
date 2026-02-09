#!/bin/bash

# KeysBank Destroy Script
# Destrói recursos local (Docker Compose) ou AWS (Terraform)
# Uso: ./destroy.sh local|aws

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
DESTROY_TYPE=${1:-}

# Validar argumento
if [ -z "$DESTROY_TYPE" ]; then
    echo ""
    echo "╔════════════════════════════════════════╗"
    echo "║    KeysBank - Destroy Script          ║"
    echo "╚════════════════════════════════════════╝"
    echo ""
    echo "Uso: $0 <tipo>"
    echo ""
    echo "Tipos de destroy:"
    echo "  local    - Destruir Docker Compose"
    echo "  aws      - Destruir infraestrutura AWS (Terraform)"
    echo ""
    echo "Exemplo:"
    echo "  $0 local"
    echo "  $0 aws"
    echo ""
    exit 1
fi

# ============================================
# DESTROY LOCAL (Docker Compose)
# ============================================
if [ "$DESTROY_TYPE" = "local" ]; then
    log_warning "════════════════════════════════════════════════════════════"
    log_warning "   KeysBank - Destruir Deploy Local (Docker Compose)"
    log_warning "════════════════════════════════════════════════════════════"
    echo ""
    read -p "⚠️  Você tem certeza que deseja parar todos os containers? (s/N) " -n 1 -r
    echo ""
    
    if [[ ! $REPLY =~ ^[Ss]$ ]]; then
        log_info "Operação cancelada."
        exit 0
    fi

    cd "$PROJECT_ROOT/infra"

    # Verificar Docker
    if ! command -v docker-compose &> /dev/null; then
        log_error "Docker Compose não está instalado!"
        exit 1
    fi

    log_info "Parando e removendo containers..."
    docker-compose down

    if [ $? -eq 0 ]; then
        log_success "Deploy local destruído!"
        echo ""
        echo "Para remover volumes (CUIDADO - apaga dados do banco):"
        echo "  docker-compose down -v"
        echo ""
    else
        log_error "Falha ao destruir deploy local!"
        exit 1
    fi

# ============================================
# DESTROY AWS (Terraform)
# ============================================
elif [ "$DESTROY_TYPE" = "aws" ]; then
    log_warning "════════════════════════════════════════════════════════════"
    log_warning "   KeysBank - Destruir Infraestrutura AWS (Terraform)"
    log_warning "════════════════════════════════════════════════════════════"
    echo ""
    log_warning "⚠️  ATENÇÃO: Esta operação vai DELETAR:"
    log_warning "   • Instâncias EC2"
    log_warning "   • Load Balancer (ALB)"
    log_warning "   • RDS Database"
    log_warning "   • VPC e componentes de rede"
    echo ""
    read -p "⚠️  Digite 'sim' para confirmar a destruição: " -r
    echo ""
    
    if [ "$REPLY" != "sim" ]; then
        log_info "Operação cancelada."
        exit 0
    fi

    cd "$PROJECT_ROOT/infra/terraform/environments/dev"

    if [ ! -f "terraform.tfvars" ]; then
        log_error "Arquivo terraform.tfvars não encontrado!"
        exit 1
    fi

    log_info "Inicializando Terraform..."
    terraform init -reconfigure > /dev/null || {
        log_error "Terraform init falhou!"
        exit 1
    }

    log_warning "Destruindo infraestrutura na AWS..."
    terraform destroy -auto-approve

    if [ $? -eq 0 ]; then
        log_success "════════════════════════════════════════════════════════════"
        log_success "Infraestrutura AWS destruída!"
        log_success "════════════════════════════════════════════════════════════"
        echo ""
    else
        log_error "Falha ao destruir infraestrutura AWS!"
        exit 1
    fi

else
    log_error "Tipo de destroy inválido: '$DESTROY_TYPE'"
    log_info "Use: local ou aws"
    exit 1
fi
