Feature: Update Users

  Scenario: Update Employee Information
    Given admin logs in
    And employee exists
    When employee's phone number is changed to "123456789"
    Then employee's phone number in database is "123456789"

  Scenario: Update Corporate Client's Information
    Given admin logs in
    And corporate client exists
    When corporate client's username is changed to "newTestClientName"
    Then corporate client's username in database is "newTestClientName"

  Scenario: Update Private Client's Information
    Given admin logs in
    And private client exists
    When private client's surname is changed to "newSurname"
    Then private client's surname in database is "newSurname"