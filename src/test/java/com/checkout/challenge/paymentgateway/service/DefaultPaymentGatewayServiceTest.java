package com.checkout.challenge.paymentgateway.service;

import com.checkout.challenge.paymentgateway.acquiringbank.AcquiringBankMock;
import com.checkout.challenge.paymentgateway.dto.acquirer.AcquirerResponseDTO;
import com.checkout.challenge.paymentgateway.entity.Payment;
import com.checkout.challenge.paymentgateway.error.exception.ExistingPaymentException;
import com.checkout.challenge.paymentgateway.repository.PaymentRepository;
import io.cucumber.java.cy_gb.A;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@SpringBootTest
@Transactional
class DefaultPaymentGatewayServiceTest {

    private static final String REQUEST_ID = UUID.randomUUID().toString();
    private static final String MERCHANT_ID = UUID.randomUUID().toString();

    @InjectMocks
    private DefaultPaymentGatewayService paymentGatewayService;

    @Mock
    private PaymentRepository repository;

    @Mock
    private AcquiringBankMock acquiringBankMock;

    @Test
    public void retrievePayment_paymentIsRetrieved() {
        // Given
        Payment expectedPayment = buildPaymentWithAuthCodes();
        given(repository.findById(any())).willReturn(Optional.of(expectedPayment));

        // When
        Optional<Payment> optionalPayment = paymentGatewayService.retrievePayment(expectedPayment.getPaymentId());

        // Then
        assertPaymentFields(expectedPayment, optionalPayment.get());
    }

    @Test
    public void makePayment_newPaymentIsRetrieved() {
        // Given
        Payment expectedResult = buildPaymentWithAuthCodes();
        Payment paymentRequest = buildPaymentRequest();
        AcquirerResponseDTO acquirerResponseDTO = buildAcquirerResponse();

        given(repository.findById(any())).willReturn(Optional.empty());
        given(acquiringBankMock.authorisePayment(any())).willReturn(acquirerResponseDTO);
        given(repository.save(any())).willReturn(expectedResult);

        // When
        Payment actualPayment = paymentGatewayService.makePayment(paymentRequest);

        // Then
        assertPaymentFields(expectedResult, actualPayment);
    }

    @Test
    public void makePayment_existingPaymentId_ExceptionIsThrown() {
        // Given
        Payment existingPayment = buildPaymentWithAuthCodes();
        Payment paymentRequest = buildPaymentRequest();

        given(repository.findByRequestId(any())).willReturn(Optional.of(existingPayment));

        // When - Then
        assertThrows(ExistingPaymentException.class, () -> {
            paymentGatewayService.makePayment(paymentRequest);
        });
    }

    private AcquirerResponseDTO buildAcquirerResponse() {
        AcquirerResponseDTO acquirerResponseDTO = new AcquirerResponseDTO();
        acquirerResponseDTO.setAuthorisationCode("AUTH1234");
        acquirerResponseDTO.setStatus("AUTHORISED");
        return acquirerResponseDTO;
    }

    private Payment buildPaymentRequest() {
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

        return payment;
    }

    private Payment buildPaymentWithAuthCodes() {
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
        payment.setStatus("AUTHORISED");
        payment.setAuthorisationCode("AUTH1234");

        return payment;
    }

    private void assertPaymentFields(Payment expectedPayment, Payment actualPayment) {
        assertEquals(expectedPayment, actualPayment);
        assertEquals(expectedPayment.getPaymentId(), actualPayment.getPaymentId());
        assertEquals(expectedPayment.getRequestId(), actualPayment.getRequestId());
        assertEquals(expectedPayment.getMerchantId(), actualPayment.getMerchantId());
        assertEquals(expectedPayment.getAmount(), actualPayment.getAmount());
        assertEquals(expectedPayment.getCurrency(), actualPayment.getCurrency());
        assertEquals(expectedPayment.getCardHolder(), actualPayment.getCardHolder());
        assertEquals(expectedPayment.getCardNumber(), actualPayment.getCardNumber());
        assertEquals(expectedPayment.getExpiryDate(), actualPayment.getExpiryDate());
        assertEquals(expectedPayment.getCvv(), actualPayment.getCvv()); //??
        assertEquals(expectedPayment.getStatus(), actualPayment.getStatus());
        assertEquals(expectedPayment.getDescription(), actualPayment.getDescription());
        assertEquals(expectedPayment.getAuthorisationCode(), actualPayment.getAuthorisationCode());
    }

}