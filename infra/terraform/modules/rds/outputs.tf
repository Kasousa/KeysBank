output "db_instance_id" {
  description = "ID of the RDS instance"
  value       = aws_db_instance.postgres.id
}

output "db_instance_endpoint" {
  description = "Endpoint of the RDS instance"
  value       = aws_db_instance.postgres.endpoint
}

output "db_instance_address" {
  description = "Address of the RDS instance"
  value       = aws_db_instance.postgres.address
}

output "db_instance_port" {
  description = "Port of the RDS instance"
  value       = aws_db_instance.postgres.port
}

output "db_instance_name" {
  description = "Name of the database"
  value       = aws_db_instance.postgres.db_name
}

output "db_instance_username" {
  description = "Master username"
  value       = aws_db_instance.postgres.username
  sensitive   = true
}
