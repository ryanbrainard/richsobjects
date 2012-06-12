package com.github.ryanbrainard.richsobjects.api.client;

import java.io.*;
import java.lang.reflect.Array;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

class ObjectInputStreamWithRegisteredClassLoaders extends ObjectInputStream {

    private static Map<String, ClassLoader> registeredClassLoaders = new LinkedHashMap<String, ClassLoader>();
    static {
        registeredClassLoaders.put("", ClassLoader.getSystemClassLoader());
    }
    
    public ObjectInputStreamWithRegisteredClassLoaders(InputStream in) throws IOException {
        super(in);
    }

    /**
     * Make a primitive array class
     */

    private Class primitiveType(char type) {
        switch (type) {
            case 'B':
                return byte.class;
            case 'C':
                return char.class;
            case 'D':
                return double.class;
            case 'F':
                return float.class;
            case 'I':
                return int.class;
            case 'J':
                return long.class;
            case 'S':
                return short.class;
            case 'Z':
                return boolean.class;
            default:
                return null;
        }
    }

    /**
     * Use the given ClassLoader rather than using the system class
     */
    @Override
    protected Class resolveClass(ObjectStreamClass classDesc)
            throws IOException, ClassNotFoundException {
        final String cname = classDesc.getName();
        for (ClassLoader loader : registeredClassLoaders.values()) {

            if (cname.startsWith("[")) {
                // An array
                Class component;        // component class
                int dcount;            // dimension
                for (dcount = 1; cname.charAt(dcount) == '['; dcount++) ;
                if (cname.charAt(dcount) == 'L') {
                    try {
                        component = loader.loadClass(cname.substring(dcount + 1, cname.length() - 1));
                    } catch (ClassNotFoundException e) {
                        continue;
                    }
                } else {
                    if (cname.length() != dcount + 1) {
                        throw new ClassNotFoundException(cname);// malformed
                    }
                    component = primitiveType(cname.charAt(dcount));
                }
                int dim[] = new int[dcount];
                for (int i = 0; i < dcount; i++) {
                    dim[i] = 0;
                }
                return Array.newInstance(component, dim).getClass();
            } else {
                try {
                    return loader.loadClass(cname);
                } catch (ClassNotFoundException e) {
                    //noinspection UnnecessaryContinue
                    continue;
                }
            }
        }
        throw new ClassNotFoundException(cname);
    }

    static boolean isRegistered(Class loadee) {
        return registeredClassLoaders.containsKey(loadee.getName());
    }
    
    static void register(Class loadee) {
        registeredClassLoaders.put(loadee.getName(), loadee.getClassLoader());
    }
}