package com.checkout.challenge.paymentgateway.dto.acquirer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AcquirerRequestDTO {

    private String requestID;

    private String merchantID;

    private BigDecimal amount;

    private String currency;

    private String cardHolder;

    private String cardNumber;

    private String expiryDate;

    private String cvv;


}
