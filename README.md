# Online Cinema With Security

## Description

Online Cinema With Security is an MVP for an online cinema that offers user authentication, personalized
recommendations, subscriptions, notifications, and AI support. The application allows users to watch high-rated TV
series and receive personalized recommendations based on their preferences.

## Key Features

- **Context Preservation After Authentication**: Users can continue from where they left off after logging in.
- **Email Verification**: Enhanced security through email confirmation with a secret code.
- **High-Rated TV Series**: Providing a list of top-rated series.
- **Personalized Recommendations**: Using AI to suggest content based on user preferences.
- **Subscriptions and Notifications**: Notifications about new releases, updates, and news based on user subscriptions.

## Technologies

- **Spring Boot**: The primary framework for developing the application.
- **Spring Security**: Authentication and authorization.
- **PostgreSQL**: Relational database for data storage.
- **Redis**: Caching and temporary data storage.
- **Docker**: Containerization of services for easy deployment.
- **AI (Artificial Intelligence)**: Using AI for personalized recommendations.

## Installation Guide

### Prerequisites

Ensure you have Docker installed on your system.

### Steps to Install and Run

1. **Clone the Repository**
   ```bash
   git clone <repository-url>
   cd online-cinema

2. **Start the Application**
   Navigate to the project directory containing Docker Compose files:
   ```bash
   cd path/to/your/project

3. **Run Docker Compose**
   Execute the following command to start the application and its dependencies:
   ```bash
   docker-compose up

4. **Access the Application**
   Once Docker containers are running, access the application at http://localhost:8080.

5. **Initialize Database**
   The application will create necessary database tables automatically on startup.
   If needed, you can manually initialize the database using the provided SQL script (script.sql)
   located in the resources directory of the project.

6. Configure Application Properties
   After installation, configure the application properties (application.properties or application.yml)
   to set environment-specific parameters such as database URLs, tokens, etc.

7. **Registration and Verification**

   To register and verify your email, follow these steps using Postman or any API testing tool:

    - **Registration:**
        - **Endpoint:** POST `http://localhost:8080/api/auth/registration`
        - **JSON Body:**
          ```json
          {
            "email": "example@domain.com",
            "password": "password"
          }
          ```

    - **Verification:**
        - **Endpoint:** POST `http://localhost:8080/api/auth/verify`
        - **JSON Body:**
          ```json
          {
            "email": "example@domain.com",
            "code": "code"
          }
          ```

   Make sure to replace `"example@domain.com"` with your email and `"password"` with your desired password for
   registration. The `"code"` in the verification request should be replaced with the actual code received in your
   email.

   Open Postman, configure these requests, and follow the instructions to complete registration and email verification.

8. **Explore API Documentation**

   Explore the API endpoints and their descriptions using Swagger UI:
    - Visit [http://localhost:8080/swagger-ui/index.html#/](http://localhost:8080/swagger-ui/index.html#/)

   Swagger UI provides a detailed overview of available endpoints and their functionality.

