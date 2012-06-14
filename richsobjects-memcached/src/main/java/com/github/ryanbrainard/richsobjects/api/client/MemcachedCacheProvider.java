package com.github.ryanbrainard.richsobjects.api.client;

import net.spy.memcached.ConnectionFactory;
import net.spy.memcached.ConnectionFactoryBuilder;
import net.spy.memcached.MemcachedClient;
import net.spy.memcached.auth.AuthDescriptor;
import net.spy.memcached.auth.PlainCallbackHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Arrays;

/**
 * @author Ryan Brainard
 */
public class MemcachedCacheProvider implements SfdcApiCacheProvider {

    private static Logger logger = LoggerFactory.getLogger(MemcachedCacheProvider.class);
    private static final MemcachedClient memcached = initClient();

    private static MemcachedClient initClient() {
        final String username = System.getenv("MEMCACHE_USERNAME");
        final String password = System.getenv("MEMCACHE_PASSWORD");
        final String servers  = System.getenv("MEMCACHE_SERVERS");
        
        if (username == null || password == null || servers == null) {
            logger.error("WARNING: Memcached is not configured properly and will not be used for caching Rich SObjects. " +
                         "Be sure to set environment variables for MEMCACHE_USERNAME, MEMCACHE_PASSWORD, MEMCACHE_SERVERS.");
            return null;
        }
        
        final AuthDescriptor ad = new AuthDescriptor(new String[]{"PLAIN"}, new PlainCallbackHandler(username,password));
        final ConnectionFactoryBuilder factoryBuilder = new ConnectionFactoryBuilder();
        final ConnectionFactory cf = factoryBuilder.setProtocol(ConnectionFactoryBuilder.Protocol.BINARY)
                .setAuthDescriptor(ad)
                .setTranscoder(new ClassLoaderRegisteringSerializingTranscoder())
                .build();

        try {
            return new MemcachedClient(cf, Arrays.asList(new InetSocketAddress(servers, 11211)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public SfdcApiClient get(String key, SfdcApiClient apiClient) {
        if (memcached != null) {
            return new MemcachedUserCache(memcached, key, apiClient);
        } else {
            return apiClient;
        }
    }
}
