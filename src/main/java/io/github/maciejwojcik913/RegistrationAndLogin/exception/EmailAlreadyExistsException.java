package io.github.maciejwojcik913.RegistrationAndLogin.exception;

/**
 * TODO raised....
 */
public class EmailAlreadyExistsException extends RegistrationException{

    public EmailAlreadyExistsException(String message) {
        super(message);
    }
    public EmailAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}
