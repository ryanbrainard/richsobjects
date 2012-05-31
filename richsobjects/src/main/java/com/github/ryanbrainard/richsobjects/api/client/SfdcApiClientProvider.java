package com.github.ryanbrainard.richsobjects.api.client;

/**
 * @author Ryan Brainard
 */
public interface SfdcApiClientProvider {
    SfdcApiClient get(String accessToken, String apiEndpoint, String version);
}
