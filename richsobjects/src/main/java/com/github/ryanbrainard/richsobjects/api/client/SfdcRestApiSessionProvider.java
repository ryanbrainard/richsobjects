package com.github.ryanbrainard.richsobjects.api.client;

/**
 * @author Ryan Brainard
 */
public interface SfdcRestApiSessionProvider {
    String getAccessToken();
    String getApiEndpoint();
}
