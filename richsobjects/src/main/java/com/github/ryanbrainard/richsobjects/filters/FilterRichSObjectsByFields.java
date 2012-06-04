package com.github.ryanbrainard.richsobjects.filters;

import com.github.ryanbrainard.richsobjects.RichSObject;

import java.util.Iterator;

/**
 * Convenience class for filtering fields at the record level.
 * This can be used when access to filter during field iteration is not possible.
 *
 * @author Ryan Brainard
 */
public class FilterRichSObjectsByFields extends RichSObjectWrapper {

    private final IteratorFilter<RichField> fieldFilterIterator;

    public FilterRichSObjectsByFields(RichSObject wrapped, IteratorFilter<RichSObject.RichField> filter) {
        super(wrapped);
        fieldFilterIterator = filter;
    }

    @Override
    public Iterator<RichField> getFields() {
        return fieldFilterIterator;
    }

    public static FilterRichSObjectsByFields CreateableFieldsOnly(RichSObject wrapped) {
        return new FilterRichSObjectsByFields(wrapped, new CreateableFieldsOnly(wrapped.getFields()));
    }

    public static FilterRichSObjectsByFields UpdateableFieldsOnly(RichSObject wrapped) {
        return new FilterRichSObjectsByFields(wrapped, new UpdateableFieldsOnly(wrapped.getFields()));
    }

    public static FilterRichSObjectsByFields PopulatedFieldsOnly(RichSObject wrapped) {
        return new FilterRichSObjectsByFields(wrapped, new PopulatedFieldsOnly(wrapped.getFields()));
    }

    public static FilterRichSObjectsByFields StringFieldsOnly(RichSObject wrapped) {
        return new FilterRichSObjectsByFields(wrapped, new StringFieldsOnly(wrapped.getFields()));
    }

}
