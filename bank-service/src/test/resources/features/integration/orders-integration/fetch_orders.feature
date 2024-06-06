Feature: Get All Orders

  Scenario: Successfully retrieve all orders
    Given orders exist in the repository
    When the user requests all orders
    Then the user should receive a list of all orders
