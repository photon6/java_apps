# Spring Boot Demo Project

This is a basic Spring Boot project with a REST API endpoint.

## Requirements

- Java 17 or higher
- Maven 3.6.3 or higher

## Getting Started

To run the application:

1. Build the project:

```bash
./mvnw clean install
```

2. Run the application:

```bash
./mvnw spring-boot:run
```

The application will start on port 8080. You can access the endpoint at:

- http://localhost:8080/

## Project Structure

- `src/main/java/com/example/demo/DemoApplication.java`: Main application class
- `src/main/java/com/example/demo/controller/HelloController.java`: REST controller
- `src/main/resources/application.properties`: Application configuration
- `pom.xml`: Project dependencies and build configuration
