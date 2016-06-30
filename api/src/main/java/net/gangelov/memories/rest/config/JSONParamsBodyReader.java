package net.gangelov.memories.rest.config;

import net.gangelov.memories.rest.requests.JSONParams;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

@Provider
public class JSONParamsBodyReader implements MessageBodyReader<JSONParams> {
    @Override
    public boolean isReadable(
            Class<?> aClass,
            Type type,
            Annotation[] annotations,
            MediaType mediaType
    ) {
        return mediaType.equals(MediaType.APPLICATION_JSON_TYPE);
    }

    @Override
    public JSONParams readFrom(
            Class<JSONParams> aClass,
            Type type,
            Annotation[] annotations,
            MediaType mediaType,
            MultivaluedMap<String, String> multivaluedMap,
            InputStream inputStream
    ) throws IOException, WebApplicationException {
        return new JSONParams(inputStream);
    }
}
