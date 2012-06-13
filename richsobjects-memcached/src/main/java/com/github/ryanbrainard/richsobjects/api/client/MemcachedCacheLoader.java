package com.github.ryanbrainard.richsobjects.api.client;

import net.spy.memcached.ConnectionFactory;
import net.spy.memcached.ConnectionFactoryBuilder;
import net.spy.memcached.MemcachedClient;
import net.spy.memcached.auth.AuthDescriptor;
import net.spy.memcached.auth.PlainCallbackHandler;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Collections;

/**
 * @author Ryan Brainard
 */
public class MemcachedCacheLoader implements SfdcApiCacheLoaderProvider {

    private static final MemcachedClient memcached;

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
            memcached = new MemcachedClient(cf, Collections.singletonList(
                    new InetSocketAddress(getEnvOrThrow("MEMCACHE_SERVERS"), 11211)));
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


    @Override
    public SfdcApiUserCache get(String key, SfdcApiClient apiClient) {
        return new MemcachedUserCache(memcached, key, apiClient);
    }
}
