#
# Build stage
#
FROM maven:3.8.3-openjdk-17 AS build

COPY . .

EXPOSE 8080

ENTRYPOINT ["./mvnw","spring-boot:run"]