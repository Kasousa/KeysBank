#!/bin/bash
set -e

# Update system
yum update -y

# Install Java 17 (compatível com a aplicação)
yum install -y java-17-amazon-corretto

# Install Nginx for frontend + reverse proxy
amazon-linux-extras install nginx1 -y
systemctl start nginx
systemctl enable nginx

# Install Docker
yum install docker -y
systemctl start docker
systemctl enable docker
usermod -aG docker ec2-user

# Install Docker Compose
curl -L "https://github.com/docker/compose/releases/latest/download/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
chmod +x /usr/local/bin/docker-compose

# Install CloudWatch Agent
wget https://s3.${region}.amazonaws.com/amazoncloudwatch-agent-${region}/amazon_linux/amd64/latest/amazon-cloudwatch-agent.rpm
rpm -U ./amazon-cloudwatch-agent.rpm

# Create application directory
mkdir -p /opt/keysbank/{backend,frontend,logs}
cd /opt/keysbank

# Create environment file
cat > /opt/keysbank/backend/.env <<EOF
SPRING_DATASOURCE_URL=jdbc:postgresql://${db_host}:${db_port}/${db_name}
SPRING_DATASOURCE_USERNAME=${db_username}
SPRING_DATASOURCE_PASSWORD=${db_password}
SERVER_PORT=8080
SPRING_JPA_HIBERNATE_DDL_AUTO=update
SPRING_JPA_SHOW_SQL=false
EOF

# Create systemd service for backend
cat > /etc/systemd/system/keysbank-backend.service <<EOF
[Unit]
Description=KeysBank Backend Service
After=network.target

[Service]
Type=simple
User=ec2-user
WorkingDirectory=/opt/keysbank/backend
EnvironmentFile=/opt/keysbank/backend/.env
ExecStart=/usr/bin/java -jar /opt/keysbank/backend/app.jar
Restart=on-failure
RestartSec=10
StandardOutput=append:/opt/keysbank/logs/backend.log
StandardError=append:/opt/keysbank/logs/backend-error.log

[Install]
WantedBy=multi-user.target
EOF

# Set permissions
chown -R ec2-user:ec2-user /opt/keysbank

# Enable service (will start after JAR is deployed)
systemctl daemon-reload
systemctl enable keysbank-backend

echo "EC2 setup completed successfully"
