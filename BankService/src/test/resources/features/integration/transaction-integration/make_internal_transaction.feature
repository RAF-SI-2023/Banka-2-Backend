Feature: Create internal transfer transactions
  @txn
  Scenario: Successfully create an internal transfer transaction
    Given a sender account with number "123456" with a balance of 10000 for internal transaction - happyFlow
    And a receiver account with number "654321" with a balance of 5000 for internal transaction - happyFlow
    When I request an internal transfer of 2000 from "123456" to "654321" - happyFlow
    Then the internal transfer should be successful
    And the sender's new balance should be 8000 after internal transaction - happyFlow
    And the receiver's new balance should be 7000 after internal transaction - happyFlow
  @txn
  Scenario: Fail to create an internal transfer transaction due to insufficient funds
    Given a sender account with number "111111" with a balance of 1000 for internal transaction - insufficientFunds
    And a receiver account with number "222222" with a balance of 5000 for internal transaction - insufficientFunds
    When I request an internal transfer of 2000 from "111111" to "222222" - insufficientFunds
    Then the internal transfer should be declined
    And the sender's new balance should be 1000 after internal transaction - insufficientFunds
    And the receiver's new balance should be 5000 after internal transaction - insufficientFunds
