# Outputs do módulo Auto Scaling

output "autoscaling_group_name" {
  description = "Nome do Auto Scaling Group"
  value       = aws_autoscaling_group.backend.name
}

output "autoscaling_group_arn" {
  description = "ARN do Auto Scaling Group"
  value       = aws_autoscaling_group.backend.arn
}

output "launch_template_id" {
  description = "ID do Launch Template"
  value       = aws_launch_template.backend.id
}

output "launch_template_name" {
  description = "Nome do Launch Template"
  value       = aws_launch_template.backend.name
}

output "cpu_high_alarm_arn" {
  description = "ARN do alarme de CPU alta (scale-up)"
  value       = aws_cloudwatch_metric_alarm.cpu_high.arn
}

output "cpu_low_alarm_arn" {
  description = "ARN do alarme de CPU baixa (scale-down)"
  value       = aws_cloudwatch_metric_alarm.cpu_low.arn
}

output "sns_topic_arn" {
  description = "ARN do SNS Topic para notificações de scaling"
  value       = aws_sns_topic.scaling_notifications.arn
}

output "min_size" {
  description = "Tamanho mínimo do ASG"
  value       = aws_autoscaling_group.backend.min_size
}

output "max_size" {
  description = "Tamanho máximo do ASG"
  value       = aws_autoscaling_group.backend.max_size
}

output "current_capacity" {
  description = "Capacidade desejada atual"
  value       = aws_autoscaling_group.backend.desired_capacity
}

output "scaling_policies" {
  description = "Nomes das scaling policies"
  value = {
    scale_up   = aws_autoscaling_policy.scale_up.name
    scale_down = aws_autoscaling_policy.scale_down.name
    target_tracking = aws_autoscaling_policy.target_tracking.name
  }
}
