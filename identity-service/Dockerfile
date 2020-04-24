FROM openjdk:9
ENV TZ=Europe/Kiev
COPY target/identity-service-0.0.1.jar /identity-service-0.0.1.jar
COPY sumo_credentials.txt /home/sumo_credentials.txt
COPY sumo-sources.json /home/sumo-sources.json
COPY run.sh run.sh
RUN ["chmod", "+x", "run.sh"]
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezon
RUN wget "https://collectors.sumologic.com/rest/download/linux/64" -O SumoCollector.sh && chmod +x SumoCollector.sh
ENTRYPOINT ["./run.sh"]
