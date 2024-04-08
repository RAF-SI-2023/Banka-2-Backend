Feature: Delete user by Email
  Scenario: Delete existing user
    Given User with email "testMail@gmail.com"
    When deleting user with email "testMail@gmail.com"
    Then user with email "testMail@gmail.com" is deleted from database

    Scenario: Delete non existing user OR input wrong email
      Given bad email
      Then deleting user with that email will fail