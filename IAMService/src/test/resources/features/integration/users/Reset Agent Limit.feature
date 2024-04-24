Feature: Agent's limit reset

    Scenario: Reset agent's limit
        Given Agent "agentLimitReset@gmail.com" exists
        When Agent's "agentLimitReset@gmail.com" left limit is reset
        Then agent's "agentLimitReset@gmail.com" left limit is equal to their max limit

    Scenario: Reset user's limit while user is not agent
        Given user that is not agent
        Then resetting user's limit will fail

