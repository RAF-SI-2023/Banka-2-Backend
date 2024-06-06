Feature: Transfer funds
  Scenario: Successfully transfer funds
    Given a sender account with number "1122334455667788" with a balance of 15000 "RSD", 5000 of that reserved for transfer
    And a receiver account with number "9988776655443322" with a balance of 10000 "RSD"
    When I request a transfer of 5000 from "1122334455667788" to "9988776655443322"
    Then the transfer should be successful
    And the sender's new balance should be 10000 after transfer
    And the receiver's new balance should be 15000 after transfer
