Feature: Testing Credit Controller
  
Scenario: Creating new credit request
    Given user is logged in as employee;
    When user creates new credit request;
    Then credit request is created; and we can see it in the list of credit requests;

Scenario: Approving credit request
    Given user is logged in as employee;
    Given credit request is pending approval and is in the list of credit requests
    When user approves credit request;
    Then credit request is approved; and we can see credit in the list of approved credits for the client;
