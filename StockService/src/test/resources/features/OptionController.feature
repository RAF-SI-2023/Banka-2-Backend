Feature: OptionController API

  Scenario: Retrieve options by stock listing
    Given a stock listing exists
    When the client requests options by "/stock-listing/TEST"
    Then option should return list by stock listing

