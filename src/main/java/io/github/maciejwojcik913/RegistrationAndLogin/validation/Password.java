package io.github.maciejwojcik913.RegistrationAndLogin.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * An interface used for password validation.
 * Included methods allow to restrict password length, number upper case letters and allowance of white spaces.
 * @see PasswordValidator
 */
@Documented
@Constraint(validatedBy = {PasswordValidator.class})
@Retention(RUNTIME)
@Target(FIELD)
public @interface Password {

    int minLength() default 4;

    int maxLength() default 64;

    int minUpperCaseLetters() default 0;

    boolean allowsWhiteSpace() default true;

    String message() default "Invalid password: ";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
