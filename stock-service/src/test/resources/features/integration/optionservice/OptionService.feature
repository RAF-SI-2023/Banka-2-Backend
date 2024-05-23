Feature: OptionService API

  Scenario: Retrieve options by stock listing
    Given a stock listing exists
    When the client requests options for root "E"
    Then option should return list by stock listing

