# Checkout.com Payment Gateway

A simple Rest API application that will allow a merchant to offer a
way for their shoppers to pay for their product.

The Payment Gateway is responsible for validating requests, storing card information and forwarding
payment requests and accepting payment responses to and from the acquiring bank.

## Prerequisites
```
- Java 11+
- Gradle
```

## API Reference
Complete API documentation can be build using the provided ``yaml/CheckoutPaymentGateway.yaml`` file.

## Authentication
All endpoints are authenticated using Basic Auth.

Spring's _actuator_ endpoints, used to monitor the application's health, don't require any authentication.

## Validation
All the request fields are mandatory, there are some additional validation on card and amount info.
 - **Amount**: should be greater than 0
 - **Currency**: should be an ISO4217 alphabetic currency code
 - **Card Number**: should pass Luhn check
 - **Card Expiry Date**: should be after today's month
 - **Card CVV**: should be 3 or 4 digits

 
## Encoding
As soon as the payment request pass the validation, to "meet" the PCI standard on cardholder data,
all the sensitive information (_Card Holder_, _Card Number_, _Card Expiry Date_ and _Card CVV_)
are encoded using a base64 encoding scheme.

The encoded values will be used when logging and persisting the Payment request.

The decoded value will be used to send the payment request to the Acquiring Bank.

## Testing

The critical area of the project are tested via unit test using JUnit.

Two e2e tests that cover thw two main user jurney (make and retrieve payment) are tested using cucumber.

To execute these tests, run this command from the repository root:
```
./gradlew cucumberTests
```
The result is a summary of all the configured scenarios:
```
2 Scenarios (2 passed)
6 Steps (6 passed)
0m6,795s
```

The full Cucumber report can  be found in the build folder (_checkout.com-payment-gateway/build/cucumber-report.html_)

## Running
Application entry point:  _PaymentGatewayApplication_

**POST** - Make new payment

_http://localhost:8080/api/payment_

Use this endpoint to process a new payment through the payment gateway.

Request example:
```
{
    "requestId": "e2f4d10b-e16a-46f2-846c-943b871c8702",
    "merchantId": "28f4dc58-2903-443f-8e10-0c01a554f5c2",
    "amount": 100.00,
    "currency": "GBP",
    "cardHolder": "Federico Silveri",
    "cardNumber": "5555555555554444",
    "expiryDate": "08/24",
    "cvv": "123"
}
```

Response example:
   1. 201 Created 
   ```
    {
        "paymentId": "4276f057-3d6a-4f0d-9f61-7607990f8048",
        "requestId": "e2f4d10b-e16a-46f2-846c-943b871c8702",
        "merchantId": "28f4dc58-2903-443f-8e10-0c01a554f5c2",
        "amount": 100.00,
        "currency": "EUR",
        "cardHolder": "Federico Silveri",
        "cardNumber": "5555-xxxx-xxxx-xx44",
        "expiryDate": "xx/xx",
        "cvv": "xxx",
        "status": "AUTHORISED",
        "authorisationCode": "AUTH4736"
    }
   ```
    
   2. 400 Bad Request (Invalid Payment request)
   ```
      {
         "type": "request",
         "message": "Invalid Payment Request",
         "errors": [
            "Card Expiry Date should be after today's month",
            "Card Number should pass Luhn check",
            "Card CVV should be 3 or 4 digits"
         ]
      }
   ```
   3. 400 Bad Request (Existing payment) 
   ```
      {
         "type": "request",
         "message": "Payment already exist",
         "errors": [
            "Payment with id e2f4d10b-e16a-46f2-846c-943b871c8703 already exist"
         ]
      }
   ```

**GET** - Retrieve an existing payment

_http://localhost:8080/api/payment/{{paymentId}}_

Retrieve the details of a previously made payment.
The cardholder and card details are masked.

Response example:
```
{
    "paymentId": "4276f057-3d6a-4f0d-9f61-7607990f8048",
    "requestId": "e2f4d10b-e16a-46f2-846c-943b871c8702",
    "merchantId": "28f4dc58-2903-443f-8e10-0c01a554f5c2",
    "amount": 100.00,
    "currency": "EUR",
    "cardHolder": "Federico Silveri",
    "cardNumber": "5555-xxxx-xxxx-xx44",
    "expiryDate": "xx/xx",
    "cvv": "xxx",
    "status": "AUTHORISED",
    "authorisationCode": "AUTH4736"
}
```

## Improvements

 - **Authentication**: Use a stronger type of authentication (like JWT) instead of basic auth on the endpoints.
 - **Merchant**: Enroll merchant on the system using another microservice. With that we could perform
    some additional validation based on the merchantId (e.g. Allow payments only to configured merchant).
 - **Acquiring Bank**:  
   - Enhance the system to route payment request to more than one Acquiring Banks. Especially if they
      are third party applications, they'll most likely apply fees on requests. These fees might be based on
      card brand (VISA, Mastercard, etc...) or card issuer. Having a payment routing engine that route the payment request
      to the Acquiring Bank that applies the lowest fees on that particular card will allow us to save on commission.
   - Implement a retry mechanism in case of timeout or network issue for the request to the acquiring bank.
 - **DataStore**: Use a traditional database instead of the in-memory one. Using a well known RDBMS like MySQL, PostregSQL, etc..
    could be a good option as they all are ACID compliant, a key aspect if we're working on a payment system.
    Some NoSQL database offer ACID-compliant solution (e.g. MongoDB).
 - **Encryption**: Use a strong encryption instead of Base64 encoding for all sensitive cardholder data.
 - **Idempotency**: Implement idempotency on _makePayment_ endpoint to avoid multiple charges. In this project, if the requestId (set by the merchant)
     already exist in the database, an error will be returned.
