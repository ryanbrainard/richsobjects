package com.github.ryanbrainard.richsobjects.api.client;

/**
 * @author Ryan Brainard
 */
public class MemcachedCacheLoader implements SfdcApiCacheLoaderProvider {

    @Override
    public SfdcApiUserCache get(String key, SfdcApiClient apiClient) {
        return new MemcachedUserCache(key, apiClient);
    }

    @Override
    public void invalidate() {
        throw new UnsupportedOperationException(); // TODO
    }
}
