package net.gangelov.orm;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class QueryResult<T extends Model> {
    private ResultSet resultSet;
    private Class<T> modelClass = null;

    public QueryResult(PreparedStatement statement) throws SQLException {
        resultSet = statement.getResultSet();
    }

    public QueryResult(PreparedStatement statement, Class<T> modelClass) throws SQLException {
        resultSet = statement.getResultSet();
        this.modelClass = modelClass;
    }

    public List<T> results() throws SQLException {
        if (modelClass == null) {
            throw new RuntimeException("Cannot get results without type argument");
        }

        return results(modelClass);
    }

    public <E extends Model> List<E> results(Class<E> modelClass) throws SQLException {
        List<E> results = new ArrayList<E>();

        while (resultSet.next()) {
            results.add((E)ModelReflection.get(modelClass).loadFromResultSetRow(resultSet));
        }

        return results;
    }

    public Object getSingle() throws SQLException {
        resultSet.next();
        return resultSet.getObject(1);
    }

    public int getInt() throws SQLException {
        resultSet.next();
        return resultSet.getInt(1);
    }

    public boolean exists() throws SQLException {
        return resultSet.isBeforeFirst();
    }

    public void close() throws SQLException {
        if (!resultSet.isClosed()) {
            resultSet.close();
        }
    }
}
