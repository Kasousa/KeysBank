# Infraestrutura KeysBank

Infraestrutura completa para deploy local e AWS da aplicação KeysBank.

## 📁 Estrutura

```
infra/
├── terraform/                      # Infraestrutura como código
│   ├── modules/                    # Módulos reutilizáveis
│   │   ├── vpc/                    # Virtual Private Cloud
│   │   ├── security-groups/        # Security Groups
│   │   ├── ec2/                    # Instâncias EC2
│   │   ├── rds/                    # PostgreSQL RDS
│   │   └── alb/                    # Application Load Balancer
│   └── environments/               # Configurações por ambiente
│       ├── dev/                    # Desenvolvimento
│       └── prod/                   # Produção
├── local/                          # Configurações locais
│   └── init-db/                    # Scripts de inicialização do BD
├── scripts/                        # Scripts de automação
│   ├── deploy.sh                   # Deploy unificado
│   ├── setup-aws.sh                # Setup inicial AWS
│   └── cleanup.sh                  # Limpeza de recursos
├── docker-compose.yml              # Orquestração local
└── .env.example                    # Variáveis de ambiente
```

## 🚀 Quick Start

### Ambiente Local

1. **Configurar variáveis de ambiente:**
```bash
cd infra
cp .env.example .env
# Edite .env conforme necessário
```

2. **Iniciar aplicação:**
```bash
./scripts/deploy.sh
# Escolha opção 1: Deploy Local
```

3. **Acessar serviços:**
- Frontend: http://localhost
- Backend API: http://localhost:8080
- Swagger UI: http://localhost:8080/swagger-ui.html
- Database: localhost:5432

### AWS (Dev)

1. **Preparar ambiente AWS:**
```bash
./scripts/setup-aws.sh dev us-east-1
```

2. **Criar key pair:**
```bash
aws ec2 create-key-pair \
  --key-name keysbank-dev-key \
  --query 'KeyMaterial' \
  --output text > ~/.ssh/keysbank-dev-key.pem
chmod 400 ~/.ssh/keysbank-dev-key.pem
```

3. **Configurar Terraform:**
```bash
cd terraform/environments/dev
cp terraform.tfvars.example terraform.tfvars
# Edite terraform.tfvars
```

4. **Deploy:**
```bash
cd ../../..
./scripts/deploy.sh
# Escolha opção 2: Deploy AWS (dev)
```

## 🏗️ Arquitetura AWS

### Recursos Provisionados

**Rede:**
- VPC com CIDR 10.0.0.0/16
- 2 Subnets públicas (10.0.1.0/24, 10.0.2.0/24)
- 2 Subnets privadas (10.0.10.0/24, 10.0.11.0/24)
- Internet Gateway
- NAT Gateways
- Route Tables

**Compute:**
- Application Load Balancer (ALB)
- EC2 instances com Auto Scaling (opcional)
- Launch Templates
- IAM Roles e Instance Profiles

**Database:**
- RDS PostgreSQL 16
- Multi-AZ (produção)
- Automated backups
- Encryption at rest

**Security:**
- Security Groups segregados (ALB, EC2, RDS)
- IAM Policies
- Encryption habilitada
- VPC Flow Logs (opcional)

### Diagrama de Arquitetura

```
                                    Internet
                                       |
                                   [IGW]
                                       |
                        ┌──────────────┴──────────────┐
                        │                             │
                 [Public Subnet 1]           [Public Subnet 2]
                        │                             │
                     [ALB]─────────────────────────[ALB]
                        │                             │
                [EC2 Backend 1]              [EC2 Backend 2]
                        │                             │
                   [NAT GW]──────────────────────[NAT GW]
                        │                             │
                 [Private Subnet 1]          [Private Subnet 2]
                        │                             │
                        └──────────┬──────────────────┘
                                   │
                              [RDS Primary]
                                   │
                           [RDS Standby - Multi-AZ]
```

## 📋 Configurações por Ambiente

### Development (dev)

- **EC2:** t3.small (1 instância)
- **RDS:** db.t3.micro (Single-AZ)
- **Backups:** 7 dias
- **Deletion Protection:** Desabilitada
- **Custo estimado:** ~$50-70/mês

### Production (prod)

- **EC2:** t3.medium (2+ instâncias)
- **RDS:** db.t3.small (Multi-AZ)
- **Backups:** 30 dias
- **Deletion Protection:** Habilitada
- **SSL:** Obrigatório
- **Custo estimado:** ~$150-200/mês

## 🔧 Terraform

### Comandos Básicos

```bash
cd terraform/environments/dev

# Inicializar
terraform init

# Planejar mudanças
terraform plan

# Aplicar mudanças
terraform apply

# Ver outputs
terraform output

# Destruir (CUIDADO!)
terraform destroy
```

### Módulos

#### VPC Module
Cria toda a infraestrutura de rede.

**Inputs:**
- `vpc_cidr`: CIDR block da VPC
- `public_subnet_cidrs`: Lista de CIDRs públicos
- `private_subnet_cidrs`: Lista de CIDRs privados
- `enable_nat_gateway`: Habilitar NAT Gateway

**Outputs:**
- `vpc_id`: ID da VPC
- `public_subnet_ids`: IDs das subnets públicas
- `private_subnet_ids`: IDs das subnets privadas

#### EC2 Module
Provisiona instâncias backend.

**Inputs:**
- `instance_type`: Tipo da instância
- `instance_count`: Número de instâncias
- `ami_id`: ID da AMI
- `db_host`: Endpoint do RDS

**Outputs:**
- `instance_ids`: IDs das instâncias
- `instance_public_ips`: IPs públicos

#### RDS Module
Cria PostgreSQL gerenciado.

**Inputs:**
- `instance_class`: Classe da instância
- `allocated_storage`: Armazenamento em GB
- `multi_az`: Habilitar Multi-AZ
- `master_username`: Usuário master
- `master_password`: Senha master

**Outputs:**
- `db_instance_endpoint`: Endpoint do banco
- `db_instance_address`: Endereço do banco

#### ALB Module
Configura Application Load Balancer.

**Inputs:**
- `subnet_ids`: Subnets para o ALB
- `target_instance_ids`: Instâncias EC2 target
- `certificate_arn`: Certificado SSL (opcional)

**Outputs:**
- `alb_dns_name`: DNS do ALB
- `backend_target_group_arn`: ARN do target group

## 🐳 Docker Compose

### Serviços

**postgres:** PostgreSQL 16 Alpine
- Health checks configurados
- Volume persistente
- Scripts de inicialização

**backend:** Spring Boot API
- Build automático do Dockerfile
- Conecta ao PostgreSQL
- Health checks via Actuator

**frontend:** React + Nginx
- Build multi-stage
- Serve arquivos estáticos
- Proxy reverso para API

**adminer:** Database UI (opcional)
- Profile: `tools`
- Acesso: http://localhost:8081

### Comandos Docker

```bash
# Iniciar todos os serviços
docker-compose up -d

# Ver logs
docker-compose logs -f

# Ver status
docker-compose ps

# Parar serviços
docker-compose stop

# Remover containers
docker-compose down

# Remover tudo (incluindo volumes)
docker-compose down -v

# Iniciar com tools (adminer)
docker-compose --profile tools up -d
```

## 🔐 Segurança

### Boas Práticas Implementadas

✅ Senhas e secrets via variáveis de ambiente
✅ Terraform state remoto com encryption
✅ Security Groups com princípio do menor privilégio
✅ RDS com encryption at rest
✅ SSL/TLS para produção
✅ Volumes Docker criptografados
✅ IAM roles com políticas específicas
✅ Backup automático do RDS
✅ Multi-AZ para alta disponibilidade (prod)

### Secrets Management

**Local:** Arquivo `.env` (não commitado)
**AWS:** 
- Terraform: `terraform.tfvars` (não commitado)
- Runtime: AWS Secrets Manager (recomendado para prod)
- CI/CD: GitHub Secrets

## 📊 Monitoramento

### CloudWatch (AWS)

Terraform configura automaticamente:
- CloudWatch Logs para EC2
- RDS CloudWatch integration
- ALB metrics
- Custom metrics via CloudWatch Agent

### Logs Locais

```bash
# Backend logs
docker-compose logs -f backend

# PostgreSQL logs
docker-compose logs -f postgres

# Frontend logs
docker-compose logs -f frontend

# Todos os logs
docker-compose logs -f
```

## 🔄 CI/CD com GitHub Actions

### Integração com Monorepo

O repositório possui GitHub Actions workflows para:

**Backend Deploy:**
- Trigger: Push em `back-end/**` ou `infra/**`
- Steps: Test → Build → Deploy → Verify

**Frontend Deploy:**
- Trigger: Push em `front-end/**`
- Steps: Test → Build → Deploy → Verify

### Configurar GitHub Secrets

Após provisionar infraestrutura com Terraform:

```bash
# Obter outputs
cd terraform/environments/dev
terraform output

# Adicionar ao GitHub:
# - AWS_ACCESS_KEY_ID
# - AWS_SECRET_ACCESS_KEY
# - EC2_INSTANCE_IP (do output)
# - EC2_PRIVATE_KEY (conteúdo do .pem)
```

## 🧪 Testes

### Executar Todos os Testes

```bash
./scripts/deploy.sh
# Escolha opção 6: Executar testes
```

### Testes Individuais

```bash
# Backend
cd back-end
./mvnw clean test

# Frontend
cd front-end
npm run test
```

## 🆘 Troubleshooting

### Docker Compose

**Problema:** Container backend não inicia
```bash
# Ver logs detalhados
docker-compose logs backend

# Verificar conectividade com PostgreSQL
docker-compose exec backend ping postgres

# Restart do serviço
docker-compose restart backend
```

**Problema:** Porta já em uso
```bash
# Editar .env e mudar portas
BACKEND_PORT=8081
FRONTEND_PORT=81
```

### Terraform

**Problema:** State lock
```bash
# Forçar unlock (CUIDADO!)
terraform force-unlock <LOCK_ID>
```

**Problema:** Recursos já existem
```bash
# Importar recurso existente
terraform import aws_instance.backend i-1234567890abcdef0
```

### AWS

**Problema:** EC2 não responde
```bash
# SSH na instância
ssh -i ~/.ssh/keysbank-dev-key.pem ec2-user@<EC2_IP>

# Verificar serviço
sudo systemctl status keysbank-backend

# Ver logs
sudo journalctl -u keysbank-backend -f
```

**Problema:** RDS não conecta
```bash
# Testar conectividade
telnet <RDS_ENDPOINT> 5432

# Verificar Security Group
aws ec2 describe-security-groups --group-ids <SG_ID>
```

## 💰 Custos AWS

### Estimativa Mensal

**Development:**
- EC2 t3.small: ~$15
- RDS db.t3.micro: ~$15
- ALB: ~$20
- NAT Gateway: ~$35
- EBS Storage: ~$5
- Data Transfer: ~$5
- **Total: ~$95/mês**

**Production:**
- EC2 t3.medium x2: ~$60
- RDS db.t3.small Multi-AZ: ~$50
- ALB: ~$20
- NAT Gateway x2: ~$70
- EBS Storage: ~$15
- Backups: ~$10
- Data Transfer: ~$20
- **Total: ~$245/mês**

**Otimizações:**
- Use Reserved Instances (-30% a -50%)
- Savings Plans
- Remova NAT Gateway se não precisar (use endereços públicos)
- Single-AZ para dev

## 📚 Próximos Passos

### Melhorias Recomendadas

1. **Auto Scaling:** Implementar ASG para EC2
2. **CloudFront:** CDN para frontend
3. **Route 53:** DNS gerenciado
4. **ACM:** Certificados SSL automáticos
5. **WAF:** Web Application Firewall
6. **Secrets Manager:** Gestão centralizada de secrets
7. **ECS/Fargate:** Migrar para containers serverless
8. **Monitoring:** Datadog, New Relic ou Prometheus
9. **Backup:** AWS Backup policy
10. **DR:** Disaster Recovery plan

### Roadmap

- [ ] Adicionar módulo de Auto Scaling
- [ ] Configurar CloudFront
- [ ] Implementar CI/CD completo
- [ ] Adicionar testes de integração
- [ ] Configurar monitoring avançado
- [ ] Criar ambiente de staging
- [ ] Documentar DR procedures
- [ ] Implementar blue-green deployment

## 📖 Referências

- [Terraform AWS Provider](https://registry.terraform.io/providers/hashicorp/aws/latest/docs)
- [Docker Compose](https://docs.docker.com/compose/)
- [AWS Well-Architected](https://aws.amazon.com/architecture/well-architected/)
- [Spring Boot on AWS](https://spring.io/guides/gs/spring-boot-aws/)

## 🤝 Contribuindo

Para contribuir com a infraestrutura:

1. Crie branch feature
2. Teste localmente com Docker Compose
3. Valide Terraform: `terraform validate`
4. Formate código: `terraform fmt`
5. Abra Pull Request

## 📄 Licença

Este projeto está sob licença MIT. Veja LICENSE para mais detalhes.
