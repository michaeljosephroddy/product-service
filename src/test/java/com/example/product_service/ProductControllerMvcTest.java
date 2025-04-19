package com.example.product_service;

import com.example.product_service.controller.ProductController;
import com.example.product_service.model.Product;
import com.example.product_service.repository.ProductRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link ProductController} class using Spring MVC
 * Test.
 * This class verifies the behavior of the controller endpoints by mocking the
 * repository layer.
 */
@WebMvcTest(ProductController.class)
public class ProductControllerMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductRepository productRepository;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Tests the {@link ProductController#all()} method when products exist.
     * Verifies that the endpoint returns a list of products.
     *
     * @throws Exception if the request fails.
     */
    @Test
    void testGetAllProducts_WhenProductsExist() throws Exception {
        // Arrange
        Product product = new Product();
        product.setId(1L);
        product.setName("Test Product");
        product.setPrice(19.99);

        when(productRepository.findAll()).thenReturn(Arrays.asList(product));

        // Act & Assert
        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is("Test Product")))
                .andExpect(jsonPath("$[0].price", is(19.99)));

        verify(productRepository, times(1)).findAll();
    }

    /**
     * Tests the {@link ProductController#all()} method when no products exist.
     * Verifies that the endpoint returns an empty list.
     *
     * @throws Exception if the request fails.
     */
    @Test
    void testGetAllProducts_WhenNoProducts() throws Exception {
        // Arrange
        when(productRepository.findAll()).thenReturn(Collections.emptyList());

        // Act & Assert
        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));

        verify(productRepository, times(1)).findAll();
    }

    /**
     * Tests the {@link ProductController#create(Product)} method.
     * Verifies that the endpoint creates a new product and returns it.
     *
     * @throws Exception if the request fails.
     */
    @Test
    void testCreateProduct() throws Exception {
        // Arrange
        Product productToCreate = new Product();
        productToCreate.setName("New Product");
        productToCreate.setPrice(29.99);

        Product savedProduct = new Product();
        savedProduct.setId(1L);
        savedProduct.setName("New Product");
        savedProduct.setPrice(29.99);

        when(productRepository.save(any(Product.class))).thenReturn(savedProduct);

        // Act & Assert
        mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productToCreate)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("New Product")))
                .andExpect(jsonPath("$.price", is(29.99)));

        verify(productRepository, times(1)).save(any(Product.class));
    }

    /**
     * Tests the {@link ProductController#update(Long, Product)} method.
     * Verifies that the endpoint updates an existing product and returns it.
     *
     * @throws Exception if the request fails.
     */
    @Test
    void testUpdateProduct() throws Exception {
        // Arrange
        Product productToUpdate = new Product();
        productToUpdate.setName("Updated Product");
        productToUpdate.setPrice(39.99);

        Product updatedProduct = new Product();
        updatedProduct.setId(1L);
        updatedProduct.setName("Updated Product");
        updatedProduct.setPrice(39.99);

        when(productRepository.save(any(Product.class))).thenReturn(updatedProduct);

        // Act & Assert
        mockMvc.perform(put("/api/products/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productToUpdate)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Updated Product")))
                .andExpect(jsonPath("$.price", is(39.99)));

        verify(productRepository, times(1)).save(any(Product.class));
    }

    /**
     * Tests the {@link ProductController#delete(Long)} method.
     * Verifies that the endpoint deletes a product by its ID.
     *
     * @throws Exception if the request fails.
     */
    @Test
    void testDeleteProduct() throws Exception {
        // Act & Assert
        mockMvc.perform(delete("/api/products/1"))
                .andExpect(status().isOk());

        verify(productRepository, times(1)).deleteById(1L);
    }
}