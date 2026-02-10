# Variáveis para o módulo Auto Scaling

variable "project_name" {
  description = "Nome do projeto"
  type        = string
}

variable "environment" {
  description = "Ambiente (dev, prod)"
  type        = string
}

variable "region" {
  description = "Região AWS"
  type        = string
}

variable "ami_id" {
  description = "ID da AMI (Amazon Machine Image) com backend pré-instalado"
  type        = string
}

variable "instance_type" {
  description = "Tipo de instância (ex: t3.micro para free tier, t4g.micro para ARM free tier)"
  type        = string
  default     = "t3.micro"
}

variable "subnet_ids" {
  description = "IDs das subnets para distribuir instâncias"
  type        = list(string)
}

variable "security_group_id" {
  description = "ID do Security Group para as instâncias"
  type        = string
}

variable "target_group_arn" {
  description = "ARN do Target Group do ALB"
  type        = string
}

variable "iam_instance_profile_arn" {
  description = "ARN do IAM Instance Profile"
  type        = string
}

# Scaling Configuration
variable "min_size" {
  description = "Número mínimo de instâncias"
  type        = number
  default     = 2
}

variable "max_size" {
  description = "Número máximo de instâncias"
  type        = number
  default     = 5
}

variable "desired_capacity" {
  description = "Número desejado de instâncias normalmente"
  type        = number
  default     = 2
}

variable "desired_capacity_peak" {
  description = "Número desejado durante horas de pico"
  type        = number
  default     = 4
}

variable "enable_scheduled_scaling" {
  description = "Habilita scaling agendado (picos horários)"
  type        = bool
  default     = true
}

# Database Configuration
variable "db_host" {
  description = "Host do PostgreSQL RDS"
  type        = string
  sensitive   = true
}

variable "db_port" {
  description = "Porta do PostgreSQL"
  type        = number
  default     = 5432
}

variable "db_name" {
  description = "Nome do banco de dados"
  type        = string
  default     = "bank"
}

variable "db_username" {
  description = "Usuário do banco de dados"
  type        = string
  sensitive   = true
}

variable "db_password" {
  description = "Senha do banco de dados"
  type        = string
  sensitive   = true
}

variable "backend_jar_url" {
  description = "URL do JAR do backend (ex: S3)"
  type        = string
  default     = "http://localhost:8080"
}

variable "frontend_url" {
  description = "URL do frontend para CORS"
  type        = string
}

# Storage
variable "root_volume_size" {
  description = "Tamanho do volume root em GB"
  type        = number
  default     = 20
}

# Tags
variable "tags" {
  description = "Tags para aplicar aos recursos"
  type        = map(string)
  default     = {}
}
