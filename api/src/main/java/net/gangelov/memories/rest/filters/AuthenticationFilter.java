package net.gangelov.memories.rest.filters;

import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerRequestFilter;
import net.gangelov.memories.models.User;
import net.gangelov.memories.rest.config.SQLExceptionMapper;
import net.gangelov.memories.rest.responses.ApiError;
import net.gangelov.utils.Strings;

import javax.ws.rs.WebApplicationException;
import java.sql.SQLException;

public class AuthenticationFilter implements ContainerRequestFilter {
    @Override
    public ContainerRequest filter(ContainerRequest containerRequest) throws WebApplicationException {
        String accessToken = containerRequest.getHeaderValue("Authorization");

        if (Strings.isBlank(accessToken)) {
            throw new ApiError(401, "unauthorized", "This call requires an access token");
        }

        try {
            boolean tokenValid = User.query().where("access_token", accessToken).exists();

            if (tokenValid) {
                throw new ApiError(403, "invalid_token", "Invalid access token");
            }

            return containerRequest;
        } catch (SQLException e) {
            throw new ApiError(500, "internal_error", "An error occurred");
        }
    }
}
