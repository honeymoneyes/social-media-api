# Social Media App

+ JWT Registration and Authentication:
Register with ease by creating a personalized profile. Authenticate securely with JSON Web Token (JWT), ensuring strong data security and login.

+ Social Connections:
Build your network of contacts by subscribing to other users and adding them as friends. Track the activity of your friends and coworkers.

+ Personal Interactions:
Exchange private messages with your friends. Read and correspond, staying in touch at your convenience.

+ Create and Manage Posts:
Express your thoughts and ideas by creating posts on your page. Edit and delete them at any time. View photos in your posts to visually enrich your content.

+ News Feed:
Get the latest updates from users you are subscribed to in your personalized feed. Never miss out on interesting events and news from your network of contacts.

The Swagger will start running at http://localhost:8080/swagger-ui/index.html

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
<img src="https://github.com/honeymoneyes/social-media-api/blob/master/src/main/resources/static/1.jpg"/>
<img src="https://github.com/honeymoneyes/social-media-api/blob/master/src/main/resources/static/2.jpg"/>
