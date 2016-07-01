package net.gangelov.memories.models;

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

import java.sql.SQLException;
import java.time.Instant;

@Table(name="images")
public class Image extends Model {
    @Id(name="id")
    public Integer id;

    @Field(name="user_id")
    public Integer userId;

    @Field(name="file_path")
    public String filePath;

    @Field(name="name")
    public String name;

    @CreateTimestamp
    @Field(name="created_at")
    @JsonSerialize(using = InstantSerializer.class)
    public Instant createdAt;

    @UpdateTimestamp
    @Field(name="updated_at")
    @JsonSerialize(using = InstantSerializer.class)
    public Instant updatedAt;

    @BelongsTo(model=User.class, key="user_id")
    public User user;

    public Image() {
        super(User.class);
    }

    public static Query<Image> query() {
        return query(Image.class);
    }

    public User user() throws SQLException {
        return User.query().where("id", userId).first();
    }

    private static final Validator<Image> validator = new CompositeValidator<>(
            new FieldPresenceValidator<>("user_id", image -> image.userId),
            new FieldPresenceValidator<>("file_path", image -> image.filePath)
    );

    public ValidationErrors validate() {
        return validator.validate(this);
    }

    public void ensureValid() throws ValidationException {
        validator.ensureValid(this);
    }
}
