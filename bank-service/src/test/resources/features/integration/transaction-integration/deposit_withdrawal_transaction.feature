Feature: Deposit and Withdrawal transaction - ATM

  Scenario: Deposit funds to account
    Given an account with number "1122334455667788" with a balance of 15000 "RSD"
    When deposits 5000 "RSD" to the account
    Then the account's new balance should be 20000 after transfer

  Scenario: Withdraw funds from account
    Given an account with number "1122334455667788" with a balance of 15000 "RSD"
    When withdraws -5000 "RSD" from the account
    Then the account's new balance should be 10000 after transfer