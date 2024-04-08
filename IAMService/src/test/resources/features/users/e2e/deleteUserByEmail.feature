Feature: Delete User by Email

  Scenario: Delete User in database
    Given admin logs in
    And employee exists
    When employee is deleted
    Then employee is not in database