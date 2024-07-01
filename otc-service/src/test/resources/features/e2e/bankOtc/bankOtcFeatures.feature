Feature:
  Scenario: We want to create an offer to sell to another bank
    Given generate jwt
    When Make an offer
    Then  response status ok and body user

  Scenario: We want to get all offers from another bank
    When Get all offers from another bank
    Then  response status ok and body user

  Scenario: We want to get all offers
    When Get all offers
    Then  response status ok and body user

  Scenario: We want to get all offers which we created
    When Get all our offers
    Then  response status ok and body user

  Scenario: Refresh all offers
    When Refresh offers
    Then  response status ok and body user


