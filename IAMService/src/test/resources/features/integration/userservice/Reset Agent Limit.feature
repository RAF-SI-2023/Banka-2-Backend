#Feature: Agent's limit reset
#
#  Scenario: Reset agent's limit
#    Given Agent "dusan@gmail.com" exists
#    When Agent's "dusan@gmail.com" left limit is reset
#    Then agent's "dusan@gmail.com" left limit is equal to their max limit
#
#  Scenario: Reset user's limit while user is not agent
#    Given user exists
#    Then resetting user's limit will fail
#
