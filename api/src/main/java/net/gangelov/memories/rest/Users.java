package net.gangelov.memories.rest;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.gangelov.memories.models.User;
import net.gangelov.memories.rest.requests.JSONParams;
import net.gangelov.memories.rest.responses.ApiError;
import net.gangelov.memories.rest.responses.AuthenticationResponse;
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
    public User create(JSONParams params) throws SQLException, IOException, ValidationException {
        User user = new User();
        user.name = params.requireString("name");
        user.email = params.requireString("email");
        user.setPassword(params.requireString("password"));

        user.ensureValid();
        user.save();

        return user;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<User> index() throws SQLException {
        return User.query().results();
    }

    @GET
    @Path("/{accessToken}")
    @Produces(MediaType.APPLICATION_JSON)
    public User findByAccessToken(@PathParam("accessToken") String accessToken) throws SQLException {
        User user = User.query().where("access_token", accessToken).first();

        if (user == null) {
            throw new ApiError(404, "invalid_token", "Invalid access token");
        }

        return user;
    }

    @POST
    @Path("/auth")
    public AuthenticationResponse authenticate(JSONParams params) throws ValidationException, SQLException {
        String email = params.requireString("email");
        String password = params.requireString("password");

        User user = User.query().where("email", email).first();

        if (user == null || !user.checkPassword(password)) {
            throw new ApiError(400, "invalid_credentials", "Invalid email or password");
        }

        return new AuthenticationResponse(user);
    }

    @POST
    @Path("/deauth")
    public void deauthenticate(JSONParams params) throws SQLException, ValidationException {
        User user = User.query()
                .where("access_token", params.requireString("accessToken"))
                .first();

        if (user == null) {
            return;
        }

        user.generateAccessToken();
        user.save();
    }
}
