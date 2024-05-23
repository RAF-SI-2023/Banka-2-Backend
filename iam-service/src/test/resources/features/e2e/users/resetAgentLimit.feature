Feature: Reset Agent's Limit

    Scenario: Admin resets agent's limit
        Given admin logs in
        And agent "dummyAgent@gmail.com" exists
        When agent's "dummyAgent@gmail.com" left limit is reset
        Then agent's "dummyAgent@gmail.com" left limit is same as their max limit
