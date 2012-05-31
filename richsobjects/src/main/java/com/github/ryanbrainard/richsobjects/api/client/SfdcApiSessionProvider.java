package com.github.ryanbrainard.richsobjects.api.client;

/**
 * @author Ryan Brainard
 */
public interface SfdcApiSessionProvider {
    String getAccessToken();
    String getApiEndpoint();
}
