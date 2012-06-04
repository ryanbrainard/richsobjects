package com.github.ryanbrainard.richsobjects.api.client;

/**
 * @author Ryan Brainard
 */
public interface SfdcApiUserCacheProvider {
    SfdcApiUserCache get(SfdcApiClient apiClient);
}
