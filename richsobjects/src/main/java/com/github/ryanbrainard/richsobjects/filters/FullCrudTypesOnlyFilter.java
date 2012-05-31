package com.github.ryanbrainard.richsobjects.filters;

import com.github.ryanbrainard.richsobjects.api.model.SObjectDescription;

import java.util.Iterator;

/**
 * @author Ryan Brainard
 */
public class FullCrudTypesOnlyFilter extends IteratorFilter<SObjectDescription> {

    public FullCrudTypesOnlyFilter(Iterator<SObjectDescription> filteree) {
        super(filteree);
    }

    @Override
    boolean canBeNext(SObjectDescription maybeNext) {
        return maybeNext.isCreateable() &&
               maybeNext.isQueryable()  &&
               maybeNext.isUpdateable() &&
               maybeNext.isDeletable();
    }
}
