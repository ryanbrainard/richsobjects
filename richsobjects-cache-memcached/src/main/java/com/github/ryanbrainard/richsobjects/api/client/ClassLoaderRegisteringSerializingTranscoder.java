package com.github.ryanbrainard.richsobjects.api.client;

import net.spy.memcached.compat.CloseUtil;
import net.spy.memcached.transcoders.SerializingTranscoder;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

/**
 *
 * Specialized {@link SerializingTranscoder} that registers the class loader with
 * {@link ClassLoaderRegisteringObjectInputStream} upon serialization so it can
 * be used during deserialization. This is not ideal, but is needed for classes
 * that do not use the system class loader.
 *
 * @author Ryan Brainard
 */
public class ClassLoaderRegisteringSerializingTranscoder extends SerializingTranscoder {

    /**
     * Same as method in {@link SerializingTranscoder}, but registers object's class loader with
     * {@link ClassLoaderRegisteringObjectInputStream} for later deserialization.
     */
    @Override
    protected byte[] serialize(Object o) {
        ClassLoaderRegisteringObjectInputStream.register(o.getClass());
        return super.serialize(o);
    }

    /**
     * Same as method in {@link SerializingTranscoder}, but uses
     * {@link ClassLoaderRegisteringObjectInputStream} for deserialization.
     */
    @Override
    protected Object deserialize(byte[] in) {
        Object rv = null;
        ByteArrayInputStream bis = null;
        ObjectInputStream is = null;
        try {
            if (in != null) {
                bis = new ByteArrayInputStream(in);
                is = new ClassLoaderRegisteringObjectInputStream(bis);
                rv = is.readObject();
                is.close();
                bis.close();
            }
        } catch (IOException e) {
            getLogger().warn("Caught IOException decoding %d bytes of data",
                    in == null ? 0 : in.length, e);
        } catch (ClassNotFoundException e) {
            getLogger().warn("Caught CNFE decoding %d bytes of data",
                    in == null ? 0 : in.length, e);
        } finally {
            CloseUtil.close(is);
            CloseUtil.close(bis);
        }
        return rv;
    }
}
