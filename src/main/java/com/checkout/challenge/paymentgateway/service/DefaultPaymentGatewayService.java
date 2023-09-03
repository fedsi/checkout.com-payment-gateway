package com.checkout.challenge.paymentgateway.service;

import com.checkout.challenge.paymentgateway.acquiringbank.AcquiringBankMock;
import com.checkout.challenge.paymentgateway.dto.acquirer.AcquirerRequestDTO;
import com.checkout.challenge.paymentgateway.dto.acquirer.AcquirerResponseDTO;
import com.checkout.challenge.paymentgateway.entity.Payment;
import com.checkout.challenge.paymentgateway.error.exception.ExistingPaymentException;
import com.checkout.challenge.paymentgateway.error.message.ExceptionMessages;
import com.checkout.challenge.paymentgateway.repository.PaymentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class DefaultPaymentGatewayService implements PaymentGatewayService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private AcquiringBankMock acquiringBankMock;

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Payment> retrievePayment(UUID id) {
        return paymentRepository.findById(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Payment> retrievePaymentByRequestId(String requestId) {
        return paymentRepository.findByRequestId(requestId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Payment makePayment(Payment payment) {
        Optional<Payment> existingPayment = this.retrievePaymentByRequestId(payment.getRequestId());

        if(existingPayment.isPresent()) {
            throw new ExistingPaymentException(ExceptionMessages.EXISTING_PAYMENT_EXCEPTION_MESSAGE,
                    ExceptionMessages.EXISTING_PAYMENT_EXCEPTION_ERROR, payment.getRequestId());
        }

        authorisePayment(payment);

        return paymentRepository.save(payment);
    }

    private void authorisePayment(Payment payment) {
        AcquirerRequestDTO acquirerRequestDTO = buildAcquiringBankRequest(payment);
        AcquirerResponseDTO acquirerResponseDTO = sendPaymentToAcquiringBank(acquirerRequestDTO);

        payment.setStatus(acquirerResponseDTO.getStatus());
        payment.setDescription(acquirerResponseDTO.getDescription());
        payment.setAuthorisationCode(acquirerResponseDTO.getAuthorisationCode());
    }

    private AcquirerRequestDTO buildAcquiringBankRequest(Payment payment) {
        AcquirerRequestDTO request = new AcquirerRequestDTO();

        request.setRequestID(payment.getRequestId());
        request.setMerchantID(payment.getMerchantId());
        request.setAmount(payment.getAmount());
        request.setCurrency(payment.getCurrency());
        request.setCardHolder(payment.getDecodedCardHolder());
        request.setCardNumber(payment.getDecodedCardNumber());
        request.setExpiryDate(payment.getDecodedExpiryDate());
        request.setCvv(payment.getCvv());

        return request;
    }

    private AcquirerResponseDTO sendPaymentToAcquiringBank(AcquirerRequestDTO acquirerRequestDTO) {
        return acquiringBankMock.authorisePayment(acquirerRequestDTO);
    }
}
