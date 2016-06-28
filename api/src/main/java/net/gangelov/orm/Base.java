package net.gangelov.orm;

import org.reflections.Reflections;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;

public class Base<T> {
    static String modelPackage;
    static Connection connection;
    static Map<String, ModelReflection> reflections = new HashMap<>();

    public static void initialize(String connectionString, String modelPackage) throws SQLException {
        Base.modelPackage = modelPackage;

        connect(connectionString);
        loadClasses();
    }

    private static void connect(String connectionString) throws SQLException {
        Connection conn = DriverManager.getConnection(connectionString);
    }

    private static void loadClasses() {
        Reflections modelReflections = new Reflections(modelPackage);

        Set<Class<? extends Base>> models = modelReflections.getSubTypesOf(Base.class);

        models.forEach(model -> {
            ModelReflection reflection = new ModelReflection(model);

            try {
                model.getDeclaredField("reflection").set(null, reflection);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }

            reflections.put(reflection.tableName, reflection);
        });
    }

    public Connection connection() {
        return connection;
    }

    public Base(ModelReflection reflection) {
//        this.reflection = reflection;
    }

    public void save() {

    }
}
