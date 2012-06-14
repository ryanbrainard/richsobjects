package com.github.ryanbrainard.richsobjects.filters;

import com.github.ryanbrainard.richsobjects.RichSObject;
import com.github.ryanbrainard.richsobjects.api.model.SObjectDescription;

import java.util.Iterator;
import java.util.Map;

/**
 * @author Ryan Brainard
 */
public abstract class RichSObjectWrapper implements RichSObject {

    protected final RichSObject wrapped;

    protected RichSObjectWrapper(RichSObject wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public SObjectDescription getMetadata() {
        return wrapped.getMetadata();
    }

    @Override
    public RichField get(String fieldName) {
        return wrapped.get(fieldName);
    }

    @Override
    public Iterator<RichField> getFields() {
        return wrapped.getFields();
    }

    @Override
    public boolean hasField(String fieldName) {
        return wrapped.hasField(fieldName);
    }

    @Override
    public Map<String, Object> getRaw() {
        return wrapped.getRaw();
    }

    @Override
    public Iterator<RichField> iterator() {
        return wrapped.iterator();
    }
}
