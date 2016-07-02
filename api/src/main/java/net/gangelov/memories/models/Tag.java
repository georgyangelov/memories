package net.gangelov.memories.models;

import net.gangelov.orm.*;
import net.gangelov.utils.Strings;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Table(name = "tags")
public class Tag extends Model {
    public Tag() {
        super(Tag.class);
    }

    @Id(name="id")
    public Integer id;

    @Field(name="name")
    public String name;

    @Hook(on= Hook.Type.BEFORE_SAVE)
    public void squishName() {
        name = Strings.squish(name);
    }

    public static List<Tag> tagsFromString(String tags) throws SQLException {
        List<String> tagNames = Arrays.stream(tags.split(","))
                .map(tag -> Strings.squish(tag))
                .filter(tag -> Strings.isPresent(tag))
                .distinct()
                .collect(Collectors.toList());

        List<Tag> existingTags = query().whereIn("name", tagNames).results();

        existingTags.forEach(tag -> {
            tagNames.remove(tag.name);
        });

        List<Tag> newTags = tagNames.stream().map(tagName -> {
            Tag tag = new Tag();
            tag.name = tagName;
            return tag;
        }).collect(Collectors.toList());

        existingTags.addAll(newTags);

        return existingTags;
    }

    public static Query<Tag> query() {
        return Model.query(Tag.class);
    }
}
