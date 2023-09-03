package com.checkout.challenge.paymentgateway.validation.card;

import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class CardCvvValidator implements ConstraintValidator<CardCvv, String> {

    private String message;

    @Override
    public void initialize(CardCvv constraintAnnotation) { message = constraintAnnotation.message(); }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        boolean valid = (value != null && isValidCvv(value));

        if (!valid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(message)
                    .addConstraintViolation();
        }
        return valid;
    }

    /**
     * A valid CVV (Card Verification Value) number must satisfy the following conditions:
     *
     * It should have 3 or 4 digits.
     * It should have a digit between 0-9.
     * It should not have any alphabet or special characters.
     * @param cvv - Card Verification Value
     * @return true if the CVV matches the conditions
     */
    private boolean isValidCvv(String cvv) {
        String regex = "^[0-9]{3,4}$";

        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(cvv);
        return m.matches();
    }
}