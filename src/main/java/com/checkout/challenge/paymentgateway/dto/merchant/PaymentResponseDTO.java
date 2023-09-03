package com.checkout.challenge.paymentgateway.dto.merchant;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PaymentResponseDTO {

    private UUID paymentId;

    private String requestId;

    private String merchantId;

    private BigDecimal amount;

    private String currency;

    private String cardHolder;

    private String cardNumber;

    private String expiryDate;

    private String cvv;

    private String status;

    private String description;

    private String authorisationCode;
}
