Feature: Retrieving exchange rates for a currency

  Scenario: Successfully retrieve exchange rates for a currency
    Given user is logged in as employee;
    When the getAllExchangeRates endpoint is called to get exchange rates for the currency "RSD"
    Then it should return a success response with the exchange rates for the currency "RSD"

  Scenario: Attempt to retrieve exchange rates for an invalid currency
    Given user is logged in as employee;
    When the getAllExchangeRates endpoint is called to get exchange rates for the currency "INVALID"
    Then it should return empty response with the exchange rates for the currency "INVALID"

  Scenario: Attempt to exchange currency
    Given user is logged in as employee;
    Given a valid account number "0932345666666666"
    Then the putExchangeCurrency endpoint is called from account "0932345666666666" to account "7772345666556666" and from "RSD" to "USD" amount of 1000
    Then it should return a success response for account "0932345666666666" to account "7772345666556666" and from "RSD" to "USD" amount of 1000

  Scenario: Attempt to exchange currency but its not same user
    Given user is logged in as employee;
    Given a valid account number "0932345666666666"
    Then it should fail to return a response for account "0932345666666666" to account "0932345111111111" and from "RSD" to "USD" amount of 1000

