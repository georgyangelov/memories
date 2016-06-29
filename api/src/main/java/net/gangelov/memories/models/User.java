package net.gangelov.memories.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import net.gangelov.memories.Database;
import net.gangelov.orm.*;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Table(name="users")
public class User extends Model {
    @Id(name="id")
    public Integer id;

    @Field(name="email")
    public String email;

    @JsonIgnore
    @Field(name="password")
    public String password;

    @JsonIgnore
    @Field(name="access_token")
    public String accessToken;

    public User() {
        super(User.class);
    }

    public static Query query() {
        return query(User.class);
    }

    public void setPassword(String password) {
        this.password = BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public boolean checkPassword(String password) {
        return BCrypt.checkpw(password, this.password);
    }
}
