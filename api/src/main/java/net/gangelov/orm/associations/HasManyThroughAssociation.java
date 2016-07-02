package net.gangelov.orm.associations;

import net.gangelov.orm.Model;
import net.gangelov.orm.ModelReflection;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class HasManyThroughAssociation<T extends Model, TR extends Model, R extends Model> extends Association<T> {
    private final Class<T> modelClass;
    private final Class<R> refModelClass;
    private final HasManyAssociation<T, TR> hasMany;
    private final String foreignKey;
    private final Field field;

    public HasManyThroughAssociation(
            Class<T> modelClass,
            HasManyAssociation<T, TR> hasMany,
            Class<R> refModelClass,
            String foreignKey,
            Field field
    ) {
        this.modelClass = modelClass;
        this.hasMany = hasMany;
        this.refModelClass = refModelClass;
        this.foreignKey = foreignKey;
        this.field = field;
    }

    public void load(List<T> models) throws SQLException {
        ModelReflection r = ModelReflection.get(modelClass);
        ModelReflection trR = ModelReflection.get(hasMany.refModelClass);
        ModelReflection refR = ModelReflection.get(refModelClass);

        hasMany.load(models);

        List<Object> keys = models.stream()
                .flatMap(model -> {
                    try {
                        return ((Collection<TR>)hasMany.field.get(model)).stream();
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                })
                .map(throughModel -> trR.valueFor(throughModel, foreignKey))
                .distinct()
                .collect(Collectors.toList());

        List<R> refs = refR.db.query().forModel(refModelClass)
                .whereIn(refR.idName, keys)
                .results();


        Map<Object, R> keyToRef = new HashMap<>();

        refs.forEach(ref -> {
            keyToRef.put(refR.valueFor(ref, refR.idName), ref);
        });

        models.forEach(model -> {
            Collection<TR> hasManyModels;
            try {
                hasManyModels = (Collection<TR>)hasMany.field.get(model);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }

            List<Object> keysForModel = hasManyModels.stream()
                    .map(trModel -> trR.valueFor(trModel, foreignKey))
                    .collect(Collectors.toList());

            Set<R> refsForModel = keysForModel.stream()
                    .map(keyToRef::get)
                    .collect(Collectors.toSet());

            try {
                field.set(model, refsForModel);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
