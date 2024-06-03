Feature: Approve Contracts
    Scenario: Bank approves contract
        Given Non approved contract exists
        When Calling bankApproveContract
        Then Contract gets bank approved
    Scenario: Bank denies contract
        # bank confirmation status needs to be True to be able to deny it
        Given Approved contract exists
        When Calling bankDenyContract
        Then Contract gets bank denied