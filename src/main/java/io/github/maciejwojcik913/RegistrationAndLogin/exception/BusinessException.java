package io.github.maciejwojcik913.RegistrationAndLogin.exception;

/**
 * TODO
 */
public class BusinessException extends RuntimeException {

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }
}
