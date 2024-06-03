Feature: Deny Contracts
    Scenario: Bank denies contract
        Given Admin logs in
        When Calls Bank Deny Contract
        Then Contract is denied by bank
    Scenario: Seller denies contract
        Given Seller logs in
        When Calls Seller Deny Contract
        Then Contract is denied by seller