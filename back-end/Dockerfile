FROM eclipse-temurin:17-jre

WORKDIR /app

# Adicionar o JAR da aplicação
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar

# Expor porta
EXPOSE 8080

# Variáveis de ambiente (serão sobrescritas no runtime)
ENV SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/bank
ENV SPRING_DATASOURCE_USERNAME=bankuser
ENV SPRING_DATASOURCE_PASSWORD=bankpass123
ENV SERVER_PORT=8080

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=40s \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8080/actuator/health || exit 1

# Rodar aplicação
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
