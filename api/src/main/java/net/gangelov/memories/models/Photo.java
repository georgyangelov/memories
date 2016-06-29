package net.gangelov.memories.models;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.InstantSerializer;
import net.gangelov.orm.*;

import java.sql.SQLException;
import java.time.Instant;

@Table(name="photos")
public class Photo extends Model {
    @Id(name="id")
    public Integer id;

    @Field(name="user_id")
    public Integer userId;

    @Field(name="file_path")
    public String filePath;

    @CreateTimestamp
    @Field(name="created_at")
    @JsonSerialize(using = InstantSerializer.class)
    public Instant createdAt;

    @UpdateTimestamp
    @Field(name="updated_at")
    @JsonSerialize(using = InstantSerializer.class)
    public Instant updatedAt;

    public Photo() {
        super(User.class);
    }

    public static Query<Photo> query() {
        return query(Photo.class);
    }

    public User user() throws SQLException {
        return User.query().where("id", userId).first();
    }
}
