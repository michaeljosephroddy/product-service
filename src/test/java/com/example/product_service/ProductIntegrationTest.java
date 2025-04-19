package com.example.product_service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;

import com.example.product_service.model.Product;
import com.example.product_service.repository.ProductRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for the Product API.
 * This class tests the CRUD operations for the Product entity
 * by interacting with the API endpoints.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProductIntegrationTest {

        @LocalServerPort
        private int port;

        @Autowired
        private TestRestTemplate restTemplate;

        @Autowired
        private ProductRepository productRepository;

        /**
         * Constructs the base URL for the Product API.
         *
         * @return the base URL as a String.
         */
        private String getBaseUrl() {
                return "http://localhost:" + port + "/api/products";
        }

        /**
         * Cleans the database before each test to ensure a consistent state.
         */
        @BeforeEach
        void setUp() {
                productRepository.deleteAll();
        }

        /**
         * Tests the CRUD operations for the Product API.
         * <ul>
         * <li>Verifies the product list is initially empty.</li>
         * <li>Creates a new product and verifies its creation.</li>
         * <li>Retrieves all products and verifies the created product is present.</li>
         * <li>Updates the product and verifies the update.</li>
         * <li>Deletes the product and verifies the product list is empty again.</li>
         * </ul>
         */
        @Test
        void testProductCrudOperations() {
                // 1. Initially, the product list should be empty
                ResponseEntity<List<Product>> initialResponse = restTemplate.exchange(
                                getBaseUrl(),
                                HttpMethod.GET,
                                null,
                                new ParameterizedTypeReference<List<Product>>() {
                                });

                assertEquals(HttpStatus.OK, initialResponse.getStatusCode());
                assertTrue(initialResponse.getBody().isEmpty());

                // 2. Create a product
                Product newProduct = new Product();
                newProduct.setName("Integration Test Product");
                newProduct.setPrice(49.99);

                ResponseEntity<Product> createResponse = restTemplate.postForEntity(
                                getBaseUrl(),
                                newProduct,
                                Product.class);

                assertEquals(HttpStatus.OK, createResponse.getStatusCode());
                assertNotNull(createResponse.getBody().getId());
                assertEquals("Integration Test Product", createResponse.getBody().getName());

                Long createdProductId = createResponse.getBody().getId();

                // 3. Get all products - should now return one product
                ResponseEntity<List<Product>> afterCreateResponse = restTemplate.exchange(
                                getBaseUrl(),
                                HttpMethod.GET,
                                null,
                                new ParameterizedTypeReference<List<Product>>() {
                                });

                assertEquals(HttpStatus.OK, afterCreateResponse.getStatusCode());
                assertEquals(1, afterCreateResponse.getBody().size());

                // 4. Update the product
                Product productToUpdate = new Product();
                productToUpdate.setName("Updated Integration Product");
                productToUpdate.setPrice(59.99);

                HttpEntity<Product> requestEntity = new HttpEntity<>(productToUpdate);

                ResponseEntity<Product> updateResponse = restTemplate.exchange(
                                getBaseUrl() + "/" + createdProductId,
                                HttpMethod.PUT,
                                requestEntity,
                                Product.class);

                assertEquals(HttpStatus.OK, updateResponse.getStatusCode());
                assertEquals("Updated Integration Product", updateResponse.getBody().getName());
                assertEquals(59.99, updateResponse.getBody().getPrice());

                // 5. Delete the product
                restTemplate.delete(getBaseUrl() + "/" + createdProductId);

                // 6. Get all products - should be empty again
                ResponseEntity<List<Product>> afterDeleteResponse = restTemplate.exchange(
                                getBaseUrl(),
                                HttpMethod.GET,
                                null,
                                new ParameterizedTypeReference<List<Product>>() {
                                });

                assertEquals(HttpStatus.OK, afterDeleteResponse.getStatusCode());
                assertTrue(afterDeleteResponse.getBody().isEmpty());
        }
}