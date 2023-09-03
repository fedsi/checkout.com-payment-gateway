package com.checkout.challenge.paymentgateway.validation;

import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;

/**
 * Used to validate a field with a valid ISO4217 alphabetic currency code.
 * The list of valid currencies is loaded from the file iso4217-currency-codes.txt on the classpath.
 */
@Component
public class CurrencyCodeIso4217Validator implements ConstraintValidator<CurrencyCodeIso4217, String> {

    private static final Set<String> ACCEPTED_CURRENCY_CODES;

    static {
        try {
            Set<String> codes = new HashSet<>();
            InputStream is = CurrencyCodeIso4217.class.getResourceAsStream("/iso4217-currency-codes.txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                codes.add(line);
            }
            ACCEPTED_CURRENCY_CODES = codes;
        } catch (IOException e) {
            throw new RuntimeException("Unable to read currency codes file", e);
        }
    }

    private String message;

    @Override
    public void initialize(CurrencyCodeIso4217 constraintAnnotation) { message = constraintAnnotation.message(); }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        boolean valid = (value != null && ACCEPTED_CURRENCY_CODES.contains(value));

        if (!valid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(message)
                    .addConstraintViolation();
        }
        return valid;
    }
}
