FROM openjdk:9
ADD target/identity-service-0.0.1.jar /identity-service-0.0.1.jar
RUN wget "https://collectors.us2.sumologic.com/rest/download/linux/64" -O SumoCollector.sh && chmod +x SumoCollector.sh
RUN ./SumoCollector.sh -q -Vsumo.token_and_url=ZVBvWHBBb3k2eUk0bkgxN3lnSUhlWE9LWTBxZ1k1cThodHRwczovL2NvbGxlY3RvcnMudXMyLnN1bW9sb2dpYy5jb20= -Vcollector.name=identity
ENTRYPOINT java -jar identity-service-0.0.1.jar
