Feature: Agent's limit reset

  Scenario: Reset agent's limit
    Given agent exists
    When agent's left limit is reset
    Then agent's left limit is equal to their max limit

  Scenario: Reset user's limit while user is not agent
    Given user exists
    Then reseting user's limit will fail

