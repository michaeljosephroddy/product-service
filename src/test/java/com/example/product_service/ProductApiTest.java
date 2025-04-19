package com.example.product_service;

import com.intuit.karate.junit5.Karate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

/**
 * Integration test for the Product API using Karate.
 * This class runs the Karate feature file to test the API endpoints.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProductApiTest {

    @LocalServerPort
    private int port;

    /**
     * Runs the Karate feature file for the Product API.
     * Sets the server port dynamically for the test environment.
     *
     * @return the result of the Karate test execution.
     */
    @Karate.Test
    Karate testProductApi() {
        System.setProperty("karate.port", String.valueOf(port));
        return Karate.run("classpath:karate/product-api.feature");
    }
}