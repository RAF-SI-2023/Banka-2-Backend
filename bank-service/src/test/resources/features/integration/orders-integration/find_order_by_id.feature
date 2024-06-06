Feature: Find Order by ID

  Scenario: Successfully find an order by ID
    Given the order exists in db
    When the client requests to find the order
    Then the order details should be returned

  Scenario: Failure find an order by ID
    Given  the order is non existent with ID "-1"
    When the client requests to find the order
    Then the order should throw an exception
# non existing id