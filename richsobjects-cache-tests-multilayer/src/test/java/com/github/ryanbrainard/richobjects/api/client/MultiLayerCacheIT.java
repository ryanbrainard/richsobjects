package com.github.ryanbrainard.richobjects.api.client;

import com.github.ryanbrainard.richsobjects.RichSObject;
import com.github.ryanbrainard.richsobjects.api.client.*;
import com.sun.jersey.api.client.UniformInterfaceException;
import org.testng.annotations.Test;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.ServiceLoader;

import static org.testng.Assert.*;

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
        final SfdcApiClient outer = service.api();
        assertEquals(outer.getClass().getSimpleName(), "SimpleInMemoryUserCache");

        final SfdcApiClient middle = getFieldValue(outer, "api");
        assertEquals(middle.getClass().getSimpleName(), "MemcachedUserCache");

        final SfdcApiClient core = getFieldValue(middle, "apiClient");
        assertEquals(core.getClass().getSimpleName(), "SfdcApiJerseyClient");
    }

    @Test
    public void testInvalidation() throws Exception {
        final SfdcApiClient rawClient = new SfdcApiJerseyClientProvider().get(new SfdcApiTestSessionProvider().getAccessToken(), new SfdcApiTestSessionProvider().getApiEndpoint(), "v24.0");

        final String account = "Account";
        final RichSObject test1 = service.insert(service.of(account).getField("Name").setValue("TEST1"));
        final String id = test1.getField("Id").asString();
        assertEquals(test1.getField("Name").asString(), "TEST1");

        final RichSObject test2 = service.update(test1.getField("Name").setValue("TEST2"));
        assertEquals(test2.getField("Name").asString(), "TEST2");

        rawClient.updateSObject(account, id, Collections.singletonMap("Name", "TEST3"));
        assertEquals(rawClient.getSObject(account, id).get("Name"), "TEST3");
        assertEquals(service.fetch(account, id).getField("Name").asString(), "TEST2");

        service.delete(test2);
        try {
            service.fetch(account, id);
            fail();
        } catch (UniformInterfaceException e) {
            assertTrue(e.getResponse().getEntity(String.class).contains("NOT_FOUND"));
        }
    }

    private <V> V getFieldValue(Object o, String fieldName) throws NoSuchFieldException, IllegalAccessException {
        final Field middleField = o.getClass().getDeclaredField(fieldName);
        middleField.setAccessible(true);
        //noinspection unchecked
        return (V) middleField.get(o);
    }
}
