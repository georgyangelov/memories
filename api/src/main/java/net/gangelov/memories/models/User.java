package net.gangelov.memories.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.InstantSerializer;
import net.gangelov.orm.*;
import net.gangelov.orm.validators.FieldUniquenessValidator;
import net.gangelov.validation.ValidationErrors;
import net.gangelov.validation.ValidationException;
import net.gangelov.validation.Validator;
import net.gangelov.validation.validators.CompositeValidator;
import net.gangelov.validation.validators.EmailValidator;
import net.gangelov.validation.validators.FieldPresenceValidator;
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

    public Query<Image> images() {
        return Image.query().where("user_id", id);
    }

    public void setPassword(String password) {
        if (password == null) {
            return;
        }

        this.password = BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public boolean checkPassword(String password) {
        return BCrypt.checkpw(password, this.password);
    }

    private static final SecureRandom random = new SecureRandom();

    @Hook(on = Hook.Type.BEFORE_CREATE)
    public void generateAccessToken() {
        accessToken = new BigInteger(130, random).toString(32);
    }

    private static final Validator<User> validator = new CompositeValidator<>(
            new FieldPresenceValidator<>("email", user -> user.email),
            new EmailValidator<>("email", user -> user.email),
            new FieldPresenceValidator<>("password", user -> user.password),
            new FieldUniquenessValidator<>(User.class, "email")
    );

    public ValidationErrors validate() {
        return validator.validate(this);
    }

    public void ensureValid() throws ValidationException {
        validator.ensureValid(this);
    }
}
