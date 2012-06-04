package com.github.ryanbrainard.richsobjects.filters;

import com.github.ryanbrainard.richsobjects.RichSObject;

import java.util.Iterator;

/**
* @author Ryan Brainard
*/
class UpdateableFieldsOnly extends IteratorFilter<RichSObject.RichField> {
    public UpdateableFieldsOnly(Iterator<RichSObject.RichField> wrappedFields) {
        super(wrappedFields);
    }

    @Override
    boolean canBeNext(RichSObject.RichField maybeNext) {
        return maybeNext.getMetadata().isUpdateable() && !FieldFilterUtil.isPersonAccountField(maybeNext);
    }
}
