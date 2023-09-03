package com.checkout.challenge.paymentgateway.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Used to validate a field with a valid ISO4217 alphabetic currency code.
 */
@Target({FIELD, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = { CurrencyCodeIso4217Validator.class })
@Documented
public @interface CurrencyCodeIso4217 {

    String message();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
