package com.github.ryanbrainard.richsobjects.filters;

import com.github.ryanbrainard.richsobjects.RichSObject;

import java.util.Iterator;

/**
* @author Ryan Brainard
*/
public class CreateableFieldsOnly extends IteratorFilter<RichSObject.RichField> {
    public CreateableFieldsOnly(Iterator<RichSObject.RichField> wrappedFields) {
        super(wrappedFields);
    }

    @Override
    boolean canBeNext(RichSObject.RichField maybeNext) {
        return maybeNext.getMetadata().isCreateable() && !FieldFilterUtil.isPersonAccountField(maybeNext);
    }
}
