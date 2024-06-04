Feature: Get Contracts
    Scenario: All Contracts
        Given Contracts exist
        When Calling getAllContracts
        Then Get list of all contracts
    Scenario: All Waiting Contracts
        Given Contracts exist
        When Calling getAllWaitingContracts
        Then Get list of all waiting contracts
    Scenario: Contract by ID
        Given Contract with id 4 exists
        When Calling getById 4
        Then Get contract with id 4