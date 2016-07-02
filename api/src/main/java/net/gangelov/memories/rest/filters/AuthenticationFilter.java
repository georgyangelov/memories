package net.gangelov.memories.rest.filters;

import com.sun.jersey.spi.container.ContainerRequestFilter;
import com.sun.jersey.spi.container.ContainerResponseFilter;
import com.sun.jersey.spi.container.ResourceFilter;

public class AuthenticationFilter implements ResourceFilter {
    @Override
    public ContainerRequestFilter getRequestFilter() {
        return new AuthenticationRequestFilter();
    }

    @Override
    public ContainerResponseFilter getResponseFilter() {
        return null;
    }
}
