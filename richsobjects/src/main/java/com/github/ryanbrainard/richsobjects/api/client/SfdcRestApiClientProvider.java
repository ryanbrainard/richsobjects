package com.github.ryanbrainard.richsobjects.api.client;

/**
 * @author Ryan Brainard
 */
public interface SfdcRestApiClientProvider {
    SfdcRestApiClient get(String accessToken, String apiEndpoint, String version);
}
