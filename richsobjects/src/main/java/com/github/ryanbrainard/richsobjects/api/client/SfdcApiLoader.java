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

    private static final ServiceLoader<SfdcApiCacheProvider> cacheLoader = ServiceLoader.load(
            SfdcApiCacheProvider.class,
            SfdcApiLoader.class.getClassLoader());
    
    public static SfdcApiClient get(double version) {
        final SfdcApiSessionProvider sessionProvider = getFirstOrThrow(sessionLoader);
        final SfdcApiClientProvider clientProvider = getFirstOrThrow(clientLoader);
        final SfdcApiCacheProvider cacheProvider = getFirstOrElse(cacheLoader, DEFAULT_CACHE_PROVIDER);

        return cacheProvider.get(
                clientProvider.get(
                        sessionProvider.getAccessToken(),
                        sessionProvider.getApiEndpoint(),
                        String.format("v%.1f", version)
                )
        );
    }

    private static <S> S getFirstOrElse(ServiceLoader<S> loader, S defaultProvider) {
        final Iterator<S> providerIterator = loader.iterator();
        if (providerIterator.hasNext()) {
            return providerIterator.next();
        } else {
            return defaultProvider;
        }
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

    private static final SfdcApiCacheProvider DEFAULT_CACHE_PROVIDER = new SfdcApiCacheProvider() {
        @Override
        public SfdcApiCache get(SfdcApiClient apiClient) {
            return new SimpleInMemorySfdcApiClientCache(apiClient);
        }
    };
}