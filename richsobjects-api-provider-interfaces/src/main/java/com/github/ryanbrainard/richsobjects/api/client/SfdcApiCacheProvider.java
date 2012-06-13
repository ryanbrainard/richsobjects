package com.github.ryanbrainard.richsobjects.api.client;

/**
 * Provides a caching layer that wraps {@link SfdcApiClient}.
 *
 * @author Ryan Brainard
 */
public interface SfdcApiCacheProvider {

    /**
     * Gets a user-level cache that decorates the provided underlying {@link SfdcApiClient}.
     *
     * If there is a cache miss or the value should never be cached,
     * the call should be delegated to the wrapped api client.
     *
     * If the caching does not apply, it is acceptable to simply return the provided apiClient unwrapped.
     * 
     * @param key unique key to identify a given user for a given session. may not correspond to web session.
     * @param apiClient underlying apiClient to be decorated
     * @return wrapped api client with caching layer
     */
    SfdcApiClient get(String key, SfdcApiClient apiClient);
}
