package com.example.product_service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.product_service.controller.ProductController;
import com.example.product_service.model.Product;
import com.example.product_service.repository.ProductRepository;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the {@link ProductController} class.
 * This class uses Mockito to mock dependencies and test the behavior of the
 * controller methods.
 */
@ExtendWith(MockitoExtension.class)
public class ProductControllerUnitTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductController productController;

    private Product testProduct;

    /**
     * Sets up the test environment by initializing a test product.
     * This method is executed before each test.
     */
    @BeforeEach
    void setUp() {
        testProduct = new Product();
        testProduct.setId(1L);
        testProduct.setName("Test Product");
        testProduct.setPrice(19.99);
    }

    /**
     * Tests the {@link ProductController#all()} method.
     * Verifies that the method retrieves all products from the repository.
     */
    @Test
    void testGetAllProducts() {
        // Arrange
        List<Product> products = Arrays.asList(testProduct);
        when(productRepository.findAll()).thenReturn(products);

        // Act
        List<Product> result = productController.all();

        // Assert
        assertEquals(1, result.size());
        assertEquals("Test Product", result.get(0).getName());
        verify(productRepository, times(1)).findAll();
    }

    /**
     * Tests the {@link ProductController#create(Product)} method.
     * Verifies that the method saves a new product to the repository.
     */
    @Test
    void testCreateProduct() {
        // Arrange
        when(productRepository.save(any(Product.class))).thenReturn(testProduct);

        // Act
        Product newProduct = new Product();
        newProduct.setName("New Product");
        newProduct.setPrice(29.99);
        Product result = productController.create(newProduct);

        // Assert
        assertEquals("Test Product", result.getName());
        verify(productRepository, times(1)).save(any(Product.class));
    }

    /**
     * Tests the {@link ProductController#update(Long, Product)} method.
     * Verifies that the method updates an existing product in the repository.
     */
    @Test
    void testUpdateProduct() {
        // Arrange
        when(productRepository.save(any(Product.class))).thenReturn(testProduct);

        // Act
        Product updatedProduct = new Product();
        updatedProduct.setName("Updated Product");
        updatedProduct.setPrice(39.99);
        Product result = productController.update(1L, updatedProduct);

        // Assert
        assertEquals(1L, updatedProduct.getId());
        assertEquals("Test Product", result.getName());
        verify(productRepository, times(1)).save(any(Product.class));
    }

    /**
     * Tests the {@link ProductController#delete(Long)} method.
     * Verifies that the method deletes a product from the repository by its ID.
     */
    @Test
    void testDeleteProduct() {
        // Act
        productController.delete(1L);

        // Assert
        verify(productRepository, times(1)).deleteById(1L);
    }
}