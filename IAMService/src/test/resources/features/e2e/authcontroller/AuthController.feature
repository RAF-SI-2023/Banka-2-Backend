Feature: Login functionality

  Scenario: User logs in with valid credentials
    Given a valid login request with email "nikola@gmail.com" and password "Nikola123!"
    When the user logs in
    Then a valid JWT token is returned

  Scenario: User logs in with invalid credentials
    Given an invalid login request with email "nikola@gmail.com" and password "wrongpassword"
    When the user logs in
    Then a 401 unauthorized response is returned