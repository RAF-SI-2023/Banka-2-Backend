Feature:
  Scenario: Get all order transactions
    Given I am logged in
    And one order transaction exists with a specific id
    When I visit the order transactions page
    Then I receive a success response
    And I receive a list of order transactions

  Scenario: Get order transaction by id
    Given I am logged in
    And one order transaction exists with a specific id
    When I visit the order transaction page with that id
    Then I receive a success response
    And I should see the order transaction with that id

  Scenario: Get order transaction by orderId
    Given I am logged in
    And one order transaction exists with orderId 456
    When I visit the order transaction page with orderId 456
    Then I receive a success response
    And I should see the order transaction with orderId 456

  Scenario: Get order transaction by account number
    Given I am logged in
    And one order transaction exists that belongs to an account with account number "1122334455667788"
    When I visit the order transaction page with account number "1122334455667788"
    Then I receive a success response
    And I should see the order transaction with account number "1122334455667788"