Feature: Login functionality



  Scenario: User logs in with invalid credentials
    Given an invalid login request with email "loginTestUser@gmail.com" and password "wrongpassword"
    When the user logs in
    Then a 401 unauthorized response is returned