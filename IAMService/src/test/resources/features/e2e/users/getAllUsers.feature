Feature: Get All users

    Scenario: Get all users and confirm everyone is present
        Given admin logs in
        When listing all users
        Then confirm everyone is present

    Scenario: Get all users and confirm user is present
        Given admin logs in
        And user "dummyUser@gmail.com" exists
        When listing all users
        Then confirm user "dummyUser@gmail.com" is present

