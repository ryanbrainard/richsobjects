package com.github.ryanbrainard.richsobjects.api.client;

/**
 * @author Ryan Brainard
 */
public interface SfdcApiCacheLoaderProvider {
    SfdcApiUserCache get(String key, SfdcApiClient apiClient);

    void invalidate();
}
