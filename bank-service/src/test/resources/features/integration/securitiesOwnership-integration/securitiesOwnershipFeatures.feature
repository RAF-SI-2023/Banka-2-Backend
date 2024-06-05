Feature: User interacting with securityOwnerships

  Scenario: User looks up securities ownerships by account number
    Given user has securities ownerships with account number "3334444999999999"
    When the user requests securities ownerships for account number "3334444999999999"
    Then the user should receive a response with status code 200
    And the response should contain the securities ownerships

  Scenario: User looks up all public securities ownerships
    Given user has securities ownerships with account number "3334444999999999"
    When the user requests public securities ownerships
    Then the user should receive a response with status code 200
    And the response should contain the public securities ownerships

  Scenario: User looks up all public securities ownerships from companies
    Given user has securities ownerships with account number "3334444999999999"
    When the user requests public securities ownerships from companies
    Then the user should receive a response with status code 200
    And the response should contain the public securities ownerships from companies

  Scenario: User looks up all public securities ownerships from privates
    Given user has securities ownerships with account number "3334444999999999"
    When the user requests public securities ownerships from privates
    Then the user should receive a response with status code 200
    And the response should contain the public securities ownerships from privates

  Scenario: User updates the publicly available quantity with a valid input
    Given a securities ownership exists with id 1, quantity 100, and publicly available quantity 50
    When the user updates the publicly available quantity to 5 for the securities ownership with id 1
    Then the response should contain the updated publicly available quantity 5

#  Scenario: User tries to update the publicly available quantity to an invalid value (greater than total quantity)
#    Given a securities ownership exists with id 2, quantity 100, and publicly available quantity 50
#    When the user updates the publicly available quantity to 120 for the securities ownership with id 2
#    Then the response should contain an error message "cannot set amount to more than u have"

  Scenario: User requests securities values for a given account number
    Given user has securities ownerships with account number "1234567890"
    When the user requests securities values for account number "1234567890"
    Then the user should receive a response with status code 200
    And the response should contain the securities values
