package net.gangelov.validation;

abstract public class Validator<T> {
    public abstract ValidationErrors validate(T object);

    public void ensureValid(T object) throws ValidationException {
        ValidationErrors errors = validate(object);

        if (errors.hasAny()) {
            throw new ValidationException(errors);
        }
    }
}
