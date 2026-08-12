# ============================================
# ETAPA RUNTIME
# ============================================
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY target/*.jar /app/user-service.jar

EXPOSE 8081

ENV JAVA_OPTS="-Xmx512m -Xms256m"

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app/user-service.jar"]

