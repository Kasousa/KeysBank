# 📤 Guia de Exportação - Documentação da API

Este guia mostra como exportar e compartilhar a documentação da API KeysBank.

---

## 📁 Arquivos de Documentação

Existem 3 arquivos principais para compartilhar:

### 1. **openapi.json** (Arquivo Principal)
**Localização**: `/back-end/openapi.json`
**Formato**: JSON (OpenAPI 3.0)
**Tamanho**: ~15 KB
**Uso**: Importar em Swagger UI, Postman, Insomnia, Lovable

### 2. **API_DOCUMENTATION.md** (Guia Técnico)
**Localização**: `/back-end/API_DOCUMENTATION.md`
**Formato**: Markdown
**Tamanho**: ~50 KB
**Uso**: Ler em navegador, clonar em documentação interna

### 3. **DOCUMENTATION_GUIDE.md** (Guia Geral)
**Localização**: `/DOCUMENTATION_GUIDE.md`
**Formato**: Markdown
**Tamanho**: ~25 KB
**Uso**: Resumo e índice de toda documentação

### 4. **README.md** (Visão Geral)
**Localização**: `/README.md`
**Formato**: Markdown
**Tamanho**: ~15 KB
**Uso**: Início rápido do projeto

---

## 🚀 Para Compartilhar com Lovable

### Passo 1: Abrir o arquivo openapi.json
```bash
cat /path/to/KeysBank/back-end/openapi.json
```

### Passo 2: Copiar todo o conteúdo JSON

### Passo 3: Ir para https://lovable.dev

### Passo 4: Colar o JSON no campo de importação

### Passo 5: Lovable gerará automaticamente:
- ✅ Componentes React 19
- ✅ Tipos TypeScript
- ✅ Hooks para API
- ✅ Validação de formulários
- ✅ Tratamento de erros

### Resultado
Frontend 100% tipado e funcional, sem erros de integração!

---

## 📤 Para Compartilhar com Desenvolvedores

### Opção 1: Arquivo ZIP
```bash
cd /path/to/KeysBank
zip -r KeysBank-API-Docs.zip \
  back-end/openapi.json \
  back-end/API_DOCUMENTATION.md \
  DOCUMENTATION_GUIDE.md \
  README.md
```

### Opção 2: Via Git
```bash
git clone <repository>
cd KeysBank
# Arquivos estão em:
# - back-end/openapi.json
# - back-end/API_DOCUMENTATION.md
# - DOCUMENTATION_GUIDE.md
```

### Opção 3: Publicar em Wiki
1. Copie o conteúdo de `API_DOCUMENTATION.md`
2. Crie página em seu wiki/confluence
3. Compartilhe o link

---

## 🔗 URLs para Documentação Local

Quando o backend estiver rodando:

| Recurso | URL |
|---------|-----|
| Swagger UI | http://localhost:8080/swagger-ui.html |
| OpenAPI JSON | http://localhost:8080/v3/api-docs |
| OpenAPI YAML | http://localhost:8080/v3/api-docs.yaml |

---

## 📋 Checklist de Documentação

- ✅ Arquivo `openapi.json` presente em `/back-end/`
- ✅ Arquivo `API_DOCUMENTATION.md` presente em `/back-end/`
- ✅ Arquivo `DOCUMENTATION_GUIDE.md` presente em raiz
- ✅ Arquivo `README.md` presente em raiz
- ✅ Swagger UI acessível em http://localhost:8080/swagger-ui.html
- ✅ Todos os 5 endpoints documentados
- ✅ Todos os 6 DTOs com @Schema
- ✅ Todos os controllers com @Operation e @ApiResponses
- ✅ Exemplos de request/response inclusos
- ✅ Validações documentadas

---

## 🎯 Conteúdo da Documentação

### OpenAPI JSON Inclui:
```
✅ Informações gerais da API (título, versão, descrição)
✅ 5 endpoints (customers, accounts, login, statement, transaction)
✅ 6 schemas (DTOs completos)
✅ Exemplos de valores
✅ Validações (required, enum, etc)
✅ Códigos de erro (201, 400, 404, 500)
✅ Descrições detalhadas
✅ Tags para agrupamento
✅ Formatos de dados (uuid, date, decimal, etc)
```

### API_DOCUMENTATION.md Inclui:
```
✅ Visão geral da API
✅ Autenticação e fluxo de login
✅ Documentação de cada endpoint
✅ Exemplos em cURL
✅ Tratamento de erros
✅ Tipos de transações
✅ Swagger UI info
✅ Integração com Lovable
```

### DOCUMENTATION_GUIDE.md Inclui:
```
✅ Resumo da implementação
✅ Recursos de documentação
✅ Como usar a documentação
✅ Exemplos de uso (cURL)
✅ Estrutura do projeto
✅ Links úteis
✅ Funcionalidades implementadas
✅ Fluxo de aprendizado
```

### README.md Inclui:
```
✅ Visão geral do projeto
✅ Estrutura de pastas
✅ Como começar
✅ Endpoints principais
✅ Fluxo de uso
✅ Integração com Lovable
✅ Funcionalidades
✅ Solução de problemas
```

---

## 💡 Dicas de Uso

### Para Ler a Documentação
1. Comece pelo `README.md` (visão geral)
2. Leia `DOCUMENTATION_GUIDE.md` (guia estruturado)
3. Consulte `API_DOCUMENTATION.md` (referência técnica)
4. Use Swagger UI para testar (interativo)

### Para Integrar com Lovable
1. Abra `back-end/openapi.json`
2. Copie todo o conteúdo JSON
3. Vá para https://lovable.dev
4. Crie novo projeto
5. Importe o OpenAPI JSON
6. Lovable gerará o frontend automaticamente

### Para Adicionar a Postman
1. Abra Postman
2. Clique em "Import"
3. Escolha "Raw text"
4. Cole o conteúdo de `openapi.json`
5. Crie coleção automaticamente
6. Teste endpoints

---

## 🔄 Manter Documentação Atualizada

### Quando Adicionar Novo Endpoint:
1. Edite o controller com `@Operation` e `@ApiResponses`
2. Edite o DTO com `@Schema`
3. Atualize `openapi.json` manualmente (ou regenere com Swagger)
4. Atualize `API_DOCUMENTATION.md` com exemplos
5. Commit dos arquivos

### Workflow:
```bash
# Edite seu endpoint
# vim src/main/java/...Controller.java

# Recompile para gerar swagger-ui
mvn clean package

# Acesse http://localhost:8080/swagger-ui.html
# Copie o novo OpenAPI JSON

# Atualize openapi.json
# Atualize API_DOCUMENTATION.md

# Commit
git add back-end/openapi.json back-end/API_DOCUMENTATION.md
git commit -m "docs: add new endpoint documentation"
```

---

## 📊 Estatísticas da Documentação

| Métrica | Valor |
|---------|-------|
| Endpoints Documentados | 5 |
| DTOs Documentados | 6 |
| Exemplos inclusos | 10+ |
| Linhas de documentação | 1500+ |
| Formatos suportados | JSON, YAML, Markdown |
| Ferramentas compatíveis | Swagger UI, Postman, Insomnia, Lovable |

---

## 🎓 Exemplos de Documentação

### Endpoint Documentado
```yaml
GET /accounts/{accountId}/statement:
  summary: "Recuperar extrato da conta"
  description: "Retorna o extrato com filtros opcionais"
  parameters:
    - accountId (UUID, obrigatório)
    - startDate (date, opcional)
    - endDate (date, opcional)
    - type (string: CREDIT|DEBIT|BALANCE, opcional)
  responses:
    200: Array de StatementItemResponse
    404: Conta não encontrada
    400: Parâmetros inválidos
```

### DTO Documentado
```java
@Schema(description = "Resposta de login")
public class LoginResponse {
  @Schema(description = "ID único da conta", example = "xxx-xxx")
  private UUID accountId;
  
  @Schema(description = "Número da agência", example = "0001")
  private String agency;
  
  @Schema(description = "Número da conta", example = "343316")
  private String accountNumber;
  
  @Schema(description = "Nome do cliente", example = "João")
  private String customerName;
}
```

---

## ✅ Validação de Documentação

Para verificar se tudo está OK:

```bash
# 1. Verificar se arquivos existem
ls -la /back-end/openapi.json
ls -la /back-end/API_DOCUMENTATION.md
ls -la /DOCUMENTATION_GUIDE.md
ls -la /README.md

# 2. Validar JSON
cat /back-end/openapi.json | jq '.' > /dev/null && echo "✅ JSON válido"

# 3. Iniciar backend e testar
cd /back-end && mvn spring-boot:run &
sleep 5
curl -s http://localhost:8080/swagger-ui.html | head -20

# 4. Verificar endpoints
curl -s -X POST http://localhost:8080/customers \
  -H "Content-Type: application/json" \
  -d '{"name":"Test","email":"test@test.com"}' | jq '.id'
```

---

## 🚀 Próximos Passos

1. **Compartilhe com Lovable**
   - Cole o `openapi.json` em https://lovable.dev
   - Gere frontend automático

2. **Importe em Postman**
   - File → Import → openapi.json
   - Teste todos os endpoints

3. **Publique a Documentação**
   - Copie para seu wiki corporativo
   - Ou hospede em GitHub Pages

4. **Mantenha Atualizado**
   - Atualize quando adicionar endpoints
   - Mantenha síncrono com código

---

## 📞 Suporte

- **Documentação**: Veja `/DOCUMENTATION_GUIDE.md`
- **API Reference**: Veja `/back-end/API_DOCUMENTATION.md`
- **OpenAPI**: Use `back-end/openapi.json`
- **Swagger UI**: http://localhost:8080/swagger-ui.html

---

**Documentação Versão**: 1.0.0
**Data**: 19 de Janeiro de 2026
**Status**: ✅ Completa e operacional
