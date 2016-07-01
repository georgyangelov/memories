package net.gangelov.orm.associations;

import net.gangelov.orm.Model;
import net.gangelov.orm.ModelReflection;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BelongsToAssociation<T extends Model, R extends Model> extends Association {
    private final Class<T> modelClass;
    private final Class<R> refModelClass;
    private final String key;
    private final Field field;

    public BelongsToAssociation(Class<T> modelClass, Class<R> refModelClass, String key, Field field) {
        this.modelClass = modelClass;
        this.refModelClass = refModelClass;
        this.key = key;
        this.field = field;
    }

    public void load(List<T> models) throws SQLException {
        ModelReflection r = ModelReflection.get(modelClass);
        ModelReflection refR = ModelReflection.get(refModelClass);

        List<Object> keys = models.stream()
                .map(model -> r.valueFor(model, key))
                .filter(value -> value != null)
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
            R ref = keyToRef.getOrDefault(r.valueFor(model, key), null);

            try {
                field.set(model, ref);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
