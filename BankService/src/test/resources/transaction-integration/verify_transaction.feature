Feature: Verify transfer transactions

  Scenario: Successfully verify an external transfer transaction
    Given a pending external transfer transaction from "111222" to "222111" with token "12345" and amount "1000" - happyFlow
    When I verify the transaction with ID and token "12345" - happyFlow
    Then the transaction should be confirmed
    And the sender's new balance should be "9000" after verification - happyFlow
    And the receiver's new balance should be "11000" after verification - happyFlow

  Scenario: Fail to verify an external transfer transaction with an incorrect token
    Given a pending external transfer transaction from "123456" to "999999" with token "12345" and amount "1000" - invalidToken
    When I verify the transaction with ID and token "invalidToken" - invalidToken
    Then the transaction should be declined
    And the sender's new balance should be "10000" after verification - invalidToken
    And the receiver's new balance should be "10000" after verification - invalidToken
