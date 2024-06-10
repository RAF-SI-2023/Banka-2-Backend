Feature: SecuritiesController API

  Scenario: Retrieve securities by settlement date
    Given a settlement date exists
    When the client requests securities for the given date
    Then the controller should return a list of securities