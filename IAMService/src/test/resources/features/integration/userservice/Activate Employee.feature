Feature: Activate employees
  Scenario: Activate employee
    Given unactivated employee exists
    When unactivated employee is activated
    Then employee isActive status is true

  Scenario: Deactivate employee
    Given an activated employee exists
    When activated employee is deactivated
    Then employee isActive status is false

  Scenario:
    # User is not an employee so activate should fail
    Given user exists
    Then activating user will fail