Feature: Finding order transaction by order id
  Scenario: User looks up order transaction by order id
    Given there is an order transaction with an order id 789
    When I request order transaction with order id 789
    Then I should receive the order transaction with order id 789