FROM ubuntu:latest
LABEL authors="Alejandro-Durango"

COPY target/ApiBookKinder-1.0.0.jar /usr/src/ApiBookKinder-1.0.0.jar
WORKDIR /usr/src
EXPOSE 8082
CMD ["java", "-jar", "ApiBookKinder-1.0.0.jar"]
