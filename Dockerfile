FROM gradle:8.14.3-jdk17 AS build

WORKDIR /home/gradle/project

COPY . .

RUN gradle clean bootJar --no-daemon


FROM eclipse-temurin:17-jre-ubi9-minimal

WORKDIR /app

COPY --from=build /home/gradle/project/build/libs/semillero-cicd-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 10000

ENTRYPOINT ["java", "-jar", "app.jar"]