package com.github.ryanbrainard.richsobjects.api.client;

import java.util.ServiceLoader;

/**
 * @author Ryan Brainard
 */
public class SfdcRestApiClientLoader {

    private SfdcRestApiClientLoader() {}

    private static final ServiceLoader<SfdcRestApiClientProvider> loader = ServiceLoader.load(
            SfdcRestApiClientProvider.class,
            SfdcRestApiClientLoader.class.getClassLoader());

    public static SfdcRestApiClient get() {
        for (SfdcRestApiClientProvider provider : loader) {
            final SfdcRestApiClient client = provider.get();
            if (client != null) {
                return client;
            }
        }

        throw new IllegalStateException("SfdcRestApiClient not found");
    }
}
