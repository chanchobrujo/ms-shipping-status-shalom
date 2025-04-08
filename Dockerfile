FROM openjdk:17

VOLUME /tmp
EXPOSE 8000
ARG FILE_NAME=target/ms-shipping-status-0.8.jar
ADD ${FILE_NAME} app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]