package com.github.ryanbrainard.richsobjects.filters;

import com.github.ryanbrainard.richsobjects.api.model.BasicSObjectDescription;

import java.util.Iterator;

/**
 * @author Ryan Brainard
 */
public class FullCrudTypesOnlyFilter extends IteratorFilter<BasicSObjectDescription> {

    public FullCrudTypesOnlyFilter(Iterator<BasicSObjectDescription> filteree) {
        super(filteree);
    }

    @Override
    boolean canBeNext(BasicSObjectDescription maybeNext) {
        return maybeNext.isCreateable() &&
               maybeNext.isQueryable()  &&
               maybeNext.isUpdateable() &&
               maybeNext.isDeletable();
    }
}
