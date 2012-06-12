package com.github.ryanbrainard.richsobjects.api.client;

import com.github.ryanbrainard.richsobjects.api.model.BasicSObjectInformation;
import com.github.ryanbrainard.richsobjects.api.model.GlobalDescription;
import com.github.ryanbrainard.richsobjects.api.model.QueryResult;
import com.github.ryanbrainard.richsobjects.api.model.SObjectDescription;
import net.spy.memcached.ConnectionFactory;
import net.spy.memcached.ConnectionFactoryBuilder;
import net.spy.memcached.MemcachedClient;
import net.spy.memcached.auth.AuthDescriptor;
import net.spy.memcached.auth.PlainCallbackHandler;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Collections;
import java.util.Map;

/**
 * @author Ryan Brainard
 */
public class MemcachedUserCache implements SfdcApiUserCache {

    static final MemcachedClient memcachedClient;

    static {
        AuthDescriptor ad = new AuthDescriptor(new String[]{"PLAIN"}, new PlainCallbackHandler(
                getEnvOrThrow("MEMCACHE_USERNAME"),
                getEnvOrThrow("MEMCACHE_PASSWORD")));
        ConnectionFactoryBuilder factoryBuilder = new ConnectionFactoryBuilder();
        ConnectionFactory cf = factoryBuilder
                .setProtocol(ConnectionFactoryBuilder.Protocol.BINARY)
                .setAuthDescriptor(ad).setTranscoder(new ClassLoaderRegisteringSerializingTranscoder())
                .build();

        try {
            memcachedClient = new MemcachedClient(cf, Collections.singletonList(new InetSocketAddress(getEnvOrThrow("MEMCACHE_SERVERS"), 11211)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static String getEnvOrThrow(String name) {
        final String value = System.getenv(name);
        if (value == null) {
            throw new NullPointerException("[" + name + "] is must not be null");
        }
        return value;
    }

    private final String userKey;
    private final SfdcApiClient apiClient;
    
    public MemcachedUserCache(String userKey, SfdcApiClient apiClient) {
        this.userKey = userKey;
        this.apiClient = apiClient;
    }

    private String fullyQualifiedKey(String entityKey) {
        return userKey + entityKey;
    }

    private <T> T loadEntity(final String entityKey, final ValueProvider<T> provider) {
        final Object fromCache = memcachedClient.get(fullyQualifiedKey(entityKey));

        if (fromCache != null) {
            //noinspection unchecked
            return (T) fromCache;
        } else {
            final T fromProvider = provider.get();
            memcachedClient.set(fullyQualifiedKey(entityKey), 5 * 60, fromProvider);
            return fromProvider;
        }
    }

    private void invalidateEntity(final String entityKey) {
        memcachedClient.delete(fullyQualifiedKey(entityKey));
    }
    
    @Override
    public void invalidate() {
        throw new UnsupportedOperationException(); //TODO
    }

    @Override
    public GlobalDescription describeGlobal() {
        return loadEntity("DESCRIBE_GLOBAL", new ValueProvider<GlobalDescription>() {
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
        return loadEntity(type, new ValueProvider<SObjectDescription>() {
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
        return loadEntity(id, new ValueProvider<Map<String, ?>>() {
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
