FROM maven:3.8.1-jdk-11 AS build

WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy the source code and build the WAR file
COPY src ./src
RUN mvn package -DskipTests

# Use the official Tomcat 10 image from the Docker Hub
FROM tomcat:10.0

# Copy the WAR file to the webapps directory of Tomcat
COPY --from=build /app/target/ServletExample-1.0-SNAPSHOT.war /usr/local/tomcat/webapps/ROOT.war

# Expose the port Tomcat runs on
EXPOSE 8080

# Run Tomcat
CMD ["catalina.sh", "run"]
