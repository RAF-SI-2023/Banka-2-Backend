Feature: Listing all order transactions
  Scenario: User lists all order transactions
    Given there is one order transaction
    When I request all order transactions
    Then I should receive one order transaction