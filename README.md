# 🏦 KeysBank - Sistema Bancário Virtual

[![Java 17](https://img.shields.io/badge/Java-17-orange.svg)](https://www.oracle.com/java/technologies/javase-jdk17-downloads.html)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.0-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![React](https://img.shields.io/badge/React-18.3.1-blue.svg)](https://react.dev)
[![TypeScript](https://img.shields.io/badge/TypeScript-5.8-blue.svg)](https://www.typescriptlang.org)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-336791.svg)](https://www.postgresql.org)
[![Tests](https://img.shields.io/badge/Tests-43%2F43%20passing-brightgreen.svg)](https://github.com/your-repo)
[![License](https://img.shields.io/badge/License-MIT-green.svg)](./LICENSE)

Uma plataforma bancária moderna e robusta construída com Spring Boot, React e PostgreSQL 16. Totalmente tipada com TypeScript, com **43 testes automatizados** (17 backend + 26 frontend) e pronta para produção com deploy automático em Docker e AWS.

## 📋 Índice

- [🎯 Visão Geral](#-visão-geral)
- [⚡ Recursos Principais](#-recursos-principais)
- [💻 Stack Tecnológica](#-stack-tecnológica)
- [🏗️ Arquitetura do Sistema](#-arquitetura-do-sistema)
- [🚀 Quick Start (5 minutos)](#-quick-start-5-minutos)
- [🎨 Frontend - Stack Detalhado](#-frontend---stack-detalhado)
- [📡 Backend - Arquitetura](#-backend---arquitetura)
- [🔌 API - Principais Endpoints](#-api---principais-endpoints)
- [✅ Testes e Cobertura](#-testes-e-cobertura)
- [🚢 Deploy](#-deploy)
  - [Local (Docker Compose)](#deploy-local-docker-compose)
  - [AWS (EC2 + RDS + Terraform)](#deploy-aws-ec2--rds--terraform)
- [🏗️ Infraestrutura AWS](#-infraestrutura-aws)
- [🔐 Segurança](#-segurança)
- [💰 Custos AWS](#-custos-aws)
- [🆘 Troubleshooting](#-troubleshooting)
- [📊 Monitoramento](#-monitoramento)
- [🔄 CI/CD com GitHub Actions](#-cicd-com-github-actions)
- [📖 Próximos Passos](#-próximos-passos)

---

## 🎯 Visão Geral

**KeysBank** é uma plataforma bancária virtual de código aberto, pronta para produção, que demonstra boas práticas modernas de desenvolvimento full-stack:

#### O que KeysBank oferece?

- 🏦 **Sistema Bancário Completo:** Gerenciamento de clientes, contas, transações e extratos
- 💳 **Conta Digital:** Abertura instantânea com bônus de R$ 100,00
- 📊 **Extrato Detalhado:** Filtros avançados por data e tipo de transação
- 🔐 **Segurança:** Validação em múltiplas camadas, CORS configurado, encriptação
- ⚡ **Performance:** React 18, Spring Boot 4, PostgreSQL 16 otimizado
- 🧪 **43 Testes Automatizados:** 17 backend (JUnit 5) + 26 frontend (Vitest)
- 🚀 **Deploy Automático:** Docker local, AWS com Terraform, CI/CD com GitHub Actions
- 📚 **Bem Documentado:** README completo, API OpenAPI/Swagger, exemplos cURL

#### Stack Tecnológico

| Camada | Tecnologia |
|--------|-----------|
| Backend | Java 17 + Spring Boot 4.0.0 + JPA |
| Frontend | React 18.3.1 + TypeScript 5.8 + Tailwind CSS |
| Database | PostgreSQL 16 + Flyway migrations |
| Testing | JUnit 5 + Mockito (backend), Vitest + RTL (frontend) |
| Deployment | Docker Compose (local), AWS EC2/RDS (produção) |
| IaC | Terraform para AWS |
| CI/CD | GitHub Actions |

---

## 🚀 Quick Start (5 minutos)

**Option 1: Docker Compose (Recomendado - Mais Simples)**

```bash
# 1. Clone e entre no diretório
git clone <repo-url>
cd KeysBank/infra

# 2. Inicie os serviços (PostgreSQL, Backend, Frontend)
docker-compose up -d

# 3. Acesse a aplicação
# Frontend: http://localhost:3000
# Backend: http://localhost:8080
# API Docs: http://localhost:8080/swagger-ui/index.html

# 4. Ver logs (se necessário)
docker-compose logs -f
```

**Option 2: Desenvolvimento Local (Sem Docker)**

```bash
# Terminal 1: PostgreSQL
docker run -d -e POSTGRES_DB=bank -e POSTGRES_PASSWORD=bankpass123 \
  -p 5432:5432 postgres:16

# Terminal 2: Backend
cd back-end
mvn spring-boot:run  # Estará em http://localhost:8080

# Terminal 3: Frontend  
cd front-end
npm install && npm run dev  # Estará em http://localhost:3000
```

---

## ⚡ Recursos Principais

### Backend
- ✅ Arquitetura em 3 camadas (Controller → Service → Repository)
- ✅ Validação abrangente com Jakarta Validation
- ✅ Tratamento global de erros com mensagens customizadas
- ✅ CORS configurado para múltiplos ambientes
- ✅ Migrações automáticas com Flyway (versioning de BD)
- ✅ 17 testes unitários com JUnit 5 + Mockito
- ✅ Documentação OpenAPI/Swagger integrada

### Frontend
- ✅ React 18 com TypeScript (type-safe 100%)
- ✅ Tailwind CSS para estilização responsiva
- ✅ Shadcn/UI para componentes profissionais
- ✅ React Router para navegação SPA
- ✅ 26 testes unitários com Vitest + React Testing Library
- ✅ Design totalmente responsivo (mobile, tablet, desktop)
- ✅ Tratamento robusto de formulários com validação

### Arquitetura
- ✅ Deploy automático em Docker (local)
- ✅ Infraestrutura completa em Terraform (AWS)
- ✅ CI/CD com GitHub Actions
- ✅ RDS PostgreSQL multi-AZ em produção
- ✅ Load Balancer com health checks
- ✅ Segurança em múltiplas camadas

---

## 💻 Stack Tecnológica

| Camada | Tecnologia | Versão |
|--------|-----------|---------|
| **Backend Language** | Java | 17 LTS |
| **Backend Framework** | Spring Boot | 4.0.0 |
| **Backend Data** | Spring Data JPA | 4.0.0 |
| **Frontend Library** | React | 18.3.1 |
| **Frontend Language** | TypeScript | 5.8 |
| **Frontend Styling** | Tailwind CSS | 3.4 |
| **Frontend Components** | Shadcn/UI | latest |
| **Frontend Build** | Vite | 5.4.21 |
| **Database** | PostgreSQL | 16.11 |
| **Backend Build** | Maven | 3.9+ |
| **Containerization** | Docker & Docker Compose | latest |
| **Infrastructure** | AWS (EC2, RDS, ALB) | - |
| **IaC** | Terraform | 1.5+ |
| **Backend Tests** | JUnit 5, Mockito | 5.10, 5.7 |
| **Frontend Tests** | Vitest, React Testing Library | 1.0+, 14.1+ |

---

## 🏗️ Arquitetura do Sistema

### Fluxo de Requisições

```
┌─────────────────────────────────────────────────────────────────┐
│                    CLIENTE (Browser)                            │
│                    http://localhost:3000                        │
└────────────────────────────┬────────────────────────────────────┘
                             │
                    ┌────────▼────────┐
                    │  React Frontend │
                    │  TypeScript 5.8 │
                    │   Tailwind CSS  │
                    └────────┬────────┘
                             │
                    ┌────────▼────────────┐
                    │  API Calls (Fetch) │
                    │  CORS Habilitado   │
                    └────────┬────────────┘
                             │
        ┌────────────────────▼──────────────────────┐
        │      Spring Boot Backend (Port 8080)      │
        │  ┌──────────────────────────────────────┐ │
        │  │     REST Controllers (V1)            │ │
        │  ├──────────────────────────────────────┤ │
        │  │  • CustomerController                │ │
        │  │  • AccountController                 │ │
        │  │  • TransactionController             │ │
        │  │  • StatementController               │ │
        │  └──────────────────────────────────────┘ │
        │                    │                      │
        │  ┌─────────────────▼───────────────────┐ │
        │  │     Service Layer (Business Logic) │ │
        │  ├──────────────────────────────────────┤ │
        │  │  • CustomerService                  │ │
        │  │  • AccountService                   │ │
        │  │  • TransactionService               │ │
        │  │  • StatementService                 │ │
        │  └──────────────────────────────────────┘ │
        │                    │                      │
        │  ┌─────────────────▼───────────────────┐ │
        │  │  Repository Layer (JPA)             │ │
        │  ├──────────────────────────────────────┤ │
        │  │  • CustomerRepository                │ │
        │  │  • AccountRepository                 │ │
        │  │  • TransactionRepository             │ │
        │  └──────────────────────────────────────┘ │
        └────────────────────┬──────────────────────┘
                             │
                    ┌────────▼──────────┐
                    │  PostgreSQL 16    │
                    │  (Database)       │
                    │  Tables:          │
                    │  • customers      │
                    │  • accounts       │
                    │  • transactions   │
                    └───────────────────┘
```

### Estrutura de Camadas (Backend)

```
📦 Spring Boot Application
│
├── 🎛️ Controllers (Entrada de Requisições)
│   └─ Recebem requisições HTTP, validam input, chamam serviços
│
├── 💼 Services (Lógica de Negócio)
│   └─ Implementam regras de negócio, orquestram repositórios
│
├── 🗄️ Repositories (Acesso a Dados)
│   └─ Interface com banco de dados via JPA
│
├── 🏗️ Entity/Models (Estrutura de Dados)
│   └─ Mapeamento das tabelas do banco
│
├── 📤 DTOs (Contato com Cliente)
│   └─ Request e Response para API
│
└── ⚙️ Utilities & Config
    └─ Tratamento de erros, CORS, validações globais
```

---

## 🎨 Frontend - Stack Detalhado

### Dependências Principais

```json
{
  "react": "^18.3.1",
  "react-router-dom": "^6",
  "typescript": "^5.8",
  "tailwindcss": "^3.4",
  "vite": "^5.4.21",
  "vitest": "^1.0.0",
  "@testing-library/react": "^14.1.2"
}
```

### 📁 Estrutura de Diretórios

```
front-end/src/
├── components/              # Componentes reutilizáveis
│   ├── auth/               # Login, Register
│   ├── layout/             # Navigator, Footer
│   └── ui/                 # Botões, inputs, etc
│
├── pages/                  # Páginas (rotas principais)
│   ├── LoginPage.tsx
│   ├── DashboardPage.tsx
│   └── StatementPage.tsx
│
├── context/                # Context API para estado global
│   └── AuthContext.tsx     # Gerenciamento de autenticação
│
├── services/               # Chamadas de API
│   └── api.ts             # Cliente HTTP (fetch wrapper)
│
├── utils/                  # Funções utilitárias
│   ├── formatters.ts       # Formatação de moeda, data, etc
│   └── validators.ts       # Validações comuns
│
└── test/
    └── setup.ts            # Configuração de mocks globais
```

### Fluxo de Autenticação (Frontend)

```
User Input (Login Form)
         │
         ▼
   Validação Local
         │
         ▼
   API Call (fetch)
         │
         ▼
   Backend Valida
         │
         ├─ Sucesso ──▶ Salva Token (localStorage)
         │                      │
         │                      ▼
         │             AuthContext atualiza
         │                      │
         │                      ▼
         │             Redireciona para Dashboard
         │
         └─ Erro ────▶ Exibe Mensagem de Erro
```

---

## 📡 Backend - Arquitetura

### Estrutura de Diretórios

```
back-end/src/main/java/com/backend/keysbankapi/
├── customer/                       # Gerenciamento de Clientes
│   ├── Customer.java              # Entidade JPA
│   ├── CustomerController.java    # REST Controller
│   ├── CustomerService.java       # Lógica de negócio
│   ├── CustomerRepository.java    # Acesso a dados (JPA)
│   └── dto/                        # Data Transfer Objects
│       ├── CreateCustomerRequest.java
│       └── CustomerResponse.java
│
├── account/                        # Gerenciamento de Contas
│   ├── Account.java               # Entidade JPA
│   ├── AccountController.java     # REST Controller
│   ├── AccountService.java        # Lógica de negócio
│   ├── AccountRepository.java     # Acesso a dados (JPA)
│   └── dto/
│       ├── CreateAccountRequest.java
│       └── AccountResponse.java
│
├── ledger/                         # Sistema de Transações
│   ├── Transaction.java           # Entidade JPA
│   ├── TransactionController.java # REST Controller
│   ├── TransactionService.java    # Lógica de transações
│   ├── TransactionRepository.java # Acesso a dados (JPA)
│   ├── StatementController.java   # Extrato bancário
│   ├── StatementService.java      # Lógica de extrato
│   └── dto/
│       ├── TransactionCreatedRequest.java
│       ├── TransactionCreatedResponse.java
│       └── StatementItemResponse.java
│
├── common/                         # Utilidades Compartilhadas
│   ├── ApiError.java              # Classe de erro padrão
│   └── GlobalExceptionHandler.java # Tratamento global de exceções
│
└── KeysbankapiApplication.java    # Classe main (Spring Boot)
```

### Padrão de Arquitetura (3 Camadas)

```
┌──────────────────────────────────────────────────────────────────┐
│                      HTTP Requests                               │
└───────────────────────────────────────┬──────────────────────────┘
                                       │
                                       ▼
                        ┌─────────────────────────┐
                        │  Controllers (REST)     │
                        │  - Validação básica     │
                        │  - Mapeamento de rotas  │
                        │  - Serialização JSON    │
                        └────────────┬────────────┘
                                    │
                        ┌───────────▼──────────┐
                        │  Services (Lógica)   │
                        │  - Regras de negócio │
                        │  - Validações        │
                        │  - Orquestração      │
                        └────────────┬─────────┘
                                    │
                        ┌───────────▼──────────┐
                        │ Repositories (Dados) │
                        │  - JPA/SQL           │
                        │  - Queries           │
                        │  - Persistência      │
                        └────────────┬─────────┘
                                    │
                                    ▼
                        ┌────────────────────┐
                        │  PostgreSQL 16     │
                        │  (Database)        │
                        └────────────────────┘
```

### Principais Tecnologias Backend

| Componente | Tecnologia | Versão | Propósito |
|-----------|-----------|--------|----------|
| **Framework** | Spring Boot | 4.0.0 | Web framework e DI |
| **Persistence** | Spring Data JPA | 4.0.0 | ORM e queries |
| **Validation** | Jakarta Validation | 3.0.2 | Validação de dados |
| **API Docs** | SpringDoc OpenAPI | 2.0 | Swagger/OpenAPI |
| **Banco de Dados** | PostgreSQL | 16 | Banco relacional |
| **Migrations** | Flyway | 9.22 | Versionamento de schema |
| **Build** | Maven | 3.9+ | Compilação e testes |
| **Testing** | JUnit 5 | 5.10 | Framework de testes |
| **Mocking** | Mockito | 5.7 | Mocks para testes |

### Fluxo de Requisição (Exemplo: Criar Cliente)

```
POST /customers
  ├─ Body: { "name": "João", "email": "joao@email.com" }
  │
  ├─ CustomerController.createCustomer()
  │   ├─ Valida sintaxe JSON
  │   ├─ Chama CustomerService
  │   └─ Retorna 201 + Response
  │
  ├─ CustomerService.createCustomer()
  │   ├─ Valida email não duplicado
  │   ├─ Valida nome não vazio
  │   ├─ Chama CustomerRepository.save()
  │   └─ Retorna Customer criado
  │
  └─ CustomerRepository.save()
      ├─ Mapeia entity para tabela
      ├─ Executa INSERT SQL
      └─ Retorna record inserido

Response 201 Created
{
  "id": "71475965-0ea9-46e7-87c7-ca98320189af",
  "name": "João",
  "email": "joao@email.com"
}
```

### Entidades Principais

**Customer (Cliente)**
```
- id: UUID (PK)
- name: String (not null)
- email: String (unique, not null)
- createdAt: LocalDateTime
- accounts: List<Account> (1:N)
```

**Account (Conta Bancária)**
```
- id: UUID (PK)
- customerId: UUID (FK → Customer)
- agency: String (0001)
- accountNumber: String (único)
- balance: BigDecimal
- createdAt: LocalDateTime
- transactions: List<Transaction> (1:N)
```

**Transaction (Transação)**
```
- id: UUID (PK)
- accountId: UUID (FK → Account)
- type: Enum (CREDIT, DEBIT, BALANCE)
- category: Enum (BONUS, SAQUE, DEPOSITO, etc)
- amount: BigDecimal
- description: String
- createdAt: LocalDateTime
```

### Configurações Importantes

**application.yaml:**
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/bank
    username: bankuser
    password: bankpass123
  
  jpa:
    hibernate:
      ddl-auto: none  # Flyway controla schema
  
  flyway:
    locations: classpath:db/migration

server:
  servlet:
    context-path: /
```

### Scripts Maven Úteis

```bash
cd back-end

# Compilar e testar
mvn clean verify

# Executar específico
mvn spring-boot:run

# Build apenas (sem testes)
mvn clean package -DskipTests

# Atualizar dependências
mvn dependency:tree

# Gerar SBOM (software bill of materials)
mvn org.cyclonedx:cyclonedx-maven-plugin:generatePackageSbom
```

---

## 🔌 API - Endpoints Principais

### 1. Clientes (Customers)

#### Criar Cliente
**POST** `/customers`

```json
// Request
{
  "name": "João Silva",
  "email": "joao.silva@email.com"
}

// Response (201 Created)
{
  "id": "71475965-0ea9-46e7-87c7-ca98320189af",
  "name": "João Silva",
  "email": "joao.silva@email.com"
}
```

**Validações:**
- `name`: Obrigatório, não vazio
- `email`: Obrigatório, formato válido, deve ser único

### 2. Contas (Accounts)

#### Criar Conta
**POST** `/accounts`

```json
// Request
{
  "customerId": "71475965-0ea9-46e7-87c7-ca98320189af"
}

// Response (201 Created)
{
  "id": "b837e6e2-1b3c-4267-825f-741fb798f066",
  "customerId": "71475965-0ea9-46e7-87c7-ca98320189af",
  "agency": "0001",
  "accountNumber": "343316"
}
```

**Regras:**
- Cria automaticamente uma transação de bônus de R$ 100,00
- Número da conta gerado automaticamente

#### Login (Validar Conta)
**GET** `/accounts/login?agency=0001&accountNumber=343316`

```json
// Response (200 OK)
{
  "accountId": "b837e6e2-1b3c-4267-825f-741fb798f066",
  "agency": "0001",
  "accountNumber": "343316",
  "customerName": "João Silva"
}
```

### 3. Transações

#### Criar Transação
**POST** `/transaction`

```json
// Request
{
  "accountId": "b837e6e2-1b3c-4267-825f-741fb798f066",
  "type": "DEBIT",
  "category": "SAQUE",
  "amount": 50.00,
  "description": "Saque no caixa eletrônico"
}

// Response (201 Created)
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "type": "DEBIT",
  "category": "SAQUE",
  "amount": 50.00,
  "description": "Saque no caixa eletrônico",
  "createdAt": "2026-01-15T22:45:34.000Z"
}
```

**Validações:**
- `accountId`: Obrigatório, UUID válido
- `type`: Obrigatório (`CREDIT` ou `DEBIT`)
- `category`: Obrigatório (ex: `SAQUE`, `DEPOSITO`, `TRANSFERENCIA`)
- `amount`: Obrigatório, deve ser positivo

### 4. Extrato (Statement)

#### Visualizar Extrato
**GET** `/accounts/{accountId}/statement`

**Query Parameters (Opcionais):**
- `startDate`: Data inicial (formato YYYY-MM-DD)
- `endDate`: Data final (formato YYYY-MM-DD)
- `type`: Filtrar por tipo (`CREDIT`, `DEBIT`, ou `BALANCE`)

```json
// Response (200 OK)
[
  {
    "transactionId": "550e8400-e29b-41d4-a716-446655440000",
    "type": "BALANCE",
    "category": "DAILY_BALANCE",
    "amount": 50.00,
    "description": "Saldo do dia",
    "createdAt": "2026-01-15T23:59:59.000Z"
  },
  {
    "transactionId": "660e8400-e29b-41d4-a716-446655440001",
    "type": "DEBIT",
    "category": "SAQUE",
    "amount": 50.00,
    "description": "Saque no caixa eletrônico",
    "createdAt": "2026-01-15T22:45:34.000Z"
  }
]
```

### 📊 Tipos de Transações

**Type:**
- `CREDIT`: Crédito/Depósito
- `DEBIT`: Débito/Saque
- `BALANCE`: Saldo diário (gerado automaticamente)

**Category:**
- `BONUS_ABERTURA`: Bônus inicial de R$ 100
- `SAQUE`: Saque de dinheiro
- `DEPOSITO`: Depósito de dinheiro
- `TRANSFERENCIA`: Transferência bancária
- `DAILY_BALANCE`: Saldo diário automático

### 🌐 Exemplos com cURL

```bash
# Criar cliente
curl -X POST http://localhost:8080/customers \
  -H "Content-Type: application/json" \
  -d '{"name": "João Silva", "email": "joao.silva@email.com"}'

# Criar conta
curl -X POST http://localhost:8080/accounts \
  -H "Content-Type: application/json" \
  -d '{"customerId": "71475965-0ea9-46e7-87c7-ca98320189af"}'

# Login
curl "http://localhost:8080/accounts/login?agency=0001&accountNumber=343316"

# Criar transação
curl -X POST http://localhost:8080/transaction \
  -H "Content-Type: application/json" \
  -d '{"accountId": "b837e6e2-1b3c-4267-825f-741fb798f066", "type": "DEBIT", "category": "SAQUE", "amount": 50.00, "description": "Saque"}'

# Visualizar extrato
curl "http://localhost:8080/accounts/b837e6e2-1b3c-4267-825f-741fb798f066/statement"

# Extrato com filtros
curl "http://localhost:8080/accounts/b837e6e2-1b3c-4267-825f-741fb798f066/statement?startDate=2026-01-01&type=CREDIT"
```

### ⚠️ Tratamento de Erros

Formato padrão de erro:
```json
{
  "status": 400,
  "error": "Bad Request",
  "message": "Descrição do erro",
  "path": "/endpoint",
  "timestamp": "2026-01-16T02:01:49.765021Z"
}
```

**Códigos de Erro:**
- `400`: Validação falhou (campos obrigatórios, formato inválido)
- `404`: Recurso não encontrado (conta, cliente, transação)
- `500`: Erro no servidor

---

## � Deploy

Esta seção guia você através do deploy local (Docker) e em produção (AWS).

### Deploy Local (Docker Compose)

**Ambiente:** 🐳 Docker, ideal para desenvolvimento e testes

```bash
cd infra

# 1. Configurar variáveis de ambiente (copia template)
cp .env.example .env

# 2. Executar deploy
./scripts/deploy.sh local
```

**Serviços iniciados:**
- 🐘 **PostgreSQL 16:** Banco de dados (porta 5432)
- ☕ **Backend API:** Spring Boot (porta 8080)
- ⚛️ **Frontend:** React (porta 3000)
- 🛠️ **Adminer** (opcional): UI para BD

**Acessar aplicação:**

| Componente | URL | Descrição |
|-----------|-----|-----------|
| Frontend | http://localhost:3000 | Aplicação React |
| Backend API | http://localhost:8080 | Spring Boot REST |
| Swagger/OpenAPI | http://localhost:8080/swagger-ui/index.html | Documentação interativa |
| Adminer (se ativado) | http://localhost:8081 | UI do banco |
| PostgreSQL | localhost:5432 | Banco de dados |

**Após mudanças no código:**
```bash
# Reconstruir e reiniciar
cd infra
./scripts/deploy.sh local

# Ou acessar logs
docker-compose logs -f backend   # Logs do backend
docker-compose logs -f frontend  # Logs do frontend
```

### Deploy AWS (EC2 + RDS + Terraform)

**Ambiente:** ☁️ Production-ready, com alta disponibilidade, auto-scaling, e backups automáticos

#### 📋 Pré-requisitos

1. **AWS Account** com permissões EC2, RDS, VPC
2. **AWS CLI** instalado e configurado (`aws configure`)
3. **Terraform** v1.5+
4. **SSH Key** criada na AWS

#### 🚀 Setup Inicial (Primeira Vez)

```bash
# 1. Autenticar com AWS
aws configure
# Adicione: Access Key, Secret Key, Region (sa-east-1), Output format (json)

# 2. Criar chave SSH para acesso às instâncias
aws ec2 create-key-pair \
  --key-name keysbank-dev-key \
  --region sa-east-1 \
  --query 'KeyMaterial' \
  --output text > ~/.ssh/keysbank-dev-key.pem

chmod 400 ~/.ssh/keysbank-dev-key.pem

# 3. Configurar Terraform para AWS
cd infra/terraform/environments/dev
cp terraform.tfvars.example terraform.tfvars

# Edite terraform.tfvars com suas credenciais:
# - AWS region
# - DB password
# - Environment name
```

#### 🔧 Deploy para AWS

```bash
# Do diretório infra/
cd infra

# Executar deploy AWS completo
./scripts/deploy.sh aws
```

**O script executa automaticamente:**

| Passo | Descrição | Tempo |
|------|-----------|-------|
| 1️⃣ **Test Backend** | Executa 17 testes JUnit 5 | ~1 min |
| 2️⃣ **Test Frontend** | Executa 26 testes Vitest | ~30 seg |
| 3️⃣ **Build Backend** | Maven clean package | ~2 min |
| 4️⃣ **Build Frontend** | npm build (Vite) | ~1 min |
| 5️⃣ **Terraform Init** | Inicializa Terraform | ~30 seg |
| 6️⃣ **Terraform Plan** | Planeja infraestrutura | ~1 min |
| 7️⃣ **Terraform Apply** | Cria recursos AWS | ~5-10 min |
| 8️⃣ **Deploy Backend** | SSH + systemctl restart | ~2 min |
| 9️⃣ **Deploy Frontend** | S3 upload + CloudFront | ~2 min |
| 🔟 **Health Check** | Valida endpoints | ~1 min |

**Tempo total estimado:** 15-20 minutos (primeira vez)

#### � Acessar Aplicação na AWS

Após o deploy bem-sucedido, a aplicação estará disponível em:

| Componente | URL | Descrição |
|-----------|-----|-----------|
| **Frontend (CDN)** | https://d39jyhz7f6842h.cloudfront.net | React App via CloudFront (recomendado) |
| **Backend API** | https://d39jyhz7f6842h.cloudfront.net/api | API REST através do CloudFront |
| **Swagger/OpenAPI** | https://d39jyhz7f6842h.cloudfront.net/api/swagger-ui/index.html | Documentação interativa |
| **ALB Direto** | http://keysbank-dev-alb-838873394.sa-east-1.elb.amazonaws.com | Load Balancer (sem CDN) |

**Notas:**
- CloudFront fornece cache global e melhor performance
- O ALB é o endpoint interno, normalmente não é acessado diretamente
- HTTPS está ativado automaticamente via CloudFront
- Health checks são executados a cada 30 segundos

#### 📊 Verificar Status da Implantação

```bash
# Ver saídas do Terraform (incluindo URLs)
cd infra/terraform/environments/dev
terraform output

# Conferir CloudFront status
aws cloudfront get-distribution \
  --id E1GSFEU8VTDSRQ \
  --query 'Distribution.Status'

# Testar endpoints
curl https://d39jyhz7f6842h.cloudfront.net/api/actuator/health
```

#### �🏗️ Infraestrutura AWS Criada

```
┌─────────────────────────────────────────────────────┐
│          AWS Region (sa-east-1)                     │
│                                                     │
│  ┌───────────────────────────────────────────────┐ │
│  │ VPC (Virtual Private Cloud)                   │ │
│  │ CIDR: 10.0.0.0/16                            │ │
│  │                                               │ │
│  │ ┌─────────────────────────────────────────┐  │ │
│  │ │ Public Subnets                          │  │ │
│  │ │ ├─ ALB (Application Load Balancer)      │  │ │
│  │ │ └─ NAT Gateways                         │  │ │
│  │ └─────────────────────────────────────────┘  │ │
│  │                     ↓                         │ │
│  │ ┌─────────────────────────────────────────┐  │ │
│  │ │ Private Subnets                         │  │ │
│  │ │ ├─ EC2 Backend 1 (t3.micro)            │  │ │
│  │ │ ├─ EC2 Backend 2 (t3.micro)            │  │ │
│  │ │ └─ RDS PostgreSQL 16 (db.t3.micro)     │  │ │
│  │ │    └─ Multi-AZ Standby                 │  │ │
│  │ └─────────────────────────────────────────┘  │ │
│  │                                               │ │
│  │ Security Groups:                              │ │
│  │ ├─ ALB: Permite 80, 443 de qualquer IP       │ │
│  │ ├─ EC2: Permite 8080 apenas de ALB           │ │
│  │ └─ RDS: Permite 5432 apenas de EC2           │ │
│  └───────────────────────────────────────────────┘ │
└─────────────────────────────────────────────────────┘
```

**Componentes provisionados:**

- **VPC:** Rede isolada com 2 subnets públicas + 2 privadas
- **EC2:** 1-5 instâncias t3.micro com Java 17 + Nginx + Spring Boot (Auto Scaling)
- **RDS:** PostgreSQL 16 gerenciado, backup automático 7 dias, Multi-AZ standby
- **ALB:** Load Balancer com health checks, HTTPS (opcional)
- **Security Groups:** Firewall com regras de mínimo privilégio
- **CloudWatch:** Logs automáticos, métricas, alarmes

#### 📘 Terraform State Management

```bash
# Ver estado atual da infraestrutura
cd terraform/environments/dev
terraform show

# Ver outputs (IPs, endpoints, etc)
terraform output

# Listar recursos
terraform state list
```

**Arquivo de state:**
- Localização: `infra/terraform/environments/dev/terraform.tfstate`
- ⚠️ **IMPORTANTE:** Fazer backup regular
- Contém senhas e configurações sensíveis - **NÃO commitar no git**

#### 🔄 Atualizar Aplicação em Produção

```bash
# Após fazer push do código
cd infra
./scripts/deploy.sh aws

# Script detecta mudanças e:
# ✅ Testa código (backend + frontend)
# ✅ Faz build das novas versões
# ✅ Atualiza infraestrutura se necessário
# ✅ Faz deploy das novas versões
# ✅ Verifica saúde dos serviços
```

#### ⚠️ Destruir Ambiente AWS

```bash
# Remover TODOS os recursos AWS (cuidado!)
cd infra
./scripts/destroy.sh aws

# Será solicitado confirmação:
# ❓ Digite "DESTRUIR" para confirmar
# Tempo: ~3-5 minutos
```

---

## 🏗️ Infraestrutura AWS

### Estrutura de Diretórios

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

### Arquitetura AWS

```
                                Internet
                                   |
                               [IGW]
                                   |
                    ┌──────────────┴──────────────┐
                    │                             │
             [Public Subnet 1]         [Public Subnet 2]
                    │                             │
                 [ALB]─────────────────────────[ALB]
                    │                             │
            [EC2 Backend 1]              [EC2 Backend 2]
                    │                             │
               [NAT GW]──────────────────────[NAT GW]
                    │                             │
             [Private Subnet 1]        [Private Subnet 2]
                    │                             │
                    └──────────┬──────────────────┘
                               │
                          [RDS Primary]
                               │
                       [RDS Standby - Multi-AZ]
```

### Configurações por Ambiente

#### Development (dev)
- EC2: t3.micro (1-5 instâncias com Auto Scaling)
- RDS: db.t3.micro (Single-AZ)
- Backups: 7 dias
- Custo estimado: ~$50-70/mês (sem free tier)

#### Production (prod)
- EC2: t3.micro (2+ instâncias com Auto Scaling)
- RDS: db.t3.micro (Multi-AZ recomendado)
- Backups: 30 dias
- SSL: Obrigatório
- Custo estimado: ~$100-150/mês

### Terraform - Comandos Básicos

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

# Validar código
terraform validate

# Formatar código
terraform fmt
```

### Docker Compose - Serviços

**postgres:** PostgreSQL 16 Alpine
- Health checks configurados
- Volume persistente

**backend:** Spring Boot API
- Build automático
- Conecta ao PostgreSQL
- Health checks

**frontend:** React + Nginx
- Build multi-stage
- Serve arquivos estáticos
- Proxy reverso para API

**adminer:** Database UI (opcional)
- Profile: `tools`
- Acesso: http://localhost:8081

### Docker Compose - Comandos

```bash
# Iniciar serviços
docker-compose up -d

# Ver logs
docker-compose logs -f

# Ver status
docker-compose ps

# Parar serviços
docker-compose stop

# Remover containers e volumes
docker-compose down -v

# Iniciar com tools (adminer)
docker-compose --profile tools up -d
```

---

## 🔐 Segurança

### Implementado
- ✅ Validação em múltiplas camadas
- ✅ CORS configurado
- ✅ Variáveis sensíveis em GitHub Secrets
- ✅ SQL parameterizado (prevenção SQL injection)
- ✅ Senhas e secrets via variáveis de ambiente
- ✅ Terraform state remoto com encryption
- ✅ Security Groups com princípio do menor privilégio
- ✅ RDS com encryption at rest
- ✅ Multi-AZ para alta disponibilidade (prod)

### Secrets Management

**Local:** Arquivo `.env` (não commitado)
**AWS:**
- Terraform: `terraform.tfvars` (não commitado)
- Runtime: AWS Secrets Manager (recomendado para prod)
- CI/CD: GitHub Secrets

---

## 💰 Custos AWS

### Estimativa Mensal

**Development:**
- EC2 t3.micro: $0 (free tier 1º ano) ou ~$7/mês
- RDS db.t3.micro: $0 (free tier 1º ano) ou ~$14/mês
- ALB: ~$16/mês
- NAT Gateway: ~$32/mês
- EBS Storage: ~$2/mês
- Data Transfer: ~$3/mês
- **Total: ~$53-70/mês** (ou ~$7/mês com free tier ativo)

**Production:**
- EC2 t3.micro x2: ~$14/mês
- RDS db.t3.micro Multi-AZ: ~$28/mês
- ALB: ~$16/mês
- NAT Gateway x2: ~$64/mês
- EBS Storage: ~$10/mês
- Backups: ~$8/mês
- Data Transfer: ~$20
- **Total: ~$245/mês**

**Otimizações:**
- Use Reserved Instances (-30% a -50%)
- Savings Plans
- Remova NAT Gateway se não precisar
- Single-AZ para dev

---

## 🆘 Troubleshooting

### ✅ Quick Check - Tudo Funcionando?

```bash
# Teste rápido
cd infra

# Local
./scripts/validate-tests.sh     # Verifica ambos tests
docker-compose ps               # Verifica containers

# AWS
terraform output                # Verifica infraestrutura
./scripts/deploy.sh aws         # Verifica deploy
```

---

### 🐳 Docker Compose (Local)

#### ❌ Container Backend Não Inicia

**Sintomas:**
- `docker-compose up` falha para backend
- Backend container fica em estado `Exited` ou `Restarting`

**Solução:**

```bash
# 1. Ver logs detalhados
docker-compose logs backend

# 2. Verificar conectividade com PostgreSQL
docker-compose exec backend ping postgres

# 3. Verificar variáveis de ambiente
docker-compose config | grep -A5 backend

# 4. Reiniciar serviço
docker-compose restart backend

# 5. Se não funcionar, reconstruir imagem
docker-compose down -v
docker-compose build --no-cache backend
docker-compose up -d backend
```

#### ❌ Porta Já em Uso

**Sintomas:**
- `Address already in use` ao iniciar containers
- ALB ou outro serviço já usando as portas

**Solução:**

```bash
# Ver qual processo está usando porta
lsof -i :3000    # Frontend
lsof -i :8080    # Backend
lsof -i :5432    # PostgreSQL

# Opção 1: Matar processo (perigoso!)
kill -9 <PID>

# Opção 2: Preferível - Usar portas diferentes
# Editar docker-compose.yml ou .env:
BACKEND_PORT=8081
FRONTEND_PORT=3001
POSTGRES_PORT=5433

# Reinicar
docker-compose down -v
docker-compose up -d
```

#### ❌ Frontend Não Consegue Chamar Backend

**Sintomas:**
- CORS errors no console do navegador
- Network errors ao carregar dados
- `localhost:3000` carrega mas sem dados

**Solução:**

```bash
# 1. Verificar se backend está rodando
curl http://localhost:8080/customers  # Deve retornar erro válido (401/200)

# 2. Verificar CORS no backend
grep -A10 "CorsConfigurer\|@CrossOrigin" back-end/src/main/java/*/config/*

# 3. Verificar configuração de porta do frontend
grep -r "8080\|API_URL\|localhost" front-end/src/ --include="*.ts" --include="*.tsx"

# 4. Limpar cache do navegador
# Chrome: Ctrl+Shift+Del → Cookies/cached -> Clear
# Ou use incognito window
```

---

### 🗄️ Banco de Dados (PostgreSQL)

#### ❌ Conexão com Banco Falha

**Sintomas:**
- `Unable to acquire a Connection`
- `FATAL: password authentication failed`
- `connection refused`

**Solução:**

```bash
# 1. Verificar se PostgreSQL está rodando
docker-compose logs postgres

# 2. Testar conectividade
docker-compose exec postgres psql -U bankuser -d bank -c "SELECT 1;"

# 3. Verificar credenciais em .env
grep -i postgres .env

# 4. Reconectar diretamente
psql -U bankuser -d bank -h localhost -p 5432

# 5. Se falhar, resetar banco
docker-compose down -v
docker-compose up -d postgres
# Aguardar ~5 segundos para inicializar
sleep 5
docker-compose up -d backend
```

#### ❌ Dados Perdidos Após Restart

**Solução:**

```bash
# Usar volumes nomeados (persistência)
docker volume ls  # Verificar volumes

# Evitar 'docker-compose down -v' que remove volumes
# Use 'docker-compose down' sem -v

# Backup do banco antes de destruir
docker-compose exec postgres pg_dump -U bankuser -d bank > backup.sql

# Restaurar se necessário
docker-compose exec -T postgres psql -U bankuser -d bank < backup.sql
```

---

### ✅ Testes Falhando

#### ❌ Backend Tests Falhando

**Sintomas:**
- `mvn test` retorna failures
- Testes específicos falhando

**Solução:**

```bash
cd back-end

# 1. Limpar e tentar novamente
mvn clean test

# 2. Testar classe específica
mvn test -Dtest=CustomerServiceTest

# 3. Com saída verbosa
mvn test -e -X

# 4. Se problema com BD
# Garantir PostgreSQL está disponível
docker-compose up -d postgres
mvn test

# 5. Último recurso
mvn clean install -DskipTests
mvn test  # Rodar após build bem-sucedido
```

#### ❌ Frontend Tests Falhando

**Sintomas:**
- `npm run test` retorna failures
- Testes específicos falhando

**Solução:**

```bash
cd front-end

# 1. Limpar cache e dependências
rm -rf node_modules package-lock.json
npm install

# 2. Rodar testes
npm run test:ci

# 3. Com saída verbose
npm run test -- --reporter=verbose

# 4. Testar arquivo específico
npm run test src/utils/formatters.test.ts

# 5. Checar timezone issues (formatação de datas)
# Editar vitest.config.ts se necessário
```

---

### 🚀 Deploy Falhando

#### ❌ Deploy Local Falha

**Sintomas:**
- `./scripts/deploy.sh local` com erros
- Build falha

**Solução:**

```bash
cd infra

# 1. Ver output completo do erro
./scripts/deploy.sh local 2>&1 | tee deploy.log

# 2. Limpar estado anterior
docker-compose down -v
./scripts/deploy.sh local

# 3. Se falha no build:
# Backend
cd ../back-end
mvn clean package

# Frontend
cd ../front-end
npm ci  # Limpar install
npm run build
```

#### ❌ Deploy AWS Falha

**Deploy Falhando - Passo que Falha?**

```bash
# 1. Testar AWS CLI
aws ec2 describe-instances
aws sts get-caller-identity

# 2. Verificar credenciais
cat ~/.aws/credentials
cat ~/.aws/config

# 3. Testar Terraform
cd infra/terraform/environments/dev
terraform plan

# 4. Ver em qual passo do script falha
./scripts/deploy.sh aws 2>&1 | tail -100
```

**Terraform Apply Falha:**

```bash
# Verificar syntax
terraform validate

# Retentar apply
terraform apply

# Limpar estado problemático
terraform taint aws_instance.backend
terraform apply
```

**SSH para EC2 Falha:**

```bash
# 1. Verificar chave SSH
ls -la ~/.ssh/keysbank-dev-key.pem
chmod 400 ~/.ssh/keysbank-dev-key.pem

# 2. Encontrar IP da instância
aws ec2 describe-instances --query 'Reservations[].Instances[].PublicIpAddress'

# 3. Testar SSH
ssh -i ~/.ssh/keysbank-dev-key.pem -vvv ec2-user@<IP>

# 4. Se Security Group bloqueia:
aws ec2 modify-security-group-rules --group-id sg-xxx \
  --security-group-rules '{"IpProtocol":"tcp","FromPort":22,"ToPort":22,"IpRanges":[{"CidrIp":"0.0.0.0/0"}]}'
```

**Backend não responde após deploy:**

```bash
# SSH para EC2
ssh -i ~/.ssh/keysbank-dev-key.pem ec2-user@<EC2_IP>

# Verificar status do serviço
sudo systemctl status keysbank-backend
sudo systemctl restart keysbank-backend

# Ver logs
sudo journalctl -u keysbank-backend -n 50
sudo tail -f /var/log/keysbank/app.log
```

---

### ☁️ Terraform Issues

#### ❌ State Lock

**Sintomas:**
- `Error: error acquiring the state lock`
- Aparecem locks antigos

**Solução:**

```bash
cd terraform/environments/dev

# Forçar unlock (CUIDADO - pode corromper estado)
terraform force-unlock <LOCK_ID>

# Melhor: Esperar ou investigar
# Ver quem está com o lock
cat .terraform.tfstate.lock.current

# Depois tenta novamente
terraform apply
```

#### ❌ Recursos Já Existem

**Sintomas:**
- Erro ao criar recurso que já existe
- `Error: error creating xxx: xxx already exists`

**Solução:**

```bash
# Importar recurso existente para state
terraform import aws_instance.backend i-1234567890abcdef0

# Listar recursos no state
terraform state list

# Remover if needed (cuidado)
terraform state rm aws_instance.backend
```

---

### 🏥 Monitoramento de Integridade

#### Health Check

```bash
# Verificar saúde da aplicação
# Local
curl http://localhost:8080/actuator/health
curl http://localhost:3000

# AWS (substituir IP)
curl http://<ALB_IP>/actuator/health
curl http://<ALB_IP>
```

#### Logs

```bash
# Local
docker-compose logs -f                          # Todos
docker-compose logs -f backend                  # Apenas backend
docker-compose logs -f backend --tail 100       # Últimas 100 linhas

# AWS (via SSH)
sudo journalctl -u keysbank-backend -f          # Live log
sudo journalctl -u keysbank-backend -n 100      # Últimas 100 linhas
sudo tail -f /var/log/keysbank/app.log
```

#### Database

```bash
# Ver tamanho do banco
docker-compose exec postgres du -sh /var/lib/postgresql/data

# Backup rápido
docker-compose exec postgres pg_dump -U bankuser bank > backup.sql

# Listar tabelas
docker-compose exec postgres psql -U bankuser -d bank -c "\\dt"

# Ver conexões ativas
docker-compose exec postgres psql -U bankuser -d bank -c "SELECT * FROM pg_stat_activity;"
```

---

## � Best Practices

### Backend (Spring Boot + Java)

#### 1. **Validação em Múltiplas Camadas**
```java
// ✅ BOAS PRÁTICAS
// Controller: Validação básica
// Service: Lógica de negócio
// Repository: Constraints BD

@PostMapping
public ResponseEntity createCustomer(@Valid @RequestBody CreateCustomerRequest req) {
    return customerService.create(req);  // Service valida regras de negócio
}
```

#### 2. **DTOs para Comunicação**
```java
// ✅ Use DTOs, não entities direto
public recordCustomerResponse(UUID id, String name, String email) {}

// ❌ Expor entities diretamente
public class Customer { /* ... */ }  // Não retornar do controller
```

#### 3. **Exception Handling Centralizado**
```java
// ✅ GlobalExceptionHandler faz todo o tratamento
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(EntityNotFoundException.class)
    public ApiError handleNotFound(EntityNotFoundException e) {
        return new ApiError(404, "Not Found", e.getMessage());
    }
}
```

#### 4. **Logging Estruturado**
```java
// ✅ Log eventos importantes
logger.info("Conta criada: accountId={}, customerId={}", accountId, customerId);
logger.warn("Saldo insuficiente: required={}, available={}", required, available);
logger.error("Erro ao processar transação", exception);  // Sempre logar exceptions
```

#### 5. **Database Migrations**
```sql
-- Use Flyway para versionamento
-- V1__init.sql
-- V2__add_indexes.sql
-- V3__add_constraints.sql

-- Não use Hibernate ddl-auto=update em produção
```

### Frontend (React + TypeScript)

#### 1. **Type Safety Always**
```typescript
// ✅ Tipos bem definidos
interface Customer {
  id: string;
  name: string;
  email: string;
  createdAt: Date;
}

// ❌ any é o enemigo
const handleCustomer = (data: any) => { }  // Não faça isso!
```

#### 2. **Context API com Cuidado**
```typescript
// ✅ Apenas para dados globais (auth, theme)
const AuthContext = createContext<AuthState>(initialState);

// ❌ Não use Context para dados que mudam frequentemente
// Use Redux, Zustand ou Jotai para estado complexo
```

#### 3. **Component Organization**
```
components/
├── ui/           # Componentes puros (reutilizáveis)
├── layout/       # Componentes de estrutura
├── auth/         # Features específicas
└── dashboard/
```

#### 4. **Error Boundaries**
```typescript
// ✅ Sempre capture erros de componentes
class ErrorBoundary extends React.Component {
  componentDidCatch(error, errorInfo) {
    logger.error('Component error:', error);
    // Mostrar UI de erro ao usuário
  }
}
```

#### 5. **Performance**
```typescript
// ✅ Use React.memo para componentes puros
const TransactionItem = React.memo(({ transaction }) => (...));

// ✅ Lazy load componentes grandes
const Dashboard = lazy(() => import('./pages/Dashboard'));
```

### DevOps & Infrastructure

#### 1. **Secrets Management**
```bash
# ✅ Usar variáveis de ambiente para secrets
export DB_PASSWORD=$(aws secretsmanager get-secret-value --secret-id rds/password)

# ❌ Nunca commitar secrets no git
# .gitignore
terraform.tfvars
.env
*.pem
```

#### 2. **Environment Separation**
```
environments/
├── dev/
│   ├── terraform.tfvars
│   └── variables.tf
├── prod/
│   ├── terraform.tfvars
│   └── variables.tf
```

#### 3. **Backup & Recovery**
```bash
# ✅ Backup regular do Terraform state
aws s3 cp terraform.tfstate s3://backups/keysbank/

# ✅ Database backups automatizados
# RDS Multi-AZ + automated backups (7-30 dias)
```

#### 4. **Monitoring & Alerts**
```bash
# ✅ CloudWatch alarms para:
# - CPU > 80%
# - Memory > 85%
# - Disk > 90%
# - Error rate > 1%
```

#### 5. **Security**
```bash
# ✅ Security Groups com mínimo privilégio
# Frontend: apenas 80/443 de qualquer IP
# Backend: apenas 8080 do ALB
# RDS: apenas 5432 das EC2s

# ✅ HTTPS em produção
# Use ACM (AWS Certificate Manager) para SSL

# ✅ SSH keys com restrição de IP
# Não exponha SSH para 0.0.0.0/0
```

### Testing Standards

#### 1. **Test Coverage**
```bash
# ✅ Manter > 70% de cobertura
backend: 43% é mínimo aceitável mas apuntar para > 70%
frontend: 60%+ cobertura

# ✅ Testes críticos:
# - Validações
# - Fluxos de autenticação
# - Operações financeiras
```

#### 2. **Test Naming**
```bash
# ✅ Nomes descritivos
testCreateCustomerWithValidEmail
testThrowExceptionForDuplicateEmail
testTransactionDebitReducesBalance

# ❌ Nomes genéricos
test1
testCustomer
testFail
```

#### 3. **Mocking**
```java
// ✅ Mock apenas dependências externas
@Mock
private CustomerRepository customerRepository;

// ✅ Teste camada de negócio isolada
@InjectMocks
private CustomerService service;
```

---

## �📊 Monitoramento

### CloudWatch (AWS)

Terraform configura automaticamente:
- CloudWatch Logs para EC2
- RDS CloudWatch integration
- ALB metrics
- Custom metrics

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

---

## 🔄 CI/CD com GitHub Actions

### Workflows Automáticos

O repositório está configurado com GitHub Actions para deploy automático:

| Trigger | Workflow | Status |
|---------|----------|--------|
| Push em `back-end/**` ou `infra/**` | Backend Test → Build → Deploy | ✅ Active |
| Push em `front-end/**` | Frontend Test → Build → Deploy | ✅ Active |
| Pull Request | Run Tests (Backend + Frontend) | ✅ Active |
| Manual | Full Deploy (Local/AWS) | ✅ Available |

### GitHub Secrets Requeridos

Para deploy automático, configure estes secrets:

```bash
# Acessar: Repository Settings → Secrets → New repository secret

# AWS Credentials
AWS_ACCESS_KEY_ID              # ID da chave de acesso AWS
AWS_SECRET_ACCESS_KEY          # Chave secreta AWS
AWS_REGION                     # Região AWS (sa-east-1)

# Deployment Info
EC2_INSTANCE_IP                # IP público da instância EC2
EC2_PRIVATE_KEY                # Conteúdo completo do arquivo .pem

# Database
RDS_ENDPOINT                   # Endpoint do RDS
RDS_PASSWORD                   # Senha do banco de dados

# Terraform
TF_VAR_environment            # Nome do ambiente (dev/prod)
TF_VAR_db_password            # Senha do banco (Terraform)
```

**Como obter outputs do Terraform:**

```bash
cd infra/terraform/environments/dev
terraform output -raw ec2_instance_ip
terraform output -raw rds_endpoint
```

---

## 📚 Documentação Completa

### 📖 Estrutura de Documentação

```
KeysBank/
├── README.md                              # ← Este arquivo (overview completo)
├── DEPLOY_GUIDE.md                       # Guia detalhado de deployment
├── .github/workflows/                    # GitHub Actions automação
│   ├── backend-deploy.yml
│   ├── frontend-deploy.yml
│   └── tests.yml
│
├── back-end/
│   ├── README.md                         # Setup backend local
│   ├── API_DOCUMENTATION.md              # Documentação técnica API
│   └── openapi.html                      # Swagger UI (após build)
│
├── front-end/
│   ├── README.md                         # Setup frontend local
│   └── vite.config.ts                    # Configuração de build
│
└── infra/
    ├── README.md                         # Instruções de infraestrutura
    ├── terraform/                        # IaC documentation
    └── scripts/
        ├── deploy.sh                     # Deploy unificado
        └── validate-tests.sh             # Validação rápida
```

### 🔗 Links Úteis

| Recurso | Link | Descrição |
|---------|------|-----------|
| **OpenAPI/Swagger** | [/openapi.html](http://localhost:8080/swagger-ui/index.html) * | Documentação interativa da API |
| **Backend Setup** | [back-end/README.md](./back-end/README.md) | Instruções de setup backend |
| **Frontend Setup** | [front-end/README.md](./front-end/README.md) | Instruções de setup frontend |
| **Deploy Guide** | [DEPLOY_GUIDE.md](./DEPLOY_GUIDE.md) | Guia completo de deployment |
| **Infrastructure** | [infra/README.md](./infra/README.md) | Documentação de infraestrutura |

*Disponível após executar `mvn spring-boot:run` no backend

### 📑 Documentação por Tópico

**🎯 Começar Rápido:**
- [Quick Start (5 minutos)](# -quick-start-5-minutos)
- [Deploy Local Docker](# deploy-local-docker-compose)

**📡 Desenvolvimento:**
- [Arquitetura do Sistema](#-arquitetura-do-sistema)
- [Stack Tecnológica](#-stack-tecnológica)
- [Frontend Stack Detalhado](#-frontend---stack-detalhado)
- [Backend Arquitetura](#-backend---arquitetura)

**🧪 Testes:**
- [Testes e Cobertura](#-testes-e-cobertura)
- [Best Practices](#-best-practices)

**☁️ Deployment:**
- [Deploy Local](#deploy-local-docker-compose)
- [Deploy AWS](#deploy-aws-ec2--rds--terraform)
- [CI/CD com GitHub Actions](#-cicd-com-github-actions)

**🆘 Problemas:**
- [Troubleshooting](#-troubleshooting)
- [Best Practices](#-best-practices)

---

## 📖 Próximos Passos

### 🚀 Desenvolvimento

**Próximas Features Sugeridas:**

1. **Transferências Bancárias**
   - Endpoint POST `/transaction/transfer`
   - Validação de conta destino
   - Testes de duplicação

2. **Autenticação com JWT**
   - Replaced login simples com JWT
   - Refresh tokens
   - Role-based access control (RBAC)

3. **Notificações**
   - Email para transações
   - SMS para saque > R$ 1000
   - Push notifications (mobile)

4. **Relatórios**
   - Exportar extrato (PDF/CSV)
   - Gráficos de gastos por categoria
   - Dashboard de analytics

### 🏢 Infraestrutura (Production)

**Melhorias Recomendadas:**

1. **Auto Scaling**
   - Auto Scaling Group (ASG) para EC2
   - Escalas horizontais por CPU/memória

2. **CDN & Performance**
   - CloudFront para cachear frontend
   - Route 53 DNS gerenciado
   - ElastiCache (Redis) para sessões

3. **Segurança Avançada**
   - Web Application Firewall (WAF)
   - Secrets Manager para credenciais
   - VPN para acesso administrativo

4. **Disaster Recovery**
   - Multi-region failover
   - Backup estratégia (completo + incremental)
   - RTO/RPO definidos

5. **Observabilidade**
   - APM (Application Performance Monitoring)
   - Datadog, New Relic ou Prometheus + Grafana
   - Log aggregation (CloudWatch Logs Insights)

### 📊 Analytics & Growth

1. **Métricas de Negócio**
   - Usuários ativos diários (DAU)
   - Taxa de retenção
   - Valor transacional

2. **Performance**
   - P95 latency de endpoints
   - Taxa de erro
   - Cobertura de testes

---

## 🎯 Getting Help

### Recursos

- **Issues:** [GitHub Issues](../../issues) - Reporte bugs e features
- **Discussions:** [GitHub Discussions](../../discussions) - Faça perguntas
- **Documentation:** Consulte [DEPLOY_GUIDE.md](./DEPLOY_GUIDE.md) para detalhes
- **Troubleshooting:** Veja seção [🆘 Troubleshooting](#-troubleshooting)

### Contato

- **Autor:** [Your Name]
- **Email:** [your.email@example.com]
- **LinkedIn:** [Your Profile]

---

## 📄 Licença

Este projeto está sob licença **MIT**. Veja [LICENSE](./LICENSE) para mais detalhes.

Você é livre para:
- ✅ Usar comercialmente
- ✅ Modificar o código
- ✅ Distribuir
- ✅ Usar em projetos privados

Com a condição de incluir a licença original em distribuições.

## 🤝 Contribuindo

Contribuições são bem-vindas! Para contribuir:

### Processo

1. **Fork** o repositório
2. **Crie branch** feature
   ```bash
   git checkout -b feature/sua-feature
   ```

3. **Teste localmente** com Docker Compose
   ```bash
   cd infra
   ./scripts/deploy.sh local
   ```

4. **Valide código:**
   ```bash
   # Backend
   cd back-end
   mvn clean verify

   # Frontend
   cd front-end
   npm run lint
   npm run test
   npm run type-check
   ```

5. **Validate Infraestrutura:**
   ```bash
   cd infra/terraform/environments/dev
   terraform validate
   terraform fmt
   ```

6. **Commit** com mensagens descritivas
   ```bash
   git commit -m "feat: add transfer endpoint"
   git commit -m "fix: cors validation error"
   ```

7. **Push** e **Abra Pull Request**
   ```bash
   git push origin feature/sua-feature
   ```

### Diretrizes

- Siga convenções de código existentes
- Adicione testes para novas features
- Atualize documentação se necessário
- Mantenha coverage de testes > 70%
- Use commits semânticos (feat:, fix:, docs:, etc)

---

## 🙏 Agradecimentos

Obrigado por usar **KeysBank**! 

- 💪 Contribuidores que ajudaram a melhorar
- 🐛 Usuários que reportaram issues
- 📚 Comunidade open source

---

**Last Updated:** 2024
**Version:** 1.0.0
**Status:** Production Ready ✅

---

**Documentação Relacionada:**
- [back-end/API_DOCUMENTATION.md](./back-end/API_DOCUMENTATION.md) - Detalhes técnicos de endpoints
- [DEPLOY_GUIDE.md](./DEPLOY_GUIDE.md) - Guia completo de deployment
- [infra/README.md](./infra/README.md) - Documentação de infraestrutura
- [GitHub Actions Workflows](./.github/workflows) - CI/CD automação
