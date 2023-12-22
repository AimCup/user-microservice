FROM maven:3.9.3-eclipse-temurin-17-focal AS build
COPY src /acservice/src
COPY pom.xml /acservice
RUN mvn -f /acservice/pom.xml clean package -P stg

FROM eclipse-temurin:17.0.8_7-jre-focal
COPY --from=build /acservice/target/*.jar app.jar
EXPOSE 8502
ENTRYPOINT ["java","-Dspring.profiles.active=stg","-jar","/app.jar"]
