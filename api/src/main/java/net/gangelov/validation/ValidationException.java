package net.gangelov.validation;

import net.gangelov.utils.Strings;

public class ValidationException extends Exception {
    public ValidationErrors errors;

    public ValidationException(ValidationErrors errors) {
        super(errors.toString());

        this.errors = errors;
    }

    public ValidationException(String key, String message) {
        super(Strings.capitalize(key) + " " + message + ".");

        ValidationErrors errors = new ValidationErrors();
        errors.add(key, message);

        this.errors = errors;
    }

    @Override
    public String toString() {
        return errors.toString();
    }
}
