Feature: Create external transfer transactions
  @txn
  Scenario: Successfully create an external transfer transaction
    Given a sender account with number "9345449924599991" with a balance of 10000 for external transaction - happyFlow
    And a receiver account with number "1115449924591199" with a balance of 5000 for external transaction - happyFlow
    When I request an external transfer of 2000 from "9345449924599991" to "1115449924591199" - happyFlow
    Then the external transfer should be pending
    And the sender's new balance should be 10000 after external transaction - happyFlow
    And the receiver's new balance should be 5000 after external transaction - happyFlow
  @txn
  Scenario: Fail to create an external transfer transaction due to insufficient funds
    Given a sender account with number "5545449924599995" with a balance of 1000 for external transaction - insufficientFunds
    And a receiver account with number "6645449924599666" with a balance of 5000 for external transaction - insufficientFunds
    When I request an external transfer of 2000 from "5545449924599995" to "6645449924599666" - insufficientFunds
    Then the external transfer should be declined
    And the sender's new balance should be 1000 after external transaction - insufficientFunds
    And the receiver's new balance should be 5000 after external transaction - insufficientFunds
