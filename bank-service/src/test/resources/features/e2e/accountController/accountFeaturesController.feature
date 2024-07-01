Feature: AccountController Scenario

  Scenario: Admin or supervisor need to get accounts information
    Given no paramethers to give
    When they send request for accounts informations to get account paramethers
    Then response is back with status ok and accounts informations

#  Scenario: Addition cashe assets to account
#    Given account number "0932345111111111" and amount "100"
#    When request is send for changing user money balance
#    Then response is back with status ok

  Scenario: Addition cashe assets to account without account number
    Given data of account number "dfdf" and amount "100"
    When request is send for changing user money balance for user
    Then response is back with exception not found

#  Scenario: Subtraction of cashe assets to account
#    Given subtraction account number "0932345111111111" and amount "100"
#    When request is send for reducing user money balance
#    Then response is recieved back with status ok

  Scenario: Subtraction of cashe to account without account number
    Given data of account number "dfdf" and amount "100" for subtraction
    When request is send for reducing user money balance for user but not found
    Then response is back with exception error message

  Scenario: Subtraction of cashe to account with to big amount
    Given data of account number "0932345111111111" and amount "10000000000" for tihs subtraction
    When request is send for reducing user money balance for user but to much
    Then response is back with exception run time exception


#  Scenario: Admin or supervisor need to get account information depending on amount
#    Given they are giving account number "0932345111111111"
#    When they send request for account informations to get
#    Then response is back with status ok and account informations

#  Scenario: Admin or supervisor creates request without necessery data
#    Given they are giving, account number "0932345111111111"
#    When they send request for account informations
#    Then response is back with error message