Feature: Get All users
    Scenario: Get ALl users
        Given users exist
        When calling find all users
        Then get list of all users
    Scenario: Get all users & find specific user
        Given User with email "activeEmplyee@gmail.com" exists
        When calling find all users
        Then find user "activeEmplyee@gmail.com"