package com.checkout.challenge.paymentgateway.validation.card;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.Annotation;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class CardNumberValidatorTest {

    private static final String MESSAGE = "message";

    @InjectMocks
    private CardNumberValidator validator;

    @Mock
    private ConstraintValidatorContext context;

    @Mock
    private ConstraintValidatorContext.ConstraintViolationBuilder builder;

    @BeforeEach
    public void setup() {
        given(context.buildConstraintViolationWithTemplate(MESSAGE)).willReturn(builder);
        CardNumber annotation = new CardNumber() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return null;
            }

            @Override
            public String message() {
                return MESSAGE;
            }

            @Override
            public Class<?>[] groups() {
                return new Class[0];
            }

            @Override
            public Class<? extends Payload>[] payload() {
                return new Class[0];
            }
        };

        validator.initialize(annotation);
    }

    @Test
    public void notValid_nullCardNumber() {
        assertThat(validator.isValid(null, context), is(false));
        thenVerifyConstraintViolation();
    }

    @Test
    public void notValid_emptyCardNumber() {
        assertThat(validator.isValid("", context), is(false));
        thenVerifyConstraintViolation();
    }

    @Test
    public void notValid_failedLuhnCheck() {
        assertThat(validator.isValid("1234567890123456", context), is(false));
        thenVerifyConstraintViolation();
    }

    @Test
    public void valid_passLuhnCheck() {
        assertThat(validator.isValid("5555555555554444", context), is(true));
        verifyNoMoreInteractions(context);
    }

    private void thenVerifyConstraintViolation() {
        verify(context).disableDefaultConstraintViolation();
        verify(context).buildConstraintViolationWithTemplate(MESSAGE);
        verifyNoMoreInteractions(context);
        verify(builder).addConstraintViolation();
        verifyNoMoreInteractions(builder);
    }
}