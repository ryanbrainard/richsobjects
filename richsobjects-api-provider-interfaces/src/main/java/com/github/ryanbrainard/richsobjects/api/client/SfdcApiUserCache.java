package com.github.ryanbrainard.richsobjects.api.client;

/**
 * @author Ryan Brainard
 */
public interface SfdcApiUserCache extends SfdcApiClient {
    void invalidate();
}
