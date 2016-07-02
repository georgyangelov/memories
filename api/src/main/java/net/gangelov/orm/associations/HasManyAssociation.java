package net.gangelov.orm.associations;

import net.gangelov.orm.Model;
import net.gangelov.orm.ModelReflection;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class HasManyAssociation<T extends Model, R extends Model> extends Association<T> {
    public final Class<T> modelClass;
    public final Class<R> refModelClass;
    public final String foreignKey;
    public final Field field;

    public HasManyAssociation(Class<T> modelClass, Class<R> refModelClass, String foreignKey, Field field) {
        this.modelClass = modelClass;
        this.refModelClass = refModelClass;
        this.foreignKey = foreignKey;
        this.field = field;
    }

    public void load(List<T> models) throws SQLException {
        ModelReflection r = ModelReflection.get(modelClass);
        ModelReflection refR = ModelReflection.get(refModelClass);

        List<Object> modelIds = models.stream()
                .map(model -> r.valueFor(model, r.idName))
                .distinct()
                .collect(Collectors.toList());

        List<R> refs = r.db.query().forModel(refModelClass).whereIn(foreignKey, modelIds).results();

        models.forEach(model -> {
            Object modelId = r.valueFor(model, r.idName);
            List<R> refsForModel = refs.stream()
                    .filter(ref -> refR.valueFor(ref, foreignKey) == modelId)
                    .collect(Collectors.toList());

            try {
                field.set(model, refsForModel);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        });
    }
}