# Build stage: compila el JAR con Maven y Java 17
FROM maven:3.9-eclipse-temurin-17 AS builder
WORKDIR /workspace

# Copia sólo el POM para aprovechar la caché de dependencias
COPY pom.xml ./
RUN mvn -ntp -B -q -DskipTests dependency:go-offline

# Copia el código fuente y compila
COPY src ./src
RUN mvn -ntp -B -q -DskipTests package

# Runtime stage: imagen ligera con JRE 17
FROM eclipse-temurin:17-jre-alpine

ENV TZ=UTC \
    JAVA_OPTS=""

WORKDIR /app

# Herramientas mínimas (curl para healthchecks) y usuario no root
RUN apk add --no-cache curl \
    && addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Copiar el JAR compilado
COPY --from=builder /workspace/target/*.jar /app/app.jar

# Puerto por defecto
EXPOSE 8080

# Permitir flags vía JAVA_OPTS (memoria, debug, etc.)
ENTRYPOINT ["sh","-c","java $JAVA_OPTS -jar /app/app.jar"]
