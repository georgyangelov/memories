package net.gangelov.memories.rest;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.gangelov.memories.models.User;
import net.gangelov.memories.rest.responses.ApiError;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.sql.SQLException;

@Path("/users")
public class Users {
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public User create(String body) throws SQLException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode data = mapper.readTree(body);

        User user = new User();

        user.email = data.get("email").asText();
        user.setPassword(user.password);

        if (!user.save()) {
            throw new ApiError(500, "save_error", "Cannot save user");
        }

        return user;
    }
}
