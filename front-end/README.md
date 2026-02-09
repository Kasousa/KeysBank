# KeysBank Frontend

Frontend da aplicação KeysBank construído com React, TypeScript e Vite.

## 🚀 Stack Técnica

- **React 18** - Biblioteca UI
- **TypeScript 5.8** - Type safety
- **Vite 5** - Build tool e dev server
- **Radix UI** - Componentes acessíveis
- **TailwindCSS** - Estilização
- **React Router** - Roteamento
- **Vitest** - Testes unitários

## 📋 Pré-requisitos

- Node.js 18+ e npm instalado - [instalar com nvm](https://github.com/nvm-sh/nvm#installing-and-updating)

## 🛠️ Desenvolvimento Local

## 🛠️ Desenvolvimento Local

```bash
# 1. Instalar dependências
npm install

# 2. Iniciar servidor de desenvolvimento
npm run dev

# 3. Acessar no navegador
# http://localhost:5173
```

## 🧪 Testes

```bash
# Executar testes unitários
npm test

# Testes com coverage
npm run test:coverage

# Testes em modo watch
npm run test:watch
```

## 🏗️ Build para Produção

```bash
# Build otimizado
npm run build

# Preview do build
npm run preview
```

## 📁 Estrutura do Projeto

```
src/
├── components/       # Componentes React
│   ├── auth/        # Autenticação (Login, Signup)
│   ├── dashboard/   # Dashboard (Balance, Transactions)
│   ├── layout/      # Layout (Header)
│   └── ui/          # Componentes UI (shadcn/ui)
├── pages/           # Páginas da aplicação
├── services/        # API clients
├── context/         # React Context (Auth)
├── hooks/           # Custom hooks
├── types/           # TypeScript types
└── utils/           # Funções utilitárias
```

## 🔗 Integração com Backend

O frontend se comunica com o backend através da API REST:

- **Base URL Local:** `http://localhost:8080`
- **Base URL AWS:** Configurado no ALB
- **Endpoints:** Ver [back-end/API_DOCUMENTATION.md](../back-end/API_DOCUMENTATION.md)

## 📝 Scripts Disponíveis

- `npm run dev` - Inicia servidor de desenvolvimento
- `npm run build` - Build para produção
- `npm run preview` - Preview do build de produção
- `npm test` - Executa testes unitários
- `npm run lint` - Executa ESLint
- `npm run type-check` - Verifica tipos TypeScript
