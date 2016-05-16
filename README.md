# Spring Boot JAX-WS demo

This application demonstrates how to use [Spring Boot](http://projects.spring.io/spring-boot/)
and the [JAX-WS reference implementation](https://jax-ws.java.net/) to
implement a web services application. It uses the
[JAX-WS Spring extension](https://github.com/revinate/jaxws-spring).

## Requirements

* Java 1.7 or newer

## Usage

To start the application, run the following command in the project root directory:

```
$ ./gradlew bootRun
```

The application implements a single service with two ports, located at:

* <http://localhost:8080/service/fibonacci>
* <http://localhost:8080/service/factorial>

The [Application class](src/main/java/me/alexleigh/springbootjaxws/Application.java)
demonstrates how to use Spring JavaConfig to configure web service components.

The [FibonacciPortImpl class](src/main/java/me/alexleigh/springbootjaxws/FibonacciPortImpl.java)
is a service endpoint implementation written as a Spring Bean with dependency
injection.
