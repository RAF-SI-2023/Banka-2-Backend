Feature: Verify transfer transactions

  Scenario: Successfully verify an external transfer transaction
    Given a pending external transfer transaction from "2225440022299222" to "1111140011199222" with token "12345" and amount "1000" - happyFlow
    When I verify the transaction with ID and token "12345" - happyFlow
    Then the transaction should be confirmed
    And the sender's new balance should be "9000" after verification - happyFlow
    And the receiver's new balance should be "11000" after verification - happyFlow

  Scenario: Fail to verify an external transfer transaction with an incorrect token
    Given a pending external transfer transaction from "5555445522299222" to "9999445522299555" with token "12345" and amount "1000" - invalidToken
    When I verify the transaction with ID and token "invalidToken" - invalidToken
    Then the transaction should be declined
    And the sender's new balance should be "10000" after verification - invalidToken
    And the receiver's new balance should be "10000" after verification - invalidToken
