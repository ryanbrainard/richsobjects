package com.github.ryanbrainard.richsobjects.api.client;

import java.util.Iterator;
import java.util.ServiceLoader;

/**
 * @author Ryan Brainard
 */
public class SfdcRestApiLoader {

    private SfdcRestApiLoader() {}

    private static final ServiceLoader<SfdcRestApiSessionProvider> sessionLoader = ServiceLoader.load(
            SfdcRestApiSessionProvider.class,
            SfdcRestApiLoader.class.getClassLoader());

    private static final ServiceLoader<SfdcRestApiClientProvider> clientLoader = ServiceLoader.load(
            SfdcRestApiClientProvider.class,
            SfdcRestApiLoader.class.getClassLoader());

    public static SfdcRestApiClient get(double version) {
        final SfdcRestApiSessionProvider sessionProvider = getFirstOrThrow(sessionLoader);
        final SfdcRestApiClientProvider clientProvider = getFirstOrThrow(clientLoader);

        return clientProvider.get(
                sessionProvider.getAccessToken(),
                sessionProvider.getApiEndpoint(),
                String.format("v%.1f", version));

    }

    private static <S> S getFirstOrThrow(ServiceLoader<S> loader) {
        final Iterator<S> providerIterator = loader.iterator();
        if (!providerIterator.hasNext()) {
            throw new IllegalStateException(
                    "Could not load service from " + loader +
                    "\nEnsure an entry in META-INF/services has been loaded on the classpath.");
        }
        return providerIterator.next();
    }
}