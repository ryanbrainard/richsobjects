package com.github.ryanbrainard.richsobjects.api.client;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

class LruMap<A,B> extends LinkedHashMap<A,B> {
    
    private final int maxEntries;

    public LruMap(final int maxEntries) {
        super(maxEntries + 1, 1.0f, true);
        this.maxEntries = maxEntries;
    }

    @Override
    protected boolean removeEldestEntry(final Map.Entry<A,B> eldest) {
        return super.size() > maxEntries;
    }

    static <A,B> Map<A,B> newSync(final int maxEntries) {
        return Collections.synchronizedMap(new LruMap<A, B>(maxEntries));
    }
}
