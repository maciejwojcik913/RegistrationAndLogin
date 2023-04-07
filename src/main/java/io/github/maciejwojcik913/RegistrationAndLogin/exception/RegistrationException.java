package io.github.maciejwojcik913.RegistrationAndLogin.exception;

public class RegistrationException extends BusinessException{

    public RegistrationException(String message) {
        super(message);
    }

    public RegistrationException(String message, Throwable cause) {
        super(message, cause);
    }
}
