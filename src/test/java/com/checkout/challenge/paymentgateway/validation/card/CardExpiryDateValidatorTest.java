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
import java.util.Date;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class CardExpiryDateValidatorTest {

    private static final String MESSAGE = "message";

    @InjectMocks
    private CardExpiryDateValidator validator;

    @Mock
    private ConstraintValidatorContext context;

    @Mock
    private ConstraintValidatorContext.ConstraintViolationBuilder builder;

    @BeforeEach
    public void setup() {
        given(context.buildConstraintViolationWithTemplate(MESSAGE)).willReturn(builder);
        CardExpiryDate annotation = new CardExpiryDate() {
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
    public void notValid_nullExpiryDate() {
        assertThat(validator.isValid(null, context), is(false));
        thenVerifyConstraintViolation();
    }

    @Test
    public void notValid_emptyExpiryDate() {
        assertThat(validator.isValid("", context), is(false));
        thenVerifyConstraintViolation();
    }

    @Test
    public void notValid_datePastCurrentMonthYear() {
        assertThat(validator.isValid("08/23", context), is(false));
        thenVerifyConstraintViolation();
    }

    @Test
    public void notValid_dateEqualCurrentMonthYear() {
        assertThat(validator.isValid(new Date().toString(), context), is(false));
        thenVerifyConstraintViolation();
    }

    @Test
    public void valid_dateAfterCurrentMonthYear() {
        assertThat(validator.isValid("09/27", context), is(true));
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