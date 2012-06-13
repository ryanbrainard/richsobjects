package com.github.ryanbrainard.richsobjects.api.client;

import java.util.*;

/**
 * @author Ryan Brainard
 */
public class SimpleInMemoryCacheProvider implements SfdcApiCacheProvider {

    private static Map<String, SfdcApiUserCache> allCaches = LruMap.newSync(5);
    
    @Override
    public SfdcApiUserCache get(String key, SfdcApiClient apiClient) {
        if (allCaches.containsKey(key)) {
            return allCaches.get(key);
        } else {
            final SfdcApiUserCache userCache = new SimpleInMemoryUserCache(apiClient);
            allCaches.put(key, userCache);
            return userCache;
        }
    }
}
