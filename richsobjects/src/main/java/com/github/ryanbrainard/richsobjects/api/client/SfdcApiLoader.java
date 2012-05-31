package com.github.ryanbrainard.richsobjects.api.client;

import java.util.Iterator;
import java.util.ServiceLoader;

/**
 * @author Ryan Brainard
 */
public class SfdcApiLoader {

    private SfdcApiLoader() {}

    private static final ServiceLoader<SfdcApiSessionProvider> sessionLoader = ServiceLoader.load(
            SfdcApiSessionProvider.class,
            SfdcApiLoader.class.getClassLoader());

    private static final ServiceLoader<SfdcApiClientProvider> clientLoader = ServiceLoader.load(
            SfdcApiClientProvider.class,
            SfdcApiLoader.class.getClassLoader());

    public static SfdcApiClient get(double version) {
        final SfdcApiSessionProvider sessionProvider = getFirstOrThrow(sessionLoader);
        final SfdcApiClientProvider clientProvider = getFirstOrThrow(clientLoader);

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