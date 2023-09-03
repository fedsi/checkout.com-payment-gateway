package com.checkout.challenge.paymentgateway.controller;

import com.checkout.challenge.paymentgateway.dto.merchant.PaymentRequestDTO;
import com.checkout.challenge.paymentgateway.dto.merchant.PaymentResponseDTO;
import com.checkout.challenge.paymentgateway.entity.Payment;
import com.checkout.challenge.paymentgateway.service.PaymentGatewayService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest(controllers = PaymentGatewayController.class)
class PaymentGatewayControllerTest {

    private static final String MAKE_PAYMENT_ENDPOINT = "/payment";
    private static final String RETRIEVE_PAYMENT_ENDPOINT = "/payment/e2f4d10b-e16a-46f2-846c-943b871c8702";


    private static final String BASIC_AUTH = "Basic dXNlcjpwYXNzd29yZA==";

    private static final String REQUEST_ID = UUID.randomUUID().toString();
    private static final String MERCHANT_ID = UUID.randomUUID().toString();

    @MockBean
    private PaymentGatewayService paymentGatewayService;

    @Autowired
    private MockMvc mockMvc;

    private PaymentRequestDTO mockedPaymentRequest = buildPaymentRequest();

    @Test
    public void retrievePayment_paymentIsRetrieved() throws Exception {
        // Given
        Payment mockedAuthorisedPayment = buildPayment(true, null);
        PaymentResponseDTO expectedResponse = buildAuthorisedPaymentResponse(mockedAuthorisedPayment.getPaymentId());

        given(paymentGatewayService.retrievePayment(any())).willReturn(Optional.of(mockedAuthorisedPayment));

        // When
        MockHttpServletResponse response = mockMvc.perform(get(RETRIEVE_PAYMENT_ENDPOINT)
                        .header("Authorization", BASIC_AUTH))
                .andReturn().getResponse();

        // Then
        ObjectMapper mapper = new ObjectMapper();
        PaymentResponseDTO actualResponse = mapper.readValue(response.getContentAsString(), PaymentResponseDTO.class);

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    public void retrievePayment_paymentNotExist() throws Exception {
        // Given
        given(paymentGatewayService.retrievePayment(any())).willReturn(Optional.empty());

        // When
        MockHttpServletResponse response = mockMvc.perform(get(RETRIEVE_PAYMENT_ENDPOINT)
                        .header("Authorization", BASIC_AUTH))
                .andReturn().getResponse();

        // Then
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
    }

    @Test
    public void makePayment_whenPaymentIsAuthorised_paymentIsRetrieved() throws Exception {
        // Given
        Payment mockedAuthorisedPayment = buildPayment(true, null);
        PaymentResponseDTO expectedResponse = buildAuthorisedPaymentResponse(mockedAuthorisedPayment.getPaymentId());
        given(paymentGatewayService.makePayment(any())).willReturn(mockedAuthorisedPayment);

        ObjectMapper mapper = new ObjectMapper();

        // When
        MockHttpServletResponse response = mockMvc.perform(post(MAKE_PAYMENT_ENDPOINT)
                        .header("Authorization", BASIC_AUTH)
                        .contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(mockedPaymentRequest)))
                .andReturn().getResponse();

        // Then
        PaymentResponseDTO actualResponse = mapper.readValue(response.getContentAsString(), PaymentResponseDTO.class);

        assertEquals(HttpStatus.CREATED.value(), response.getStatus());
        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    public void makePayment_whenPaymentIsDeclined_paymentIsRetrieved() throws Exception {
        // Given
        Payment mockedAuthorisedPayment = buildPayment(false, "Insufficient Funds");
        PaymentResponseDTO expectedResponse = buildDeclinedPaymentResponse(mockedAuthorisedPayment.getPaymentId());
        given(paymentGatewayService.makePayment(any())).willReturn(mockedAuthorisedPayment);

        ObjectMapper mapper = new ObjectMapper();

        // When
        MockHttpServletResponse response = mockMvc.perform(post(MAKE_PAYMENT_ENDPOINT)
                        .header("Authorization", BASIC_AUTH)
                        .contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(mockedPaymentRequest)))
                .andReturn().getResponse();

        // Then
        PaymentResponseDTO actualResponse = mapper.readValue(response.getContentAsString(), PaymentResponseDTO.class);

        assertEquals(HttpStatus.CREATED.value(), response.getStatus());
        assertEquals(expectedResponse, actualResponse);
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

    private Payment buildPayment(boolean authorised, String declineDescription) {
        Payment payment = new Payment();
        payment.setPaymentId(UUID.randomUUID());
        payment.setRequestId(REQUEST_ID);
        payment.setMerchantId(MERCHANT_ID);
        payment.setAmount(new BigDecimal(100.00));
        payment.setCurrency("GBP");
        payment.setCardHolder("Federico Silveri");
        payment.setCardNumber("5555555555554444");
        payment.setExpiryDate("09/27");
        payment.setCvv("123");
        if (authorised) {
            payment.setStatus("AUTHORISED");
            payment.setAuthorisationCode("AUTH1234");
        } else {
            payment.setStatus("DECLINED");
            payment.setDescription(declineDescription);
        }

        return payment;
    }

    private PaymentResponseDTO buildAuthorisedPaymentResponse(UUID paymentId) {
        PaymentResponseDTO responseDTO = new PaymentResponseDTO();
        responseDTO.setPaymentId(paymentId);
        responseDTO.setRequestId(REQUEST_ID);
        responseDTO.setMerchantId(MERCHANT_ID);
        responseDTO.setAmount(new BigDecimal(100.00));
        responseDTO.setCurrency("GBP");
        responseDTO.setCardHolder("Federico Silveri");
        responseDTO.setCardNumber("5555-xxxx-xxxx-xx44");
        responseDTO.setExpiryDate("xx/xx");
        responseDTO.setCvv("xxx");
        responseDTO.setStatus("AUTHORISED");
        responseDTO.setAuthorisationCode("AUTH1234");
        return responseDTO;
    }

    private PaymentResponseDTO buildDeclinedPaymentResponse(UUID paymentId) {
        PaymentResponseDTO responseDTO = new PaymentResponseDTO();
        responseDTO.setPaymentId(paymentId);
        responseDTO.setRequestId(REQUEST_ID);
        responseDTO.setMerchantId(MERCHANT_ID);
        responseDTO.setAmount(new BigDecimal(100.00));
        responseDTO.setCurrency("GBP");
        responseDTO.setCardHolder("Federico Silveri");
        responseDTO.setCardNumber("5555-xxxx-xxxx-xx44");
        responseDTO.setExpiryDate("xx/xx");
        responseDTO.setCvv("xxx");
        responseDTO.setStatus("DECLINED");
        responseDTO.setDescription("Insufficient Funds");
        return responseDTO;
    }

}