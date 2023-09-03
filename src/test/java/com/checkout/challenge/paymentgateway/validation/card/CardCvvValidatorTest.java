package com.checkout.challenge.paymentgateway.validation.card;

import com.checkout.challenge.paymentgateway.validation.CurrencyCodeIso4217;
import com.checkout.challenge.paymentgateway.validation.CurrencyCodeIso4217Validator;
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
class CardCvvValidatorTest {

    private static final String MESSAGE = "message";

    @InjectMocks
    private CardCvvValidator validator;

    @Mock
    private ConstraintValidatorContext context;

    @Mock
    private ConstraintValidatorContext.ConstraintViolationBuilder builder;

    @BeforeEach
    public void setup() {
        given(context.buildConstraintViolationWithTemplate(MESSAGE)).willReturn(builder);
        CardCvv annotation = new CardCvv() {
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
    public void notValid_nullCvv() {
        assertThat(validator.isValid(null, context), is(false));
        thenVerifyConstraintViolation();
    }

    @Test
    public void notValid_emptyCvv() {
        assertThat(validator.isValid("", context), is(false));
        thenVerifyConstraintViolation();
    }

    @Test
    public void notValid_lessThan3Digit() {
        assertThat(validator.isValid("12", context), is(false));
        thenVerifyConstraintViolation();
    }

    @Test
    public void notValid_moreThan4Digit() {
        assertThat(validator.isValid("12345", context), is(false));
        thenVerifyConstraintViolation();
    }

    @Test
    public void notValid_containsChar() {
        assertThat(validator.isValid("12a", context), is(false));
        thenVerifyConstraintViolation();
    }

    @Test
    public void notValid_containsSpecialChar() {
        assertThat(validator.isValid("12%", context), is(false));
        thenVerifyConstraintViolation();
    }

    @Test
    public void valid_validCvv3Digit() {
        assertThat(validator.isValid("123", context), is(true));
        verifyNoMoreInteractions(context);
    }

    @Test
    public void valid_validCvv4Digit() {
        assertThat(validator.isValid("1234", context), is(true));
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