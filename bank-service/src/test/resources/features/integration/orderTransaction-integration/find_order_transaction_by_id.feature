Feature: Finding order transaction by id
  Scenario: User looks up order transaction by id
    Given there is an order transaction with a specific id
    When I request order transaction with that id
    Then I should receive the order transaction with that id