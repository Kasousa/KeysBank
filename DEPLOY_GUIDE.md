# Deploy Automatizado - KeysBank

Este projeto agora utiliza **deploy automatizado local** ao invés de GitHub Actions.

## 🚀 Como Fazer Deploy

### Pré-requisitos
- AWS CLI configurado (`aws configure`) - para deploy AWS
- Terraform instalado - para deploy AWS
- Maven e Node.js instalados
- Docker Compose - para deploy local
- Chave SSH criada na AWS - para deploy AWS

### Deploy Local (Docker Compose)

```bash
cd infra
./scripts/deploy.sh local
```

Este comando:
1. ✅ Build local do backend e frontend
2. ✅ Inicia containers do backend, frontend e PostgreSQL
3. ✅ Disponibiliza a aplicação em http://localhost:3000

### Deploy AWS (Terraform + EC2)

```bash
cd infra
./scripts/deploy.sh aws
```

Este comando único executa:
1. ✅ Build e testes do backend (Maven)
2. ✅ Build e testes do frontend (npm)
3. ✅ Criação/atualização da infraestrutura AWS (Terraform)
4. ✅ Deploy automático do backend nas instâncias EC2
5. ✅ Deploy automático do frontend nas instâncias EC2
6. ✅ Verificação de saúde da aplicação

## ⚙️ Configuração Inicial (Primeira Vez - AWS)

```bash
# 1. Configurar AWS
aws configure

# 2. Criar chave SSH
aws ec2 create-key-pair \
  --key-name keysbank-dev-key \
  --region sa-east-1 \
  --query 'KeyMaterial' \
  --output text > ~/.ssh/keysbank-dev-key.pem
chmod 400 ~/.ssh/keysbank-dev-key.pem

# 3. Configurar variáveis do Terraform
cd infra/terraform/environments/dev
cp terraform.tfvars.example terraform.tfvars
nano terraform.tfvars  # Editar senha do banco e outros valores

# 4. Executar deploy completo
cd ../../..
./scripts/deploy.sh aws
```

## 📋 Recursos Criados na AWS

### Banco de Dados - RDS PostgreSQL
**Por que?** Sua aplicação Spring Boot precisa de um banco de dados PostgreSQL para armazenar:
- Clientes (customers)
- Contas bancárias (accounts)
- Transações (transactions)
- Saldos diários (daily_balance)

O RDS é um **banco de dados gerenciado** pela AWS, oferecendo:
- ✅ Backups automáticos (se configurado)
- ✅ Alta disponibilidade (Multi-AZ)
- ✅ Atualizações automáticas
- ✅ Monitoramento integrado
- ✅ Escalabilidade fácil

**Alternativa local:** Docker Compose usa PostgreSQL em container.

### Estado do Terraform - Local

O Terraform armazena o estado da infraestrutura em um arquivo local:
- Arquivo: `infra/terraform/environments/dev/terraform.tfstate`
- Este arquivo mapeia os recursos reais da AWS (IDs, configurações)
- **IMPORTANTE:** Faça backup regularmente ou versione no Git

**Vantagens do estado local:**
- ✅ Simples (não precisa criar S3 + DynamoDB)
- ✅ Sem custos adicionais (~$0.50/mês economizados)
- ✅ Operações mais rápidas (sem latência de rede)
- ✅ Ideal para projetos pessoais ou desenvolvedores solo

**Alternativa para times:** Se futuramente você trabalhar em equipe, pode migrar para estado remoto (S3 + DynamoDB) para compartilhar o estado e ter lock de operações concorrentes.

### Outros Recursos
- **VPC**: Rede privada isolada na AWS
- **EC2**: Servidores para rodar backend (Spring Boot) e frontend (Nginx)
- **ALB**: Load balancer para distribuir tráfego
- **Security Groups**: Firewall para proteger os recursos

## 🔄 Atualizar Aplicação

Após fazer mudanças no código:

**Local:**
```bash
cd infra
./scripts/deploy.sh local
```

**AWS:**
```bash
cd infra
./scripts/deploy.sh aws
```

O script irá:
- Rebuildar apenas o que mudou
- Aplicar mudanças de infraestrutura (se houver)
- Fazer deploy das novas versões

## 🗑️ Destruir Ambientes

### Destruir Deploy Local
```bash
cd infra
./scripts/destroy.sh local
```

Remove todos os containers locais. Os dados do banco são preservados em volumes Docker.

### Destruir Infraestrutura AWS
```bash
cd infra
./scripts/destroy.sh aws
```

Remove:
- Instâncias EC2
- Load Balancer (ALB)
- RDS Database
- VPC e componentes de rede

**Importante:**
- O arquivo terraform.tfstate será mantido localmente
- Se você ainda tem S3 bucket e DynamoDB table do Terraform antigo, eles NÃO serão removidos automaticamente (proteção)

## 💰 Custos Estimados

**Ambiente Dev:**
- EC2 t3.small: ~$15/mês
- RDS db.t3.micro (free tier elegível): ~$15/mês se não free tier
- ALB: ~$20/mês
- **Total:** ~$50/mês (ou ~$20/mês com free tier de 12 meses)

**Nota:** Removemos S3 + DynamoDB do Terraform (~$0.50/mês economizados), agora usando state local.

## 📚 Arquivos Importantes

- `infra/scripts/deploy.sh` - Script principal de deploy (local ou AWS)
- `infra/scripts/destroy.sh` - Script de destruição de ambientes
- `infra/scripts/setup-aws.sh` - Configuração inicial do Terraform
- `infra/docker-compose.yml` - Configuração do ambiente local
- `infra/terraform/environments/dev/` - Configuração do ambiente AWS
- `infra/terraform/modules/` - Módulos reutilizáveis do Terraform

## ❌ GitHub Actions Removido

Os workflows do GitHub Actions foram **removidos** porque o deploy agora é feito localmente.

Vantagens do deploy local:
- ✅ Mais controle sobre o processo
- ✅ Não depende de secrets no GitHub
- ✅ Feedback imediato durante o deploy
- ✅ Mais fácil de debugar
- ✅ Não consome minutos do GitHub Actions

## 🐛 Troubleshooting

### Deploy falha no backend
```bash
# Ver logs na EC2
ssh -i ~/.ssh/keysbank-dev-key.pem ec2-user@<EC2_IP>
sudo journalctl -u keysbank-backend -f
```

### Frontend não carrega
```bash
# Ver logs do Nginx na EC2
ssh -i ~/.ssh/keysbank-dev-key.pem ec2-user@<EC2_IP>
sudo tail -f /var/log/nginx/error.log
```

### Terraform apply falha
```bash
cd infra/terraform/environments/dev
terraform init -reconfigure
terraform plan  # Ver o que será alterado
```

## 🎯 Fluxo de Desenvolvimento

1. **Desenvolvimento local:** Docker Compose (`docker-compose up -d`)
2. **Testes:** `mvn test` (backend) e `npm test` (frontend)
3. **Deploy para AWS:** `./infra/scripts/deploy-to-aws.sh dev`
4. **Verificar:** Acessar URL do ALB

## 📞 Suporte

- Documentação AWS: https://docs.aws.amazon.com
- Documentação Terraform: https://www.terraform.io/docs
- Issues do projeto: Criar issue no repositório
