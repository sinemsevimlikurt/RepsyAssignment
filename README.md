# RepsyAssignment

## Overview

RepsyAssignment is a Java-based project designed to demonstrate key backend development skills, including RESTful API design, configuration management, and containerization. The project is structured for clarity and maintainability, making it suitable for both learning and production use.

## Features

- RESTful API endpoints
- Configuration management with `application.properties`
- Docker and Docker Compose support
- Maven-based dependency management
- Environment variable support for flexible deployments

## Technologies Used

- Java 17
- Spring Boot 3.x
- Spring Data JPA
- PostgreSQL
- MinIO (optional for object storage)
- Maven
- Docker
- Docker Compose

## Getting Started

### Prerequisites

- Java 17 or higher
- Maven 3.6+
- Docker (optional, for containerization)

### Clone the Repository

```bash
git clone https://github.com/sinemsevimlikurt/RepsyAssignment.git
cd RepsyAssignment
```

### Build the Project

```bash
mvn clean package
```

## Configuration

Application configuration is managed via the `src/main/resources/application.properties` file. You can override properties using environment variables or command-line arguments.

## Usage

### Running Locally

```bash
mvn spring-boot:run
```

The application will start on the default port (typically 8081). You can access the API endpoints via `http://localhost:8081`.

### Running with Docker

To build and run the application using Docker:

```bash
docker build -t repsyassignment .
docker run -p 8081:8081 repsyassignment
```

Or use Docker Compose:

```bash
docker-compose up --build
```

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.