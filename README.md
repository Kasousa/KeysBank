# 🏦 KeysBank - Sistema Bancário Virtual

Uma plataforma bancária moderna e robusta construída com Spring Boot, React e PostgreSQL 16. Totalmente tipada com TypeScript, documentada com OpenAPI 3.0, com testes completos e pronta para produção.

## 📋 Índice
- [Visão Geral](#visão-geral)
- [Recursos](#recursos)
- [Tecnologias](#tecnologias)
- [Como Começar](#como-começar)
- [API - Endpoints Principais](#api---endpoints-principais)
- [Testes](#-testes)
- [Deploy Automatizado na AWS](#-deploy-automatizado-na-aws)
- [Troubleshooting](#troubleshooting)

---

## 🎯 Visão Geral

O **KeysBank** é uma aplicação de banco virtual completa com:
- ✅ Gerenciamento de clientes
- ✅ Criação e gerenciamento de contas bancárias
- ✅ Sistema de transações (débito/crédito)
- ✅ Extrato com filtros avançados
- ✅ Cálculo automático de saldo diário
- ✅ Bônus de abertura de conta (R$ 100,00)
- ✅ API RESTful totalmente documentada
- ✅ Frontend responsivo e intuitivo

---

## ⚡ Recursos

### Backend
- ✅ Arquitetura de camadas (Controller → Service → Repository)
- ✅ Validação de dados com Jakarta Validation
- ✅ CORS configurado para produção
- ✅ Tratamento global de erros
- ✅ Documentação OpenAPI 3.0 / Swagger
- ✅ Migrations automáticas com Flyway
- ✅ Testes unitários

### Frontend
- ✅ React 18.3.1 com TypeScript 5.8
- ✅ Tailwind CSS 3.4 para estilização
- ✅ Shadcn/UI para componentes
- ✅ React Router para navegação
- ✅ Integração HTTP com fetch API
- ✅ Design responsivo (mobile/desktop)
- ✅ Testes unitários com Vitest

---

## 💻 Tecnologias

| Camada | Tecnologia |
|--------|-----------|
| **Backend** | Java 17, Spring Boot 4.0.0, Spring Data JPA |
| **Frontend** | React 18.3.1, TypeScript 5.8, Tailwind CSS 3.4 |
| **Banco de Dados** | PostgreSQL 16.11 |
| **Documentação** | OpenAPI 3.0, Swagger UI |
| **Build** | Maven (Backend), Vite 5 (Frontend) |
| **Deployment** | Docker, AWS EC2, Terraform |
| **Testes** | JUnit 5 (Backend), Vitest (Frontend) |

---

## 🚀 Como Começar

### Pré-requisitos
- **Java 17+** (Backend)
- **Node.js 18+** (Frontend)
- **PostgreSQL 16+** (Banco de Dados)
- **Maven 3.9+** (Build Backend)

### 1️⃣ Configurar Banco de Dados

```bash
# Criar usuário e banco
psql -U postgres
CREATE USER bankuser WITH PASSWORD 'bankpass123';
CREATE DATABASE bank OWNER bankuser;
\q

# Ou usar Docker
docker run -d \
  --name postgres-keysbank \
  -e POSTGRES_USER=bankuser \
  -e POSTGRES_PASSWORD=bankpass123 \
  -e POSTGRES_DB=bank \
  -p 5432:5432 \
  postgres:16
```

### 2️⃣ Iniciar Backend

```bash
cd back-end

# Compilar
mvn clean package

# Executar
mvn spring-boot:run
```

**Backend estará em:** `http://localhost:8080`
**Swagger UI:** `http://localhost:8080/swagger-ui.html`

### 3️⃣ Iniciar Frontend

```bash
cd front-end

# Instalar dependências
npm install

# Executar modo desenvolvimento
npm run dev
```

**Frontend estará em:** `http://localhost:3000`

---

## 🔌 API - Endpoints Principais

### Clientes (Customers)
```bash
# Criar cliente
POST /api/customers
{
  "name": "João Silva",
  "email": "joao@email.com"
}
```

### Contas (Accounts)
```bash
# Criar conta (recebe R$ 100 de bônus)
POST /api/accounts
{
  "customerId": "uuid-aqui"
}

# Login (validação de conta)
GET /api/accounts/login?agency=0001&accountNumber=343316
```

### Transações
```bash
# Criar transação
POST /api/transaction
{
  "accountId": "uuid-aqui",
  "type": "CREDIT",
  "category": "DEPOSITO",
  "amount": 100.00,
  "description": "Depósito inicial"
}
```

### Extrato
```bash
# Visualizar extrato
GET /api/accounts/{accountId}/statement

# Com filtros
GET /api/accounts/{accountId}/statement?startDate=2026-01-01&endDate=2026-01-31&type=CREDIT
```

Veja **[DEPLOY_GUIDE.md](./DEPLOY_GUIDE.md)** para documentação de deploy.

---

## 🧪 Testes

### Backend (JUnit 5)
```bash
cd back-end
mvn test
```

**Arquivo de teste:**
- `src/test/java/com/backend/keysbankapi/KeysbankapiApplicationTests.java` - Spring Boot context test

### Frontend (Vitest + React Testing Library)
**29 testes unitários** cobrindo componentes, hooks e utilitários:
```bash
cd front-end
npm run test:ci
```

**Testes implementados:**
- `src/utils/formatters.test.ts` (16 testes) - Funções de formatação
- `src/context/AuthContext.test.tsx` (5 testes) - Hook de autenticação
- `src/components/auth/LoginForm.test.tsx` (7 testes) - Formulário de login
- `src/test/example.test.ts` (1 teste) - Exemplo básico

---

## 🚀 Deploy Automatizado

### Deploy Local (Docker Compose)

```bash
cd infra
./scripts/deploy.sh local
```

Disponibiliza:
- Backend: http://localhost:8080
- Frontend: http://localhost:3000
- Database: localhost:5432

### Deploy AWS

```bash
cd infra
./scripts/deploy.sh aws
```

Este comando executa:
- ✅ Build e testes do backend (Maven)
- ✅ Build e testes do frontend (npm)
- ✅ Criação/atualização da infraestrutura AWS (Terraform)
- ✅ Deploy automático nas instâncias EC2
- ✅ Verificação de saúde da aplicação

### Setup Inicial - AWS

```bash
# 1. Configurar AWS CLI
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
nano terraform.tfvars  # Editar senha do banco

# 4. Deploy completo
cd ../../..
./scripts/deploy.sh aws
```

### Infraestrutura Criada

**Recursos AWS:**
- **VPC**: Rede privada isolada com subnets públicas/privadas
- **EC2**: Instâncias t3.small com Java 17 + Nginx
- **RDS PostgreSQL 16**: Banco de dados gerenciado da aplicação
- **ALB**: Load balancer para distribuir tráfego
- **Security Groups**: Firewall configurado

**Recursos Terraform (auxiliares):**
- **S3 Bucket**: Armazena estado do Terraform (compartilhado entre devs)
- **DynamoDB Table**: Lock para prevenir conflitos simultâneos

**Custo estimado:** ~$50/mês no ambiente dev (~$20/mês com free tier)

### Por que RDS e S3?

**RDS PostgreSQL:**
- É o **banco de dados da sua aplicação** Spring Boot
- Armazena: clientes, contas, transações, saldos
- Alternativa local: PostgreSQL no Docker Compose

**S3 + DynamoDB:**
- **NÃO são da aplicação**, são para o Terraform
- Mantêm estado da infraestrutura sincronizado
- Permitem múltiplos desenvolvedores trabalharem juntos
- Custo: ~$0.50/mês (praticamente gratuito)

### Atualizar Aplicação

Após mudanças no código:

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

### Destruir Ambientes

**Local:**
```bash
cd infra
./scripts/destroy.sh local
```

**AWS:**
```bash
cd infra
./scripts/destroy.sh aws
```

📖 **Documentação completa:** [DEPLOY_GUIDE.md](DEPLOY_GUIDE.md)

---

## 📚 Documentação

**API Documentação:**
- Swagger UI: `http://localhost:8080/swagger-ui.html` (local)
- OpenAPI spec: [back-end/openapi.json](./back-end/openapi.json)
- Documentação técnica: [back-end/API_DOCUMENTATION.md](./back-end/API_DOCUMENTATION.md)

**Arquitetura:**
```
KeysBank/
├── back-end/
│   ├── src/main/java/com/backend/keysbankapi/
│   │   ├── customer/         → Gerenciamento de clientes
│   │   ├── account/          → Gerenciamento de contas
│   │   └── ledger/           → Sistema de transações
│   ├── src/test/java/        → 17 testes unitários
│   ├── pom.xml               → Dependências Maven
│   └── openapi.json          → Documentação Swagger
│
├── front-end/
│   ├── src/
│   │   ├── components/       → Componentes React
│   │   ├── pages/            → Páginas/rotas
│   │   ├── context/          → Context API (auth)
│   │   ├── services/         → Chamadas API
│   │   └── utils/            → Funções utilitárias
│   ├── src/**/*.test.ts      → 29 testes unitários
│   ├── package.json          → Dependências npm
│   └── vite.config.ts        → Configuração build
│
└── infra/
    ├── scripts/
    │   ├── deploy-to-aws.sh  → Deploy automatizado
    │   └── setup-aws.sh      → Setup inicial
    ├── terraform/            → Infraestrutura AWS
    └── docker-compose.yml    → Docker Compose local
```

---

## 🔐 Segurança

- ✅ Autenticação JWT
- ✅ Senhas com bcrypt
- ✅ Validação em múltiplas camadas
- ✅ CORS configurado
- ✅ Variáveis sensíveis em GitHub Secrets
- ✅ SQL parameterizado (prevenção SQL injection)

---

## Troubleshooting

### Problema: Conexão com banco falha
```bash
# Verificar se PostgreSQL está rodando
docker logs postgres-keysbank
# ou
psql -U bankuser -d bank -h localhost
```

### Problema: Frontend não conecta com backend
```bash
# Verificar porta do backend
netstat -an | grep 8080

# Verificar CORS em application.yaml
cat back-end/src/main/resources/application.yaml
```

### Problema: Testes falhando
```bash
# Limpar cache e dependências
rm -rf back-end/target front-end/node_modules

# Reinstalar
mvn clean install  # Backend
npm install        # Frontend

# Reexecutar testes
mvn test           # Backend
npm run test       # Frontend
```

### Problema: Deploy falhando
```bash
# Verificar status do script
cd infra
./scripts/deploy-to-aws.sh dev 2>&1 | tail -50

# Testar SSH manualmente
ssh -i ~/.ssh/keysbank-dev-key.pem ec2-user@52.67.105.85

# Verificar status da aplicação em EC2
ssh -i ~/.ssh/keysbank-dev-key.pem ec2-user@52.67.105.85 'systemctl status keysbank-backend'
```

---
