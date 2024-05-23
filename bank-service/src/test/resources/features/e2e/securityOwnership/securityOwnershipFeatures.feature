Feature: SecuritiesOwnershipsController Scenario

  Scenario: get all securities ownerships for account number
    Given employee is logged in
    When I visit the securities ownerships page
    Then It should return a success response

  Scenario: get all securities ownerships by security
    Given employee is logged in
    When I visit the securities ownerships page with security
    Then It should return a success response

  Scenario: get all aviable securities ownerships
    Given employee is logged in
    When I visit the securities ownerships page to find all aviable
    Then It should return a success response

  Scenario: get all publicily available securities ownerships from companies
    Given employee is logged in
    When I visit the securities ownerships page to get all publicily available from companies
    Then It should return a success response

  Scenario: get all publicily available securities ownerships from companies
    Given employee is logged in
    When I visit the securities ownerships page to get all publicily available from private
    Then It should return a success response

  Scenario: get all publicily available securities ownerships from companies
    Given employee is logged in
    When I visit the securities ownerships page to get all publicily available from private
    Then It should return a success response

  Scenario: update publicily available quantity
    Given employee is logged in
    When update publicily available quantity
    Then It should return a success response