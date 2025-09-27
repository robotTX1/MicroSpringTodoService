# 1st stage, build the app
FROM maven:3.9.9-eclipse-temurin-21 AS builder

WORKDIR /builder

# Create a first layer to cache the "Maven World" in the local repository.
# Incremental docker builds will always resume after that, unless you update
# the pom
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Do the Maven build!
# Incremental docker builds will resume here when you change sources
COPY build_config build_config
COPY src src
RUN mvn package -DskipTests

# This points to the built jar file in the target folder
ARG JAR_FILE=target/*.jar

# Copy the jar file to the working directory and rename it to application.jar
RUN cp ${JAR_FILE} application.jar

# Extract the jar file using an efficient layout
RUN java -Djarmode=tools -jar application.jar extract --layers --destination extracted

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Create user
RUN addgroup -S appgroup && adduser -S appuser -G appgroup
USER appuser

# Copy the binary built in the 1st stage
COPY --from=builder /builder/extracted/dependencies/ ./
COPY --from=builder /builder/extracted/spring-boot-loader/ ./
COPY --from=builder /builder/extracted/snapshot-dependencies/ ./
COPY --from=builder /builder/extracted/application/ ./

EXPOSE 8080 9000

ENTRYPOINT ["java", "-jar", "application.jar"]
