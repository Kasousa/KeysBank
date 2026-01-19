# 📚 Documentação Completa - KeysBank API

## 🎯 Resumo da Implementação

A documentação da API KeysBank foi configurada com **OpenAPI 3.0** (também conhecido como Swagger 3.0) e está totalmente documentada com exemplos, validações e descrições detalhadas de cada endpoint.

---

## 📖 Recursos de Documentação

### 1. **Arquivo de Documentação Markdown**
   - **Arquivo**: `API_DOCUMENTATION.md` (na pasta `/back-end`)
   - **Conteúdo**: Guia completo de uso com exemplos, fluxos e casos de uso
   - **Público-alvo**: Desenvolvedores, integradores, documentação para Lovable

### 2. **OpenAPI JSON Specification**
   - **Arquivo**: `openapi.json` (na pasta `/back-end`)
   - **Formato**: OpenAPI 3.0.0
   - **Uso**: Importar em ferramentas como Swagger UI, Postman, Insomnia, Lovable
   - **Conteúdo Completo**:
     - 5 endpoints documentados
     - 6 DTOs com validações
     - Exemplos de request/response
     - Códigos de erro e descrições
     - Tags para agrupamento (Customers, Accounts, Transactions, Statement)

### 3. **Swagger UI Interactive**
   - **URL**: `http://localhost:8080/swagger-ui.html`
   - **Recursos**:
     - Visualização interativa de endpoints
     - "Try it out" para testar endpoints
     - Documentação em tempo real
     - Schemas de request/response

---

## 🔄 Endpoints Documentados

### Customers (Clientes)
- **POST /customers** - Criar novo cliente
  - ✅ Validação de email único
  - ✅ Nome obrigatório
  - ✅ Retorna ID para uso posterior

### Accounts (Contas)
- **POST /accounts** - Criar conta bancária
  - ✅ Vincula a cliente existente
  - ✅ Bônus automático de R$ 100,00
  - ✅ Gera agência (0001) e número de conta

- **GET /accounts/login** - Validação/Login
  - ✅ Query params: agency, accountNumber
  - ✅ Retorna accountId necessário para operações
  - ✅ Usado no frontend para autenticação

### Transactions (Transações)
- **POST /transaction** - Criar transação
  - ✅ Tipos: CREDIT (crédito/depósito), DEBIT (débito/saque)
  - ✅ Saldo diário recalculado automaticamente
  - ✅ Validação de valores

### Statement (Extratos)
- **GET /accounts/{accountId}/statement** - Recuperar extrato
  - ✅ Filtro por data (startDate/endDate)
  - ✅ Filtro por tipo (CREDIT, DEBIT, BALANCE)
  - ✅ Retorna array com todas as transações
  - ✅ Ordenado por data decrescente

---

## 🚀 Como Usar a Documentação

### Para Desenvolvedores

1. **Acessar Swagger UI**:
   ```
   http://localhost:8080/swagger-ui.html
   ```
   - Visualize todos os endpoints
   - Teste diretamente no navegador
   - Veja exemplos de request/response

2. **Ler Documentação em Markdown**:
   - Abra o arquivo `API_DOCUMENTATION.md`
   - Contém guias completos com exemplos em cURL
   - Fluxos de uso passo a passo

3. **Integrar com Ferramentas**:
   - **Postman**: Importe o arquivo `openapi.json`
   - **Insomnia**: Importe o arquivo `openapi.json`
   - **VS Code**: Use extensão REST Client

### Para Lovable (Geração de Frontend)

1. **Obtenha o arquivo OpenAPI**:
   ```bash
   # Copie o arquivo openapi.json
   cat /back-end/openapi.json
   ```

2. **Importe no Lovable**:
   - Cole o JSON na ferramenta Lovable
   - Lovable gerará automaticamente:
     - ✅ Componentes React com chamadas de API
     - ✅ Tipos TypeScript baseados no schema
     - ✅ Validação de inputs
     - ✅ Tratamento de erros

3. **Benefícios**:
   - Tipos TypeScript automáticos
   - Sem erros de integração
   - Componentes prontos para uso
   - Mantém sincronização com backend

---

## 📝 Exemplos de Uso (cURL)

### 1. Criar Cliente
```bash
curl -X POST http://localhost:8080/customers \
  -H "Content-Type: application/json" \
  -d '{
    "name": "João Silva",
    "email": "joao@email.com"
  }'
```

**Resposta**:
```json
{
  "id": "71475965-0ea9-46e7-87c7-ca98320189af",
  "name": "João Silva",
  "email": "joao@email.com"
}
```

### 2. Criar Conta
```bash
curl -X POST http://localhost:8080/accounts \
  -H "Content-Type: application/json" \
  -d '{
    "customerId": "71475965-0ea9-46e7-87c7-ca98320189af"
  }'
```

**Resposta**:
```json
{
  "id": "b837e6e2-1b3c-4267-825f-741fb798f066",
  "customerId": "71475965-0ea9-46e7-87c7-ca98320189af",
  "agency": "0001",
  "accountNumber": "343316",
  "status": "ATIVA"
}
```

### 3. Fazer Login
```bash
curl -X GET "http://localhost:8080/accounts/login?agency=0001&accountNumber=343316"
```

**Resposta**:
```json
{
  "accountId": "b837e6e2-1b3c-4267-825f-741fb798f066",
  "agency": "0001",
  "accountNumber": "343316",
  "customerName": "João Silva"
}
```

### 4. Recuperar Extrato
```bash
# Extrato completo
curl -X GET "http://localhost:8080/accounts/b837e6e2-1b3c-4267-825f-741fb798f066/statement"

# Com filtros
curl -X GET "http://localhost:8080/accounts/b837e6e2-1b3c-4267-825f-741fb798f066/statement?startDate=2026-01-01&endDate=2026-01-31&type=CREDIT"
```

### 5. Criar Transação
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

---

## 📂 Estrutura da Documentação no Projeto

```
back-end/
├── API_DOCUMENTATION.md      ← Documentação em Markdown
├── openapi.json              ← Especificação OpenAPI 3.0
├── pom.xml                   ← Adicionada dependência springdoc-openapi
├── src/main/java/
│   └── com/backend/keysbankapi/
│       ├── config/
│       │   └── OpenApiConfig.java  ← Configuração do OpenAPI
│       ├── account/
│       │   ├── AccountController.java  ← Anotações @Operation, @Tag
│       │   └── dto/
│       │       ├── LoginResponse.java     ← @Schema annotations
│       │       ├── AccountResponse.java   ← @Schema annotations
│       │       └── CreateAccountRequest.java ← @Schema annotations
│       ├── customer/
│       │   ├── CustomerController.java   ← Anotações @Operation, @Tag
│       │   └── dto/
│       │       ├── CustomerResponse.java    ← @Schema annotations
│       │       └── CreateCustomerRequest.java ← @Schema annotations
│       ├── ledger/
│       │   ├── StatementController.java   ← Anotações @Operation, @Tag
│       │   ├── TransactionController.java ← Anotações @Operation, @Tag
│       │   └── dto/
│       │       ├── StatementItemResponse.java    ← @Schema annotations
│       │       ├── TransactionCreatedResponse.java ← @Schema annotations
│       │       └── TransactionCreatedRequest.java  ← @Schema annotations
│       └── resources/
│           └── application.yaml    ← Configuração springdoc
```

---

## 🔗 Links Úteis

### Documentação Local
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8080/v3/api-docs
- **Markdown Guide**: Ver arquivo `API_DOCUMENTATION.md`

### Ferramentas Recomendadas
- **Postman**: https://www.postman.com/ (importar openapi.json)
- **Insomnia**: https://insomnia.rest/ (importar openapi.json)
- **Swagger UI online**: https://swagger.io/tools/swagger-ui/
- **OpenAPI Editor**: https://www.swagger.io/tools/swagger-editor/

### Lovable Integration
- Importar `openapi.json` em Lovable
- Lovable gerará frontend React automático
- Tipos TypeScript sincronizados

---

## ✅ Funcionalidades de Documentação Implementadas

- ✅ Anotações OpenAPI em todos os controllers (@Operation, @Tag, @ApiResponse)
- ✅ Anotações de schema em todos os DTOs (@Schema com descriptions)
- ✅ Validações documentadas (NotNull, NotBlank, Positive, Email)
- ✅ Exemplos de valores nos campos
- ✅ Enum allowableValues para tipos limitados
- ✅ Códigos de erro HTTP documentados (201, 400, 404, 500)
- ✅ Descri ções detalhadas de cada endpoint
- ✅ Arquivo OpenAPI JSON exportável
- ✅ Swagger UI interativo
- ✅ Documentação Markdown completa com guias de uso
- ✅ Compatível com Lovable, Postman, Insomnia

---

## 🎓 Fluxo de Aprendizado

### 1º Passo: Entender a API
- Leia `API_DOCUMENTATION.md`
- Entenda os 5 endpoints principais
- Veja exemplos de uso

### 2º Passo: Explorar Interativamente
- Abra http://localhost:8080/swagger-ui.html
- Use "Try it out" em cada endpoint
- Teste com dados reais

### 3º Passo: Integrar no Frontend
- Importe `openapi.json` em Lovable
- Gere componentes React automáticos
- Desenvolva interface baseada na API

### 4º Passo: Compartilhar
- Distribua o arquivo `openapi.json`
- Distribua o arquivo `API_DOCUMENTATION.md`
- Compartilhe a URL do Swagger UI

---

## 📞 Suporte

Para mais informações:
- **Email**: support@keysbank.com
- **Website**: https://keysbank.com
- **Documentação Local**: `API_DOCUMENTATION.md`
- **Swagger UI**: http://localhost:8080/swagger-ui.html

---

**Status**: ✅ Documentação completa e operacional
**Versão API**: 1.0.0
**Última atualização**: 19 de Janeiro de 2026
