# spring-redis-angular

In this project, a spring boot application has been developed and consumed via RestFul services which uses angularJS while handling view part and as a consumer.

# redis configuration
Redis should be downloaded from http://redis.io/ and used by default settings, currently localhost:6379 is used by this project without a password.

# backend server configuration
Simply by running application.java, spring boot starts an application server and nothing more needs to be done. localhost:8080 is default backend server address for this project.

# running frontend on application server
To start an application server for frontend, app.groovy can be called by command line after downloading spring command line tool. Here is an example by spring.io, http://spring.io/guides/gs/consuming-rest-angularjs/. To start that server on another port use the following command.
For example;
spring run app.groovy -- --server.port=8091
or 
spring run app.groovy -- --server.port=anotherPort
