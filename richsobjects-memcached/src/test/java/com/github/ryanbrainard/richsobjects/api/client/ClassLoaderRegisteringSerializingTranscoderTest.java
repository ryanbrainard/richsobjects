package com.github.ryanbrainard.richsobjects.api.client;

import com.github.ryanbrainard.richobjects.api.client.ObjectMapperProvider;
import org.testng.annotations.Test;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.testng.Assert.*;

/**
 * @author Ryan Brainard
 */
public class ClassLoaderRegisteringSerializingTranscoderTest {

    @Test
    public void testSimpleObject() throws Exception {
        Object o = new Something();
        assertEquals(serializationDeserialize(o), o);
    }

    @Test
    public void testSimpleObjectList() throws Exception {
        List<Something> os = new ArrayList<Something>(2);
        os.add(new Something());
        os.add(new Something());

        assertEquals(serializationDeserialize(os), os);
    }

    @Test
    public void testSimpleObjectArray() throws Exception {
        Something[] os = new Something[2];
        os[0] = new Something();
        os[1] = new Something();

        assertEquals(serializationDeserialize(os), os);
    }

    @Test
    public void testWrappedPrimitiveList() throws Exception {
        List<Long> os = new ArrayList<Long>(2);
        os.add(1L);
        os.add(2L);

        assertEquals(serializationDeserialize(os), os);
    }

    @Test
    public void testPrimitiveArray() throws Exception {
        long[] os = new long[2];
        os[0] = 1L;
        os[1] = 2L;

        assertEquals(serializationDeserialize(os), os);
    }

    @Test
    public void testMaterializedObject() throws Exception {
        final UuidContaining materializedObject = createMaterializedObject();

        assertEquals(serializationDeserialize(materializedObject).getUuid(), materializedObject.getUuid());
    }

    @Test
    public void testMaterializedObjectList() throws Exception {
        List<UuidContaining> os = new ArrayList<UuidContaining>(2);
        os.add(createMaterializedObject());
        os.add(createMaterializedObject());

        assertEquals(serializationDeserialize(os).get(0).getUuid(), os.get(0).getUuid());
        assertEquals(serializationDeserialize(os).get(1).getUuid(), os.get(1).getUuid());
    }

    @Test
    public void testMaterializedObjectArray() throws Exception {
        UuidContaining[] os = new UuidContaining[2];
        os[0] = createMaterializedObject();
        os[1] = createMaterializedObject();

        assertEquals(serializationDeserialize(os)[0].getUuid(), os[0].getUuid());
        assertEquals(serializationDeserialize(os)[1].getUuid(), os[1].getUuid());
    }

    private UuidContaining createMaterializedObject() throws IOException {
        final String asJson = "{ \"uuid\":\"" + UUID.randomUUID().toString() + "\" }";
        return new ObjectMapperProvider().getContext(null).readValue(asJson, UuidContaining.class);
    }

    private <T> T serializationDeserialize(T original) {
        final ClassLoaderRegisteringSerializingTranscoder transcoder = new ClassLoaderRegisteringSerializingTranscoder();
        final byte[] serialized = transcoder.serialize(original);
        @SuppressWarnings("unchecked") final T deserialized = (T) transcoder.deserialize(serialized);
        return deserialized;
    }
    
    public static interface UuidContaining extends Serializable {
        String getUuid();
    }
    
    static class Something implements Serializable {
        final String uuid = UUID.randomUUID().toString();

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Something something = (Something) o;

            if (uuid != null ? !uuid.equals(something.uuid) : something.uuid != null) return false;

            return true;
        }

        @Override
        public int hashCode() {
            return uuid != null ? uuid.hashCode() : 0;
        }
    }

}
