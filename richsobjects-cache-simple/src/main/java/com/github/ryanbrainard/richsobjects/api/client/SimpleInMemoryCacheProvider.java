package com.github.ryanbrainard.richsobjects.api.client;

import java.util.Map;

/**
 * @author Ryan Brainard
 */
public class SimpleInMemoryCacheProvider implements SfdcApiCacheProvider {

    private static Map<String, SimpleInMemoryUserCache> allCaches = LruMap.newSync(5);
    
    @Override
    public SfdcApiClient get(String key, SfdcApiClient apiClient) {
        if (allCaches.containsKey(key)) {
            return allCaches.get(key);
        } else {
            final SimpleInMemoryUserCache userCache = new SimpleInMemoryUserCache(apiClient);
            allCaches.put(key, userCache);
            return userCache;
        }
    }
}
