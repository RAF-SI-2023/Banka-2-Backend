Feature: List all order transactions by account number
  Scenario: List all order transactions by account number
    Given the account with number "1122334455667788" has one order transaction
    When the user requests to list all order transactions by account number "1122334455667788"
    Then the response should contain one order transaction