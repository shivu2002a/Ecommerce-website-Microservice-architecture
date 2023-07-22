# Ecommerce-backend-Microservice-architecture
This is an E-commerce backend project built on **Microservice** **architecture** using the following techstack: 
1. **Spring boot** for developing services
2. **Eureka discovery server and client-server dependencies** for microservice environment
3. **Spring Cloud API gateway** for API gateway
4. **Apache Kafka** (Asynchronous communication between services)
5. **Zipkin** for distributed tracing of requests
6. **Resilience4J** for fault tolerance
7. **MongoDB** and **H3 database**
   
## Architecture diagram:
![](https://github.com/shivu2002a/Ecommerce-website-Microservice-architecture/blob/main/Ecom-ms-architecture.jpg)

## Services available: 
1. **Product-Service** - for fetching list and details of products  
2. **Order service** - for placing orders. Also publishes messages on Kafka. 
3. **Inventory service** - for checking if the specified product exists or not
4. **Notification service** - A message will be published in the Kafka topic upon confirmation of an order. This service consumes that message and sends an email to the user mail with order details. 

## Setting up in local:
1. Java 11 environment
2. You need to have Kafka. It can be installed by running the docker-compose in the project root folder by `docker compose up -d`. </br>
   Docker must be running in order to run docker scripts.
3. Mailjet account for api-keys
4. To run the whole application - in the root folder, run `./mvnw install && ./mvnw spring-boot:run -pl application` (quick and easy). </br>
   If you have maven installed, run `mvn build && java -jar target/the-package-name.jar` which is th mostly used way of building spring applications. </br>
   </br>
   Now all the applications must be up. You can check the application ports in respective _application.yml_ file.
   



