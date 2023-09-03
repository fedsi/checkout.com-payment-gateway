package com.checkout.challenge.paymentgateway.validation.card;

import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class CardExpiryDateValidator implements ConstraintValidator<CardExpiryDate, String> {

    private String message;

    @Override
    public void initialize(CardExpiryDate constraintAnnotation) { message = constraintAnnotation.message(); }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        boolean valid = (value != null && isValidExpiryDate(value));

        if (!valid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(message)
                    .addConstraintViolation();
        }
        return valid;
    }

    /**
     * A Card Expiry Date is valid if the month and year is after today's month.
     *
     * @param expiryDate - Card Expiry Date
     * @return true if the given expiry date is after today's month
     */
    private boolean isValidExpiryDate(String expiryDate) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/yy");
        simpleDateFormat.setLenient(false);
        Date expiry;
        try {
            expiry  = simpleDateFormat.parse(expiryDate);
        } catch (ParseException parseException) {
            return false;
        }
        return expiry.after(new Date());
    }
}