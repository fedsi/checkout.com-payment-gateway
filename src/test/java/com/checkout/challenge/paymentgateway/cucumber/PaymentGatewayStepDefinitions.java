package com.checkout.challenge.paymentgateway.cucumber;

import com.checkout.challenge.paymentgateway.dto.merchant.PaymentRequestDTO;
import com.checkout.challenge.paymentgateway.dto.merchant.PaymentResponseDTO;
import com.checkout.challenge.paymentgateway.entity.Payment;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class PaymentGatewayStepDefinitions {

    private static final String REQUEST_ID = UUID.randomUUID().toString();
    private static final String MERCHANT_ID = UUID.randomUUID().toString();

    @Autowired
    private HttpClient client;

    private PaymentRequestDTO paymentRequestDTO;
    private ResponseEntity result;



    @Given("we have a valid Payment request")
    public void weHaveAValidPaymentRequest() throws ParseException {
        paymentRequestDTO = buildPaymentRequest();
    }

    @When("the merchant process the payment using the makePayment endpoint")
    public void theMerchantProcessThePaymentUsingMakePaymentEndpoint() {
        result = client.post(paymentRequestDTO);
    }

    @When("the merchant calls the retrieve payment endpoint with a valid payment id")
    public void theMerchantCallsRetrievePaymentEnpointWithValidPaymentId() {
        result = client.retrievePaymentByPaymentId();
    }

    @Then("the response status code is {int}")
    public void theResponseStatusCodeIs(int status) {
        assertThat(result.getStatusCode().value(), is(status));
    }

    @Then("the response payment field are {string}, {string}, {string}, {string}, {string}, {string}, {string}, {string}, {string}, {string}, {string}")
    public void theResponsePaymentFieldsAre(String paymentId, String requestId, String merchantId, String amount, String currency, String cardHolder, String cardNumber, String expiryDate, String cvv, String status, String authorisationCode) {
        PaymentResponseDTO paymentResponse = (PaymentResponseDTO) result.getBody();
        assertThat(paymentResponse.getPaymentId().toString(), is(paymentId));
        assertThat(paymentResponse.getRequestId(), is(requestId));
        assertThat(paymentResponse.getMerchantId(), is(merchantId));
        assertThat(paymentResponse.getAmount().toString(), is(amount));
        assertThat(paymentResponse.getCurrency(), is(currency));
        assertThat(paymentResponse.getCardHolder(), is(cardHolder));
        assertThat(paymentResponse.getCardNumber(), is(cardNumber));
        assertThat(paymentResponse.getExpiryDate(), is(expiryDate));
        assertThat(paymentResponse.getCvv(), is(cvv));
        assertThat(paymentResponse.getStatus(), is(status));
        assertThat(paymentResponse.getAuthorisationCode(), is(authorisationCode));
    }

    private PaymentRequestDTO buildPaymentRequest() {
        PaymentRequestDTO paymentRequestDTO = new PaymentRequestDTO();
        paymentRequestDTO.setRequestId(REQUEST_ID);
        paymentRequestDTO.setMerchantId(MERCHANT_ID);
        paymentRequestDTO.setAmount(new BigDecimal(100.00));
        paymentRequestDTO.setCurrency("GBP");
        paymentRequestDTO.setCardHolder("Federico Silveri");
        paymentRequestDTO.setCardNumber("5555555555554444");
        paymentRequestDTO.setExpiryDate("09/27");
        paymentRequestDTO.setCvv("123");
        return paymentRequestDTO;
    }

}
