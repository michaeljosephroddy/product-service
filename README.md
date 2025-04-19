# Test Suite Evaluation – Product Service

## Purpose of the Service

The **Product Service** was developed primarily as a practical demonstration of software testing techniques within a Spring Boot application. It is not intended for production use but rather to showcase how various types of tests align with the **Testing Pyramid** and best practices in modern software development.

The service exposes basic CRUD operations for managing products, providing a suitable base for testing different layers of a typical Spring Boot application.

---

## Test Strategy

The test suite includes a variety of tests designed to cover the codebase at multiple levels:

- **Unit Tests** using JUnit and Mockito to test service logic outside of the Spring context.
- **Spring Context Tests** using `@SpringBootTest` and `@AutoConfigureMockMvc` to test controller-level functionality with realistic wiring of dependencies.
- **Integration Tests** using TestRestTemplate, **Karate**, and Selenium to simulate full-stack behaviour, API-level testing, and user interactions.

The goal is to cover both correctness and user-facing behaviour, from isolated logic to complete system flows.

---

## Test Breakdown

| Test Level            | Tools Used                             | Purpose                                              |
| --------------------- | -------------------------------------- | ---------------------------------------------------- |
| **Unit Tests**        | JUnit, Mockito                         | Fast and isolated checks of business logic           |
| **Context Tests**     | MockMvc, SpringBootTest                | Ensure controllers behave correctly with real wiring |
| **Integration Tests** | TestRestTemplate, **Karate**, Selenium | API and end-to-end checks of application behaviour   |

This follows the Testing Pyramid structure, with the majority of tests at the unit level and fewer, slower tests at the integration level.

---

## Summary

This project serves as an example of how to implement a **testable Spring Boot service** using modern tools. It demonstrates:

- Structuring tests by purpose and level
- Improving testability through dependency injection
- Ensuring test coverage across logic, web, and UI layers

The test suite balances **speed, stability, and coverage** while clearly illustrating key testing principles.
