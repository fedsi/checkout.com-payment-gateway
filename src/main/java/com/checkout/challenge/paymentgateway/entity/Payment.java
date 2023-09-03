package com.checkout.challenge.paymentgateway.entity;

import com.checkout.challenge.paymentgateway.common.Encoder;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Payment {

    @Id
    @Column(columnDefinition = "UUID")
    private UUID paymentId;

    private String requestId;

    private String merchantId;

    private BigDecimal amount;

    private String currency;

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private String cardHolder;

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private String cardNumber;

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private String expiryDate;

    /**
     * To be PCI-compliant we can't store CVV info
     */
    @Transient
    private String cvv;

    private String status;

    private String description;

    private String authorisationCode;

    public void setCardHolder(String cardHolder) {
        this.cardHolder = Encoder.encodeData(cardHolder);
    }

    public String getCardHolder() {
        return this.cardHolder;
    }

    public String getDecodedCardHolder() {
        return Encoder.decodeData(this.cardHolder);
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = Encoder.encodeData(cardNumber);
    }

    public String getCardNumber() {
        return this.cardNumber;
    }

    public String getDecodedCardNumber() {
        return Encoder.decodeData(this.cardNumber);
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = Encoder.encodeData(expiryDate);
    }

    public String getExpiryDate() {
        return this.expiryDate;
    }

    public String getDecodedExpiryDate() {
        return Encoder.decodeData(this.expiryDate);
    }
}
