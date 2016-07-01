package net.gangelov.memories.rest;

import net.gangelov.memories.models.Image;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import java.sql.SQLException;
import java.util.List;

@Path("/images")
public class Images {
    @GET
    public List<Image> index() throws SQLException {
        return Image.query().include("user").results();
    }
}
