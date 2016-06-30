package net.gangelov.memories.rest;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.gangelov.memories.models.User;
import net.gangelov.memories.rest.responses.ApiError;
import net.gangelov.validation.ValidationException;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@Path("/users")
public class Users {
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public User create(String body) throws SQLException, IOException, ValidationException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode data = mapper.readTree(body);

        User user = new User();
        user.email = data.get("email").asText();
        user.setPassword(user.password);

        user.ensureValid();
        user.save();

        return user;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<User> index() throws SQLException {
        return User.query().results();
    }
}
