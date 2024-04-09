Feature: User Controller

  Scenario: user wants to create employee
    Given user gives valid paramethers for employee email "employee@gmail.com" and id "1"
    When  user send request
    Then status is ok returns as resonse and body id

  Scenario: user wants to create agent
    Given user gives valid paramethers for agent email "employee@gmail.com" and id "1"
    When  user send request to create agent
    Then status is ok returns as resonse and body id for agent create


  Scenario: user changing password
    Given user gives paramethers email "user@gmail.com" and password "user"
    When  user send request for changing password
    Then  response status ok and body true


  Scenario: user want to get some other user with his email
    Given user gives paramethers email "user@gmail.com"
    When user send request for geting user
    Then response status ok and body user

  Scenario: user want to get some other user with his id
    Given user gives paramethers id "1"
    When user send request to get user
    Then response status ok and body user for user geting id