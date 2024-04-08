Feature: Get All users

  Scenario: Get all users and confirm admin is present
    Given admin logs in
    When listing all users
    Then confirm admin is present