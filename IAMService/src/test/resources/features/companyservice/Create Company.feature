Feature: Create company
  Scenario: Successfully creating company
    When create new company "12" "comp name" "11111" "22222" "3333" "444444" "100" "101" "Address"
    Then company successfully created