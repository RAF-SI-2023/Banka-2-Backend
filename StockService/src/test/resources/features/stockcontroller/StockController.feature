Feature: Testing StockController

  Scenario: User fetches all stocks
    When user requests all stocks
    Then the response should contain all stocks

  Scenario: Fetching stock by existing id
    When user requests stock with id "1"
    Then response contains stock with id "1"

  Scenario: Fetching stock by non-exiting id
    When user requests stock with non-existing id "0"
    Then response is empty with status ok

  Scenario: Fetching stock by non-existing symbol
    When user requests stock with non-existing symbol "NNESYMBOL"
    Then response is empty with status ok