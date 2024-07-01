Feature: CardController Scenario

#  Scenario: Successfully create a card
#    Given employee is logged in
#    When the create card endpoint is called
#    Then it should return a success response with the created card details

  Scenario: Attempt to create a card without required data
    Given employee is logged in
    When an invalid create card request without necessary data
    Then it should return a not found status with an error message

#  Scenario: Get card Number
#    Given employee is logged in
#    When  users endpoint is called
#    Then it should return a success response with the card details

#  Scenario: change card status
#    Given employee is logged in
#    When  users endpoint put is called "http://localhost:8003/api/cards/change-status/1000000000000000"
#    Then it should return a success response with the card details

  Scenario: get card from account number
    Given employee is logged in
    When  users endpoint get is called "http://localhost:8003/api/cards/account-number/0004444999999999"
    Then it should return a success response with the card details

#  Scenario: change limit for card
#    Given employee is logged in
#    When  users endpoint put is called "http://localhost:8003/api/cards/change-card-limit" and given body
#    Then it should return a success response with the card details


