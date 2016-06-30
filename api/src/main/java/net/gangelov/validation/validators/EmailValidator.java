package net.gangelov.validation.validators;

import net.gangelov.utils.Strings;
import net.gangelov.validation.ValidationErrors;

import java.util.function.Function;

public class EmailValidator<T> extends SingleFieldValidator<T, String> {
    private static final String EMAIL_PATTERN = "[a-zA-Z0-9\\.\\-\\+]+@[a-zA-Z0-9_\\-]+\\.[a-zA-Z0-9]+";

    public EmailValidator(String key, Function<T, String> accessor) {
        super(key, accessor);
    }

    @Override
    protected void validateField(T object, String value, ValidationErrors errors) {
        if (!value.matches(EMAIL_PATTERN)) {
            errors.add(fieldName, "is not a valid email");
        }
    }
}
