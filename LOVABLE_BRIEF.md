# 📋 Brief Completo - Geração Frontend Lovable

## 🎯 Visão Geral do Projeto

**Nome**: KeysBank - Sistema Bancário Virtual
**Objetivo**: Criar um frontend moderno e responsivo para gerenciamento de contas bancárias, transações e extratos
**Tecnologia Base**: API REST (OpenAPI 3.0)
**URL da API**: `http://localhost:8080`

---

## 📑 Índice

1. [Estrutura de Páginas](#estrutura-de-páginas)
2. [Requisitos de Design](#requisitos-de-design)
3. [Especificações Técnicas](#especificações-técnicas)
4. [Fluxo de Usuário](#fluxo-de-usuário)
5. [Componentes Reutilizáveis](#componentes-reutilizáveis)
6. [Funcionalidades Críticas](#funcionalidades-críticas)
7. [Integração com API](#integração-com-api)
8. [Estados e Validações](#estados-e-validações)
9. [Dados Técnicos](#dados-técnicos)

---

## 📄 Estrutura de Páginas

### 1. **Landing Page / Login**
**Rota**: `/`
**Objetivo**: Autenticação de conta bancária

**Componentes**:
- Logo do KeysBank (topo)
- Formulário de Login com campos:
  - Agência (input text, ex: "0001")
  - Número da Conta (input text, ex: "343316")
  - Botão "Entrar"
  - Link "Não tenho conta" → Criar Conta

**Funcionalidades**:
- Validar credenciais contra `/accounts/login`
- Armazenar `accountId` em localStorage/sessionStorage
- Redirecionar para `/dashboard` após login bem-sucedido
- Exibir erro se credenciais inválidas

**Exemplo de Requisição**:
```
GET /accounts/login?agency=0001&accountNumber=343316
```

**Resposta Esperada**:
```json
{
  "accountId": "b837e6e2-1b3c-4267-825f-741fb798f066",
  "agency": "0001",
  "accountNumber": "343316",
  "customerName": "João Silva"
}
```

---

### 2. **Criar Conta**
**Rota**: `/signup`
**Objetivo**: Permitir novo usuário registrar-se

**Componentes**:
- Logo do KeysBank
- Formulário com campos:
  - Nome Completo (input text, required)
  - Email (input email, required, unique)
  - Botão "Criar Conta"
  - Link "Voltar ao Login"

**Funcionalidades**:
- Validar nome não vazio
- Validar email válido
- Chamar POST `/customers` para criar cliente
- Se sucesso: criar automaticamente conta via POST `/accounts`
- Redirecionar para login após sucesso
- Exibir erro se email já existe

**Exemplo de Fluxo**:
1. POST `/customers` com nome e email
2. Recebe `customerId`
3. POST `/accounts` com `customerId`
4. Recebe `accountId`
5. Redireciona para login

---

### 3. **Dashboard Principal**
**Rota**: `/dashboard`
**Objetivo**: Exibir visão geral da conta e saldo

**Componentes Principais**:
- **Header**: Nome do usuário, número da conta, botão logout
- **Card de Saldo**:
  - Grande exibição do saldo total
  - Animação ao carregar
  - Formato: `R$ 1.234,56`
  
- **Estatísticas Rápidas**:
  - Total de Créditos (mês)
  - Total de Débitos (mês)
  - Número de Transações

- **Abas de Navegação** (tabs):
  - Extrato (padrão)
  - Transações
  - Configurações (opcional)

- **Botão Flutuante**: "+ Nova Transação"

**Funcionalidades**:
- Carregar saldo via GET `/accounts/{accountId}/statement?type=BALANCE`
- Mostrar último lançamento de BALANCE
- Auto-refresh a cada 30 segundos (opcional)
- Mostrar loading spinner enquanto carrega

---

### 4. **Abas - Extrato**
**Rota**: `/dashboard` (tab ativa)
**Objetivo**: Mostrar histórico de transações com filtros

**Componentes**:
- **Filtros**:
  - Data Inicial (date picker)
  - Data Final (date picker)
  - Tipo de Transação (dropdown):
    - Todos
    - Crédito
    - Débito
  - Botão "Aplicar Filtros"
  - Botão "Limpar Filtros"

- **Tabela de Transações**:
  - Colunas:
    - Data (DD/MM/YYYY)
    - Descrição
    - Tipo (Badge: Crédito em verde, Débito em vermelho)
    - Valor (R$ 1.234,56)
  - Linhas clicáveis (mostrar detalhes em modal)
  - Ordenação por data decrescente

- **Paginação** (se muitas transações):
  - 10-20 itens por página
  - Botões Anterior/Próximo

- **Estado Vazio**:
  - Mensagem "Nenhuma transação encontrada"
  - Sugerir criar primeira transação

**Funcionalidades**:
- GET `/accounts/{accountId}/statement?startDate=YYYY-MM-DD&endDate=YYYY-MM-DD&type=CREDIT|DEBIT`
- Aplicar filtros em tempo real
- Mostrar loading enquanto busca
- Tratamento de erros

**Exemplo de Requisição com Filtros**:
```
GET /accounts/b837e6e2-1b3c-4267-825f-741fb798f066/statement?startDate=2026-01-01&endDate=2026-01-31&type=CREDIT
```

---

### 5. **Abas - Transações**
**Rota**: `/dashboard` (tab ativa)
**Objetivo**: Criar nova transação

**Componentes**:
- **Formulário**:
  - Descrição (input text, required, max 255 chars)
  - Tipo (radio buttons):
    - Crédito (✓ verde)
    - Débito (✗ vermelho)
  - Valor (input number, required, min 0.01, max 999999.99)
    - Máscara: R$ 1.234,56
  - Botão "Registrar Transação"
  - Botão "Limpar Formulário"

- **Feedback**:
  - Sucesso: Toast verde "Transação criada com sucesso!"
  - Erro: Toast vermelho com mensagem de erro
  - Loading: Desabilitar botão durante envio

- **Redirecionamento**:
  - Após sucesso, limpar formulário
  - Atualizar saldo no dashboard automaticamente
  - (Opcional) Mostrar nova transação na abas

**Funcionalidades**:
- POST `/transaction` com dados
- Validar campos antes de enviar
- Mostrar erros de validação inline
- Sucesso: limpar formulário e atualizar lista

**Exemplo de Requisição**:
```
POST /transaction
{
  "accountId": "b837e6e2-1b3c-4267-825f-741fb798f066",
  "description": "Compra no Supermercado",
  "type": "DEBIT",
  "amount": 125.50
}
```

**Resposta Esperada**:
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "description": "Compra no Supermercado",
  "type": "DEBIT",
  "amount": 125.50,
  "createdAt": "2026-01-19T14:30:00Z"
}
```

---

## 🎨 Requisitos de Design

### Paleta de Cores
- **Primária**: `#6366f1` (Indigo)
- **Sucesso**: `#10b981` (Verde)
- **Erro**: `#ef4444` (Vermelho)
- **Aviso**: `#f59e0b` (Âmbar)
- **Info**: `#3b82f6` (Azul)
- **Background**: `#ffffff` (Branco) ou `#f9fafb` (Cinza claro)
- **Texto**: `#1f2937` (Cinza escuro)
- **Borda**: `#e5e7eb` (Cinza claro)

### Tipografia
- **Fonte**: Inter, Segoe UI, sans-serif
- **Títulos**: Bold, 28-32px
- **Subtítulos**: Semi-bold, 18-22px
- **Body**: Regular, 14-16px
- **Pequeno**: Regular, 12-14px

### Espaçamento
- Usar escala 4px: 4, 8, 12, 16, 24, 32, 48, 64px
- Padding padrão: 16px
- Gap entre componentes: 24px
- Margens: 32px (topo/bottom)

### Ícones
- Usar Lucide React ou Feather Icons
- Tamanho padrão: 20px
- Tamanho grande: 32px
- Tamanho pequeno: 16px

### Buttons
- Primário: Fundo indigo, texto branco, hover mais escuro
- Secundário: Borda indigo, texto indigo, fundo transparente
- Perigo: Fundo vermelho, texto branco
- Estados: Normal, Hover, Active, Disabled
- Raio de borda: 8px
- Padding: 12px 24px (medium)

### Cards
- Raio de borda: 12px
- Sombra: `0 1px 3px rgba(0,0,0,0.1)`
- Padding: 24px
- Background: Branco

### Inputs
- Raio de borda: 8px
- Borda: 1px solid #e5e7eb
- Padding: 12px 16px
- Focus: Borda indigo, sombra azul suave
- Erro: Borda vermelha, ícone de erro

### Responsividade
- **Mobile**: < 640px (1 coluna, full width)
- **Tablet**: 640px - 1024px (2 colunas)
- **Desktop**: > 1024px (3+ colunas)

---

## 💻 Especificações Técnicas

### Stack Recomendado
- **Framework**: React 19+
- **Roteamento**: React Router 7+
- **HTTP Client**: Axios ou Fetch API
- **Styling**: Tailwind CSS 4 + Shadcn/ui
- **Validação**: Zod ou React Hook Form
- **State**: Context API ou TanStack Query
- **Build**: Vite 7+
- **TypeScript**: Sim (types do OpenAPI)

### Ambiente
- **URL da API**: `http://localhost:8080`
- **Node.js**: 18+ LTS
- **npm/yarn**: Última versão estável

### Estrutura de Pastas Sugerida
```
src/
├── components/
│   ├── common/       (Header, Footer, Navigation)
│   ├── auth/         (LoginForm, SignupForm)
│   ├── dashboard/    (Saldo, Cards, Stats)
│   └── transaction/  (TransactionForm, TransactionList)
├── pages/
│   ├── LoginPage
│   ├── SignupPage
│   ├── DashboardPage
│   └── NotFoundPage
├── hooks/
│   ├── useAuth
│   ├── useTransaction
│   └── useStatement
├── services/
│   ├── api.ts
│   ├── authService.ts
│   └── transactionService.ts
├── types/
│   └── index.ts       (tipos gerados do OpenAPI)
├── utils/
│   ├── formatters.ts
│   ├── validators.ts
│   └── storage.ts
├── context/
│   └── AuthContext.tsx
└── App.tsx
```

---

## 🔄 Fluxo de Usuário

### Fluxo 1: Novo Usuário
```
1. Usuário acessa /
2. Clica "Não tenho conta"
3. Vai para /signup
4. Preenche nome e email
5. Clica "Criar Conta"
6. Backend cria customer + account
7. Redireciona para / (login)
8. Login com agência e conta
9. Vai para /dashboard
```

### Fluxo 2: Usuário Existente
```
1. Usuário acessa /
2. Preenche agência e conta
3. Clica "Entrar"
4. Backend valida credenciais
5. Armazena accountId
6. Redireciona para /dashboard
7. Exibe saldo e últimas transações
```

### Fluxo 3: Ver Extrato com Filtros
```
1. Usuário está em /dashboard (abas)
2. Abre aba "Extrato"
3. Preenche filtros (data, tipo)
4. Clica "Aplicar Filtros"
5. Backend retorna transações filtradas
6. Tabela atualiza
7. Usuário vê resumo de créditos/débitos
```

### Fluxo 4: Registrar Transação
```
1. Usuário clica "+ Nova Transação"
2. Abre aba "Transações"
3. Preenche descrição, tipo, valor
4. Clica "Registrar Transação"
5. Backend cria transaction
6. Saldo atualiza automaticamente
7. Toast de sucesso
8. Formulário limpa
9. Extrato atualiza com nova transação
```

### Fluxo 5: Logout
```
1. Usuário clica "Logout" no header
2. Remove accountId do localStorage
3. Redireciona para /
4. Estado da aplicação limpo
```

---

## 🧩 Componentes Reutilizáveis

### Componentes Base
- **Button**: Primário, secundário, pequeno, grande, desabilitar
- **Input**: Text, email, number, password, com erros
- **Select/Dropdown**: Com search, placeholder
- **DatePicker**: Range, single date
- **Radio Group**: Horizontal, vertical
- **Checkbox**: Single, group
- **Badge**: Info, success, error, warning

### Componentes de Layout
- **Header**: Logo, nome usuário, logout
- **Navigation/Tabs**: Horizontal, underline indicator
- **Card**: Padrão com padding
- **Container**: Max-width, responsive
- **Grid**: 1/2/3 colunas responsivo
- **Flex**: Justify, align utils

### Componentes de Feedback
- **Toast/Alert**: Success, error, info (top-right)
- **Loading Spinner**: Circular, durante requisições
- **Error Boundary**: Catch errors
- **Empty State**: Nenhuma transação, etc
- **Modal/Dialog**: Confirmações, detalhes

### Componentes de Domínio
- **SaldoCard**: Exibe saldo com animação
- **TransactionForm**: Formulário de nova transação
- **TransactionTable**: Lista de transações com filtros
- **StatementFilters**: Data, tipo, ações
- **LoginForm**: Agência + conta + botão
- **SignupForm**: Nome + email + botão

---

## ✨ Funcionalidades Críticas

### 1. Autenticação
- ✅ Login com agência + número conta
- ✅ Criar nova conta (nome + email)
- ✅ Logout
- ✅ Persistência de sessão (localStorage)
- ✅ Proteger rotas (PrivateRoute)
- ✅ Redirecionar não autenticados para /

### 2. Saldo
- ✅ Exibir saldo total
- ✅ Atualizar após cada transação
- ✅ Buscar último lançamento BALANCE
- ✅ Formatação monetária (R$)
- ✅ Loading state

### 3. Extrato
- ✅ Listar transações com paginação
- ✅ Filtrar por data (range)
- ✅ Filtrar por tipo (CREDIT/DEBIT/BALANCE)
- ✅ Combinar filtros
- ✅ Ordenar por data decrescente
- ✅ Exibir resumo (total crédito, débito)
- ✅ Estado vazio se sem transações

### 4. Transações
- ✅ Criar nova transação
- ✅ Validar descrição (não vazio, max 255)
- ✅ Validar tipo (CREDIT/DEBIT)
- ✅ Validar valor (> 0, máximo 999999.99)
- ✅ Máscara de moeda (R$)
- ✅ Sucesso/erro feedback
- ✅ Auto-atualizar saldo

### 5. Responsividade
- ✅ Mobile first design
- ✅ Tablet layout
- ✅ Desktop layout
- ✅ Tabelas horizontais mobile
- ✅ Menu burger mobile

### 6. Tratamento de Erros
- ✅ Erros de validação inline
- ✅ Erros de API em toast
- ✅ Mensagens descritivas
- ✅ Fallback para offline
- ✅ Retry buttons

---

## 🔌 Integração com API

### Base URL
```
http://localhost:8080
```

### Endpoints Utilizados

#### 1. POST /customers
**Criar novo cliente**
```json
{
  "name": "João Silva",
  "email": "joao@example.com"
}
```
Resposta:
```json
{
  "id": "a2695dfc-fbf9-4608-b3b0-87ed5bee6b82",
  "name": "João Silva",
  "email": "joao@example.com",
  "createdAt": "2026-01-19T14:00:00Z"
}
```

#### 2. POST /accounts
**Criar nova conta**
```json
{
  "customerId": "a2695dfc-fbf9-4608-b3b0-87ed5bee6b82"
}
```
Resposta:
```json
{
  "id": "b837e6e2-1b3c-4267-825f-741fb798f066",
  "customerId": "a2695dfc-fbf9-4608-b3b0-87ed5bee6b82",
  "agency": "0001",
  "accountNumber": "343316",
  "createdAt": "2026-01-19T14:00:00Z"
}
```

#### 3. GET /accounts/login
**Validar credenciais**
```
/accounts/login?agency=0001&accountNumber=343316
```
Resposta:
```json
{
  "accountId": "b837e6e2-1b3c-4267-825f-741fb798f066",
  "agency": "0001",
  "accountNumber": "343316",
  "customerName": "João Silva"
}
```

#### 4. GET /accounts/{accountId}/statement
**Buscar extrato com filtros**
```
/accounts/b837e6e2-1b3c-4267-825f-741fb798f066/statement
?startDate=2026-01-01
&endDate=2026-01-31
&type=CREDIT
```
Resposta:
```json
[
  {
    "id": "550e8400-e29b-41d4-a716-446655440000",
    "description": "Salário",
    "type": "CREDIT",
    "amount": 5000.00,
    "date": "2026-01-15"
  },
  {
    "id": "550e8400-e29b-41d4-a716-446655440001",
    "description": "Compra Online",
    "type": "DEBIT",
    "amount": 250.00,
    "date": "2026-01-19"
  }
]
```

#### 5. POST /transaction
**Criar nova transação**
```json
{
  "accountId": "b837e6e2-1b3c-4267-825f-741fb798f066",
  "description": "Compra no Supermercado",
  "type": "DEBIT",
  "amount": 125.50
}
```
Resposta:
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440002",
  "description": "Compra no Supermercado",
  "type": "DEBIT",
  "amount": 125.50,
  "createdAt": "2026-01-19T14:30:00Z"
}
```

### CORS
- ✅ Backend configurado para aceitar requests de `http://localhost:3000`
- ✅ Frontend roda em `http://localhost:3000`
- ✅ Sem problema de CORS

### Headers
```
Content-Type: application/json
```

---

## ✅ Estados e Validações

### Validações Frontend

#### Agência
- Obrigatório
- Exatamente 4 dígitos
- Mensagem: "Agência deve ter 4 dígitos"

#### Número da Conta
- Obrigatório
- Exatamente 6 dígitos
- Mensagem: "Conta deve ter 6 dígitos"

#### Nome
- Obrigatório
- Mínimo 3 caracteres
- Máximo 100 caracteres
- Mensagem: "Nome deve ter entre 3 e 100 caracteres"

#### Email
- Obrigatório
- Formato válido (RFC 5322)
- Mensagem: "Email inválido"

#### Descrição Transação
- Obrigatório
- Mínimo 3 caracteres
- Máximo 255 caracteres
- Mensagem: "Descrição entre 3 e 255 caracteres"

#### Valor Transação
- Obrigatório
- Mínimo 0.01
- Máximo 999.999,99
- Apenas 2 casas decimais
- Mensagem: "Valor deve ser entre 0,01 e 999.999,99"

### Estados de Loading
- Botão desabilizado durante requisição
- Spinner visível
- Texto muda para "Carregando..."

### Estados de Erro
- Toast com mensagem de erro
- Retry button em alguns casos
- Erro inline em formulários

### Estados Vazios
- Mensagem "Nenhuma transação encontrada"
- Ícone de arquivo vazio
- Botão "Criar primeira transação"

---

## 📊 Dados Técnicos

### Tipos TypeScript
```typescript
interface Account {
  id: string; // UUID
  customerId: string;
  agency: string;
  accountNumber: string;
  createdAt: string; // ISO 8601
}

interface Transaction {
  id: string; // UUID
  description: string;
  type: 'CREDIT' | 'DEBIT' | 'BALANCE';
  amount: number; // Decimal com 2 casas
  date?: string; // YYYY-MM-DD
  createdAt: string; // ISO 8601
}

interface LoginResponse {
  accountId: string;
  agency: string;
  accountNumber: string;
  customerName: string;
}

interface Statement {
  id: string;
  description: string;
  type: 'CREDIT' | 'DEBIT' | 'BALANCE';
  amount: number;
  date: string; // YYYY-MM-DD
}
```

### Formato de Data
- **Entrada**: `YYYY-MM-DD` (para API)
- **Exibição**: `DD/MM/YYYY` (para usuário)
- **Exemplos**:
  - API: `2026-01-19`
  - UI: `19/01/2026`

### Formato de Moeda
- **Símbolo**: R$ (real brasileiro)
- **Separador decimal**: `,` (vírgula)
- **Separador milhar**: `.` (ponto)
- **Exemplos**:
  - 1000.50 → R$ 1.000,50
  - 125 → R$ 125,00
  - 0.01 → R$ 0,01

### Tipos de Transação
| Tipo | Cor | Ícone | Exemplo |
|------|-----|-------|---------|
| CREDIT | Verde (#10b981) | ↓ Seta para baixo | Crédito, Depósito |
| DEBIT | Vermelho (#ef4444) | ↑ Seta para cima | Compra, Pagamento |
| BALANCE | Azul (#3b82f6) | ⚖️ Balança | Saldo diário |

---

## 🚀 Como Usar Este Brief

### Para o Lovable:
1. Cole o conteúdo do `openapi.json` no Lovable
2. Compartilhe este brief como contexto adicional
3. Mencione:
   - Paleta de cores específica
   - 5 páginas principais
   - Fluxos de usuário críticos
   - Componentes reutilizáveis

### Para seu Time:
1. Use este brief como especificação técnica
2. Refira-se aos endpoints e tipos
3. Valide features contra "Funcionalidades Críticas"
4. Use a estrutura de pastas sugerida

---

## 📝 Checklist de Implementação

### Login/Signup
- [ ] Página de login funcional
- [ ] Página de signup funcional
- [ ] Validação de campos
- [ ] Erro handling
- [ ] Redirecionamento pós-login
- [ ] Persistência de sessão

### Dashboard
- [ ] Exibir saldo principal
- [ ] Header com logout
- [ ] Abas funcionando
- [ ] Loading states

### Extrato
- [ ] Listar transações
- [ ] Filtro por data
- [ ] Filtro por tipo
- [ ] Combinar filtros
- [ ] Paginação
- [ ] Resumo (total)
- [ ] Estado vazio

### Transações
- [ ] Formulário funcional
- [ ] Validação inline
- [ ] Sucesso/erro feedback
- [ ] Auto-atualizar saldo
- [ ] Máscara de moeda

### UI/UX
- [ ] Responsive design
- [ ] Cores corretas
- [ ] Tipografia correta
- [ ] Ícones presentes
- [ ] Loading spinners
- [ ] Error boundaries
- [ ] Accessibility (alt text, labels)

### Performance
- [ ] Lazy loading de routes
- [ ] Caching de dados
- [ ] Otimização de imagens
- [ ] Bundle size < 200KB

---

## 📞 Contato e Suporte

**Documentação API**: Ver `API_DOCUMENTATION.md`
**OpenAPI Schema**: Ver `openapi.json`
**Questões Técnicas**: Revisar `DOCUMENTATION_GUIDE.md`

---

**Versão**: 1.0
**Data**: 19 de Janeiro de 2026
**Status**: Pronto para Lovable
