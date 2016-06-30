package net.gangelov.memories.rest.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import net.gangelov.validation.ValidationException;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.sql.SQLException;
import java.util.List;

@Provider
public class SQLExceptionMapper implements ExceptionMapper<SQLException> {
    public Response toResponse(SQLException ex) {
        ObjectNode json = new ObjectMapper().createObjectNode();
        json.put("error", "internal_error");
        json.put("message", "Something went wrong.");

        return Response.status(500)
                .type(MediaType.APPLICATION_JSON)
                .entity(json)
                .build();
    }
}