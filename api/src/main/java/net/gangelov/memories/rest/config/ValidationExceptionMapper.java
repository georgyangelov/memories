package net.gangelov.memories.rest.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import net.gangelov.validation.ValidationException;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.List;

@Provider
public class ValidationExceptionMapper implements ExceptionMapper<ValidationException> {
    public Response toResponse(ValidationException ex) {
        ObjectNode json = new ObjectMapper().createObjectNode();
        json.put("error", "validation");

        ArrayNode messages = json.putArray("messages");
        List<String> errors = ex.errors.toErrorStrings();

        for (int i = 0; i < errors.size(); i++) {
            messages.add(errors.get(i));
        }

        json.put("message", ex.errors.toString());

        return Response.status(400)
                .type(MediaType.APPLICATION_JSON)
                .entity(json)
                .build();
    }
}