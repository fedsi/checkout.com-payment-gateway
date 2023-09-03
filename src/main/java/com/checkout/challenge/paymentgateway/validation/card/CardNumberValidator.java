package com.checkout.challenge.paymentgateway.validation.card;

import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
public class CardNumberValidator implements ConstraintValidator<CardNumber, String> {

    private String message;

    @Override
    public void initialize(CardNumber constraintAnnotation) { message = constraintAnnotation.message(); }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        boolean valid = (value != null && checkLuhn(value));

        if (!valid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(message)
                    .addConstraintViolation();
        }
        return valid;
    }

    private boolean checkLuhn(String cardNumber) {
        if(cardNumber.length() == 0)
            return false;

        int nDigits = cardNumber.length();

        int nSum = 0;
        boolean isSecond = false;
        for (int i = nDigits - 1; i >= 0; i--)
        {

            int d = cardNumber.charAt(i) - '0';

            if (isSecond == true)
                d = d * 2;

            // We add two digits to handle
            // cases that make two digits
            // after doubling
            nSum += d / 10;
            nSum += d % 10;

            isSecond = !isSecond;
        }
        return (nSum % 10 == 0);
    }
}
