package io.github.maciejwojcik913.RegistrationAndLogin.validation;

import javax.validation.ConstraintDefinitionException;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * TODO
 */
public class MatchingFieldsValidator implements ConstraintValidator<MatchingFields, Object> {

    String[] fields;
    String message;

    @Override
    public void initialize(MatchingFields constraintAnnotation) {
        this.fields = constraintAnnotation.fields();
        this.message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {

        if (fields.length == 0) {
            return true;
        }

        String className = value.getClass().getSimpleName();
        ArrayList<Object> toCompare = new ArrayList<>();

        Arrays.stream(fields).forEach(f -> {
                    Field tempField;
                    try {
                        tempField = value.getClass().getDeclaredField(f);
                    } catch (NoSuchFieldException e) {
                        throw new ConstraintDefinitionException(className + " does not contain field with name: " + f);
                    }

                    tempField.setAccessible(true);
                    Object tempObject;
                    try {
                        tempObject = tempField.get(value);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException("Could not access field with name: " + tempField.getName());
                    }

                    toCompare.add(tempObject);
        }
        );

        for (Object o : toCompare) {
            if ( (o == null) || (!o.equals(toCompare.get(0))) ) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
                return false;
            }
        }

        return true;
    }
}
