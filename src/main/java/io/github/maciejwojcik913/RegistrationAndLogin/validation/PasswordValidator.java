package io.github.maciejwojcik913.RegistrationAndLogin.validation;

import javax.validation.ConstraintDefinitionException;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Validator implementation for Password annotation.
 * It validates password length, number upper case letters and allowance of white spaces.
 */
public class PasswordValidator implements ConstraintValidator<Password, String> {

    String message;

    int minLength;
    int maxLength;
    int minUpperCaseLetters;
    boolean allowsWhiteSpace;

    /**
     * Initializes variables to be validated and checks if declaration is correct.
     * @param constraintAnnotation annotation instance for a given constraint declaration
     */
    @Override
    public void initialize(Password constraintAnnotation) {
        this.message = constraintAnnotation.message();
        this.minLength = constraintAnnotation.minLength();
        this.maxLength = constraintAnnotation.maxLength();
        this.minUpperCaseLetters = constraintAnnotation.minUpperCaseLetters();
        this.allowsWhiteSpace = constraintAnnotation.allowsWhiteSpace();

        var error = "Constraint definition error: ";

        if (minLength <= 0)
            throw new ConstraintDefinitionException(error + "minLength <= 0");

        if (maxLength <= 0)
            throw new ConstraintDefinitionException(error + "maxLength <= 0");

        if (minLength > maxLength)
            throw new ConstraintDefinitionException(error + "minLength > maxLength");

        if (minUpperCaseLetters < 0)
            throw new ConstraintDefinitionException(error + "minUpperCaseLetters < 0");

    }

    /**
     * Validates annotated value by declared constraints.
     *
     * @param value object to validate
     * @param context context in which the constraint is evaluated
     *
     * @return true if validation is success
     */
    @Override
    public boolean isValid(final String value, ConstraintValidatorContext context) {
        boolean isValid = true;
        context.disableDefaultConstraintViolation();

        if ( value == null || value.isEmpty() ) {
            context.buildConstraintViolationWithTemplate(message + "password cannot be null or empty.").addConstraintViolation();
            return false;
        }

        if ( value.length() < minLength ) {
            context.buildConstraintViolationWithTemplate(message + "password must be at least " + minLength + " long.").addConstraintViolation();
            isValid = false;
        }

        if ( value.length() > maxLength ) {
            context.buildConstraintViolationWithTemplate(message + "password must be at most " + maxLength + " long.").addConstraintViolation();
            isValid = false;
        }

        if ( upperCaseLettersCount(value) < minUpperCaseLetters ) {
            context.buildConstraintViolationWithTemplate(message + "password must contain at least " + minUpperCaseLetters + " upper case letters.").addConstraintViolation();
            isValid = false;
        }

        if ( !allowsWhiteSpace && hasWhiteSpace(value)) {
            context.buildConstraintViolationWithTemplate(message + "password cannot contain white spaces.").addConstraintViolation();
            isValid = false;
        }

        return isValid;
    }

    /**
     * Counts upper case letters.
     */
    private int upperCaseLettersCount(String value) {
        int count = 0;
        for (Character character : value.toCharArray()) {
            if (Character.isUpperCase(character))
                count++;
        }
        return count;
    }

    /**
     * Checks if contains white space character.
     */
    private boolean hasWhiteSpace(String value) {
        for (Character character : value.toCharArray()) {
            if (Character.isWhitespace(character))
                return true;
        }
        return false;
    }
}
