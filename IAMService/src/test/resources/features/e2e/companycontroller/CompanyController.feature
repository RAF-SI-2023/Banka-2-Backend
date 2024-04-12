Feature: Testing CompanyController
  Scenario: User request all companies
    When user requests all companies
    Then the response should contain all companies

  Scenario: User requests company by existing id
    When user requests company with id "1"
    Then response contains company with id "1"

  Scenario: User requests company by non-exiting id
    When user requests company with non-existing id "-1"
    Then response has status NotFound