package net.gangelov.orm;

import java.sql.Connection;

public class Database {
    private Connection connection;

    public Database(Connection connection) {
        this.connection = connection;
    }

    public Query query() {
        return new Query(connection);
    }
}
