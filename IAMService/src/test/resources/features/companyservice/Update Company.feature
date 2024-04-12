Feature: Updating a Company
  Scenario: Successfully updating a company
    Given company exists
    When updating company name with "New name"
    Then company successfully updated with "New name"