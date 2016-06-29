package net.gangelov.orm;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Model {
    public static void initialize(String connectionString, String modelPackage) throws SQLException {
        Connection connection = DriverManager.getConnection(connectionString);
        ModelReflection.loadClasses(modelPackage, connection);
    }

    public static Query query(Class<? extends Model> klass) {
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
        QueryResult result = r.db.query()
                .insert()
                .into(r.tableName)
                .set(r.valuesFor(this, false))
                .returning(r.idName)
                .execute();

        r.setValue(this, r.idName, result.getSingle());
    }

    private void update(ModelReflection r) throws SQLException {
        QueryResult result = r.db.query()
                .update()
                .table(r.tableName)
                .set(r.valuesFor(this, false))
                .where(r.idName, r.valueFor(this, r.idName))
                .execute();
    }
}
