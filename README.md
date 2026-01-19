# 🏦 KeysBank - Sistema Bancário Virtual

Uma plataforma bancária moderna e robusta construída com Spring Boot 4 e React 19.

## 📋 Índice
- [Visão Geral](#visão-geral)
- [Documentação da API](#documentação-da-api)
- [Estrutura do Projeto](#estrutura-do-projeto)
- [Como Começar](#como-começar)
- [Endpoints Principais](#endpoints-principais)
- [Integração com Lovable](#integração-com-lovable)

---

## 🎯 Visão Geral

O **KeysBank** é uma aplicação de banco virtual que oferece:
- ✅ Gerenciamento de clientes
- ✅ Criação e gerenciamento de contas bancárias
- ✅ Sistema de transações (débito/crédito)
- ✅ Extrato com filtros avançados
- ✅ Cálculo automático de saldo diário
- ✅ Bônus de abertura de conta

### Tecnologias
- **Backend**: Java 25 + Spring Boot 4.0.0
- **Frontend**: React 19.2.0 + Tailwind CSS 4
- **Banco de Dados**: PostgreSQL 16.11
- **Documentação**: OpenAPI 3.0 / Swagger

---

## 📚 Documentação da API

### Principais Documentos
1. **[DOCUMENTATION_GUIDE.md](./DOCUMENTATION_GUIDE.md)** - Guia completo de documentação (início aqui)
2. **[back-end/API_DOCUMENTATION.md](./back-end/API_DOCUMENTATION.md)** - Documentação técnica detalhada
3. **[back-end/openapi.json](./back-end/openapi.json)** - Especificação OpenAPI 3.0

### Acessar Swagger UI
```
http://localhost:8080/swagger-ui.html
```

### Importar em Ferramentas
- **Postman**: Importe `back-end/openapi.json`
- **Insomnia**: Importe `back-end/openapi.json`
- **Lovable**: Cole o conteúdo do `openapi.json`

---

## 📂 Estrutura do Projeto

```
KeysBank/
├── back-end/                          # API Spring Boot
│   ├── src/main/java/
│   │   └── com/backend/keysbankapi/
│   │       ├── account/               # Gerenciamento de contas
│   │       ├── customer/              # Gerenciamento de clientes
│   │       ├── ledger/                # Transações e extratos
│   │       ├── common/                # Utilitários e tratamento de erros
│   │       └── config/                # Configurações (CORS, OpenAPI)
│   ├── src/main/resources/
│   │   ├── application.yaml           # Configuração da aplicação
│   │   └── db/migration/              # Scripts de banco de dados
│   ├── API_DOCUMENTATION.md           # Documentação técnica
│   ├── openapi.json                   # Especificação OpenAPI 3.0
│   └── pom.xml                        # Dependências Maven
│
├── front-end/                         # Aplicação React
│   ├── src/
│   │   ├── pages/
│   │   │   ├── LoginPage.jsx
│   │   │   ├── CreateAccountPage.jsx
│   │   │   └── DashboardPage.jsx
│   │   ├── components/
│   │   │   ├── StatementTab.jsx       # Visualização de extrato
│   │   │   └── TransactionTab.jsx     # Criar transações
│   │   ├── api.js                     # Cliente HTTP (Axios)
│   │   └── App.jsx
│   ├── package.json
│   └── vite.config.js
│
├── infra/                             # Infraestrutura (Docker)
│   └── docker-compose.yml
│
├── DOCUMENTATION_GUIDE.md             # Guia de documentação (INÍCIO AQUI)
├── README.md                          # Este arquivo
└── .gitignore

```

---

## 🚀 Como Começar

### Pré-requisitos
- Java 25+
- Node.js 18+
- PostgreSQL 16
- Maven 3.9+

### 1. Configurar Banco de Dados
```bash
# PostgreSQL
createuser bankuser --password
createdb -O bankuser bank

# Migrate (automático ao iniciar backend)
```

### 2. Iniciar Backend
```bash
cd back-end
mvn spring-boot:run
# ou
java -jar target/keysbankapi-0.0.1-SNAPSHOT.jar
```

Backend rodará em: **http://localhost:8080**

### 3. Iniciar Frontend
```bash
cd front-end
npm install
npm run dev
```

Frontend rodará em: **http://localhost:3000**

### 4. Documentação
- Swagger UI: **http://localhost:8080/swagger-ui.html**
- OpenAPI JSON: **http://localhost:8080/v3/api-docs**

---

## 🔌 Endpoints Principais

### Clientes
- `POST /customers` - Criar cliente
  ```bash
  curl -X POST http://localhost:8080/customers \
    -H "Content-Type: application/json" \
    -d '{"name":"João Silva","email":"joao@email.com"}'
  ```

### Contas
- `POST /accounts` - Criar conta
- `GET /accounts/login?agency=0001&accountNumber=343316` - Login

### Transações
- `POST /transaction` - Criar transação (débito/crédito)
- `GET /accounts/{accountId}/statement` - Extrato (com filtros)

Veja **[API_DOCUMENTATION.md](./back-end/API_DOCUMENTATION.md)** para documentação completa.

---

## 🎨 Fluxo de Uso

### Cenário 1: Novo Cliente
```
1. POST /customers → cria cliente (retorna ID)
2. POST /accounts → cria conta com bônus R$ 100
3. GET /accounts/login → valida conta (retorna accountId)
4. GET /accounts/{accountId}/statement → visualiza extrato
5. POST /transaction → faz transação
6. GET /accounts/{accountId}/statement → saldo atualizado
```

### Cenário 2: Frontend (React)
```
1. Usuário acessa http://localhost:3000
2. LoginPage: insere agência e número da conta
3. Frontend: GET /accounts/login (valida conta)
4. Dashboard: mostra extrato e permite transações
5. StatementTab: filtro por data e tipo
6. TransactionTab: criar débito/crédito
```

---

## 🔗 Integração com Lovable

### Passo 1: Obter OpenAPI
```bash
cat back-end/openapi.json
```

### Passo 2: Usar no Lovable
1. Abra https://lovable.dev
2. Cole o conteúdo de `openapi.json`
3. Lovable gerará:
   - ✅ Componentes React
   - ✅ Tipos TypeScript
   - ✅ Funções API
   - ✅ Formulários com validação

### Passo 3: Resultado
- Frontend 100% tipado
- Sem erros de integração
- Pronto para produção

---

## 📊 Funcionalidades Implementadas

### Backend ✅
- [x] CRUD de clientes
- [x] CRUD de contas
- [x] Sistema de transações (CREDIT/DEBIT)
- [x] Cálculo automático de saldo (BALANCE)
- [x] Filtros no extrato (data, tipo)
- [x] Validação de dados
- [x] Tratamento de erros
- [x] CORS configurado
- [x] Documentação OpenAPI 3.0
- [x] Anotações Swagger

### Frontend ✅
- [x] Login com validação de conta
- [x] Criação de conta
- [x] Visualização de extrato
- [x] Filtros de extrato
- [x] Criar transações
- [x] Design responsivo
- [x] Integração com API
- [x] Tratamento de erros

---

## 🔐 Autenticação

O fluxo de autenticação funciona assim:

1. Usuário insere **agência** e **número da conta**
2. Frontend faz: `GET /accounts/login?agency=0001&accountNumber=343316`
3. Backend valida se conta existe
4. Retorna `accountId` (UUID)
5. Frontend armazena `accountId`
6. Todas as operações usam este `accountId`

---

## ⚙️ Configuração

### Backend (application.yaml)
```yaml
server:
  port: 8080

spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/bank
    username: bankuser
    password: bankpass

  jpa:
    hibernate:
      ddl-auto: validate

springdoc:
  api-docs:
    path: /v3/api-docs
  swagger-ui:
    path: /swagger-ui.html
```

### Frontend (vite.config.js)
```javascript
export default defineConfig({
  server: {
    port: 3000
  }
})
```

---

## 📝 Tipos de Transações

### Type
- `CREDIT` - Crédito/Depósito
- `DEBIT` - Débito/Saque
- `BALANCE` - Saldo diário (automático)

### Category (exemplos)
- `BONUS_ABERTURA` - Bônus inicial
- `SAQUE` - Saque
- `DEPOSITO` - Depósito
- `DAILY_BALANCE` - Saldo do dia

---

## 🐛 Solução de Problemas

### Backend não inicia
```bash
# Verifique se PostgreSQL está rodando
psql -U bankuser -d bank

# Limpe Maven
mvn clean

# Recompile
mvn package
```

### Frontend não conecta ao backend
- Verifique se backend está em http://localhost:8080
- Verifique CORS em `KeysbankapiApplication.java`
- Veja erro no console do navegador

### Swagger não aparece
```bash
# URL correta:
http://localhost:8080/swagger-ui.html

# Ou obtenha JSON:
http://localhost:8080/v3/api-docs
```

---

## 📞 Suporte e Documentação

### Documentos Principais
1. **[DOCUMENTATION_GUIDE.md](./DOCUMENTATION_GUIDE.md)** - Guia de documentação
2. **[back-end/API_DOCUMENTATION.md](./back-end/API_DOCUMENTATION.md)** - API detalhada
3. **[back-end/openapi.json](./back-end/openapi.json)** - Especificação OpenAPI

### Links Úteis
- Swagger UI: http://localhost:8080/swagger-ui.html
- OpenAPI JSON: http://localhost:8080/v3/api-docs
- Email: support@keysbank.com
- Website: https://keysbank.com

---

## 📄 Licença

Apache 2.0 - Veja LICENSE para detalhes

---

## 👥 Contribuidores

- Kaique Santos Sousa - Desenvolvedor

---

**Status do Projeto**: ✅ Em Produção
**Versão**: 1.0.0
**Última Atualização**: 19 de Janeiro de 2026
