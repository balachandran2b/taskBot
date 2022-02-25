FROM openjdk:11
RUN mkdir -p /opt/taskBotApp
WORKDIR /opt/taskBotApp
COPY ./build/libs/*.jar taskBotApp.jar
EXPOSE 8080
CMD ["java", "-jar", "taskBotApp.jar"]
