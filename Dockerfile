# Base image
FROM eclipse-temurin:21-jre

# Working directory
WORKDIR /app
COPY target/Battleship-1.0.0.jar /app

# Entrypoint
ENTRYPOINT ["java","-jar","Battleship-1.0.0.jar"]

# Command to run
CMD ["--help"]
