package net.gangelov.memories.rest.requests;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.gangelov.utils.Strings;
import net.gangelov.validation.ValidationErrors;
import net.gangelov.validation.ValidationException;

import java.io.IOException;
import java.io.InputStream;

public class JSONParams {
    private JsonNode data;

    public JSONParams(String body) throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        this.data = mapper.readTree(body);
    }

    public JSONParams(InputStream body) throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        this.data = mapper.readTree(body);
    }

    public String getString(String property) {
        JsonNode value = data.get(property);

        if (value == null) {
            return null;
        }

        return value.asText();
    }

    public Integer getInt(String property) {
        JsonNode value = data.get(property);

        if (value == null) {
            return null;
        }

        return value.asInt();
    }

    public Boolean getBool(String property) {
        JsonNode value = data.get(property);

        if (value == null) {
            return null;
        }

        return value.asBoolean();
    }

    public String requireString(String property) throws ValidationException {
        JsonNode value = data.get(property);

        if (value == null || Strings.isBlank(value.asText())) {
            throw new ValidationException(property, "is a required parameter (string)");
        }

        return value.asText();
    }

    public Integer requireInt(String property) throws ValidationException {
        JsonNode value = data.get(property);

        if (value == null) {
            throw new ValidationException(property, "is a required parameter (int)");
        }

        return value.asInt();
    }

    public Boolean requireBool(String property) throws ValidationException {
        JsonNode value = data.get(property);

        if (value == null) {
            throw new ValidationException(property, "is a required parameter (bool)");
        }

        return value.asBoolean();
    }
}
