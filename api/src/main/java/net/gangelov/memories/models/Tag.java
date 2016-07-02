package net.gangelov.memories.models;

import net.gangelov.orm.Field;
import net.gangelov.orm.Id;
import net.gangelov.orm.Model;
import net.gangelov.orm.Table;

@Table(name = "tags")
public class Tag extends Model {
    public Tag() {
        super(Tag.class);
    }

    @Id(name="id")
    public Integer id;

    @Field(name="name")
    public String name;
}
