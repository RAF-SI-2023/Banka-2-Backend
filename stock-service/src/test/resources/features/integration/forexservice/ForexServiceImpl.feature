Feature: Testing ForexServiceImpl

  Scenario: Fetching all forex pairs
    When fetching all forex pairs
    Then returns list of forex pairs

  Scenario: Fetching forex pair by existing id
    When Fetching forex pair with id "29352"
    Then Returns forex pair with id "29352"

  Scenario: Fetching forex pair by non-exiting id
    Then Fetching forex pair with non-existing id "0" throws NotFound exception

  Scenario: Fetching forex pairs with base-currency
    When fetching forex pairs with base-currency "AUD"
    Then returned list contains forex pairs with base-currency "AUD"

  Scenario: Fetching forex pairs with non-existing base-currency
    When fetching forex pairs with non-existing base-currency "NONEXISTING"
    Then returned list is empty

  Scenario: Fetching forex pairs with quote-currency
    When fetching forex pairs with quote-currency "AUD"
    Then returned list contains forex pairs with quote-currency "AUD"

  Scenario: Fetching forex pairs with non-existing quote-currency
    When fetching forex pairs with non-existing quote-currency "NONEXISTING"
    Then returned list is empty