package net.gangelov.memories.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/")
public class Root {
    @GET
    @Produces(MediaType.TEXT_HTML)
    public String getMessage() {
        return "Hello world!";
    }
}