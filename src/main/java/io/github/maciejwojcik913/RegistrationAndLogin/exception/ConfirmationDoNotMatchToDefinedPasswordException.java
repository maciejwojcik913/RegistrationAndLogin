package io.github.maciejwojcik913.RegistrationAndLogin.exception;

/**
 * TODO raised....
 */
public class ConfirmationDoNotMatchToDefinedPasswordException extends RegistrationException{

    public ConfirmationDoNotMatchToDefinedPasswordException(String message) {
        super(message);
    }
    public ConfirmationDoNotMatchToDefinedPasswordException(String message, Throwable cause) {
        super(message, cause);
    }
}
