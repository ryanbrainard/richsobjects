package com.github.ryanbrainard.richobjects;

import com.github.ryanbrainard.richsobjects.api.client.SfdcRestApiClient;
import com.github.ryanbrainard.richsobjects.api.client.SfdcRestApiClientProvider;
/**
 * @author Ryan Brainard
 */
public class SfdcRestApiJerseyClientProvider implements SfdcRestApiClientProvider {
    
    @Override
    public SfdcRestApiClient get() {
        return new SfdcRestApiJerseyClient();
    }

}
