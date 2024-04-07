Feature: Testing StockServiceImpl

  Scenario:  Fetching all stocks
    When fetching all stocks
    Then return list of stocks

  Scenario: Fetching stock by existing id
    When fetching stock with id "1"
    Then return stock with id "1"

  Scenario: Fetching stock by non-exiting id
    When fetching stock with  non-existing id "0"
    Then returns null

  Scenario: Fetching stock by non-existing symbol
    When fetching stock with non-existing symbol "NNESYMBOL"
    Then returns null