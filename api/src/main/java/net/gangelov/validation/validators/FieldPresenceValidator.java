package net.gangelov.validation.validators;

import net.gangelov.utils.Strings;
import net.gangelov.validation.ValidationErrors;

import java.util.function.Function;

public class FieldPresenceValidator<T> extends SingleFieldValidator<T, Object> {
    private String message;

    public FieldPresenceValidator(String key, Function<T, Object> accessor) {
        super(key, accessor);

        this.message = "is not present";
    }

    public FieldPresenceValidator(String key, Function<T, Object> accessor, String message) {
        super(key, accessor);

        this.message = message;
    }

    @Override
    protected void validateField(T object, Object value, ValidationErrors errors) {
        if (value instanceof String) {
            if (Strings.isBlank((String)value)) {
                errors.add(fieldName, message);
            }
        } else {
            if (value == null) {
                errors.add(fieldName, message);
            }
        }
    }
}
