package com.github.ryanbrainard.richsobjects.api.client;

/**
 * @author Ryan Brainard
 */
public interface SfdcApiCacheProvider {
    SfdcApiUserCache get(String key, SfdcApiClient apiClient);
}
