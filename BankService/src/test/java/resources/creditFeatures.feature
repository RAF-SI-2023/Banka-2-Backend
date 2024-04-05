Feature: User interacting with credits

  Scenario: User can view credits
    Given user has an account with account number "3334444111111111"
    When user checks his credits/loans
    Then he should see his own credits