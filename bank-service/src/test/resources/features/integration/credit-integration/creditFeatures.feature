Feature: User interacting with credits

  Scenario: User can view credits
    Given user has an account with account number "0004444999999999"
    When user checks his credits/loans
    Then he should see his own credits

#  Scenario: User can create a credit request
#    Given user has an account with account number "0004444999999999"
#    When user creates a credit request on his account number "0004444999999999"
#    Then he should see a confirmation message

  Scenario: Employee can view credit requests
    When employee checks credit requests
    Then he should see all credit requests