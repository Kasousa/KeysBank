# Configuração de Auto Scaling para ambiente dev
# Escalas de 1 a 5 instâncias t3.micro (free tier: 1 instância 24/7)

module "autoscaling" {
  source = "../../modules/autoscaling"

  project_name = var.project_name
  environment  = "Development"
  region       = var.aws_region

  # Instâncias
  instance_type = "t3.micro"  # Free tier elegível (750h/mês)
  ami_id        = data.aws_ami.amazon_linux_2.id
  subnet_ids    = module.vpc.private_subnet_ids

  # Segurança
  security_group_id         = module.security_groups.backend_security_group_id
  iam_instance_profile_arn  = ""  # Optional - deixar vazio se não tiver perfil específico

  # ALB Target Group
  target_group_arn = module.alb.backend_target_group_arn

  # Scaling Configuration
  min_size                = 1   # Começa com 1 (free tier: 750h/mês)
  max_size                = 5   # Máximo 5
  desired_capacity        = 1   # Normalmente 1
  desired_capacity_peak   = 3   # Em pico de tráfego

  enable_scheduled_scaling = true  # Scale up/down por horário

  # Database
  db_host     = module.rds.db_instance_address
  db_port     = module.rds.db_instance_port
  db_name     = var.database_name
  db_username = var.db_username
  db_password = var.db_password

  # URLs - Using direct docker images or inline deployment
  backend_jar_url = "via-docker"  # Will use Docker container instead
  frontend_url    = "http://localhost:3000"

  # Storage
  root_volume_size = 20

  # Tags
  tags = merge(
    local.common_tags,
    {
      "Name" = "${var.project_name}-backend-asg"
      "Module" = "autoscaling"
    }
  )

  depends_on = [
    module.vpc,
    module.alb,
    module.rds,
    module.security_groups
  ]
}

# Output do Auto Scaling Group
output "autoscaling_group_name" {
  description = "Nome do Auto Scaling Group"
  value       = module.autoscaling.autoscaling_group_name
}

output "current_instances" {
  description = "Comando para ver quantas instâncias estão rodando"
  value       = "aws autoscaling describe-auto-scaling-groups --auto-scaling-group-names ${module.autoscaling.autoscaling_group_name} --query 'AutoScalingGroups[0].[MinSize,DesiredCapacity,MaxSize]' --region ${var.aws_region}"
}

output "scaling_alarms" {
  description = "Alarmes de scaling (CPU alta/baixa)"
  value = {
    cpu_high = module.autoscaling.cpu_high_alarm_arn
    cpu_low  = module.autoscaling.cpu_low_alarm_arn
  }
}
