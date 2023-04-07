package io.github.maciejwojcik913.RegistrationAndLogin.validation;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

/**
 * Class lets manually validate object which uses ConstraintValidator
 */
public class ManualConstraintValidator {

    public static boolean isValid(Object toValidate) {
        return getConstraintViolations(toValidate).size() == 0;
    }

    public static Set<ConstraintViolation<Object>> getConstraintViolations(Object toValidate) {
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        return validator.validate(toValidate);
    }
}
