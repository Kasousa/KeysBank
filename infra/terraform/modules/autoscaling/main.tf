# Auto Scaling Group Module for KeysBank
# Escalas automaticamente entre min_size e max_size baseado em métricas

# Launch Template (compartilhado com ASG)
resource "aws_launch_template" "backend" {
  name_prefix   = "${var.project_name}-backend-asg-"
  image_id      = var.ami_id
  instance_type = var.instance_type

  # IAM Instance Profile para acesso a CloudWatch, logs, etc
  iam_instance_profile {
    arn = var.iam_instance_profile_arn
  }

  # Security Group
  vpc_security_group_ids = [var.security_group_id]

  # User Data Script
  user_data = base64encode(templatefile("${path.module}/user-data.sh", {
    region          = var.region
    environment     = var.environment
    db_host         = var.db_host
    db_port         = var.db_port
    db_name         = var.db_name
    db_username     = var.db_username
    db_password     = var.db_password
    backend_jar_url = var.backend_jar_url
    frontend_url    = var.frontend_url
  }))

  # Monitoring
  monitoring {
    enabled = true
  }

  # Metadata para security
  metadata_options {
    http_tokens                 = "required"
    http_put_response_hop_limit = 1
  }

  # Root volume
  block_device_mappings {
    device_name = "/dev/xvda"

    ebs {
      volume_size           = var.root_volume_size
      volume_type           = "gp3"
      iops                  = 3000
      throughput            = 125
      encrypted             = true
      delete_on_termination = true
    }
  }

  # Tags
  tag_specifications {
    resource_type = "instance"
    tags = merge(
      var.tags,
      {
        Name = "${var.project_name}-backend-asg"
      }
    )
  }

  tag_specifications {
    resource_type = "volume"
    tags = merge(
      var.tags,
      {
        Name = "${var.project_name}-backend-volume"
      }
    )
  }

  tag_specifications {
    resource_type = "network-interface"
    tags = merge(
      var.tags,
      {
        Name = "${var.project_name}-backend-eni"
      }
    )
  }

  lifecycle {
    create_before_destroy = true
  }
}

# Auto Scaling Group
resource "aws_autoscaling_group" "backend" {
  name                = "${var.project_name}-backend-asg"
  vpc_zone_identifier = var.subnet_ids
  target_group_arns   = [var.target_group_arn]
  health_check_type   = "ELB"
  health_check_grace_period = 300

  min_size         = var.min_size
  max_size         = var.max_size
  desired_capacity = var.desired_capacity

  launch_template {
    id      = aws_launch_template.backend.id
    version = "$Latest"
  }

  # Rebalance between AZs
  capacity_rebalance = true

  # Lifecycle - não terminar instâncias ao destruir ASG
  termination_policies = [
    "OldestLaunchTemplate",
    "OldestInstance"
  ]

  # Tag todas instâncias
  tag {
    key                 = "Name"
    value               = "${var.project_name}-backend-instance"
    propagate_at_launch = true
  }

  tag {
    key                 = "Environment"
    value               = var.environment
    propagate_at_launch = true
  }

  tag {
    key                 = "ManagedBy"
    value               = "Terraform-ASG"
    propagate_at_launch = true
  }

  dynamic "tag" {
    for_each = var.tags
    content {
      key                 = tag.key
      value               = tag.value
      propagate_at_launch = true
    }
  }

  lifecycle {
    create_before_destroy = true
  }

  depends_on = [
    aws_launch_template.backend
  ]
}

# Scaling Policy - Scale Up (aumenta instâncias)
resource "aws_autoscaling_policy" "scale_up" {
  name                   = "${var.project_name}-scale-up"
  scaling_adjustment     = 1
  adjustment_type        = "ChangeInCapacity"
  cooldown               = 300
  autoscaling_group_name = aws_autoscaling_group.backend.name
}

# Scale Up Alarm - Ativa quando CPU > 70%
resource "aws_cloudwatch_metric_alarm" "cpu_high" {
  alarm_name          = "${var.project_name}-cpu-high"
  comparison_operator = "GreaterThanThreshold"
  evaluation_periods  = 2
  metric_name         = "CPUUtilization"
  namespace           = "AWS/EC2"
  period              = 60
  statistic           = "Average"
  threshold           = 70
  alarm_description   = "Ativa scale-up quando CPU média > 70% por 2 minutos"
  alarm_actions       = [aws_autoscaling_policy.scale_up.arn]

  dimensions = {
    AutoScalingGroupName = aws_autoscaling_group.backend.name
  }

  tags = var.tags
}

# Scaling Policy - Scale Down (diminui instâncias)
resource "aws_autoscaling_policy" "scale_down" {
  name                   = "${var.project_name}-scale-down"
  scaling_adjustment     = -1
  adjustment_type        = "ChangeInCapacity"
  cooldown               = 600
  autoscaling_group_name = aws_autoscaling_group.backend.name
}

# Scale Down Alarm - Ativa quando CPU < 30%
resource "aws_cloudwatch_metric_alarm" "cpu_low" {
  alarm_name          = "${var.project_name}-cpu-low"
  comparison_operator = "LessThanThreshold"
  evaluation_periods  = 5
  metric_name         = "CPUUtilization"
  namespace           = "AWS/EC2"
  period              = 60
  statistic           = "Average"
  threshold           = 30
  alarm_description   = "Ativa scale-down quando CPU média < 30% por 5 minutos"
  alarm_actions       = [aws_autoscaling_policy.scale_down.arn]

  dimensions = {
    AutoScalingGroupName = aws_autoscaling_group.backend.name
  }

  tags = var.tags
}

# Scaling Policy Alternativa - Target Tracking (mais sofisticado)
resource "aws_autoscaling_policy" "target_tracking" {
  name                   = "${var.project_name}-target-tracking-cpu"
  autoscaling_group_name = aws_autoscaling_group.backend.name
  policy_type            = "TargetTrackingScaling"

  target_tracking_configuration {
    predefined_metric_specification {
      predefined_metric_type = "ASGAverageCPUUtilization"
    }

    target_value = 60.0
  }
}

# Scheduled Action - Scale Up para hora de pico (exemplo: 18:00)
resource "aws_autoscaling_schedule" "scale_up_peak_hours" {
  count                  = var.enable_scheduled_scaling ? 1 : 0
  scheduled_action_name  = "${var.project_name}-scale-up-peak"
  min_size               = var.min_size
  max_size               = var.max_size
  desired_capacity       = var.desired_capacity_peak
  recurrence             = "0 18 * * MON-FRI"  # 18h (6 PM) seg-sex
  time_zone              = "America/Sao_Paulo"
  autoscaling_group_name = aws_autoscaling_group.backend.name
}

# Scheduled Action - Scale Down de madrugada (exemplo: 02:00)
resource "aws_autoscaling_schedule" "scale_down_night" {
  count                  = var.enable_scheduled_scaling ? 1 : 0
  scheduled_action_name  = "${var.project_name}-scale-down-night"
  min_size               = var.min_size
  max_size               = var.max_size
  desired_capacity       = var.min_size
  recurrence             = "0 2 * * *"  # 02h (2 AM) todos dias
  time_zone              = "America/Sao_Paulo"
  autoscaling_group_name = aws_autoscaling_group.backend.name
}

# Lifecycle Hook - Para graceful shutdown
resource "aws_autoscaling_lifecycle_hook" "backend_termination" {
  name                   = "${var.project_name}-termination-hook"
  autoscaling_group_name = aws_autoscaling_group.backend.name
  lifecycle_transition   = "autoscaling:EC2_INSTANCE_TERMINATING"
  heartbeat_timeout      = 300
  default_result         = "CONTINUE"
}

# SNS Topic para notificações de scaling
resource "aws_sns_topic" "scaling_notifications" {
  name = "${var.project_name}-scaling-notifications"

  tags = var.tags
}

resource "aws_autoscaling_notification" "backend_scaling" {
  group_names = [aws_autoscaling_group.backend.name]

  notifications = [
    "autoscaling:EC2_INSTANCE_LAUNCH",
    "autoscaling:EC2_INSTANCE_TERMINATE",
    "autoscaling:EC2_INSTANCE_LAUNCH_ERROR",
    "autoscaling:EC2_INSTANCE_TERMINATE_ERROR"
  ]

  topic_arn = aws_sns_topic.scaling_notifications.arn
}
