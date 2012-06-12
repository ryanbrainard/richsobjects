package com.github.ryanbrainard.richsobjects.api.client;

import net.spy.memcached.compat.CloseUtil;
import net.spy.memcached.transcoders.SerializingTranscoder;

import java.io.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Ryan Brainard
 */
public class SerializingTranscoderWithRegisteredClassLoaders extends SerializingTranscoder {

    @Override
    protected Object deserialize(byte[] in) {
        Object rv=null;
        ByteArrayInputStream bis = null;
        ObjectInputStream is = null;
        try {
            if(in != null) {
                bis=new ByteArrayInputStream(in);
                is=new ObjectInputStreamWithRegisteredClassLoaders(bis);
                rv=is.readObject();
                is.close();
                bis.close();
            }
        } catch(IOException e) {
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
