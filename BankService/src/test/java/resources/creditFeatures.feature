Feature: User interacting with credits

  Scenario: User can view credits
    Given user has an account with account number "3334444111111111"
    When user checks his credits/loans
    Then he should see his own credits

  Scenario: User can create a credit request
    Given user has an account with account number "3334444111111111"
    When user creates a credit request on his account number "3334444111111111"
    Then he should see a confirmation message

  Scenario: Employee can view credit requests
    When employee checks credit requests
    Then he should see all credit requests

  Scenario: Employee can approve credit requests
    Given employee sees a credit request
    When employee approves the credit request
    Then the credit request should be approved and credit created