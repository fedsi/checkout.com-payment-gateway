package com.checkout.challenge.paymentgateway.error.exception;

/**
 * This exception will be thrown when a payment with the specified id
 * already exist.
 */
public class ExistingPaymentException extends PaymentGatewayException {

    String paymentId;

    public ExistingPaymentException(String message, String error, String paymentId) {
        super(message, error);
        this.paymentId = paymentId;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }
}