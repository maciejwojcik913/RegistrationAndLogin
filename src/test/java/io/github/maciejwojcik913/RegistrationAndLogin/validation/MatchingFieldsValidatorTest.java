package io.github.maciejwojcik913.RegistrationAndLogin.validation;

import org.junit.jupiter.api.Test;

import javax.validation.*;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;

class MatchingFieldsValidatorTest {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void shouldThrow_ValidationException_causedBy_ConstraintDefinitionException_ifClassDoesNotContainFieldWithDefinedName() {
        @MatchingFields(name = "Single Field Matcher", fields = "wrongDefinition")
        class OneFieldClass { private final String singleField = "some value"; }

        var toValidate = new OneFieldClass();

        // when then
        assertThatThrownBy( () -> validator.validate(toValidate) )
                .isInstanceOf(ValidationException.class)
                .getCause().isInstanceOf(ConstraintDefinitionException.class)
                .hasMessageContaining("OneFieldClass does not contain field with name: wrongDefinition");
    }

    @Test
    void validator_shouldNotReturnViolationsWhenOnlyOneFieldIsDefined() {
        // given
        @MatchingFields(name = "Single Field Matcher", fields = "singleField")
        class OneFieldClass { private final String singleField = "some value"; }

        var toValidate = new OneFieldClass();

        // when
        List<ConstraintViolation<OneFieldClass>> violations = new ArrayList<>( validator.validate(toValidate) );

        // then
        assertThat(violations, hasSize(0));
    }

    @Test
    void validator_shouldNotReturnViolationsWhenFieldsMatchToEachOther() {
        // given
        @MatchingFields(name = "Emails", fields = {"email", "emailConf"})
        class MatchingEmailsClass {
            private final String email = "secret";
            private final String emailConf = "secret";
        }
        var toValidate = new MatchingEmailsClass();

        // when
        List<ConstraintViolation<MatchingEmailsClass>> violations = new ArrayList<>( validator.validate(toValidate) );

        // then
        assertThat(violations, hasSize(0));
    }

    @Test
    void validator_shouldNotReturnViolationsIfAllAreNulls() {
        // given
        @MatchingFields(name = "Nulls", fields = {"null1", "null2", "null3"})
        class NullObjectsClass {
            private final Object null1 = null;
            private final Object null2 = null;
            private final Object null3 = null;
        }
        var toValidate = new NullObjectsClass();

        // when
        List<ConstraintViolation<NullObjectsClass>> violations = new ArrayList<>( validator.validate(toValidate) );

        // then
        assertThat(violations, hasSize(0));
    }

    @Test
    void validator_shouldReturnViolationsWhenFieldsDoNotMatchToEachOther() {
        // given
        @MatchingFields(name = "Emails", fields = {"email", "emailConf"})
        class NotMatchingEmailsClass {
            private final String email = "secret";
            private final String emailConf = "secretx";
        }
        var toValidate = new NotMatchingEmailsClass();

        // when
        List<ConstraintViolation<NotMatchingEmailsClass>> violations = new ArrayList<>( validator.validate(toValidate) );

        // then
        assertAll(
                () -> assertThat(violations, hasSize(1)),
                () -> assertThat(violations.get(0).getMessage(), containsString("MatchingFields: (Emails) - don't match to each other."))
        );
    }

    @Test
    void validator_shouldAllowCheckingDifferentGroupsOfFields_and_notReturnViolationsIfEachGroupIsValid() {
        // given
        @MatchingFieldsGroups(value = {
                @MatchingFields(name = "Emails", fields = {"email", "emailConfirmation"}),
                @MatchingFields(name = "Pin numbers", fields = {"pin", "pinConfirmation"})
        })
        class ValidMatchingFieldsGroups {
            private final String email = "test@email.te";
            private final String emailConfirmation = "test@email.te";
            private final int pin = 12345678;
            private final int pinConfirmation = 12345678;
        }
        var toValidate = new ValidMatchingFieldsGroups();

        // when
        List<ConstraintViolation<ValidMatchingFieldsGroups>> violations = new ArrayList<>( validator.validate(toValidate) );

        // then
        assertThat(violations, hasSize(0));
    }

    @Test
    void validator_shouldAllowCheckingDifferentGroupsOfFields_and_returnViolationsIfAnyOfGroupsIsNotValid() {
        // given
        @MatchingFieldsGroups(value = {
                @MatchingFields(name = "Emails", fields = {"email", "emailConfirmation"}),
                @MatchingFields(name = "Pin numbers", fields = {"pin", "pinConfirmation"}),
                @MatchingFields(name = "Different names", fields = {"firstname", "lastname"})
        })
        class ValidMatchingFieldsGroups {
            private final String email = "test@email.te";
            private final String emailConfirmation = "test@email.te";
            private final int pin = 12345678;
            private final int pinConfirmation = 12345678;
            private final String firstname = "John";
            private final String lastname = "Doe";
        }
        var toValidate = new ValidMatchingFieldsGroups();

        // when
        List<ConstraintViolation<ValidMatchingFieldsGroups>> violations = new ArrayList<>( validator.validate(toValidate) );

        // then
        assertAll(
                () -> assertThat(violations, hasSize(1)),
                () -> assertThat(violations.get(0).getMessage(), containsString("MatchingFields: (Different names) - don't match to each other."))
        );
    }
}