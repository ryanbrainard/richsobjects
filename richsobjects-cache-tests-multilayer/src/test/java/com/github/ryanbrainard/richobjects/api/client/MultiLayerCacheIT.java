package com.github.ryanbrainard.richobjects.api.client;

import com.github.ryanbrainard.richsobjects.api.client.*;
import org.testng.annotations.Test;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Iterator;
import java.util.ServiceLoader;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;

/**
 * @author Ryan Brainard
 */
public class MultiLayerCacheIT extends AbstractRichSObjectServiceIT {

    @Test
    public void testServiceLoaderOrdering() throws Exception {
        Iterator<SfdcApiCacheProvider> actualProviders = ServiceLoader.load(SfdcApiCacheProvider.class).iterator();
        Iterator<Class<? extends SfdcApiCacheProvider>> expectedProviders = Arrays.asList(MemcachedCacheProvider.class, SimpleInMemoryCacheProvider.class).iterator();

        int iterations = 0;
        while (actualProviders.hasNext()) {
            final Class<? extends SfdcApiCacheProvider> actual = actualProviders.next().getClass();
            final Class<? extends SfdcApiCacheProvider> expected = expectedProviders.next();
            assertEquals(actual, expected);
            iterations++;
        }
        assertFalse(expectedProviders.hasNext());
        assertEquals(iterations, 2);
    }

    @Test
    public void testLayers() throws Exception {
        final SfdcApiClient outer = service.getApiClient();
        assertEquals(outer.getClass().getSimpleName(), "SimpleInMemoryUserCache");

        final SfdcApiClient middle = getFieldValue(outer, "api");
        assertEquals(middle.getClass().getSimpleName(), "MemcachedUserCache");

        final SfdcApiClient core = getFieldValue(middle, "apiClient");
        assertEquals(core.getClass().getSimpleName(), "SfdcApiJerseyClient");
    }

    private <V> V getFieldValue(Object o, String fieldName) throws NoSuchFieldException, IllegalAccessException {
        final Field middleField = o.getClass().getDeclaredField(fieldName);
        middleField.setAccessible(true);
        //noinspection unchecked
        return (V) middleField.get(o);
    }
}
