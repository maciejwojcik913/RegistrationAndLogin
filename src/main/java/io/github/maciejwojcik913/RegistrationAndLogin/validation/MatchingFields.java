package io.github.maciejwojcik913.RegistrationAndLogin.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * TODO
 */
@Repeatable(MatchingFieldsGroups.class)
@Constraint(validatedBy = MatchingFieldsValidator.class)
@Target({ TYPE, ANNOTATION_TYPE })
@Retention(RUNTIME)
public @interface MatchingFields{

    /**
     * Specify an array of field names, that should be validated as equals in annotated class.<br>
     * Example: @MatchingFields(fields = {"password", "passwordConfirmation"})
     */
    String[] fields();

    String name();

    String message() default "MatchingFields: ({name}) - don't match to each other.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
