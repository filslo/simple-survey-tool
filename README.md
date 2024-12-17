# Simple Survey Tool

Rate questions of an anonymous survey to give my opinion about 
teamwork.
Acceptance criteria
• Survey is displayed at the URL “/surveys/[id]”
• The user can rate 5 questions
• Survey results are stored in database
• The survey results are accessible at the URL “/api/surveys/[id]/results” in JSON format

## Architecture
- Spring Boot 3(.4) - Java 21
- REST API
- Front-End: Vue3
- Database: H2


## build project

### Backend

#### Pre-requisites

Java 21 JDK

#### Command

`mvnw clean install`

### Frontend


#### Pre-requisites

NodeJS: 20

#### Command

In the frontend folder, run:
`npm install`

## Run project

### Backend

Execute SimpleSurveyToolApplication java class

The application will be available at http://localhost:8080/api

The database is an H2 in-memory database, so no need to configure anything.
The database is created at runtime and is destroyed when the application is stopped.
The database console is available at http://localhost:8080/h2-console

Swagger is available at http://localhost:8080/swagger-ui.html

### Frontend

In the frontend folder, run:
`npm run dev`
