package com.checkout.challenge.paymentgateway.dto.merchant;

import com.checkout.challenge.paymentgateway.validation.CurrencyCodeIso4217;
import com.checkout.challenge.paymentgateway.validation.card.CardCvv;
import com.checkout.challenge.paymentgateway.validation.card.CardExpiryDate;
import com.checkout.challenge.paymentgateway.validation.card.CardNumber;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PaymentRequestDTO {

    @NotNull
    private String requestId;

    @NotNull
    private String merchantId;

    @Min(value = 0, message = "Amount should be greater than 0")
    private BigDecimal amount;

    @CurrencyCodeIso4217(message = "Payment Currency should be a valid ISO4217 alphabetic currency code")
    private String currency;

    @NotNull(message = "Card Holder should not be empty")
    private String cardHolder;

    @CardNumber(message = "Card Number should pass Luhn check")
    private String cardNumber;

    @CardExpiryDate(message = "Card Expiry Date should be after today's month")
    private String expiryDate;

    @CardCvv(message = "Card CVV should be 3 or 4 digits")
    private String cvv;

}
