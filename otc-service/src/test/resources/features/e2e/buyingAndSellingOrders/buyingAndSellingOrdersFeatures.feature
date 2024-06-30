Feature:
  Scenario: Buying stocks from another user
    Given Buyer with email "lukapa369@gmail.com" is logged in
    When User buys from another user 5 shares of "NTFL" stock
    Then it should return a success response

  Scenario: Approving stocks by bank
    Given Admin with email "lukapavlovic032@gmail.com" is logged in
    When Admin approves 5 shares of "NTFL" stock for contract for user "lpavlovic11521rn@raf.rs"
    Then it should return a success response

#  Scenario: Approving stocks by seller
#    Given Seller with email "lpavlovic11521rn@raf.rs" is logged in
#    When User approves 5 shares of "NTFL" stock
#    Then it should return a success response

# Scenario: Check buyers stock
#    Given Buyer with email "lukapa369@gmail.com" is logged in
#    When User checks his stock it should return a stock of "NTFL" with 5 shares approved