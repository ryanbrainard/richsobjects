package com.github.ryanbrainard.richobjects.api.client;

import com.github.ryanbrainard.richsobjects.api.client.SfdcApiClient;
import com.github.ryanbrainard.richsobjects.api.client.SfdcApiClientProvider;

/**
 * @author Ryan Brainard
 */
public class SfdcApiJerseyClientProvider implements SfdcApiClientProvider {
    
    @Override
    public SfdcApiClient get(String accessToken, String apiEndpoint, String version) {
        return new SfdcApiJerseyClient(accessToken, apiEndpoint, version);
    }

}
