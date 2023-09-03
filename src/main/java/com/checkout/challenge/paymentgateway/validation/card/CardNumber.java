package com.checkout.challenge.paymentgateway.validation.card;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Used to validate a field with a valid card number.
 */
@Target({FIELD, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = { CardNumberValidator.class })
@Documented
public @interface CardNumber {
    String message();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
