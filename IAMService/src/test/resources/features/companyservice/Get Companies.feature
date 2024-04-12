Feature: Get Companies
  Scenario: Get all companies
    Given company exists
    When calling find all companies
    Then get list of all companies
  Scenario: Get all companies and find company by PIB
    Given company exists
    When calling find all companies
    Then find company by pib "123456789"
