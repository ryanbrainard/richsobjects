package com.github.ryanbrainard.richsobjects;

import java.util.Iterator;

/**
 * @author Ryan Brainard
 */
public interface RichSObject extends Iterable<RichSObject.RichField> {

    DescribeSObject getMetadata();

    RichField get(String fieldName);

    Iterator<RichField> getFields();

    public interface RichField {
        RichSObject getParent();
        DescribeSObject.Field getMetadata();
        Object getValue();
    }
}
