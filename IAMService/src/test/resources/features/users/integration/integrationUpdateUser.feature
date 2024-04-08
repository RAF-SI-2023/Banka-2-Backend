Feature: update user
  Scenario: Update existing user
    Given user exists
    When updating user's name to "NewName"
    Then user's name in database is "NewName"

  Scenario: Updating user that is not in database
    Given user does not exist
    Then updating user's name will fail
