package net.gangelov.memories.rest.config;

import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerResponse;
import com.sun.jersey.spi.container.ContainerResponseFilter;

import javax.ws.rs.ext.Provider;

@Provider
public class CORSFilter implements ContainerResponseFilter {
    @Override
    public ContainerResponse filter(ContainerRequest request, ContainerResponse response) {
        response.getHttpHeaders().add("Access-Control-Allow-Origin", request.getHeaderValue("Origin"));
        response.getHttpHeaders().add("Access-Control-Allow-Headers", request.getHeaderValue("Access-Control-Request-Headers"));
        response.getHttpHeaders().add("Access-Control-Allow-Credentials", "true");
        response.getHttpHeaders().add("Access-Control-Allow-Methods", "GET, PUT, POST, DELETE");

        return response;
    }
}