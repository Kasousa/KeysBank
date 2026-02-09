# Limpeza de Recursos Antigos (S3 + DynamoDB)

## Contexto

Anteriormente, o Terraform usava **S3 + DynamoDB** para armazenar o estado remoto. Agora o projeto foi migrado para **state local** porque você é o único desenvolvedor.

Estes recursos **não são mais necessários** e podem ser removidos para economizar ~$0.50/mês.

## ⚠️ Antes de Remover

**IMPORTANTE:** Certifique-se de que:
1. ✅ O estado local está funcionando (`terraform plan` deve funcionar normalmente)
2. ✅ Você fez backup do arquivo `terraform.tfstate` local
3. ✅ Nenhum outro ambiente (staging, prod) ainda usa esses recursos

## 🗑️ Passo 1: Verificar Recursos Existentes

```bash
# Verificar bucket S3
aws s3 ls | grep keysbank-terraform-state

# Verificar tabela DynamoDB
aws dynamodb list-tables --region sa-east-1 | grep keysbank-terraform-locks
```

## 🗑️ Passo 2: Remover S3 Bucket

```bash
# Listar objetos no bucket (dev)
aws s3 ls s3://keysbank-terraform-state-dev/ --region sa-east-1

# Baixar backup do state (já fizemos isso, mas caso precise)
aws s3 cp s3://keysbank-terraform-state-dev/dev/terraform.tfstate ~/backup-terraform-state-dev.tfstate --region sa-east-1

# Esvaziar bucket (remove todos os arquivos e versões)
aws s3 rm s3://keysbank-terraform-state-dev/ --recursive --region sa-east-1

# Desabilitar versionamento (necessário antes de deletar)
aws s3api put-bucket-versioning \
  --bucket keysbank-terraform-state-dev \
  --versioning-configuration Status=Suspended \
  --region sa-east-1

# Deletar bucket
aws s3 rb s3://keysbank-terraform-state-dev --region sa-east-1
```

Se você tem ambiente de produção:

```bash
# Mesmo processo para prod
aws s3 ls s3://keysbank-terraform-state-prod/ --region sa-east-1
aws s3 cp s3://keysbank-terraform-state-prod/prod/terraform.tfstate ~/backup-terraform-state-prod.tfstate --region sa-east-1
aws s3 rm s3://keysbank-terraform-state-prod/ --recursive --region sa-east-1
aws s3api put-bucket-versioning --bucket keysbank-terraform-state-prod --versioning-configuration Status=Suspended --region sa-east-1
aws s3 rb s3://keysbank-terraform-state-prod --region sa-east-1
```

## 🗑️ Passo 3: Remover Tabela DynamoDB

```bash
# Deletar tabela de locks
aws dynamodb delete-table \
  --table-name keysbank-terraform-locks \
  --region sa-east-1

# Verificar que foi removida
aws dynamodb list-tables --region sa-east-1
```

## ✅ Verificação

Após a limpeza:

```bash
# S3 - não deve mostrar buckets do Terraform
aws s3 ls | grep keysbank-terraform-state
# Saída esperada: vazio

# DynamoDB - não deve mostrar tabela de locks
aws dynamodb list-tables --region sa-east-1 | grep keysbank-terraform-locks
# Saída esperada: vazio
```

## 💰 Economia

Após remover estes recursos, você economiza aproximadamente:
- S3 bucket: ~$0.023/mês (armazenamento + requisições)
- DynamoDB table: ~$0.50/mês (capacity units)
- **Total:** ~$0.50/mês

## 🔙 Como Reverter (Se Necessário)

Se futuramente você quiser voltar para state remoto:

1. Recriar os recursos:
```bash
cd infra
./scripts/setup-aws.sh dev sa-east-1
```

2. Adicionar backend ao `main.tf`:
```hcl
terraform {
  backend "s3" {
    bucket         = "keysbank-terraform-state-dev"
    key            = "dev/terraform.tfstate"
    region         = "sa-east-1"
    encrypt        = true
    dynamodb_table = "keysbank-terraform-locks"
  }
}
```

3. Migrar state local para S3:
```bash
terraform init -migrate-state
```

## 📝 Notas

- Os recursos S3 e DynamoDB eram **somente para o Terraform**, não afetam sua aplicação
- Sua aplicação continua usando **RDS PostgreSQL** normalmente
- O state local funciona perfeitamente para desenvolvedores solo
- Mantenha backups regulares do arquivo `terraform.tfstate` local
