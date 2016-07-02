package net.gangelov.orm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

public class Query<T extends Model> implements Cloneable {
    static final boolean VERBOSE = true;

    enum Type {
        SELECT,
        INSERT,
        UPDATE,
        DELETE
    }

    private Type type;
    private final Connection connection;
    private String table = null,
                   returning = null,
                   select = null,
                   order = null;
    private Class<T> modelClass = null;

    protected final LinkedHashMap<String, Object> values = new LinkedHashMap<>();
    protected final LinkedHashMap<String, List<Object>> wheres = new LinkedHashMap<>();

    public final List<String> includes = new ArrayList<>();

    public Query(Connection connection) {
        this.connection = connection;
    }

    public Query<T> clone() {
        Query<T> obj = null;

        try {
            obj = (Query<T>)super.clone();
        } catch (CloneNotSupportedException e) {
            // This cannot happen
            e.printStackTrace();
        }

        obj.values.putAll(values);
        obj.wheres.putAll(wheres);
        obj.includes.addAll(includes);

        return obj;
    }

    public Query<T> forModel(Class<T> klass) {
        Query<T> q = select("*").from(ModelReflection.get(klass).tableName);
        q.modelClass = klass;

        return q;
    }

    public Query<T> include(String... associations) {
        Query<T> q = clone();

        for (String a : associations) {
            q.includes.add(a);
        }

        return q;
    }

    public Query<T> select() {
        return select("*");
    }

    public Query<T> select(String select) {
        Query<T> q = clone();

        q.type = Type.SELECT;
        q.select = select;

        return q;
    }

    public Query<T> insert() {
        Query<T> q = clone();

        q.type = Type.INSERT;

        return q;
    }

    public Query<T> update() {
        Query<T> q = clone();

        q.type = Type.UPDATE;

        return q;
    }

    public Query<T> delete() {
        Query<T> q = clone();

        q.type = Type.DELETE;

        return q;
    }

    public Query<T> table(String table) {
        Query<T> q = clone();

        q.table = table;

        return q;
    }

    public Query<T> from(String table) {
        return table(table);
    }

    public Query<T> into(String table) {
        return table(table);
    }

    public Query<T> set(String column, Object object) {
        Query<T> q = clone();

        q.values.put(column, object);

        return q;
    }

    public Query<T> set(Map<String, Object> values) {
        Query<T> q = clone();

        q.values.putAll(values);

        return q;
    }

    public Query<T> where(String column, Object value) {
        return whereSql("\"" + column + "\" = ?", value);
    }

    public Query<T> whereNot(String column, Object value) {
        return whereSql("\"" + column + "\" != ?", value);
    }

    public Query<T> whereSql(String where, Object... args) {
        Query<T> q = clone();

        q.wheres.put(where, Arrays.asList(args));

        return q;
    }

    public <E> Query<T> whereIn(String column, List<E> values) {
        Query<T> q = clone();

        if (values.size() == 0) {
            q.wheres.put("0=1", new ArrayList<>());
            return q;
        }

        String placeholders = values.stream()
                .map(value -> "?")
                .collect(Collectors.joining(", "));

        q.wheres.put("\"" + column + "\" in (" + placeholders + ")", (List<Object>)values);

        return q;
    }

    public Query<T> order(String column, String orderType) {
        Query<T> q = clone();

        q.order = "\"" + column + "\" " + orderType;

        return q;
    }

    public Query<T> returning(String column) {
        Query<T> q = clone();

        q.returning = column;

        return q;
    }

    public String toSQL() {
        StringBuilder sql = new StringBuilder();

        switch(type) {
            case SELECT:
                sql.append("select ");
                sql.append(select);
                sql.append(" from ");
                sql.append("\"").append(table).append("\"");

                if (wheres.size() > 0) {
                    sql.append(" where ");
                    sql.append(buildWhereClause());
                }

                if (order != null) {
                    sql.append(" order by ");
                    sql.append(order);
                }

                break;
            case DELETE:
                sql.append("delete from ");
                sql.append("\"").append(table).append("\"");

                if (wheres.size() > 0) {
                    sql.append(" where ");
                    sql.append(buildWhereClause());
                }

                break;
            case INSERT:
                sql.append("insert into \"").append(table).append("\"");
                sql.append(" (");
                sql.append(
                        values.keySet().stream()
                                .map(key -> "\"" + key + "\"")
                                .collect(Collectors.joining(", "))
                );
                sql.append(")");
                sql.append(" values (");
                sql.append(String.join(", ", Collections.nCopies(values.size(), "?")));
                sql.append(")");

                if (returning != null) {
                    sql.append(" returning \"").append(returning).append("\"");
                }

                break;
            case UPDATE:
                sql.append("update \"").append(table).append("\"");

                if (values.size() > 0) {
                    sql.append(" set ");
                    sql.append(
                            values.keySet().stream()
                                    .map(key -> "\"" + key + "\" = ?")
                                    .collect(Collectors.joining(", "))
                    );
                }

                if (wheres.size() > 0) {
                    sql.append(" where ");
                    sql.append(buildWhereClause());
                }

                if (returning != null) {
                    sql.append(" returning \"").append(returning).append("\"");
                }

                break;
            default:
                throw new RuntimeException("Unsupported query type");
        }

        return sql.toString();
    }

    private String buildWhereClause() {
        return this.wheres.keySet().stream()
                .map(clause -> "(" + clause + ")")
                .collect(Collectors.joining(" and "));
    }

    public QueryResult<T> execute() throws SQLException {
        String sql = toSQL();
        List<Object> params = buildParameters();

        if (VERBOSE) {
            System.out.println("ORM QUERY: " + toSQL());

            if (params.size() > 0) {
                System.out.println(
                        "ORM DATA:  [" +
                                params.stream().map(o -> {
                                    if (o == null) {
                                        return "null";
                                    } else if (o instanceof Integer) {
                                        return o.toString();
                                    } else {
                                        return "'" + o.toString() + "'";
                                    }
                                }).collect(Collectors.joining(", ")) +
                                "]"
                );
            }
        }

        PreparedStatement statement = statement(sql, params);

        statement.execute();

        if (modelClass != null) {
            return new QueryResult<T>(statement, modelClass, includes);
        } else {
            return new QueryResult<T>(statement);
        }
    }

    public List<T> results() throws SQLException {
        return execute().results();
    }

    public T first() throws SQLException {
        return (T)execute().first();
    }

    public boolean exists() throws SQLException {
        return count() > 0;
    }

    public int count() throws SQLException {
        QueryResult result = clone().select("count(*)").execute();

        return result.getInt();
    }

    private List<Object> buildParameters() {
        List<Object> params = new ArrayList<>();

        for (Object value : values.values()) {
            if (value instanceof Instant) {
                params.add(Timestamp.from((Instant)value));
            } else {
                params.add(value);
            }
        }

        for (List<Object> whereValues : wheres.values()) {
            for (Object value : whereValues) {
                if (value instanceof Instant) {
                    params.add(Timestamp.from((Instant)value));
                } else {
                    params.add(value);
                }
            }
        }

        return params;
    }

    private PreparedStatement statement(String sql, List<Object> params) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(sql);

        for (int i = 0; i < params.size(); i++) {
            statement.setObject(i + 1, params.get(i));
        }

        return statement;
    }
}
