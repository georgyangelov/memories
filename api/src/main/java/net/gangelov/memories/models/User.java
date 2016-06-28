package net.gangelov.memories.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.node.ObjectNode;
import net.gangelov.memories.Database;
import org.mindrot.jbcrypt.BCrypt;

import javax.ws.rs.core.Response;
import javax.xml.bind.annotation.XmlRootElement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class User {
    public Integer id;

    public String email;

    @JsonIgnore
    public String password;

    @JsonIgnore
    public String accessToken;

    public void setPassword(String password) {
        this.password = BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public boolean checkPassword(String password) {
        return BCrypt.checkpw(password, this.password);
    }

    public boolean create() throws SQLException {
        PreparedStatement create = Database.query(
                "insert into users (email, password, access_token) VALUES (?, ?, ?) returning id"
        );

        create.setString(1, email);
        create.setString(2, password);
        create.setString(3, accessToken);

        if (!create.execute()) {
            return false;
        }

        ResultSet result = create.getResultSet();

        result.next();
        id = result.getInt(1);

        return true;
    }

    public boolean save() throws SQLException {
        if (id == null) {
            return create();
        }

        PreparedStatement update = Database.query(
                "insert into users (id, email, password, access_token) VALUES (?, ?, ?, ?)"
        );

        update.setInt(1, id);
        update.setString(2, email);
        update.setString(3, password);
        update.setString(4, accessToken);

        return update.executeUpdate() > 0;
    }
}
