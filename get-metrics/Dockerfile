FROM  openjdk:8-jdk-alpine
ARG JAR_FILE=./get-metrics/build/libs/get-metrics-*.jar
COPY ${JAR_FILE} /app/app.jar
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
LABEL org.opencontainers.image.description 'This image is used for get-metrics as part of https://github.com/sonatype-nexus-community/nexusiq-successmetrics'
