package com.example.product_service;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import com.example.product_service.repository.ProductRepository;

import org.springframework.beans.factory.annotation.Autowired;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Acceptance tests for the Product application.
 * This class uses Selenium WebDriver to test the end-to-end flow of the Product
 * CRUD operations.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class ProductAcceptanceTest {

    @LocalServerPort
    private int port;

    @Autowired
    private ProductRepository productRepository;

    private WebDriver driver;
    private WebDriverWait wait;

    /**
     * Sets up the WebDriverManager for ChromeDriver before all tests.
     */
    @BeforeAll
    public static void setupClass() {
        WebDriverManager.chromedriver().browserVersion("135.0.7049.84").setup();
    }

    /**
     * Sets up the test environment before each test.
     * This includes cleaning the database and initializing the WebDriver in
     * headless mode.
     */
    @BeforeEach
    public void setupTest() {
        // Clean database
        productRepository.deleteAll();

        // Setup ChromeDriver in headless mode
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless", "--disable-gpu", "--window-size=1920,1200");
        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    /**
     * Tears down the WebDriver after each test.
     * Ensures that the browser instance is closed.
     */
    @AfterEach
    public void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }

    /**
     * Tests the end-to-end CRUD flow for the Product application.
     * This includes creating, reading, updating, and deleting a product.
     */
    @Test
    public void testProductCrudFlow() {
        // Navigate to the product page
        driver.get("http://localhost:" + port);

        // Wait for page to load
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("productForm")));

        // Check initial state - no products
        List<WebElement> initialRows = driver.findElements(By.cssSelector("tbody#productTable tr"));
        assertTrue(initialRows.isEmpty());

        // Create a new product
        driver.findElement(By.id("name")).sendKeys("Acceptance Test Product");
        driver.findElement(By.id("price")).sendKeys("69.99");
        driver.findElement(By.id("productForm")).submit();

        // Wait for the product to appear in the table
        wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(
                By.cssSelector("tbody#productTable tr"), 0));

        // Verify product was added
        List<WebElement> rows = driver.findElements(By.cssSelector("tbody#productTable tr"));
        WebElement tableRow = rows.get(0);
        assertTrue(tableRow.getText().contains("Acceptance Test Product"));
        assertTrue(tableRow.getText().contains("€69.99"));

        // Edit the product
        tableRow.findElement(By.xpath(".//button[text()='Edit']")).click();

        // Wait for form to be populated with product data
        wait.until(ExpectedConditions.attributeToBeNotEmpty(
                driver.findElement(By.id("name")), "value"));

        // Clear and update the form
        WebElement nameInput = driver.findElement(By.id("name"));
        nameInput.clear();
        nameInput.sendKeys("Updated Acceptance Product");

        WebElement priceInput = driver.findElement(By.id("price"));
        priceInput.clear();
        priceInput.sendKeys("79.99");

        driver.findElement(By.id("productForm")).submit();

        // Wait for the updated product to appear
        wait.until(ExpectedConditions.textToBePresentInElementLocated(
                By.cssSelector("tbody#productTable tr"), "Updated Acceptance Product"));

        // Verify product was updated
        tableRow = driver.findElement(By.cssSelector("tbody#productTable tr"));
        assertTrue(tableRow.getText().contains("Updated Acceptance Product"));
        assertTrue(tableRow.getText().contains("€79.99"));

        // Delete the product
        tableRow.findElement(By.xpath(".//button[text()='Delete']")).click();

        // Wait for product to be removed
        wait.until(ExpectedConditions.invisibilityOfElementLocated(
                By.cssSelector("tbody#productTable tr")));

        // Verify product was deleted
        assertTrue(driver.findElements(By.cssSelector("tbody#productTable tr")).isEmpty());
    }
}
