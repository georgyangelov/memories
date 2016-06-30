package net.gangelov.validation;

public class ValidationException extends Exception {
    public ValidationErrors errors;

    public ValidationException(ValidationErrors errors) {
        super(errors.toString());

        this.errors = errors;
    }

    @Override
    public String toString() {
        return errors.toString();
    }
}
