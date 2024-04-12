Feature: Delete User by Email

    Scenario: Delete User in database
        Given admin logs in
        And new user "testUser33@gmail.com" is added
        When user "testUser33@gmail.com" is deleted
        Then user "testUser33@gmail.com" is not in database