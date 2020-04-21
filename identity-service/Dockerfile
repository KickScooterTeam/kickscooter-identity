FROM openjdk:9
ADD target/identity-service-0.0.1.jar /identity-service-0.0.1.jar
RUN wget "https://collectors.sumologic.com/rest/download/linux/64" -O SumoCollector.sh && chmod +x SumoCollector.sh
RUN ./SumoCollector.sh -q -Vsumo.accessid=-hidden_access_id- -Vsumo.accesskey=-hidden_access_key- -Vsources=/usr/local/SumoCollector -Vcollector.name=identity
ENTRYPOINT java -jar identity-service-0.0.1.jar
