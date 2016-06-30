package net.gangelov.orm;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.Instant;

public class Model {
    public static void initialize(String connectionString, String modelPackage) throws SQLException {
        Connection connection = DriverManager.getConnection(connectionString);
        ModelReflection.loadClasses(modelPackage, connection);
    }

    public static <T extends Model> Query<T> query(Class<T> klass) {
        return ModelReflection.get(klass).db.query()
                .forModel(klass);
    }

    private Class<? extends Model> selfClass;

    public Model(Class<? extends Model> klass) {
        this.selfClass = klass;
    }

    public void save() throws SQLException {
        ModelReflection r = ModelReflection.get(selfClass);

        if (r.valueFor(this, r.idName) == null) {
            create(r);
        } else {
            update(r);
        }
    }

    private void create(ModelReflection r) throws SQLException {
        if (r.createTimestamp != null) {
            r.setValue(this, r.createTimestamp, Instant.now());
        }

        if (r.updateTimestamp != null) {
            r.setValue(this, r.updateTimestamp, Instant.now());
        }

        r.runHook(this, Hook.Type.BEFORE_SAVE);
        r.runHook(this, Hook.Type.BEFORE_CREATE);

        QueryResult result = r.db.query()
                .insert()
                .into(r.tableName)
                .set(r.valuesFor(this, false))
                .returning(r.idName)
                .execute();

        r.setValue(this, r.idName, result.getSingle());

        r.runHook(this, Hook.Type.AFTER_CREATE);
        r.runHook(this, Hook.Type.AFTER_SAVE);
    }

    private void update(ModelReflection r) throws SQLException {
        if (r.updateTimestamp != null) {
            r.setValue(this, r.updateTimestamp, Instant.now());
        }

        r.runHook(this, Hook.Type.BEFORE_SAVE);
        r.runHook(this, Hook.Type.BEFORE_UPDATE);

        QueryResult result = r.db.query()
                .update()
                .table(r.tableName)
                .set(r.valuesFor(this, false))
                .where(r.idName, r.valueFor(this, r.idName))
                .execute();

        r.runHook(this, Hook.Type.AFTER_UPDATE);
        r.runHook(this, Hook.Type.AFTER_SAVE);
    }
}
