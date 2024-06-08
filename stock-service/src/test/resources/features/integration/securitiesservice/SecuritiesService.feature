Feature: SecuritiesService API

  Scenario: Retrieve securities by settlement date
    Given a settlement date exists
    When the client requests securities for the given date
    Then the service should return a list of securities