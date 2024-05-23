Feature: Fetch transfer transactions

  Scenario: Fetch sent transfer transactions
    Given an account with number "9345449924599999" has made one transfer transaction
    When I retrieve the transfer transactions for account
    Then I should receive a list containing 1 transaction
