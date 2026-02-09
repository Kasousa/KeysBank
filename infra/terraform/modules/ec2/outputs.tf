output "instance_ids" {
  description = "IDs of EC2 instances"
  value       = aws_instance.backend[*].id
}

output "instance_public_ips" {
  description = "Public IPs of EC2 instances"
  value       = aws_instance.backend[*].public_ip
}

output "instance_private_ips" {
  description = "Private IPs of EC2 instances"
  value       = aws_instance.backend[*].private_ip
}

output "launch_template_id" {
  description = "ID of launch template"
  value       = aws_launch_template.backend.id
}

output "iam_role_arn" {
  description = "ARN of IAM role"
  value       = aws_iam_role.ec2.arn
}
