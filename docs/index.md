# FoodWeek - Ease your daily cooking with our meal planer - Documentation
![foodweek logo](images/logo.jpeg)
[![Build](https://github.com/mobileappdevhm20/team-project-team_5/workflows/Build/badge.svg)](https://github.com/mobileappdevhm20/team-project-team_5/actions)
[![codecov](https://codecov.io/gh/mobileappdevhm20/team-project-team_5/branch/develop/graph/badge.svg)](https://codecov.io/gh/mobileappdevhm20/team-project-team_5)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=mobileappdevhm20_team-project-team_5&metric=alert_status)](https://sonarcloud.io/dashboard?id=mobileappdevhm20_team-project-team_5)\
Thank you for considering to contribute to FoodWeek! This page aims to ease the work of any willing contributor by explaining the technical background of the project.
FoodWeek is an Android app which uses Kotlin as the programming language. It also makes use of a Spring-Boot backend done in Kotlin. The app follows the [Guide to app architecture](https://developer.android.com/jetpack/guide) to ensure that FoodWeek is a robust and production-quality app!

---

## Table of content
- [Prerequisites](#prerequisites)
- [Installation guide](#installation-guide)
- [Dependencies](#dependencies)
- [Structure](#structure)
- [Architecture explanation](#architecture)

---

## Prerequisites
You need to install/configure the following prerequisites in order to work on FoodWeek:
- **IntelliJ IDEA**: [intellij jetbrains download](https://www.jetbrains.com/de-de/idea/download/#section=windows)
- **git**: [git installation guide](https://git-scm.com/book/en/v2/Getting-Started-Installing-Git)
- **android**: [android studio installation guide](https://developer.android.com/studio/install)

### Nice-to-have
- **postman**: [api development environment](https://www.postman.com/downloads/)

---

## Installation Guide

1. Clone the FoodWeek by running <br>
  ```sh
    git clone https://github.com/mobileappdevhm20/team-project-team_5.git
  ```

### App

2. Sync FoodWeeks dependencies by clicking the "elephant"-icon in the top right in Android Studio or run the gradle command<br>
  ```sh
    cd app
    gradlew build
  ```
3. Run FoodWeek on your own device or in an emulator of your choice!


### Backend

2. Build the Spring-Boot backend with following command
```sh
cd server
mvn clean install
```

3. Afterwards the jar file `foodweek-0.0.1-SNAPSHOT.jar` should be available in the subfolder `./target`. You can now either run it with the command 
```sh
java -jar ./target/foodweek-0.0.1-SNAPSHOT.jar
```
or just deploy the jar file to [AWS-Elastic-Beanstalk](https://aws.amazon.com/de/elasticbeanstalk/)

---

## Dependencies

### App
- [Material Design](https://material.io/develop/android/docs/getting-started/) - Used for building high quality user interfaces
- [Android Navigation](https://developer.android.com/jetpack/androidx/releases/navigation) - Used to ease navigation between different fragments
- [Retrofit2](https://square.github.io/retrofit/) - Used for communicating with REST-API backend
- [Room](https://developer.android.com/topic/libraries/architecture/room) - Used to locally store mealplans and recipes
- [Gson](https://github.com/google/gson) - Used to deserialize entities
- [Recyclerview](https://developer.android.com/jetpack/androidx/releases/recyclerview) - Used in most of our fragments for displaying information
- [Koin Dependency Injection](https://start.insert-koin.io/#/setup/index) - Used for reducing boilerplate code and to ease unit-testing
- [Glide](https://github.com/bumptech/glide) - Used to load images for our mealplans and recipes
- [Timeline View](https://github.com/vipulasri/Timeline-View) - Used to create timeline-styled view when displaying mealplans
- [Android Paging](https://developer.android.com/topic/libraries/architecture/paging) - Used to ensure that only a small chunk of mealplans are requested and displayed at a time
- [RxJava](https://github.com/ReactiveX/RxJava) - Used to ease the handling of asynchronous request and livedata

### Backend

- [Spring Boot](https://spring.io/projects/spring-boot)

### Test-Dependencies

- [Espresso UI-Test](https://developer.android.com/training/testing/espresso/setup) - Used for end-to-end testing
- [JUnit](https://developer.android.com/training/testing/unit-testing/local-unit-tests) - Used for unit-testing
- [Koin-Test](https://start.insert-koin.io/#/setup/index) - Used for testing koin components
- [Mockk](https://mockk.io/#installation) - Used for mocking in unit tests

---

## Structure

### App

Make sure to read through [Guide to app architecture](https://developer.android.com/jetpack/guide) to understand the structure and building blocks of our app.

![FoodWeek - Architecture](https://developer.android.com/topic/libraries/architecture/images/final-architecture.png)


- *codecov.yml* - single point of configuration for codecov
- *server/* -Contains the spring-boot backend
- *app/*
  - *build.gradle* - Contains gradle root-project dependencies
  - *app/*
    - *build.gradle* - Contains gradle module dependencies
    - *src/*
      - *androidTest/* - Contains all instrumentation tests
      - *test/* - Contains all unit tests
      - *main/*
        - *main/res/* - Contains all resources (fragment-layouts, app-icon, etc.)
        - *main/java/edu/hm/foodweek/*
          - *inject/* - Contains the Koin related module file
          - *plans/* - Contains entities, repositories, viewmodels etc. related to viewing, browsing and managing a plan
          - *recipes/* - Contains entities, repositories, viewmodels etc. related to viewing, browsing and managing a recipe
          - *shopping/* - Contains entities, repositories, viewmodels etc. related to viewing, browsing and managing the shopping list
          - *users/* - Contains entities, repositories, viewmodels etc. related to user management
          - *util/* - Contains several utility files including the FoodWeekClient and FoodWeekService for communicating with our spring-boot backend
          - *week/* - Contains entities, repositories, viewmodels related to MyWeekView
          - *FoodWeekDatabase.kt* - Abstract class file of our local room database
          - *MainActivity.kt* - Start Activity of FoodWeek
          - *MainApplication.kt* - Application file used to add start of Koin

### Backend

The backend follows the core Spring-Boot design guide. Only the service layer is skipped by now for development speed, which means that all business logic is implemented into the existing controllers. If you want to implement more logic into the backend, please consider separating out the business logic into own Spring-Boot services! 

In order to keep the documentation of the backend as simple as possible, Swagger-UI is used to keep a generated documentation for all REST-Endpoints exposed by the backend. Just go ahead and checkout the latest version of the deployed Swagger-UI [here](http://foodweek-env.eba-qy49mda5.eu-central-1.elasticbeanstalk.com/swagger-ui/index.html?configUrl=/v3/api-docs/swagger-config)

---
## Architecture

Once again make sure to read and understand [Guide to app architecture](https://developer.android.com/jetpack/guide).

---
### User management

Currently the app uses the android device id to identify the user.

### My week

Displays the mealplan that is assigned to the current week. User can open the meal plan details or navigate to the specific recipe. 

### Browse plans

All avaiable meal plans are displayed here in an recyclerview. In this view the user is able to search for a plan, subscribe to one or more plans and also navigate to a more detailed view of that plan. The recyclerview is filled by requesting all available mealplans from our spring-boot backend.

### Manage plans

View (similar to browse plans) that displays all subscribed and "owned" mealplans. The user can assign, delete and unsubscribe from them. Additionally from Manage Plans the creation of a meal plan can be initiated.

### Create meal plan

When creating a meal plan the user can select (and search) different recipes and add them to the plan. While choosing from the current recipes in our backend database the user can select the day of the week and timeslot of that day the recipe should be in.

### Navigation

The bottom navigation bar currently contains the My Week View, Plans View and the Shopping List. If necessary when moving from one fragment to the other, Android safe args are used. For example while browsing meal plans the user is able to navigate to a more detailed view of the selected plan.

### Shopping List

The shopping list displays all ingredients from the meaplan that is assigned to the current week. The ingredients are contained inside the recipe.

### Spring-Boot backend and its deployment

The backend is currently deployed as an [AWS Elastic Beanstalk](https://aws.amazon.com/de/elasticbeanstalk/) which is connected to a [PostgreSQL-Database](https://www.postgresql.org/) that is also hosted in AWS.

If you want to host the backend on your own, just create new [profile-configuration](https://www.baeldung.com/spring-profiles) in the `resources` folder and set the credentials for your deployed database. You can use the file `template-application.properties` which is already present in the resources directory to see which configs you have to set.

[This](https://aws.amazon.com/de/blogs/devops/deploying-a-spring-boot-application-on-aws-using-aws-elastic-beanstalk/) is a very useful guide on how to hoste a Spring-Boot backend in AWS Elastic Beanstalk.

## Main-Contributors
- [Daniel-Seifert](https://github.com/Daniel-Seifert)
- [Cavvar](https://github.com/Cavvar)
- [Tosaa](https://github.com/Tosaa)
- [MartinEkman](https://github.com/MartinEkman)

