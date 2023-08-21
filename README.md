# Social Media App

# Technologies Used:
+ Spring Boot 3.1.2
+ Spring Data JPA
+ Spring Security 6.1.2
+ PostgreSQL Database
+ Lombok
+ JWT Auth
+ Swagger
+ OpenAPI

# Configuration Steps:

### Clone the Application:

Clone the repository using the following command:

```
https://github.com/honeymoneyes/social-media-api
```

### Create a PostgreSQL Database:
```
Create a new PostgreSQL database named "social_media".
```

### Configure PostgreSQL Username and Password:
Open the file server/src/main/resources/application.yml and update the following properties with your PostgreSQL installation details:

```
    url: YOUR_DATABASE_URL
    username: YOUR_DATABASE_USERNAME
    password: YOUR_DATABASE_PASSWORD
```

### Run the Application Using Maven:
Open a terminal, navigate to the project root directory (social-media-api), and run the following command:

```
mvn spring-boot:run
```

The application will start running at http://localhost:8080.
The Swagger will start running at http://localhost:8080/swagger-ui/index.html
