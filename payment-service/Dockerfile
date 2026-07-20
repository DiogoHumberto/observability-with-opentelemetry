# ====================== ESTÁGIO DE BUILD ======================
FROM maven:3.9.9-eclipse-temurin-21-alpine AS builder

WORKDIR /app

# Copia apenas o POM e o wrapper (se existir) para cache de dependências
COPY pom.xml ./
RUN mvn dependency:go-offline -B

# Copia o código fonte
COPY src ./src

# Compila e empacota, ignorando testes para agilizar (em produção ajustar)
RUN mvn clean package -DskipTests

# ====================== ESTÁGIO DE RUNTIME ======================
FROM eclipse-temurin:21-jre-alpine

# Cria usuário não-root
RUN addgroup -S appgroup && adduser -S appuser -G appgroup
USER appuser

WORKDIR /app

# Baixa o agente OpenTelemetry Java (versão estável mais recente)
ADD --chown=appuser:appgroup \
    https://github.com/open-telemetry/opentelemetry-java-instrumentation/releases/latest/download/opentelemetry-javaagent.jar \
    /opt/otel/opentelemetry-javaagent.jar

# Copia o artefato gerado no estágio de build
COPY --from=builder --chown=appuser:appgroup /app/target/*.jar app.jar

# Variáveis de ambiente padrão (sobrescrevíveis no docker-compose ou runtime)
ENV JAVA_OPTS="-Xms256m -Xmx512m" \
    OTEL_SERVICE_NAME="order-service" \
    OTEL_EXPORTER_OTLP_ENDPOINT="http://otel-collector:4318" \
    OTEL_TRACES_EXPORTER="otlp" \
    OTEL_LOGS_EXPORTER="otlp" \
    OTEL_METRICS_EXPORTER="none" \
    OTEL_PROPAGATORS="tracecontext,baggage" \
    OTEL_RESOURCE_ATTRIBUTES="deployment.environment=dev,service.namespace=alura-case"

EXPOSE 8080

ENTRYPOINT ["sh", "-c", \
    "java -javaagent:/opt/otel/opentelemetry-javaagent.jar ${JAVA_OPTS} -jar app.jar"]