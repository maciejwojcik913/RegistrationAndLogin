package io.github.maciejwojcik913.RegistrationAndLogin.validation;

import org.junit.jupiter.api.Test;

import javax.validation.constraints.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ManualConstraintValidatorTest {

    @Test
    void isValid_shouldReturnTrueForObjectWithValidValueOfField() {
        // given
        class ValidNotNullField { @NotNull private final int number = 3; }
        var toValidate = new ValidNotNullField();

        // when then
        assertTrue(ManualConstraintValidator.isValid(toValidate));
    }

    @Test
    void isValid_shouldReturnFalseIfObjectHasAnyNotValidField() {
        // given
        class NotValidNotNullField {
            @NotNull private final int number = 3;
            @NotEmpty private final String empty = "";
        }
        var toValidate = new NotValidNotNullField();

        // when then
        assertFalse(ManualConstraintValidator.isValid(toValidate));
    }

    @Test
    void getConstraintViolations_shouldReturnEmptyCollectionIfAllFieldsAreValid() {
        // given
        class ValidNotNullField {
            @AssertFalse private final boolean fls = false;
            @AssertTrue private final boolean tr = true;
            @Negative private final int negative = -5;
            @Min(value = 5) private final int min5 = 7;
            @Size(min = 4, max = 6) private final String length5 = "12345";
        }
        var toValidate = new ValidNotNullField();

        // when then
        assertThat(ManualConstraintValidator.getConstraintViolations(toValidate), hasSize(0));
    }

    @Test
    void getConstraintViolations_shouldReturnCollectionOfViolationsIfFieldsAreNotValid() {
        // given
        class ValidNotNullField {
            @AssertFalse private final boolean fls = true; // opposite
            @AssertTrue private final boolean tr = false; // opposite
            @Negative private final int negative = 5; // not negative
            @Min(value = 5) private final int min5 = 3; // value under 5
            @Size(min = 4, max = 6) private final String length5 = "123"; // length out of range
        }
        var toValidate = new ValidNotNullField();

        // when then
        assertThat(ManualConstraintValidator.getConstraintViolations(toValidate), hasSize(5));
    }
}