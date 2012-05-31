package com.github.ryanbrainard.richobjects.api.client;

import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientRequest;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.filter.ClientFilter;

import javax.ws.rs.core.HttpHeaders;

/**
 * @author Ryan Brainard
 */
public class AuthorizationHeaderFilter extends ClientFilter {

    private final String sessionId;

    public AuthorizationHeaderFilter(String sessionId) {
        this.sessionId = sessionId;
    }

    @Override
    public ClientResponse handle(ClientRequest cr) throws ClientHandlerException {
        cr.getHeaders().add(HttpHeaders.AUTHORIZATION, "OAuth " + sessionId);
        return getNext().handle(cr);
    }

}
