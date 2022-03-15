FROM  openjdk:8-jdk-alpine
ARG JAR_FILE=./view-metrics/build/libs/view-metrics-*.jar
COPY ${JAR_FILE} /app/app.jar
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
LABEL org.opencontainers.image.description 'This image is used for view-metrics as part of https://github.com/sonatype-nexus-community/nexusiq-successmetrics'
