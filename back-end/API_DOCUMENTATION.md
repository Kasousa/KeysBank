# 📚 Documentação da API KeysBank

## 📋 Índice
1. [Visão Geral](#visão-geral)
2. [Endpoints](#endpoints)
3. [Autenticação](#autenticação)
4. [Fluxo de Uso](#fluxo-de-uso)
5. [Exemplos de Requisições](#exemplos-de-requisições)
6. [Tratamento de Erros](#tratamento-de-erros)
7. [Swagger UI](#swagger-ui)

---

## 🎯 Visão Geral

A **KeysBank API** é uma plataforma bancária virtual robusta que oferece endpoints para gerenciamento de clientes, contas bancárias, transações e extratos.

### Tecnologia
- **Framework**: Spring Boot 4.0.0
- **Linguagem**: Java 25
- **Banco de Dados**: PostgreSQL 16.11
- **Documentação**: OpenAPI 3.0 / Swagger

### Servidores
- **Desenvolvimento**: http://localhost:8080
- **Produção**: https://api.keysbank.com

---

## 🔌 Endpoints

### 1. Clientes (Customers)

#### 1.1 Criar Cliente
**POST** `/customers`

Cria um novo cliente no sistema bancário.

**Request Body:**
```json
{
  "name": "João Silva",
  "email": "joao.silva@email.com"
}
```

**Response (201 Created):**
```json
{
  "id": "71475965-0ea9-46e7-87c7-ca98320189af",
  "name": "João Silva",
  "email": "joao.silva@email.com"
}
```

**Validações:**
- `name`: Obrigatório, não vazio
- `email`: Obrigatório, formato válido, único no banco

**Possíveis Erros:**
- `400 Bad Request`: Email já cadastrado ou dados inválidos
- `500 Internal Server Error`: Erro do servidor

---

### 2. Contas (Accounts)

#### 2.1 Criar Conta Bancária
**POST** `/accounts`

Cria uma nova conta bancária vinculada a um cliente existente. A conta recebe automaticamente um bônus de abertura de **R$ 100,00**.

**Request Body:**
```json
{
  "customerId": "71475965-0ea9-46e7-87c7-ca98320189af"
}
```

**Response (201 Created):**
```json
{
  "id": "b837e6e2-1b3c-4267-825f-741fb798f066",
  "customerId": "71475965-0ea9-46e7-87c7-ca98320189af",
  "agency": "0001",
  "accountNumber": "343316",
  "status": "ATIVA"
}
```

**Validações:**
- `customerId`: Obrigatório, deve existir no banco

**Possíveis Erros:**
- `400 Bad Request`: Cliente não encontrado ou cliente já possui conta
- `500 Internal Server Error`: Erro do servidor

---

#### 2.2 Validar e Fazer Login (Autenticação)
**GET** `/accounts/login`

Valida as credenciais da conta e retorna o ID da conta necessário para os demais endpoints. Este é o primeiro passo do fluxo de autenticação.

**Query Parameters:**
- `agency` (string, obrigatório): Número da agência (exemplo: "0001")
- `accountNumber` (string, obrigatório): Número da conta (exemplo: "343316")

**Request Exemplo:**
```
GET /accounts/login?agency=0001&accountNumber=343316
```

**Response (200 OK):**
```json
{
  "accountId": "b837e6e2-1b3c-4267-825f-741fb798f066",
  "agency": "0001",
  "accountNumber": "343316",
  "customerName": "João Silva"
}
```

**Possíveis Erros:**
- `404 Not Found`: Conta não encontrada com os dados fornecidos
- `500 Internal Server Error`: Erro do servidor

---

### 3. Transações (Transactions)

#### 3.1 Criar Transação
**POST** `/transaction`

Cria uma nova transação (depósito ou saque) em uma conta. Após criar a transação, o saldo diário é automaticamente recalculado com uma transação do tipo BALANCE.

**Request Body:**
```json
{
  "accountId": "b837e6e2-1b3c-4267-825f-741fb798f066",
  "type": "DEBIT",
  "category": "SAQUE",
  "amount": 50.00,
  "description": "Saque no caixa eletrônico"
}
```

**Response (201 Created):**
```json
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
- `type`: Obrigatório, valores: "CREDIT" ou "DEBIT"
- `category`: Obrigatório, exemplos: "SAQUE", "DEPOSITO", "TRANSFERENCIA"
- `amount`: Obrigatório, deve ser positivo
- `description`: Obrigatório, não vazio

**Possíveis Erros:**
- `400 Bad Request`: Dados inválidos, conta não encontrada
- `500 Internal Server Error`: Erro do servidor

---

### 4. Extratos (Statements)

#### 4.1 Recuperar Extrato da Conta
**GET** `/accounts/{accountId}/statement`

Retorna o extrato (lista de transações) de uma conta com suporte a filtros opcionais por data e tipo de transação.

**Path Parameters:**
- `accountId` (UUID, obrigatório): ID único da conta

**Query Parameters (Opcionais):**
- `startDate` (date, formato YYYY-MM-DD): Data inicial do período
- `endDate` (date, formato YYYY-MM-DD): Data final do período
- `type` (string): Filtrar por tipo ("CREDIT", "DEBIT", ou "BALANCE")

**Request Exemplos:**

1. Extrato completo:
```
GET /accounts/b837e6e2-1b3c-4267-825f-741fb798f066/statement
```

2. Com filtro de data:
```
GET /accounts/b837e6e2-1b3c-4267-825f-741fb798f066/statement?startDate=2026-01-01&endDate=2026-01-31
```

3. Com filtro de tipo:
```
GET /accounts/b837e6e2-1b3c-4267-825f-741fb798f066/statement?type=CREDIT
```

4. Com múltiplos filtros:
```
GET /accounts/b837e6e2-1b3c-4267-825f-741fb798f066/statement?startDate=2026-01-01&type=DEBIT
```

**Response (200 OK):**
```json
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
  },
  {
    "transactionId": "770e8400-e29b-41d4-a716-446655440002",
    "type": "CREDIT",
    "category": "BONUS_ABERTURA",
    "amount": 100.00,
    "description": "Bônus de abertura de conta",
    "createdAt": "2026-01-15T10:00:00.000Z"
  }
]
```

**Possíveis Erros:**
- `400 Bad Request`: Parâmetros inválidos
- `404 Not Found`: Conta não encontrada
- `500 Internal Server Error`: Erro do servidor

---

## 🔐 Autenticação

O fluxo de autenticação funciona da seguinte forma:

1. **Usuário insere agência e número da conta** no frontend
2. **Frontend faz uma requisição GET** para `/accounts/login?agency=0001&accountNumber=343316`
3. **Backend valida** se a conta existe no banco de dados
4. **Backend retorna** o `accountId` (UUID) necessário para operações futuras
5. **Frontend armazena** o `accountId` em sessão/memória
6. **Todas as requisições subsequentes** usam este `accountId`

### Fluxo de Autenticação (Diagrama)
```
┌─────────────────┐
│    Frontend     │
└────────┬────────┘
         │ 1. POST /customers
         │ { name, email }
         ▼
    ✅ Cliente criado
    ✅ Retorna: id, name, email

    2. POST /accounts
    { customerId: "uuid" }
    ✅ Conta criada com bônus de R$ 100
    ✅ Retorna: id, customerId, agency, accountNumber

    3. GET /accounts/login?agency=0001&accountNumber=xxx
    ✅ Valida conta
    ✅ Retorna: accountId, agency, accountNumber, customerName

    4. GET /accounts/{accountId}/statement
    ✅ Extrato recuperado
    
    5. POST /transaction
    { accountId, type, category, amount, description }
    ✅ Transação criada
    ✅ Saldo recalculado automaticamente
```

---

## 📝 Fluxo de Uso

### Scenario 1: Novo Cliente
```
1. Criar Cliente
   POST /customers
   { "name": "João Silva", "email": "joao@email.com" }
   
2. Criar Conta
   POST /accounts
   { "customerId": "71475965-0ea9-46e7-87c7-ca98320189af" }
   ✅ Recebe R$ 100,00 de bônus automaticamente

3. Fazer Login
   GET /accounts/login?agency=0001&accountNumber=343316
   ✅ Retorna accountId

4. Visualizar Extrato
   GET /accounts/b837e6e2-1b3c-4267-825f-741fb798f066/statement
   ✅ Mostra transação de bônus e saldo

5. Fazer Saque
   POST /transaction
   { "accountId": "b837e6e2-...", "type": "DEBIT", "category": "SAQUE", 
     "amount": 50, "description": "Saque" }
   ✅ Saldo recalculado automaticamente
```

---

## 🔄 Exemplos de Requisições

### Usando cURL

#### 1. Criar Cliente
```bash
curl -X POST http://localhost:8080/customers \
  -H "Content-Type: application/json" \
  -d '{
    "name": "João Silva",
    "email": "joao.silva@email.com"
  }'
```

#### 2. Criar Conta
```bash
curl -X POST http://localhost:8080/accounts \
  -H "Content-Type: application/json" \
  -d '{
    "customerId": "71475965-0ea9-46e7-87c7-ca98320189af"
  }'
```

#### 3. Login
```bash
curl -X GET "http://localhost:8080/accounts/login?agency=0001&accountNumber=343316"
```

#### 4. Visualizar Extrato
```bash
curl -X GET "http://localhost:8080/accounts/b837e6e2-1b3c-4267-825f-741fb798f066/statement"
```

#### 5. Fazer Transação
```bash
curl -X POST http://localhost:8080/transaction \
  -H "Content-Type: application/json" \
  -d '{
    "accountId": "b837e6e2-1b3c-4267-825f-741fb798f066",
    "type": "DEBIT",
    "category": "SAQUE",
    "amount": 50.00,
    "description": "Saque no caixa eletrônico"
  }'
```

#### 6. Extrato com Filtros
```bash
# Filtrar por data
curl -X GET "http://localhost:8080/accounts/b837e6e2-1b3c-4267-825f-741fb798f066/statement?startDate=2026-01-01&endDate=2026-01-31"

# Filtrar por tipo
curl -X GET "http://localhost:8080/accounts/b837e6e2-1b3c-4267-825f-741fb798f066/statement?type=CREDIT"
```

---

## ⚠️ Tratamento de Erros

Todos os erros seguem o seguinte formato:

```json
{
  "status": 400,
  "error": "Bad Request",
  "message": "Descrição do erro",
  "path": "/endpoint",
  "timestamp": "2026-01-16T02:01:49.765021Z"
}
```

### Códigos de Erro Comuns

| Código | Descrição | Causa |
|--------|-----------|-------|
| `400` | Bad Request | Validação falhou (campos obrigatórios, formato inválido) |
| `404` | Not Found | Recurso não encontrado (conta, cliente, transação) |
| `500` | Internal Server Error | Erro no servidor (exceção não tratada) |

### Exemplos de Erros

#### Email já cadastrado
```json
{
  "status": 400,
  "error": "Bad Request",
  "message": "Email já cadastrado",
  "path": "/customers",
  "timestamp": "2026-01-16T02:01:49.765021Z"
}
```

#### Conta não encontrada (login)
```json
{
  "status": 404,
  "error": "Not Found",
  "message": "Conta inválida",
  "path": "/accounts/login",
  "timestamp": "2026-01-16T02:01:49.765021Z"
}
```

#### Validação de campo obrigatório
```json
{
  "status": 400,
  "error": "Bad Request",
  "message": "name: não deve estar em branco",
  "path": "/customers",
  "timestamp": "2026-01-16T02:01:49.765021Z"
}
```

---

## 🎨 Swagger UI

A documentação interativa está disponível em:

**URL**: http://localhost:8080/swagger-ui.html

### Recursos do Swagger UI:
- ✅ Visualização de todos os endpoints
- ✅ Descrição completa de cada endpoint
- ✅ Exemplos de request/response
- ✅ Validações e tipos de dados
- ✅ Teste interativo de endpoints (Try it out)
- ✅ Download do OpenAPI JSON/YAML

### Como usar:
1. Acesse http://localhost:8080/swagger-ui.html
2. Escolha um endpoint
3. Clique em "Try it out"
4. Preencha os parâmetros
5. Clique em "Execute"
6. Veja a resposta

---

## 📥 OpenAPI JSON/YAML

Você pode obter a especificação OpenAPI em diferentes formatos:

- **JSON**: http://localhost:8080/v3/api-docs
- **YAML**: http://localhost:8080/v3/api-docs.yaml
- **JSON (Groupado)**: http://localhost:8080/v3/api-docs/{group}

Estes arquivos podem ser usados para:
- Gerar clientes em diferentes linguagens
- Integração com ferramentas de testing (Postman, Insomnia)
- Documentação automatizada
- Geração de código (Lovable, etc)

---

## 🚀 Integração com Frontend (Lovable)

A documentação OpenAPI pode ser facilmente integrada com Lovable ou outras ferramentas de geração de frontend:

1. **Obtenha o OpenAPI JSON**:
   ```
   GET http://localhost:8080/v3/api-docs
   ```

2. **Importe no Lovable**:
   - Copie o JSON da resposta
   - Cole no gerador de frontend do Lovable
   - O Lovable gerará automaticamente componentes React com chamadas de API

3. **Benefícios**:
   - Geração automática de tipos TypeScript
   - Validação de entrada baseada no schema
   - Geração de componentes de formulário
   - Integração com seu backend sem erros

---

## 📊 Tipos de Transações

### Type (Tipo)
- `CREDIT`: Crédito/Depósito
- `DEBIT`: Débito/Saque
- `BALANCE`: Saldo diário (gerado automaticamente)

### Category (Categoria)
- `BONUS_ABERTURA`: Bônus inicial de abertura (R$ 100)
- `SAQUE`: Saque de dinheiro
- `DEPOSITO`: Depósito de dinheiro
- `TRANSFERENCIA`: Transferência bancária
- `DAILY_BALANCE`: Saldo diário automático
- Outras categorias customizadas conforme necessário

---

## 📞 Suporte

Para dúvidas ou problemas:
- **Email**: support@keysbank.com
- **Website**: https://keysbank.com
- **Documentação**: http://localhost:8080/swagger-ui.html

---

**Última atualização**: 19 de Janeiro de 2026
**Versão da API**: 1.0.0
