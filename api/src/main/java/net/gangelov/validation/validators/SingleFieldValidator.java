package net.gangelov.validation.validators;

import net.gangelov.validation.ValidationErrors;
import net.gangelov.validation.Validator;

import java.util.function.Function;

abstract public class SingleFieldValidator<T, E> extends Validator<T> {
    protected String fieldName;
    protected Function<T, E> accessor;

    public SingleFieldValidator(String fieldName, Function<T, E> accessor) {
        super();

        this.fieldName = fieldName;
        this.accessor = accessor;
    }

    protected abstract void validateField(T object, E value, ValidationErrors errors);

    public ValidationErrors validate(T object) {
        ValidationErrors errors = new ValidationErrors();
        E value = accessor.apply(object);

        validateField(object, value, errors);

        return errors;
    }
}
