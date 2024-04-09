Feature: OptionController API

  Scenario: Retrieve options by stock listing
    When the client wants to requests options for root "E"
    Then option should return value list by stock listing