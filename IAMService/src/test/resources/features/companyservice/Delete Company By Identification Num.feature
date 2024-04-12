Feature: Deleting a Company by Identification Number

  Scenario: Successfully delete a company by identification number
    Given company with identification number "123456" exists
    When  delete the company with identification number "123456"
    Then  company with identification number "123456" should be deleted from the database

  Scenario: Attempting to delete a company with an invalid identification number
    Given there is no company with identification number -1 in the database
    Then I attempt to delete the company with identification number -1


  Scenario: Attempting to delete a company with an invalid id
    Then company with id "-1" does not exist
