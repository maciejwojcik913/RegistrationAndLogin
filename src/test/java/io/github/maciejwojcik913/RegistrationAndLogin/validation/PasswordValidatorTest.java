package io.github.maciejwojcik913.RegistrationAndLogin.validation;

import org.junit.jupiter.api.Test;

import javax.validation.*;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertAll;

class PasswordValidatorTest {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void shouldThrow_ValidationException_causedBy_ConstraintDefinitionException_if_minLength_definitionIsLessThanOne() {
        // given
        class WrongDefinitionTooLowMinLength { @Password(minLength = 0) private String password; }
        var toBeValidated = new WrongDefinitionTooLowMinLength();

        // when then
        assertThatThrownBy( () -> validator.validate(toBeValidated) )
                .isInstanceOf(ValidationException.class)
                .getCause().isInstanceOf(ConstraintDefinitionException.class)
                .hasMessageContaining("minLength <= 0");
    }

    @Test
    void shouldThrow_ValidationException_causedBy_ConstraintDefinitionException_if_maxLength_definitionIsLessThanOne() {
        // given
        class WrongDefinitionTooLowMaxLength { @Password(maxLength = 0) private String password; }
        var toBeValidated = new WrongDefinitionTooLowMaxLength();

        // when then
        assertThatThrownBy( () -> validator.validate(toBeValidated) )
                .isInstanceOf(ValidationException.class)
                .getCause().isInstanceOf(ConstraintDefinitionException.class)
                .hasMessageContaining("maxLength <= 0");
    }

    @Test
    void shouldThrow_ValidationException_causedBy_ConstraintDefinitionException_if_minLength_definitionIsGreaterThan_maxLength_definition() {
        // given
        class WrongDefinitionMinGreaterThanMax { @Password(minLength = 8, maxLength = 4) private String password; }
        var toBeValidated = new WrongDefinitionMinGreaterThanMax();

        // when then
        assertThatThrownBy( () -> validator.validate(toBeValidated) )
                .isInstanceOf(ValidationException.class)
                .getCause().isInstanceOf(ConstraintDefinitionException.class)
                .hasMessageContaining("minLength > maxLength");
    }

    @Test
    void shouldThrow_ValidationException_causedBy_ConstraintDefinitionException_if_minUpperCaseLetters_isLessThanZero() {
        // given
        class WrongDefinitionTooLowMinUpperCaseLetters { @Password(minUpperCaseLetters = -1) private String password; }
        var toBeValidated = new WrongDefinitionTooLowMinUpperCaseLetters();

        // when then
        assertThatThrownBy( () -> validator.validate(toBeValidated) )
                .isInstanceOf(ValidationException.class)
                .getCause().isInstanceOf(ConstraintDefinitionException.class)
                .hasMessageContaining("minUpperCaseLetters < 0");
    }

    @Test
    void validator_shouldReturnViolationWhenFieldIsNull() {
        // given
        class NullPasswordClass { @Password private final String password = null; }
        var toValidate = new NullPasswordClass();

        // when
        List<ConstraintViolation<NullPasswordClass>> violations = new ArrayList<>( validator.validate(toValidate) );

        // then
        assertAll(
                () -> assertThat(violations, hasSize(1)),
                () -> assertThat(violations.get(0).getMessage(), containsString("password cannot be null or empty"))
        );
    }

    @Test
    void validator_shouldReturnViolationWhenFieldIsEmpty() {
        // given
        class EmptyPasswordClass { @Password private final String password = ""; }
        var toValidate = new EmptyPasswordClass();

        // when
        List<ConstraintViolation<EmptyPasswordClass>> violations = new ArrayList<>( validator.validate(toValidate) );

        // then
        assertAll(
                () -> assertThat(violations, hasSize(1)),
                () -> assertThat(violations.get(0).getMessage(), containsString("password cannot be null or empty"))
        );
    }

    @Test
    void validator_shouldReturnViolationWhenFieldLengthIsLesserThan_minLength() {
        // given
        class MinLengthPasswordClass { @Password(minLength = 9) private final String password = "password"; }
        var toValidate = new MinLengthPasswordClass();

        // when
        List<ConstraintViolation<MinLengthPasswordClass>> violations = new ArrayList<>( validator.validate(toValidate) );

        // then
        assertAll(
                () -> assertThat(violations, hasSize(1)),
                () -> assertThat(violations.get(0).getMessage(), containsString("password must be at least 9 long"))
        );
    }

    @Test
    void validator_shouldNotReturnViolationWhenFieldLengthIsEqualTo_minLength() {
        // given
        class MinLengthPasswordClass { @Password(minLength = 8) private final String password = "password"; }
        var toValidate = new MinLengthPasswordClass();

        // when
        List<ConstraintViolation<MinLengthPasswordClass>> violations = new ArrayList<>( validator.validate(toValidate) );

        // then
        assertThat(violations, hasSize(0));
    }

    @Test
    void validator_shouldNotReturnViolationWhenFieldLengthIsGreaterThan_minLength() {
        // given
        class MinLengthPasswordClass { @Password(minLength = 7) private final String password = "password"; }
        var toValidate = new MinLengthPasswordClass();

        // when
        List<ConstraintViolation<MinLengthPasswordClass>> violations = new ArrayList<>( validator.validate(toValidate) );

        // then
        assertThat(violations, hasSize(0));
    }

    @Test
    void validator_shouldReturnViolationWhenFieldLengthIsMoreThan_maxLength() {
        // given
        class MaxLengthPasswordClass { @Password(maxLength = 7) private final String password = "password"; }
        var toValidate = new MaxLengthPasswordClass();

        // when
        List<ConstraintViolation<MaxLengthPasswordClass>> violations = new ArrayList<>( validator.validate(toValidate) );

        // then
        assertAll(
                () -> assertThat(violations, hasSize(1)),
                () -> assertThat(violations.get(0).getMessage(), containsString("password must be at most 7 long"))
        );
    }

    @Test
    void validator_shouldNotReturnViolationWhenFieldLengthIsEqualTo_maxLength() {
        // given
        class MaxLengthPasswordClass { @Password(maxLength = 8) private final String password = "password"; }
        var toValidate = new MaxLengthPasswordClass();

        // when
        List<ConstraintViolation<MaxLengthPasswordClass>> violations = new ArrayList<>( validator.validate(toValidate) );

        // then
        assertThat(violations, hasSize(0));
    }

    @Test
    void validator_shouldNotReturnViolationWhenFieldLengthIsLesserThan_maxLength() {
        // given
        class MaxLengthPasswordClass { @Password(maxLength = 9) private final String password = "password"; }
        var toValidate = new MaxLengthPasswordClass();

        // when
        List<ConstraintViolation<MaxLengthPasswordClass>> violations = new ArrayList<>( validator.validate(toValidate) );

        // then
        assertThat(violations, hasSize(0));
    }

    @Test
    void validator_shouldReturnViolationWhenFieldHasLessUpperCaseLettersThan_minUpperCaseLetters() {
        // given
        class MinUpperCaseLettersPasswordClass { @Password(minUpperCaseLetters = 3) private final String password = "somePassWord"; }
        var toValidate = new MinUpperCaseLettersPasswordClass();

        // when
        List<ConstraintViolation<MinUpperCaseLettersPasswordClass>> violations = new ArrayList<>( validator.validate(toValidate) );

        // then
        assertAll(
                () -> assertThat(violations, hasSize(1)),
                () -> assertThat(violations.get(0).getMessage(), containsString("password must contain at least 3 upper case letters"))
        );

    }

    @Test
    void validator_shouldNotReturnViolationWhenFieldHasSameUpperCaseLettersCountAs_minUpperCaseLetters() {
        // given
        class MinUpperCaseLettersPasswordClass { @Password(minUpperCaseLetters = 3) private final String password = "SomePassWord"; }
        var toValidate = new MinUpperCaseLettersPasswordClass();

        // when
        List<ConstraintViolation<MinUpperCaseLettersPasswordClass>> violations = new ArrayList<>( validator.validate(toValidate) );

        // then
        assertThat(violations, hasSize(0));

    }

    @Test
    void validator_shouldNotReturnViolationWhenFieldHasMoreUpperCaseLettersThan_minUpperCaseLetters() {
        // given
        class MinUpperCaseLettersPasswordClass { @Password(minUpperCaseLetters = 2) private final String password = "SomePassWord"; }
        var toValidate = new MinUpperCaseLettersPasswordClass();

        // when
        List<ConstraintViolation<MinUpperCaseLettersPasswordClass>> violations = new ArrayList<>( validator.validate(toValidate) );

        // then
        assertThat(violations, hasSize(0));

    }
    
    @Test
    void validator_shouldReturnViolationIfWhiteSpacesAreNotAllowedButPasswordContainsSome() {
        // given // Password validator does not allow white spaces by default.
        class AllowsWhiteSpacesPasswordClass { @Password private final String password = "pass word"; }
        var toValidate = new AllowsWhiteSpacesPasswordClass();

        // when
        List<ConstraintViolation<AllowsWhiteSpacesPasswordClass>> violations = new ArrayList<>( validator.validate(toValidate) );

        // then
        assertAll(
                () -> assertThat(violations, hasSize(1)),
                () -> assertThat(violations.get(0).getMessage(), containsString("password cannot contain white spaces"))
        );
    }

    @Test
    void validator_shouldNotReturnViolationIfWhiteSpacesAreAllowedAndPasswordContainsSome() {
        // given
        class AllowsWhiteSpacesPasswordClass { @Password(allowsWhiteSpace = true) private final String password = "pass word"; }
        var toValidate = new AllowsWhiteSpacesPasswordClass();

        // when
        List<ConstraintViolation<AllowsWhiteSpacesPasswordClass>> violations = new ArrayList<>( validator.validate(toValidate) );

        // then
        assertThat(violations, hasSize(0));
    }

    @Test
    void validator_shouldNotReturnViolationsIfPasswordMeetAllRequirements() {
        // given
        class MixedRequirementsPasswordClass { @Password(minLength = 8, maxLength = 8, minUpperCaseLetters = 2) private final String password = "PassWord"; }
        var toValidate = new MixedRequirementsPasswordClass();

        // when
        List<ConstraintViolation<MixedRequirementsPasswordClass>> violations = new ArrayList<>( validator.validate(toValidate) );

        // then
        assertThat(violations, hasSize(0));
    }

    @Test
    void validator_shouldReturnViolationsIfPasswordDoesNotMeetAtLeastOneRequirement() {
        // given // Password validator does not allow white spaces by default.
        class MixedRequirementsPasswordClass { @Password(minLength = 8, maxLength = 8, minUpperCaseLetters = 2) private final String password = "Pas Word"; }
        var toValidate = new MixedRequirementsPasswordClass();

        // when
        List<ConstraintViolation<MixedRequirementsPasswordClass>> violations = new ArrayList<>( validator.validate(toValidate) );

        // then
        assertThat(violations, hasSize(1));
    }

    @Test
    void validator_shouldReturnCorrectNumberOfViolations() {
        // given // Password validator does not allow white spaces by default.
        class MixedRequirementsPasswordClass { @Password(maxLength = 8, minUpperCaseLetters = 3) private final String password = "Hello Password"; }
        var toValidate = new MixedRequirementsPasswordClass();

        // when
        List<ConstraintViolation<MixedRequirementsPasswordClass>> violations = new ArrayList<>( validator.validate(toValidate) );

        // then
        assertThat(violations, hasSize(3));
    }

    @Test
    void validator_shouldLetCheckOnManyFieldsInClassAndReturnAllViolations() {
        // given
        class ManyPasswords {
            @Password private final String pass1 = null;
            @Password private final String pass2 = "";
            @Password(maxLength = 5) private final String pass3 = "xyz";
            @Password(maxLength = 5) private final String pass4 = "more_than_5";
            @Password(minUpperCaseLetters = 2) private final String pass5 = "One_upper_case_letter";
            @Password private final String pass6 = "with white spaces";
        }
        var toValidate = new ManyPasswords();

        // when
        List<ConstraintViolation<ManyPasswords>> violations = new ArrayList<>( validator.validate(toValidate) );

        // then
        assertThat(violations, hasSize(6));
    }
}