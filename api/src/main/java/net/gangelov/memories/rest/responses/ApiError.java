package net.gangelov.memories.rest.responses;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

public class ApiError extends WebApplicationException {
    public ApiError(int status, String error, String message) {
        super(
                Response.status(status)
                        .entity(buildError(error, message))
                        .type(MediaType.APPLICATION_JSON)
                        .build()
        );
    }

    private static ObjectNode buildError(String error, String message) {
        ObjectNode json = new ObjectMapper().createObjectNode();

        json.put("error", error);
        json.put("message", message);

        return json;
    }
}