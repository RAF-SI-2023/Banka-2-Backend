Feature: Delete User by Email

  Scenario: Delete User in database
    Given admin logs in
    And new user "testUser@gmail.com" is added
    When user "testUser@gmail.com" is deleted
    Then user "testUser@gmail.com" is not in database