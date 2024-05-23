Feature: update user
    Scenario: Update existing user
        Given User with email "activeEmplyee@gmail.com" exists
        When updating user's Phone to "+38111776990"
        Then user's phone in database is "+38111776990"
