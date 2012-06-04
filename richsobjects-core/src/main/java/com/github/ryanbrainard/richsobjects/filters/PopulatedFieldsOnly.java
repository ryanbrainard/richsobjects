package com.github.ryanbrainard.richsobjects.filters;

import com.github.ryanbrainard.richsobjects.RichSObject;

import java.util.Iterator;

/**
* @author Ryan Brainard
*/
public class PopulatedFieldsOnly extends IteratorFilter<RichSObject.RichField> {
    public PopulatedFieldsOnly(Iterator<RichSObject.RichField> wrappedFields) {
        super(wrappedFields);
    }

    @Override
    boolean canBeNext(RichSObject.RichField maybeNext) {
        return maybeNext.getValue() != null;
    }
}
