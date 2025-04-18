FROM openjdk:21-jdk-slim
WORKDIR /app
COPY build/libs/demo-0.0.1-SNAPSHOT.jar app.jar
COPY src/main/resources/keystore.p12 keystore.p12
EXPOSE 8443
ENTRYPOINT ["java", "-jar", "app.jar"]