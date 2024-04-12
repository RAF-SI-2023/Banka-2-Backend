Feature: Testing ExchangeController

  Scenario: User request all exchanges
    When user requests all exchanges
    Then the response should contain all exchanges

  Scenario: User requests exchange by existing id
    When user requests exchange with id "1"
    Then response contains exchange with id "1"

  Scenario: User requests exchange by non-exiting id
    When user requests exchange with non-existing id "-1"
    Then response has status NotFound

  Scenario:  User requests exchange by existing name
    When user requests exchange with existing name "Rivercross Dark"
    Then response contains only exchange with name "Rivercross Dark":

  Scenario: User requests exchange by non-existing name
    When user requests exchange with non-existing name "Rivercross Darkk"
    Then response has status NotFound by name

  Scenario:  User requests exchange by existing miCode
    When user requests exchange with existing miCode "ICXR"
    Then response contains only exchange with miCode "ICXR":

  Scenario: User requests exchange by non-existing name
    When user requests exchange with non-existing miCode "Rivercross Darkk"
    Then response has status NotFound by miCode