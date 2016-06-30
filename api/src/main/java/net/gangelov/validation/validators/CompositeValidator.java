package net.gangelov.validation.validators;

import net.gangelov.validation.ValidationErrors;
import net.gangelov.validation.Validator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CompositeValidator<T> extends Validator<T> {
    private List<Validator<T>> validators = new ArrayList<>();

    public CompositeValidator(Validator<T>... validators) {
        super();

        this.validators = Arrays.asList(validators);
    }

    @Override
    public ValidationErrors validate(T object) {
        ValidationErrors aggregateErrors = new ValidationErrors();

        validators.stream().forEach(validator -> {
            ValidationErrors localErrors = validator.validate(object);

            aggregateErrors.addFrom(localErrors);
        });

        return aggregateErrors;
    }
}
