FROM maven:3-eclipse-temurin-25-alpine AS build

WORKDIR /workspace
COPY . .
RUN mvn -B -pl enceladus-ai-demo-documents-ingestion -am -DskipTests package

FROM eclipse-temurin:25-jre-alpine

ENV TZ UTC
ENV OTEL_VERSION=2.16.0
ENV PORT=8080

WORKDIR /app
COPY --from=build /workspace/enceladus-ai-demo-documents-ingestion/target/enceladus-ai-demo-documents-ingestion-1.0-SNAPSHOT.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
