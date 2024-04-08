Feature: Reset Agent's Limit

  Scenario: Admin resets agent's limit
    Given admin logs in
    And agent "agent1@gmail.com" exists
    When agent's "agent1@gmail.com" left limit is reset
    Then agent's "agent1@gmail.com" left limit is same as their max limit
