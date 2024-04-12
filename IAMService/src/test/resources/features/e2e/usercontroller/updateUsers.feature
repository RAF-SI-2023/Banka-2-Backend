#Feature: Update Users
#
#  Scenario: Update Employee Information
#    Given admin logs in
#    And employee "mirkomail@gmail.com" exists
#    When employee's "mirkomail@gmail.com" phone number is changed to "123456789"
#    Then employee's "mirkomail@gmail.com" phone number in database is "123456789"
#
## Donji testovi rade kad se pozovu preko iz ovog fajla. Kad se pozovu preko UserControllerE2ETests ne rade (bad request)
#
#  #Scenario: Update Corporate Client's Information
#    #Given admin logs in
#    #And corporate client "vladimir@gmail.com" exists
#    #When corporate client's "vladimir@gmail.com" name is changed to "newTestClientName"
#    #Then corporate client's "vladimir@gmail.com" name in database is "newTestClientName"
#
#  #Scenario: Update Private Client's Information
#    #Given admin logs in
#    #And private client "andrija@gmail.com" exists
#    #When private client's "andrija@gmail.com" surname is changed to "newSurname"
#    #Then private client's "andrija@gmail.com" surname in database is "newSurname"