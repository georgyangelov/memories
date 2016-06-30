package net.gangelov.memories.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.InstantSerializer;
import net.gangelov.orm.*;
import org.mindrot.jbcrypt.BCrypt;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.time.Instant;

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

    @CreateTimestamp
    @Field(name="created_at")
    @JsonSerialize(using = InstantSerializer.class)
    public Instant createdAt;

    @UpdateTimestamp
    @Field(name="updated_at")
    @JsonSerialize(using = InstantSerializer.class)
    public Instant updatedAt;

    public User() {
        super(User.class);
    }

    public static Query<User> query() {
        return query(User.class);
    }

    public Query<Photo> photos() {
        return Photo.query().where("user_id", id);
    }

    public void setPassword(String password) {
        this.password = BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public boolean checkPassword(String password) {
        return BCrypt.checkpw(password, this.password);
    }

    private static final SecureRandom random = new SecureRandom();

    @Hook(on = Hook.Type.BEFORE_CREATE)
    private void initAccessToken() {
        accessToken = new BigInteger(130, random).toString(32);
    }
}
