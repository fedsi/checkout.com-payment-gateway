package com.checkout.challenge.paymentgateway.service;

import com.checkout.challenge.paymentgateway.entity.Payment;
import com.checkout.challenge.paymentgateway.error.exception.ExistingPaymentException;

import java.util.Optional;
import java.util.UUID;

public interface PaymentGatewayService {

    /**
     * Return a {@link Payment} filtered by paymentId.
     *
     * @return {@link Payment}
     */
    Optional<Payment> retrievePayment(UUID id);

    /**
     * Return a {@link Payment} filtered by requestId.
     *
     * @return {@link Payment}
     */
    Optional<Payment> retrievePaymentByRequestId(String requestId);

    /**
     * Create a {@link Payment}
     *
     * @return the created {@link Payment}
     * @throws ExistingPaymentException When a payment with the same requestId already exist
     */
    Payment makePayment(Payment payment);

}
