[![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-22041afd0340ce965d47ae6ef1cefeee28c7c493a6346c4f15d667ab976d596c.svg)](https://classroom.github.com/a/N8jiZS9W)
# DMIT2015 Assignment Discussion

## Youfang Yao and Hansenyao

Write your assignment discussion here with the status of your assignment, time to complete, any issues encountered, any key learning points, and any feedback on improving the assignment.

## Assignment Status

- 2025-10-15: Core (Single-Entity) part was finished, 2 days had been spent on it.

- 2025-10-17: Relation Extension (Two Entities) part was finished, 1 day had been spent on it

## Assignment Introduction

- Domain: 

  - Child Entity: Bike, it's properties include Brand, Color, Size, Model, Manufacture City, and Manufacture Date.

  - Parent Entity: Brand, has properties include Name.

- Database: PostgreSQL

- Operations: CRUD

- Testing Operations: Integration Testing (for service APIs) and Selenium Testing (for web page operations)

## How to run

- Start PostgreSQL container

```
    $ podman start dmit2015-postgis
```

- Initialize database

  Run JakartaPersistenceDatabaseSchemaGenerator in file ./src/main/java/dmit2015/tools/JakartaPersistenceDatabaseSchemaGenerator.java

- Create seed data in database
  
  Run JakartaPersistenceDataGenerator in file ./src/main/java/dmit2015/tools/JakartaPersistenceDataGenerator.java

- Run Integration Testing for service APIs

  Run JakartaPersistenceBikeServiceImplementationArquillianIT in file ./src/test/java/dmit2015/service/JakartaPersistenceBikeServiceImplementationArquillianIT.java

  All tests would pass.

- Run Selenium Testing for web page operations

  - Run application on Wildfly server

  ```
    $ mvn clean package wildfly:dev
  ```
      
  - Run Testing

    Run BikeCrudPageSeleniumIT in file ./src/test/java/dmit2015/faces/BikeCrudPageSeleniumIT.java

    All tests would pass.

## What I learned

 - How to use JBoss/WildFly to deploy and run Jakarta EE web application.

 - How to use Jakarta Persistence to operate database, such as PostgreSQL.

 - How to implement entity and persistence service with CRUD.

 - How to use Arquillian to implement test cases for integration testing.

 - How to use Selenium and WebDriver to implement test case for web pages testing.