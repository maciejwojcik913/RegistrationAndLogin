package io.github.maciejwojcik913.RegistrationAndLogin.exception;

/**
 * TODO raised....
 */
public class UserRegistrationFormValidationException extends BusinessException{

    public UserRegistrationFormValidationException(String message) {
        super(message);
    }
    public UserRegistrationFormValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
