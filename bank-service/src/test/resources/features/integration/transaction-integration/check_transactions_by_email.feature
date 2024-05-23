Feature: Check transactions for email

  Scenario: Check transactions for email
    Given I have a valid email address
    When I check my transactions
    Then I should see all transactions for that email address