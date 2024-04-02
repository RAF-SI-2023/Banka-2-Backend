Feature: OptionController API


  Scenario: Retrieve all options
   Given a list of options
    When the client requests all options
    Then the server should respond with a list of options

  Scenario: Retrieve options by stock listing
    Given a stock listing exists
    When the client requests options by stock listing
    Then option should return list by stock listing

  Scenario: Retrieve option by ID
    Given a list of options
    When the client requests the option with ID 1
    Then option should retun option with ID 1
