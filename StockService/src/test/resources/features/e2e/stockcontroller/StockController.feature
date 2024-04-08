Feature: Testing StockController

  Scenario: User request all stocks
    When user requests all stocks
    Then the response should contain all stocks

  Scenario: User requests stock by existing id
    When user requests stock with id "1"
    Then response contains stock with id "1"

  Scenario: User requests stock by non-exiting id
    When user requests stock with non-existing id "0"
    Then response has status NotFound


  Scenario:  User requests stock by existing symbol
    When user requests stock with existing symbol "GOOG"
    Then response contains only stocks with symbol "GOOG":

  Scenario: User requests stock by non-existing symbol
    When user requests stock with non-existing symbol "NNESYMBOL"
    Then response is empty with status ok