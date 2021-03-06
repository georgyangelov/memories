package net.gangelov.orm;

import net.gangelov.orm.associations.Association;
import net.gangelov.orm.associations.BelongsToAssociation;
import net.gangelov.orm.associations.HasManyAssociation;
import net.gangelov.orm.associations.HasManyThroughAssociation;
import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.*;

public class ModelReflection {
    public final String tableName;
    public String idName;
    public String createTimestamp = null, updateTimestamp = null;

    public final Class<? extends Model> klass;
    public final Map<String, java.lang.reflect.Field> attributes = new HashMap<>();
    public final Map<Hook.Type, List<Method>> hooks = new HashMap<>();


    public final Map<String, Association> associations = new HashMap<>();

    public final Map<String, BelongsToAssociation> belongsTo = new HashMap<>();
    public final Map<String, HasManyAssociation> hasMany = new HashMap<>();
    public final Map<String, HasManyThroughAssociation> hasManyThrough = new HashMap<>();

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

            if (field.isAnnotationPresent(CreateTimestamp.class)) {
                createTimestamp = field.getAnnotation(Field.class).name();
            } else if (field.isAnnotationPresent(UpdateTimestamp.class)) {
                updateTimestamp = field.getAnnotation(Field.class).name();
            } else if (field.isAnnotationPresent(BelongsTo.class)) {
                BelongsTo annotation = field.getAnnotation(BelongsTo.class);
                BelongsToAssociation assoc = new BelongsToAssociation(model, annotation.model(), annotation.key(), field);

                associations.put(field.getName(), assoc);
                belongsTo.put(field.getName(), assoc);
            } else if (field.isAnnotationPresent(HasMany.class)) {
                HasMany annotation = field.getAnnotation(HasMany.class);
                HasManyAssociation assoc = new HasManyAssociation(model, annotation.model(), annotation.foreignKey(), field);

                associations.put(field.getName(), assoc);
                hasMany.put(field.getName(), assoc);
            } else if (field.isAnnotationPresent(HasManyThrough.class)) {
                HasManyThrough annotation = field.getAnnotation(HasManyThrough.class);
                HasManyAssociation throughAssociation = hasMany.get(annotation.through());

                if (throughAssociation == null) {
                    throw new RuntimeException("HasManyThrough must be declared above HasMany");
                }

                HasManyThroughAssociation assoc = new HasManyThroughAssociation(
                        model, throughAssociation, annotation.model(), annotation.foreignKey(), field
                );

                associations.put(field.getName(), assoc);
                hasManyThrough.put(field.getName(), assoc);
            }
        }

        for (Method method : klass.getDeclaredMethods()) {
            if (method.isAnnotationPresent(Hook.class)) {
                Hook.Type hookType = method.getAnnotation(Hook.class).on();

                if (!hooks.containsKey(hookType)) {
                    List<Method> methods = new ArrayList<>();
                    hooks.put(hookType, methods);
                }

                hooks.get(hookType).add(method);
            }
        }
    }

    public Object valueFor(Model model, String name) {
        try {
            java.lang.reflect.Field field = attributes.get(name);

            if (field == null) {
                throw new RuntimeException("Expected " + klass.getName() + " to have attribute named " + name);
            }

            return field.get(model);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            throw new RuntimeException("Cannot get value from field - is private");
        }
    }

    public void setValue(Model model, String name, Object value) {
        try {
            if (value instanceof Timestamp) {
                attributes.get(name).set(model, ((Timestamp) value).toInstant());
            } else {
                attributes.get(name).set(model, value);
            }
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

                setValue(model, columnName, resultSet.getObject(i));
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

    public void runHook(Model model, Hook.Type type) {
        if (!hooks.containsKey(type)) {
            return;
        }

        hooks.get(type).forEach(method -> {
            try {
                method.setAccessible(true);
                method.invoke(model);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
                throw new RuntimeException("Cannot call model hook");
            }
        });
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
