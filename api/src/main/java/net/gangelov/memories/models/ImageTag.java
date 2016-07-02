package net.gangelov.memories.models;

import net.gangelov.orm.Field;
import net.gangelov.orm.Id;
import net.gangelov.orm.Model;
import net.gangelov.orm.Table;

@Table(name = "images_tags")
public class ImageTag extends Model {
    public ImageTag() {
        super(ImageTag.class);
    }

    @Id(name="id")
    public Integer id;

    @Field(name="image_id")
    public Integer imageId;

    @Field(name="tag_id")
    public Integer tagId;
}
