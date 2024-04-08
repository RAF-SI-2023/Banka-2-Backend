Feature: Testing StockServiceImpl

  Scenario:  Fetching all stocks
    When fetching all stocks
    Then return list of stocks

  Scenario: Fetching stock by existing id
    When fetching stock with id "1"
    Then return stock with id "1"

  Scenario: Fetching stock by non-exiting id
    Then fetching stock with  non-existing id "0" throws NotFoundException


  Scenario:  Fetching stocks by existing symbol
    When fetching stocks with existing symbol "GOOG"
    Then returned list contains only stocks with symbol "GOOG":

  Scenario: Fetching stocks by non-existing symbol
    When fetching stocks with non-existing symbol "NNESYMBOL"
    Then returned list does not contain any stock