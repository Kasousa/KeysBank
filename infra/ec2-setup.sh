#!/bin/bash
# EC2 Setup Script - Execute uma única vez após criar a instância

set -e

echo "🚀 Iniciando setup da instância EC2 KeysBank..."

# Atualizar sistema
sudo yum update -y
echo "✅ Sistema atualizado"

# Instalar Java 25
sudo amazon-linux-extras install java-openjdk25 -y
java -version
echo "✅ Java 25 instalado"

# Instalar Node.js 18
curl -fsSL https://rpm.nodesource.com/setup_18.x | sudo bash -
sudo yum install -y nodejs
node --version
echo "✅ Node.js 18 instalado"

# Instalar Docker
sudo amazon-linux-extras install docker -y
sudo usermod -a -G docker ec2-user
sudo systemctl start docker
sudo systemctl enable docker
echo "✅ Docker instalado"

# Instalar Docker Compose
sudo curl -L "https://github.com/docker/compose/releases/latest/download/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
sudo chmod +x /usr/local/bin/docker-compose
docker-compose --version
echo "✅ Docker Compose instalado"

# Criar diretórios de aplicação
mkdir -p /home/ec2-user/app/{backend,frontend}
mkdir -p /home/ec2-user/backups
mkdir -p /home/ec2-user/logs

# Criar arquivo .env
cat > /home/ec2-user/app/.env << 'EOF'
# Backend
SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/bank
SPRING_DATASOURCE_USERNAME=bankuser
SPRING_DATASOURCE_PASSWORD=${DB_PASSWORD}
SERVER_PORT=8080

# Frontend
VITE_API_BASE_URL=http://localhost:8080
VITE_PORT=3000
EOF

# Instalar PostgreSQL com Docker
docker run -d \
  --name postgres-keysbank \
  --restart unless-stopped \
  -e POSTGRES_USER=bankuser \
  -e POSTGRES_PASSWORD=bankpass123 \
  -e POSTGRES_DB=bank \
  -v postgres_data:/var/lib/postgresql/data \
  -p 5432:5432 \
  postgres:16

sleep 5
echo "✅ PostgreSQL iniciado"

# Instalar Nginx como reverse proxy
sudo amazon-linux-extras install nginx -y
sudo systemctl start nginx
sudo systemctl enable nginx

# Configurar Nginx
sudo tee /etc/nginx/conf.d/keysbank.conf > /dev/null << 'EOF'
upstream backend {
    server localhost:8080;
}

upstream frontend {
    server localhost:3000;
}

server {
    listen 80;
    server_name _;

    # Frontend
    location / {
        proxy_pass http://frontend;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
    }

    # Backend API
    location /api {
        proxy_pass http://backend;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
    }
}
EOF

sudo nginx -t
sudo systemctl reload nginx
echo "✅ Nginx configurado"

# Criar scripts de deploy
cat > /home/ec2-user/deploy-backend.sh << 'EOF'
#!/bin/bash
echo "🚀 Deploy Backend iniciado..."

# Parar aplicação anterior
sudo systemctl stop keysbank-backend || true

# Remover backup anterior
rm -f /home/ec2-user/backups/keysbank-*.jar

# Fazer backup
cp /home/ec2-user/app/backend/keysbankapi-*.jar /home/ec2-user/backups/ 2>/dev/null || true

# Mover JAR
mv /home/ec2-user/keysbankapi-*.jar /home/ec2-user/app/backend/

# Iniciar aplicação
nohup java -jar /home/ec2-user/app/backend/keysbankapi-*.jar \
    --spring.datasource.url=jdbc:postgresql://localhost:5432/bank \
    --spring.datasource.username=bankuser \
    --spring.datasource.password=bankpass123 \
    --server.port=8080 \
    > /home/ec2-user/logs/backend.log 2>&1 &

echo "✅ Backend deploy concluído"
EOF

cat > /home/ec2-user/deploy-frontend.sh << 'EOF'
#!/bin/bash
echo "🚀 Deploy Frontend iniciado..."

# Fazer backup
cp -r /home/ec2-user/app/frontend/dist /home/ec2-user/backups/dist-$(date +%s) 2>/dev/null || true

# Copiar arquivos (já feito via SCP, aqui só reinicia Nginx)
sudo systemctl reload nginx

echo "✅ Frontend deploy concluído"
EOF

cat > /home/ec2-user/rollback-backend.sh << 'EOF'
#!/bin/bash
echo "⚠️  Rollback Backend iniciado..."

# Parar aplicação
sudo systemctl stop keysbank-backend || true

# Restaurar backup anterior
LATEST_JAR=$(ls -t /home/ec2-user/backups/*.jar 2>/dev/null | head -1)
if [ -n "$LATEST_JAR" ]; then
    cp "$LATEST_JAR" /home/ec2-user/app/backend/
    java -jar "$LATEST_JAR" > /home/ec2-user/logs/backend.log 2>&1 &
    echo "✅ Rollback concluído"
else
    echo "❌ Nenhum backup disponível"
    exit 1
fi
EOF

cat > /home/ec2-user/rollback-frontend.sh << 'EOF'
#!/bin/bash
echo "⚠️  Rollback Frontend iniciado..."

# Restaurar backup anterior
LATEST_DIST=$(ls -td /home/ec2-user/backups/dist-* 2>/dev/null | head -1)
if [ -n "$LATEST_DIST" ]; then
    rm -rf /home/ec2-user/app/frontend/dist
    cp -r "$LATEST_DIST" /home/ec2-user/app/frontend/dist
    sudo systemctl reload nginx
    echo "✅ Rollback concluído"
else
    echo "❌ Nenhum backup disponível"
    exit 1
fi
EOF

chmod +x /home/ec2-user/{deploy-*.sh,rollback-*.sh}
echo "✅ Scripts de deploy criados"

# Configurar CloudWatch logs (opcional)
echo "✅ Setup concluído com sucesso!"
echo ""
echo "📝 Próximos passos:"
echo "1. Adicionar secrets no GitHub:"
echo "   - EC2_INSTANCE_IP: $(/sbin/ip -o -4 addr list | grep -oP '(?<=inet\s)\d+(\.\d+){3}' | grep -v '^127')"
echo "   - EC2_PRIVATE_KEY: conteúdo de ~/.ssh/keysbank-ec2.pem"
echo "   - AWS_ACCESS_KEY_ID: sua chave AWS"
echo "   - AWS_SECRET_ACCESS_KEY: sua chave AWS secreta"
echo "2. Fazer push para main"
echo "3. Verificar status em GitHub Actions"
EOF
chmod +x /Users/kaiquesantossousa/Projects/KeysBank/infra/ec2-setup.sh
echo "✅ Script de setup criado"