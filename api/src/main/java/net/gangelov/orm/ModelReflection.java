package net.gangelov.orm;

import java.util.Map;

public class ModelReflection<T extends Base> {
    public String tableName;
    public Class<T> klass;

    ModelReflection(Class<T> model) {
        tableName = model.getAnnotation(Model.class).table();
        klass = model;
    }

//    public Map<String, String> values(T model) {
//
//    }
}
