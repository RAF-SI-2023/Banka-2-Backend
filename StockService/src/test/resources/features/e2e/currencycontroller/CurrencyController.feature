Feature: Testing CurrencyController

  Scenario: User request all currencies
    When user requests all currencies
    Then the response should contain all currencies

  Scenario: User requests currency by existing id
    When user requests currency with id "1"
    Then response contains currency with id "1"

  Scenario: User requests currency by non-exiting id
    When user requests currency with non-existing id "-1"
    Then response has status NotFound


  Scenario:  User requests currency by existing code
    When user requests currency with existing code "AUD"
    Then response contains only currency with code "AUD":

  Scenario: User requests currency by non-existing code
    When user requests currency with non-existing code "non-existing"
    Then response has status NotFound by code

  Scenario: User requests inflation by currency id
    When user requests inflation currency with existing id "1"
    Then response has inflation currency with currency id "1"