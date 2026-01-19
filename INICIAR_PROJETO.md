# 🚀 Guia de Inicialização - KeysBank

## ⚡ Quick Start (3 passos)

### Passo 1: Iniciar Backend

```bash
cd /Users/kaiquesantossousa/Projects/KeysBank/back-end
./mvnw spring-boot:run
```

Você verá:
```
Started KeysbankapiApplication in X.XXX seconds
```

✅ Backend rodando em: **http://localhost:8080**

---

### Passo 2: Iniciar Frontend

Em **outro terminal**:

```bash
cd /Users/kaiquesantossousa/Projects/KeysBank/front-end
npm run dev
```

Você verá:
```
VITE v7.3.1  ready in XXX ms
➜  Local:   http://localhost:3000/
```

✅ Frontend rodando em: **http://localhost:3000**

---

### Passo 3: Abrir no Navegador

Abra seu navegador em:

```
http://localhost:3000
```

**Pronto! 🎉 Sua aplicação está funcionando!**

---

## 📋 Fluxo para Testar

### 1️⃣ Login Inicial

Na tela de login, você pode:

**Opção A: Criar Nova Conta**
- Clique em "✨ Criar Nova Conta"
- Preencha dados pessoais
- Sistema gera agência e conta
- Anote os valores

**Opção B: Usar dados de teste**
- Agência: `0001`
- Conta: `123456`
(Se a conta existe no banco)

### 2️⃣ Usar o Dashboard

**Aba de Extrato:**
- Veja todas as transações
- Filtre por data (intervalo)
- Filtre por tipo (CREDIT, DEBIT, BALANCE)
- Veja o saldo diário calculado automaticamente

**Aba de Lançamentos:**
- Crie débitos e créditos
- Escolha uma categoria
- Insira o valor
- Escreva uma descrição
- Clique em "✅ Criar Lançamento"

---

## 📂 Estrutura de Arquivos Importantes

```
KeysBank/
├── back-end/
│   ├── src/main/java/...
│   │   └── ledger/
│   │       ├── TransactionService.java    (Cálculo de saldo)
│   │       ├── StatementService.java      (Filtros)
│   │       └── StatementController.java   (Endpoint /statement)
│   └── pom.xml
│
└── front-end/
    ├── src/
    │   ├── pages/
    │   │   ├── LoginPage.jsx
    │   │   ├── CreateAccountPage.jsx
    │   │   └── DashboardPage.jsx
    │   ├── components/
    │   │   ├── StatementTab.jsx
    │   │   └── TransactionTab.jsx
    │   └── api.js
    └── package.json
```

---

## 🔧 Comandos Úteis

### Backend

```bash
# Iniciar desenvolvimento
./mvnw spring-boot:run

# Build
./mvnw clean install

# Rodar testes
./mvnw test
```

### Frontend

```bash
# Iniciar desenvolvimento
npm run dev

# Build para produção
npm run build

# Preview do build
npm run preview

# Lint
npm run lint
```

---

## 🐛 Troubleshooting

### "Porta 8080 já em uso"
```bash
# Mude no application.yaml
server.port=8081
```

### "Porta 3000 já em uso"
```bash
npm run dev -- --port 3001
```

### "Erro ao conectar backend"
1. Verifique se backend está rodando
2. Verifique URL em `front-end/src/api.js`
3. Verifique CORS no backend

### "Dependências faltando"
```bash
# Frontend
cd front-end
npm install

# Backend
cd back-end
./mvnw clean install
```

---

## 📚 Documentação Completa

- **GUIA_RAPIDO.md** - Guia rápido (5 min)
- **DOCUMENTACAO.md** - Documentação técnica detalhada
- **GUIA_TESTES.md** - 23 testes para validar tudo
- **SUMARIO_COMPLETO.md** - Sumário de tudo que foi feito

---

## ✅ Checklist

- [ ] Backend rodando em :8080
- [ ] Frontend rodando em :3000
- [ ] Abrindo em http://localhost:3000
- [ ] Criar nova conta com sucesso
- [ ] Logar na conta criada
- [ ] Ver extrato de transações
- [ ] Criar uma transação
- [ ] Filtrar por data
- [ ] Filtrar por tipo
- [ ] Ver saldo calculado

---

## 🎉 Tudo Pronto!

Você tem um **banco virtual completo** com:
- ✅ Backend Java robusto
- ✅ Frontend React moderno
- ✅ Filtros avançados
- ✅ Saldo automático
- ✅ Design responsivo

**Aproveite! 🚀**

---

Para mais informações, veja: `SUMARIO_COMPLETO.md`
