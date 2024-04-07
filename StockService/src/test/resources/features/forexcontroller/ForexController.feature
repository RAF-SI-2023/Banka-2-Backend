Feature: Testing ForexController

  Scenario: User fetches all forex pairs
    When user requests all forex pairs
    Then the response should contain all forex pairs

  Scenario: Fetching forex pair by existing id
    When user requests forex pair with id "29352"
    Then response contains forex pair with id "29352"

  Scenario: Fetching forex pair by non-exiting id
    When user requests forex pair with non-existing id "0"
    Then response is empty and status is ok