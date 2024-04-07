Feature: Testing ForexServiceImpl

  Scenario: Fetching all forex pairs
    When fetching all forex pairs
    Then returns list of forex pairs

  Scenario: Fetching forex pair by existing id
    When Fetching forex pair with id "29352"
    Then Returns forex pair with id "29352"

  Scenario: Fetching forex pair by non-exiting id
    When Fetching forex pair with non-existing id "0"
    Then Returns null