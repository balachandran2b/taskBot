FROM openjdk:11
RUN mkdir -p /opt/taskBotApp
WORKDIR /opt/taskBotApp
ENV VERSION='0.0.1-SNAPSHOT'
COPY ./build/libs/taskbot-${VERSION}.jar ./taskBotApp.jar
EXPOSE 8080
CMD ["java", "-jar", "taskBotApp.jar"]
