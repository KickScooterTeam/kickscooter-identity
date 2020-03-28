FROM openjdk:9

ENV DATASOURCE_URL jdbc:postgresql://localhost:5432/identity_service
ENV SQL_USERNAME default
ENV SQL_PASSWORD default
ENV BOOTSRAP_SERVER localhost:9092

ADD target/identity-service-0.0.1.jar /identity-service-0.0.1.jar
ENTRYPOINT java -jar identity-service-0.0.1.jar
EXPOSE 8085:8080