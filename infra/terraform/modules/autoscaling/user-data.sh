#!/bin/bash
set -e

echo "Starting KeysBank Backend EC2 Setup..."

# Update system - Use Amazon Linux 2
yum update -y

# Install Java 17
yum install -y java-17-amazon-corretto

# Install Maven
yum install -y maven

# Install Git
yum install -y git

# Create application directory
mkdir -p /opt/keysbank/backend
cd /opt/keysbank

# Clone repository (public repo required)
echo "Cloning KeysBank backend repository..."
git clone https://github.com/yourusername/KeysBank.git . 2>&1 || {
  echo "Clone failed - trying to build from local files..."
  # Fallback: Check if files exist locally (for testing)
  if [ ! -d "back-end" ]; then
    echo "ERROR: Cannot find backend code"
    exit 1
  fi
}

# Build JAR
echo "Building backend JAR..."
cd back-end
mvn clean package -DskipTests -q
cp target/keysbankapi-*.jar /opt/keysbank/backend/app.jar
chmod +x /opt/keysbank/backend/app.jar

echo "JAR built successfully: $(ls -lh /opt/keysbank/backend/app.jar)"

# Create environment file
cat > /opt/keysbank/backend/.env <<'ENVEOF'
SPRING_DATASOURCE_URL=jdbc:postgresql://${db_host}:${db_port}/${db_name}
SPRING_DATASOURCE_USERNAME=${db_username}
SPRING_DATASOURCE_PASSWORD=${db_password}
SERVER_PORT=8080
SPRING_JPA_HIBERNATE_DDL_AUTO=update
SPRING_JPA_SHOW_SQL=false
SPRING_PROFILES_ACTIVE=prod
ENVEOF

# Create systemd service for backend
cat > /etc/systemd/system/keysbank-backend.service << 'SERVICEEOF'
[Unit]
Description=KeysBank Backend Service
After=network.target
Wants=network-online.target

[Service]
Type=simple
User=ec2-user
WorkingDirectory=/opt/keysbank/backend
EnvironmentFile=/opt/keysbank/backend/.env
ExecStart=/usr/bin/java -Xmx256m -Xms128m -jar app.jar
Restart=on-failure
RestartSec=10
StandardOutput=append:/opt/keysbank/logs/backend.log
StandardError=append:/opt/keysbank/logs/backend-error.log

[Install]
WantedBy=multi-user.target
SERVICEEOF

# Set permissions
mkdir -p /opt/keysbank/logs
chown -R ec2-user:ec2-user /opt/keysbank
chmod -R 755 /opt/keysbank

# Enable and start service
systemctl daemon-reload
systemctl enable keysbank-backend
systemctl start keysbank-backend

# Wait for service health check
echo "Waiting for backend to start..."
for i in {1..60}; do
  if curl -s http://localhost:8080/actuator/health > /dev/null 2>&1; then
    echo "✓ Backend health check passed at $(date)"
    systemctl status keysbank-backend --no-pager
    break
  fi
  echo "Attempt $i/60: Backend not ready yet..."
  sleep 1
done

echo "EC2 KeysBank setup completed at $(date)"

