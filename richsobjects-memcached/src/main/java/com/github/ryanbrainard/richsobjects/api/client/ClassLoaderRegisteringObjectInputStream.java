package com.github.ryanbrainard.richsobjects.api.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;
import java.lang.reflect.Array;
import java.util.HashSet;
import java.util.Set;

class ClassLoaderRegisteringObjectInputStream extends ObjectInputStream {

    private static Set<ClassLoader> classLoaderRegistration = new HashSet<ClassLoader>();

    public ClassLoaderRegisteringObjectInputStream(InputStream in) throws IOException {
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
     * Use the registered ClassLoaders to resolve and then use system class loader
     */
    @Override
    protected Class resolveClass(ObjectStreamClass classDesc) throws IOException, ClassNotFoundException {
        for (ClassLoader loader : classLoaderRegistration) {
            try {
                return resolveClass(classDesc, loader);
            } catch (ClassNotFoundException e) {
                // ignore
            }
        }
        return super.resolveClass(classDesc);
    }

    protected Class resolveClass(ObjectStreamClass classDesc, ClassLoader loader) throws ClassNotFoundException {
        final String cname = classDesc.getName();
        if (cname.startsWith("[")) {
            // An array
            Class component;        // component class
            int dcount;            // dimension
            for (dcount = 1; cname.charAt(dcount) == '['; dcount++) ;
            if (cname.charAt(dcount) == 'L') {
                component = loader.loadClass(cname.substring(dcount + 1, cname.length() - 1));
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
            return loader.loadClass(cname);
        }
    }
    
    static void register(Class loadee) {
        if (loadee.getClassLoader() != null && loadee.getClassLoader() != ClassLoader.getSystemClassLoader()) {
            classLoaderRegistration.add(loadee.getClassLoader());
        }
    }
}