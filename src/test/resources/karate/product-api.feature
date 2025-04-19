Feature: Product API Tests

  Background:
    * url 'http://localhost:' + karate.properties['karate.port'] + '/api/products'

  Scenario: Get all products
    When method get
    Then status 200

  Scenario: Create a new product
    And request { name: 'Karate Test Product', price: 79.99 }
    And header Content-Type = 'application/json'
    When method post
    Then status 200
    And match response contains { name: 'Karate Test Product', price: 79.99 }
    * def productId = response.id

    # Verify product was created
    When method get
    Then status 200
    And match response contains { id: '#(productId)', name: 'Karate Test Product', price: 79.99 }

  Scenario: Update an existing product
    # First create a product
    And request { name: 'Karate Test Product', price: 79.99 }
    And header Content-Type = 'application/json'
    When method post
    Then status 200
    * def productId = response.id

    # Then update it
    Given path productId
    And request { name: 'Updated Karate Product', price: 89.99 }
    And header Content-Type = 'application/json'
    When method put
    Then status 200
    And match response contains { id: '#(productId)', name: 'Updated Karate Product', price: 89.99 }

  Scenario: Delete a product
    # First create a product
    And request { name: 'Karate Test Product', price: 79.99 }
    And header Content-Type = 'application/json'
    When method post
    Then status 200
    * def productId = response.id

    # Then delete it
    Given path productId
    When method delete
    Then status 200

    # Verify it was deleted
    When method get
    Then status 200
    And match response !contains { id: '#(productId)' }