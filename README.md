# 🏦 KeysBank - Sistema Bancário Virtual

Uma plataforma bancária moderna e robusta construída com Spring Boot 4, React 19 e PostgreSQL 16. Totalmente tipada com TypeScript, documentada com OpenAPI 3.0 e pronta para produção.

## 📋 Índice
- [Visão Geral](#visão-geral)
- [Recursos](#recursos)
- [Tecnologias](#tecnologias)
- [Como Começar](#como-começar)
- [API - Endpoints Principais](#api---endpoints-principais)
- [Testes](#-testes)
- [Deploy em Produção](#-deploy-em-produção)
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
- ✅ React 19 com TypeScript
- ✅ Tailwind CSS 4 para estilização
- ✅ Shadcn/UI para componentes
- ✅ React Router para navegação
- ✅ Integração HTTP com fetch API
- ✅ Design responsivo (mobile/desktop)
- ✅ Testes unitários com Vitest

---

## 💻 Tecnologias

| Camada | Tecnologia |
|--------|-----------|
| **Backend** | Java 25, Spring Boot 4.0.0, Spring Data JPA |
| **Frontend** | React 19.2.0, TypeScript, Tailwind CSS 4 |
| **Banco de Dados** | PostgreSQL 16.11 |
| **Documentação** | OpenAPI 3.0, Swagger UI |
| **Build** | Maven (Backend), Vite (Frontend) |
| **Deployment** | Docker, AWS EC2 |
| **CI/CD** | GitHub Actions |

---

## 🚀 Como Começar

### Pré-requisitos
- **Java 25+** (Backend)
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
POST /customers
{
  "name": "João Silva",
  "email": "joao@email.com"
}
```

### Contas (Accounts)
```bash
# Criar conta (recebe R$ 100 de bônus)
POST /accounts
{
  "customerId": "uuid-aqui"
}

# Login (validação de conta)
GET /accounts/login?agency=0001&accountNumber=343316
```

### Transações
```bash
# Criar transação
POST /transaction
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
GET /accounts/{accountId}/statement

# Com filtros
GET /accounts/{accountId}/statement?startDate=2026-01-01&endDate=2026-01-31&type=CREDIT
```

Veja **[API_DOCUMENTATION.md](./back-end/API_DOCUMENTATION.md)** para documentação completa.

---

## 🧪 Testes

### Backend (JUnit 5 + Mockito)
**24 testes unitários** cobrindo Services críticos:
```bash
cd back-end
mvn test
```

**Arquivos de teste:**
- `src/test/java/.../customer/CustomerServiceTest.java` (8 testes)
- `src/test/java/.../account/AccountServiceTest.java` (8 testes)
- `src/test/java/.../ledger/TransactionServiceTest.java` (8 testes)

### Frontend (Vitest + React Testing Library)
**28 testes unitários** cobrindo componentes e hooks:
```bash
cd front-end
npm run test
```

**Arquivos de teste:**
- `src/utils/formatters.test.ts` (12 testes)
- `src/components/auth/LoginForm.test.tsx` (9 testes)
- `src/context/AuthContext.test.tsx` (7 testes)

---

## � Deploy em Produção

### 1. Pré-requisitos AWS
- Conta AWS ativa
- Chaves de acesso (Access Key ID + Secret Access Key)
- EC2 instance (Amazon Linux 2, t3.medium, 20GB SSD)

### 2. Configurar GitHub Secrets
No repositório, adicione em **Settings → Secrets and variables → Actions**:
```
AWS_ACCESS_KEY_ID          = seu-access-key
AWS_SECRET_ACCESS_KEY      = seu-secret-key
EC2_INSTANCE_IP            = seu-ec2-ip-publico
EC2_PRIVATE_KEY            = conteúdo do arquivo .pem
SLACK_WEBHOOK              = (opcional)
```

### 3. Setup EC2
```bash
# SSH na instância
ssh -i sua-chave.pem ec2-user@seu-ec2-ip

# Executar setup automático
bash /home/ec2-user/infra/ec2-setup.sh
```

**O script instala:**
- Java 25
- Node.js 18
- Docker & Docker Compose
- PostgreSQL 16
- Nginx (reverse proxy)
- Scripts de deploy e rollback

### 4. Deploy Automático
```bash
git push origin main
# GitHub Actions faz o resto automaticamente!
```

**Acompanhe em:** GitHub → Actions → Workflow

### 5. Verificar Deploy
```
Frontend:  http://seu-ec2-ip
Backend:   http://seu-ec2-ip:8080/swagger-ui.html
```

### Pipeline CI/CD
Workflows automáticos em `.github/workflows/`:
- **deploy-backend.yml**: Build → Test → Deploy → Verify → Rollback
- **deploy-frontend.yml**: Build → Test → Deploy → Verify → Rollback

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
│   ├── src/test/java/        → 24 testes unitários
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
│   ├── src/**/*.test.ts      → 28 testes unitários
│   ├── package.json          → Dependências npm
│   └── vite.config.ts        → Configuração build
│
├── .github/workflows/
│   ├── deploy-backend.yml    → Pipeline backend
│   └── deploy-frontend.yml   → Pipeline frontend
│
└── infra/
    ├── ec2-setup.sh          → Setup EC2
    └── docker-compose.yml    → Docker Compose
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

### Problema: Deploy em GitHub Actions falhando
1. Verificar GitHub Secrets estão corretos
2. Acompanhar logs em **Actions** → Workflow
3. Verificar EC2 está rodando
4. Testar SSH manualmente: `ssh -i chave.pem ec2-user@seu-ip`

---

---

**Status**: ✅ Pronto para Produção | **Versão**: 1.0.0 | **Data**: 20 de Janeiro de 2026
