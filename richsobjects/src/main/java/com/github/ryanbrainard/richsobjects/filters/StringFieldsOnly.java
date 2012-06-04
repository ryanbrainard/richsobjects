package com.github.ryanbrainard.richsobjects.filters;

import com.github.ryanbrainard.richsobjects.RichSObject;

import java.util.Iterator;

/**
* @author Ryan Brainard
*/
public class StringFieldsOnly extends IteratorFilter<RichSObject.RichField> {
    public StringFieldsOnly(Iterator<RichSObject.RichField> wrappedFields) {
        super(wrappedFields);
    }

    @Override
    boolean canBeNext(RichSObject.RichField maybeNext) {
        return "string".equals(maybeNext.getMetadata().getType());
    }
}
