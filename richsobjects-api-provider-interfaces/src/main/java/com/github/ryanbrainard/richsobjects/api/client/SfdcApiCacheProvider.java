package com.github.ryanbrainard.richsobjects.api.client;

/**
 * @author Ryan Brainard
 */
public interface SfdcApiCacheProvider {
    SfdcApiCache get(SfdcApiClient apiClient);
}
