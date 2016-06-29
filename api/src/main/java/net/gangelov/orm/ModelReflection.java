package net.gangelov.orm;

import org.reflections.Reflections;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ModelReflection {
    public final String tableName;
    public String idName;

    public final Class<? extends Model> klass;
    public final Map<String, java.lang.reflect.Field> attributes = new HashMap<>();

    public Database db;

    ModelReflection(Class<? extends Model> model) {
        tableName = model.getAnnotation(Table.class).name();
        klass = model;

        for (java.lang.reflect.Field field : klass.getFields()) {
            if (field.isAnnotationPresent(Id.class)) {
                idName = field.getAnnotation(Id.class).name();
                attributes.put(idName, field);
            } else if (field.isAnnotationPresent(Field.class)) {
                attributes.put(field.getAnnotation(Field.class).name(), field);
            }
        }
    }

    public Object valueFor(Model model, String name) {
        try {
            return attributes.get(name).get(model);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            throw new RuntimeException("Cannot get value from field - is private");
        }
    }

    public void setValue(Model model, String name, Object value) {
        try {
            attributes.get(name).set(model, value);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            throw new RuntimeException("Cannot set value on field - is private");
        }
    }

    public Map<String, Object> valuesFor(Model model, boolean includeId) {
        Map<String, Object> values = new HashMap<>();

        for (Map.Entry<String, java.lang.reflect.Field> attr : attributes.entrySet()) {
            if (!includeId && attr.getKey().equals(idName)) {
                continue;
            }

            try {
                values.put(attr.getKey(), attr.getValue().get(model));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                throw new RuntimeException("Cannot get value from field - is private");
            }
        }

        return values;
    }

    public Model loadFromResultSetRow(ResultSet resultSet) throws SQLException {
        try {
            Model model = klass.newInstance();

            ResultSetMetaData meta = resultSet.getMetaData();
            int columnCount = meta.getColumnCount();

            for (int i = 1; i <= columnCount; i++) {
                String columnName = meta.getColumnName(i);
                java.lang.reflect.Field field = attributes.get(columnName);

                if (field == null) {
                    // Unknown field, cannot deserialize it
                    continue;
                }

                field.set(model, resultSet.getObject(i));
            }

            return model;
        } catch (InstantiationException e) {
            e.printStackTrace();
            throw new RuntimeException("Cannot instantiate model class");
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            throw new RuntimeException("Cannot set model fields or constructor is private");
        }
    }

    static Map<String, ModelReflection> byTable = new HashMap<>();
    static Map<Class<? extends Model>, ModelReflection> byClass = new HashMap<>();

    public static void loadClasses(String modelPackage, Connection connection) {
        Reflections modelReflections = new Reflections(modelPackage);

        Set<Class<? extends Model>> models = modelReflections.getSubTypesOf(Model.class);

        models.forEach(model -> {
            if (!model.isAnnotationPresent(Table.class)) {
                System.out.println("No @Table annotation present on class " + model.getName() + ". Skipping it");
                return;
            }

            ModelReflection reflection = new ModelReflection(model);
            reflection.db = new Database(connection);

            byTable.put(reflection.tableName, reflection);
            byClass.put(reflection.klass, reflection);
        });
    }

    public static ModelReflection get(Class<? extends Model> klass) {
        return byClass.get(klass);
    }
}
