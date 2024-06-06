Feature: Update Order Status

  Scenario: Successfully update order status to APPROVED
    Given an order exists with ID "1" and status "WAITING_FOR_APPROVAL"
    When the client requests to update the order status to "APPROVED"
    Then the order status should be updated to "APPROVED"

  Scenario: Successfully update order status to WAITING_FOR_APPROVAL
    Given an order exists with ID "2" and status "APPROVED"
    When the client requests to update the order status to "WAITING_FOR_APPROVAL"
    Then the order status should be updated to "WAITING_FOR_APPROVAL"

  Scenario: Fail to update order status for non-existent order
    Given no order exists with ID "-1"
    When the client requests to update the order status to "APPROVED" for non-existent order
    Then an OrderNotFoundException should be thrown