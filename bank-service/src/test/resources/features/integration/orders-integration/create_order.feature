Feature: Creating an order

#  Scenario: Successfully create a buy order that is not owned by bank
    #Given is not an agent is not a supervisor
 #   Given order dto is created
 #   When user attempts to create order
 #   Then the order should be created

  Scenario: Fail to create an order non existent cash account
    Given non existent cash account
    When user attempts to create order
    Then service should throw an exception