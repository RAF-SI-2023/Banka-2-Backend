Feature: Testing ExchangeServiceImpl

  Scenario:  Fetching all exchanges
    When fetching all exchanges
    Then return list of exchanges

  Scenario: Fetching exchange by existing id
    When fetching exchange with id "1"
    Then return exchange with id "1"

  Scenario: Fetching exchange by non-exiting id
    Then fetching exchange with  non-existing id "-1" throws NotFoundException

  Scenario:  Fetching exchange by exchange name
    When fetching exchange with existing name "Rivercross Dark"
    Then returned exchange with name "Rivercross Dark"

  Scenario: Fetching exchange by non-existing name
    Then fetching exchange with non-existing name "Rivercross Darkk"

  Scenario:  Fetching exchange by MICode
    When fetching exchange with existing MICode "ICXR"
    Then returned exchange with MICode "ICXR"

  Scenario: Fetching exchange by non-existing MICode
    Then fetching exchange with non-existing MICode "Rivercross Darkk"
