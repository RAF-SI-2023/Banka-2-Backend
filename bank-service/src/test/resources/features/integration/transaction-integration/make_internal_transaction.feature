Feature: Create internal transfer transactions
  @txn
  Scenario: Successfully create an internal transfer transaction
    Given a sender account with number "0045449924599000" with a balance of 10000 for internal transaction - happyFlow
    And a receiver account with number "994544992459999" with a balance of 5000 for internal transaction - happyFlow
    When I request an internal transfer of 2000 from "0045449924599000" to "994544992459999" - happyFlow
    Then the internal transfer should be successful
    And the sender's new balance should be 8000 after internal transaction - happyFlow
    And the receiver's new balance should be 7000 after internal transaction - happyFlow
  @txn
  Scenario: Fail to create an internal transfer transaction due to insufficient funds
    Given a sender account with number "1115449921199000" with a balance of 1000 for internal transaction - insufficientFunds
    And a receiver account with number "2225449922299222" with a balance of 5000 for internal transaction - insufficientFunds
    When I request an internal transfer of 2000 from "1115449921199000" to "2225449922299222" - insufficientFunds
    Then the internal transfer should be declined
    And the sender's new balance should be 1000 after internal transaction - insufficientFunds
    And the receiver's new balance should be 5000 after internal transaction - insufficientFunds

  Scenario: Taking or giving cashe assets, to account depending on amount
    Given account number "0932345111111111" for user and amount "100" thats taken
    When request is send for changing user money balance
    Then response is back with status ok
