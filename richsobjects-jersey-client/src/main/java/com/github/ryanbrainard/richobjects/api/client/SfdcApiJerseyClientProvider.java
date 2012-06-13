package com.github.ryanbrainard.richobjects.api.client;

import com.github.ryanbrainard.richsobjects.api.client.SfdcApiClient;
import com.github.ryanbrainard.richsobjects.api.client.SfdcApiClientProvider;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;

/**
 * @author Ryan Brainard
 */
public class SfdcApiJerseyClientProvider implements SfdcApiClientProvider {

    /**
     * This is a shared connection and should remain stateless.
     */
    private static final Client pooledClient;

    static {
        final ClientConfig config = new DefaultClientConfig();
        config.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
        config.getClasses().add(ObjectMapperProvider.class);

        pooledClient = Client.create(config);
    }

    @Override
    public SfdcApiClient get(String accessToken, String apiEndpoint, String version) {
        return new SfdcApiJerseyClient(pooledClient, accessToken, apiEndpoint, version);
    }

}
