# Room reservations

## Basics

The application uses lombok so in order to properly run it from IDE certain plugins & additional settings might be required. For instance, IDEA requires presence of lombok plugin and/or enabling annotation processing.

The system can be started either by running main() method from RoomReservationsApplication class or by executing run goal in spring-boot maven plugin.

Room reservations uses in memory H2 database so no additional steps are required in order to initialize it.

Acceptance tests can either be executed by running maven verify phase or by executing all tests from package pl.sszepiet.acceptance from IDE level. Most of relevant information (incoming requests and outgoing responses) should be logged.

In order to setup the database with initial values the system uses StaticDataLoader class that implements CommandLineRunner interface.

It is obviously possible to play around with the application using external http client such as Postman, beware however, that with each restart of the application new IDs are generated for the entities inserted by StaticDataLoader.

## Technologies used

a) H2 relational database,
b) Java 8,
c) Spring Boot (web & hateoas),
d) Hibernate with Spring Data JPA,
e) maven,
f) TestNG + Awaitility + RestAssured