Feature: AccountController Scenario

  Scenario: Admin or supervisor need to get account information
    Given they are giving account number "0932345111111111"
    When they send request for account informations to get
    Then response is back with status ok and account informations

  Scenario: Admin or supervisor creates request without necessery data
    Given they are giving, account number "0932345111111111"
    When they send request for account informations
    Then response is back with error message