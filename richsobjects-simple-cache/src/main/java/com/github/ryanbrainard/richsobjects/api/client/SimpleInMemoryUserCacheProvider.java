package com.github.ryanbrainard.richsobjects.api.client;

/**
 * @author Ryan Brainard
 */
public class SimpleInMemoryUserCacheProvider implements SfdcApiUserCacheProvider {
    @Override
    public SfdcApiUserCache get(SfdcApiClient apiClient) {
        return new SimpleInMemoryUserCache(apiClient);
    }
}
