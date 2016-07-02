package net.gangelov.memories.rest;

import net.gangelov.memories.models.Tag;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import java.sql.SQLException;
import java.util.List;

@Path("/tags")
public class Tags {
    @GET
    public List<Tag> index() throws SQLException {
        return Tag.query().results();
    }
}
