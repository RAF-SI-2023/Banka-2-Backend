Feature: Testing CurrencyServiceImpl
  Scenario:  Fetching all currencies
    When fetching all currencies
    Then return list of currencies

  Scenario: Fetching currency by existing id
    When fetching currency with id "1"
    Then return currency with id "1"

  Scenario: Fetching currency by non-exiting id
    Then fetching currency with  non-existing id "-1" throws NotFoundException

  Scenario:  Fetching currency by currency code
    When fetching currency with existing name "AUD"
    Then returned currency with name "AUD"

  Scenario:  Fetching currency by currency name
    When fetching currency with existing name "Armenian Dram"
    Then returned currency with name "Armenian Dram"

  Scenario: Fetching currency by non-existing name
    Then fetching currency with non-existing name "non-existing-name"


