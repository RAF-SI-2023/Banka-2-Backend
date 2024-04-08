Feature: Activate and Deactivate Employee

  Scenario: Activate Employee
    Given admin logs in
    And employee exists
    When employee is activated
    Then employee's active status is true

  Scenario: Deactivate Employee
    Given admin logs in
    And activated employee exists
    When employee is deactivated
    Then employee's active status is false