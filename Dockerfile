FROM openjdk:17-oracle

WORKDIR /app

LABEL maintainer="madinaabaildinova" \
      version="1.0.0" \
      description="LMS System Backend Application"

COPY /build/libs/LMS-System-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]