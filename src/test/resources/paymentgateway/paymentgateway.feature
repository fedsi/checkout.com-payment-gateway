Feature: Create a new payment

  Scenario: Merchant process a payment through the payment gateway and receive a successful response.
    Given we have a valid Payment request
    When the merchant process the payment using the makePayment endpoint
    Then the response status code is 201

  Scenario Outline: Merchant retrieves the details of a previously made payment.
    When the merchant calls the retrieve payment endpoint with a valid payment id
    Then the response status code is 200
    And the response payment field are "<paymentId>", "<requestId>", "<merchantId>", "<amount>", "<currency>", "<cardHolder>", "<cardNumber>", "<expiryDate>", "<cvv>", "<status>", "<authorisationCode>"
    Examples:
      | paymentId                            | requestId                             | merchantId                            | amount | currency | cardHolder       |  cardNumber          |  expiryDate |  cvv |  status    |  authorisationCode |
      | 95b53506-b52b-4f67-8765-5b3b16c7f0e1 | e2f4d10b-e16a-46f2-846c-943b871c8702  | 28f4dc58-2903-443f-8e10-0c01a554f5c2  | 100    | GBP      | Federico Silveri |  5555-xxxx-xxxx-xx44 | xx/xx       | xxx  | AUTHORISED |  AUTH535           |
