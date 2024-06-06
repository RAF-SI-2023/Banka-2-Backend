Feature:
  Scenario: Find order by id
    Given user is logged in
    When user calls find order by id endpoint "1"
    Then it should return success response


  Scenario: Successfully get all denied orders
    Given user is logged in
    When user calls get denied orders endpoint
    Then it should return a list of denied orders
    And it should return success response

    Scenario: Successfully get all approved orders
      Given user is logged in
      When user calls get approved orders endpoint
      Then it should return a list of approved orders
      And it should return success response

  Scenario: Successfully get all non approved orders
    Given user is logged in
    When user calls get non approved orders endpoint
    Then it should return a list of non approved orders
    And it should return success response

  Scenario: Successfully get all orders
    Given user is logged in
    When user calls get all  orders endpoint
    And it should return success response

  Scenario: Successfully update order status to DENIED
    Given user is logged in
    And order exist in the repository
    When user calls reject order endpoint
    Then it should return success response
    And it should return true
    And status should be DENIED

  Scenario: Successfully update order status to APPROVED
    Given user is logged in
    And order exist in the repository
    When user calls approve order endpoint
    Then it should return success response
    And it should return true
    And status should be APPROVED
