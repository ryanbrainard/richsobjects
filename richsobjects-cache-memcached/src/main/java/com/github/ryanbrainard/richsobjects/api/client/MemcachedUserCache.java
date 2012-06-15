package com.github.ryanbrainard.richsobjects.api.client;

import com.github.ryanbrainard.richsobjects.api.model.BasicSObjectInformation;
import com.github.ryanbrainard.richsobjects.api.model.GlobalDescription;
import com.github.ryanbrainard.richsobjects.api.model.QueryResult;
import com.github.ryanbrainard.richsobjects.api.model.SObjectDescription;
import net.spy.memcached.MemcachedClient;

import java.util.Map;

/**
 * @author Ryan Brainard
 */
class MemcachedUserCache implements SfdcApiClient {

    private final MemcachedClient memcached;
    private final String userKey;
    private final SfdcApiClient apiClient;

    public MemcachedUserCache(MemcachedClient memcached, String userKey, SfdcApiClient apiClient) {
        this.memcached = memcached;
        this.userKey = userKey;
        this.apiClient = apiClient;
    }

    private String globalKey(String entityKey) {
        return userKey + entityKey;
    }

    private static interface ValueProvider<T> {
        T get();
    }

    private <T> T loadEntity(final String entityKey, int expInMinutes, final ValueProvider<T> provider) {
        final Object fromCache = memcached.get(globalKey(entityKey));

        if (fromCache != null) {
            //noinspection unchecked
            return (T) fromCache;
        } else {
            final T fromProvider = provider.get();
            memcached.set(globalKey(entityKey), expInMinutes * 60, fromProvider);
            return fromProvider;
        }
    }

    private void invalidateEntity(final String entityKey) {
        memcached.delete(globalKey(entityKey));
    }
    
    @Override
    public GlobalDescription describeGlobal() {
        return loadEntity("DESCRIBE_GLOBAL", 30, new ValueProvider<GlobalDescription>() {
            @Override
            public GlobalDescription get() {
                return apiClient.describeGlobal();
            }
        });
    }

    @Override
    public BasicSObjectInformation describeSObjectBasic(String type) {
        return apiClient.describeSObjectBasic(type);
    }

    @Override
    public SObjectDescription describeSObject(final String type) {
        return loadEntity(type, 30, new ValueProvider<SObjectDescription>() {
            @Override
            public SObjectDescription get() {
                return apiClient.describeSObject(type);
            }
        });
    }

    @Override
    public String createSObject(String type, Map<String, ?> record) {
        return apiClient.createSObject(type, record);
    }

    @Override
    public void updateSObject(String type, String id, Map<String, ?> record) {
        invalidateEntity(id);
        apiClient.updateSObject(type, id, record);
    }

    @Override
    public void deleteSObject(String type, String id) {
        invalidateEntity(id);
        apiClient.deleteSObject(type, id);
    }

    @Override
    public Map<String, ?> getSObject(final String type, final String id) {
        return loadEntity(id, 1, new ValueProvider<Map<String, ?>>() {
            @Override
            public Map<String, ?> get() {
                return apiClient.getSObject(type, id);
            }
        });
    }

    @Override
    public QueryResult query(String soql) {
        return apiClient.query(soql);
    }

    @Override
    public QueryResult queryMore(String nextRecordsUrl) {
        return apiClient.queryMore(nextRecordsUrl);
    }

    @Override
    public String getRawBase64Content(String contentUrl) {
        return apiClient.getRawBase64Content(contentUrl);
    }
}
