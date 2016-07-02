package net.gangelov.orm.associations;

import java.sql.SQLException;
import java.util.List;

abstract public class Association<T> {
    abstract public void load(List<T> models) throws SQLException;
}
