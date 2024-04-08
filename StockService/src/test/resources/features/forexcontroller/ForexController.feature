Feature: Testing ForexController

  Scenario: User requests all forex pairs
    When user requests all forex pairs
    Then the response should contain all forex pairs

  Scenario: User requests forex pair by existing id
    When user requests forex pair with id "29352"
    Then response contains forex pair with id "29352"

  Scenario: User requests forex pair by non-exiting id
    When user requests forex pair with non-existing id "0"
    Then response is status NotFound

   Scenario: User requests forex pairs with base-currency
     When user request forex pairs with base-currency "AUD"
     Then response contains forex pairs with base-currency "AUD"

  Scenario: User requests forex pairs with non-existing base-currency
    When user request forex pairs with non-existing base-currency "NONEXISTING"
    Then response is empty and has status ok

  Scenario: User requests forex pairs with quote-currency
    When user request forex pairs with quote-currency "AUD"
    Then response contains forex pairs with quote-currency "AUD"

  Scenario: User requests forex pairs with non-existing quote-currency
    When user request forex pairs with non-existing quote-currency "NONEXISTING"
    Then response is empty and has status ok