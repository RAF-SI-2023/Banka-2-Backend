Feature: Create Contracts
    Scenario: Create Private Contract
        Given Contract with seller's email "testSellerEmail@gmail.com"
        When Calling createContract
        Then Create Private Contract
    Scenario: Create Legal Entity Contract
        # contract without sellers email is considered legal entity contract
        Given Contract with seller's email ""
        When Calling createContract
        Then Create Legal Entity Contract