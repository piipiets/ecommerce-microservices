This project consists of six microservices built with a microservices architecture and uses PostgreSQL as the primary database.
<h3>Technologies Used</h3>
- Java / Spring Boot
<br> - Spring Cloud (Eureka, API Gateway)
<br> - PostgreSQL
<br> - Keycloak
<br> - Apache Kafka
<br> - Thymeleaf
<br> - Docker

<h3>1. Discovery Service</h3>
The Discovery Service provides a service registry using Netflix Eureka.
All microservices register themselves with this service to enable dynamic service discovery and communication.

<h3>2. API Gateway Service</h3>
The API Gateway acts as the single entry point for all client requests.
Every request from the user passes through this service, which routes requests to the appropriate downstream microservices.

<h3>3. Auth Service</h3>
The Auth Service integrates with Keycloak for authentication.
It is responsible for communicating with Keycloak to:
<br> - Generate access tokens
<br> - Refresh access tokens

<h3>4. Inventory Service</h3>
The Inventory Service manages product data.
It provides CRUD (Create, Read, Update, Delete) operations for products and maintains product availability information.

<h3>5. Order Service</h3>
The Order Service handles order placement.
Its responsibilities include:
<br> - Processing customer orders
<br> - Communicating with the Inventory Service to update product stock after an order is placed
<br> - Producing order events to Apache Kafka.

<h3>6. Notification Service</h3>
The Notification Service acts as a Kafka consumer. It consumes order events from Kafka and sends order summary notifications to users via email.
Email content is generated using Thymeleaf templates.

