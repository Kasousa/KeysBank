output "vpc_id" {
  description = "VPC ID"
  value       = module.vpc.vpc_id
}

output "alb_dns_name" {
  description = "DNS name of Application Load Balancer"
  value       = module.alb.alb_dns_name
}

output "backend_instance_ips" {
  description = "Public IPs of backend instances"
  value       = module.ec2.instance_public_ips
}

output "rds_endpoint" {
  description = "RDS endpoint"
  value       = module.rds.db_instance_endpoint
  sensitive   = true
}

output "rds_address" {
  description = "RDS address"
  value       = module.rds.db_instance_address
}
