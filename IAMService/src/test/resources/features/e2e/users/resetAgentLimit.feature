Feature: Reset Agent's Limit

    Scenario: Admin resets agent's limit
        Given admin logs in
        And agent "dusan@gmail.com" exists
        When agent's "dusan@gmail.com" left limit is reset
        Then agent's "dusan@gmail.com" left limit is same as their max limit
