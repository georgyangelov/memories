package net.gangelov.orm.validators;

import net.gangelov.orm.Model;
import net.gangelov.orm.ModelReflection;
import net.gangelov.validation.ValidationErrors;
import net.gangelov.validation.validators.SingleFieldValidator;

import java.sql.SQLException;
import java.util.function.Function;

public class FieldUniquenessValidator<T extends Model> extends SingleFieldValidator<T, Object> {
    private Class<T> modelClass;

    public FieldUniquenessValidator(Class<T> modelClass, String fieldName) {
        super(
                fieldName,
                (object) -> ModelReflection.get(modelClass).valueFor(object, fieldName)
        );

        this.modelClass = modelClass;
    }

    @Override
    protected void validateField(T object, Object value, ValidationErrors errors) {
        ModelReflection r = ModelReflection.get(modelClass);

        try {
            boolean hasDuplicateRecord = r.db.query().forModel(modelClass)
                    .where(fieldName, value)
                    .exists();

            if (hasDuplicateRecord) {
                errors.add(fieldName, "is not unique");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
