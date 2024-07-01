Feature: User interacting with cards

#  Scenario: User looks card by IdentificationCardNumber
#    Given user has an account with account number "0004444999999999" for the card
#    When the user enters the IdentificationCardNumber 1000000000000000
#    Then the user should return 200

  Scenario: User looks cards by account number
    Given user has an account with account number "0004444999999999" for the card
    When the user enters the account number "0004444999999999"
    Then the user should return 200

 # Scenario: User changes status in card
 #   Given user has an account with account number "0004444999999999" for the card
 #   When the user enters the IdentificationCardNumber 1000000000000000
 #   And the user changes status of 1000000000000000
 #  Then the user should return 200

 # Scenario: User changes limit in card
 #   Given user has an account with account number "0004444999999999" for the card
 #   When the user enters the IdentificationCardNumber 1000000000000000
 #   And the user changes limit of 1000000000000000
 #   Then the user should return 200

  Scenario: user creates a new card
    Given user has an account with account number "0004444999999999" for the card
    When the bank creates a new card with given account number
    Then the user should return 200 for creation



