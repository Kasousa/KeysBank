terraform {
  required_version = ">= 1.0"

  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 5.0"
    }
  }

  # Estado local - não precisa de S3/DynamoDB para um único desenvolvedor
  # O arquivo terraform.tfstate será criado automaticamente neste diretório
}

provider "aws" {
  region = var.aws_region

  default_tags {
    tags = {
      Environment = "Production"
      Project     = "KeysBank"
      ManagedBy   = "Terraform"
    }
  }
}

data "aws_availability_zones" "available" {
  state = "available"
}

data "aws_ami" "amazon_linux_2" {
  most_recent = true
  owners      = ["amazon"]

  filter {
    name   = "name"
    values = ["amzn2-ami-hvm-*-x86_64-gp2"]
  }
}

module "vpc" {
  source = "../../modules/vpc"

  project_name         = var.project_name
  vpc_cidr             = var.vpc_cidr
  public_subnet_cidrs  = var.public_subnet_cidrs
  private_subnet_cidrs = var.private_subnet_cidrs
  availability_zones   = slice(data.aws_availability_zones.available.names, 0, 2)
  enable_nat_gateway   = var.enable_nat_gateway

  tags = local.common_tags
}

module "security_groups" {
  source = "../../modules/security-groups"

  project_name      = var.project_name
  vpc_id            = module.vpc.vpc_id
  ssh_allowed_cidrs = var.ssh_allowed_cidrs

  tags = local.common_tags
}

module "rds" {
  source = "../../modules/rds"

  project_name            = var.project_name
  subnet_ids              = module.vpc.private_subnet_ids
  security_group_id       = module.security_groups.rds_security_group_id
  instance_class          = var.rds_instance_class
  allocated_storage       = var.rds_allocated_storage
  postgres_version        = var.rds_postgres_version
  database_name           = var.database_name
  master_username         = var.db_username
  master_password         = var.db_password
  backup_retention_period = var.rds_backup_retention_period
  multi_az                = var.rds_multi_az
  skip_final_snapshot     = false
  deletion_protection     = true

  tags = local.common_tags
}

module "ec2" {
  source = "../../modules/ec2"

  project_name      = var.project_name
  region            = var.aws_region
  ami_id            = data.aws_ami.amazon_linux_2.id
  instance_type     = var.ec2_instance_type
  instance_count    = var.ec2_instance_count
  subnet_ids        = module.vpc.public_subnet_ids
  security_group_id = module.security_groups.backend_security_group_id
  key_name          = var.ec2_key_name
  root_volume_size  = var.ec2_root_volume_size

  db_host     = module.rds.db_instance_address
  db_port     = module.rds.db_instance_port
  db_name     = var.database_name
  db_username = var.db_username
  db_password = var.db_password

  tags = local.common_tags

  depends_on = [module.rds]
}

module "alb" {
  source = "../../modules/alb"

  project_name               = var.project_name
  vpc_id                     = module.vpc.vpc_id
  subnet_ids                 = module.vpc.public_subnet_ids
  security_group_id          = module.security_groups.alb_security_group_id
  target_instance_ids        = module.ec2.instance_ids
  certificate_arn            = var.ssl_certificate_arn
  enable_deletion_protection = true

  tags = local.common_tags

  depends_on = [module.ec2]
}

locals {
  common_tags = {
    Environment = "Production"
    Project     = var.project_name
    ManagedBy   = "Terraform"
  }
}
