Feature: Approve Contracts
    Scenario: Bank approves contract
        Given Admin logs in
        When Calls Bank Approve Contract
        Then Contract is approved by bank
    Scenario: Seller approves contract
        Given Seller logs in
        When Calls Seller Approve Contract
        Then Contract is approved by seller
    Scenario: Seller has wrong email
        Given Wrong Seller logs in
        When Attempts to approve contract
        Then Error pops up


