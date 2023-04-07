package io.github.maciejwojcik913.RegistrationAndLogin.exception;

/**
 * TODO raised....
 */
public class UsernameAlreadyExistsException extends RegistrationException {

    public UsernameAlreadyExistsException(String message) {
        super(message);
    }
    public UsernameAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}
