package com.checkout.challenge.paymentgateway.error.exception;

public class PaymentGatewayException extends RuntimeException {
    String error;

    public PaymentGatewayException(String message, String error) {
        super(message);
        this.error = error;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

}
