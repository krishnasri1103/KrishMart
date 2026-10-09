# Multi-stage build: compile with JDK 17, run on the required Tomcat 9 / javax.servlet stack.
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /build
COPY pom.xml .
COPY src ./src
RUN mvn -B clean package -DskipTests

FROM tomcat:9.0-jdk17-temurin
ENV KRISHMART_DB_URL=jdbc:h2:/data/krishmart;AUTO_SERVER=TRUE
ENV KRISHMART_DB_USERNAME=sa
ENV KRISHMART_DB_PASSWORD=
RUN rm -rf /usr/local/tomcat/webapps/*
RUN mkdir -p /data
COPY --from=build /build/target/krishmart.war /usr/local/tomcat/webapps/ROOT.war
VOLUME ["/data"]
EXPOSE 8080
CMD ["catalina.sh", "run"]
