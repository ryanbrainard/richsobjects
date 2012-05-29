package com.github.ryanbrainard.richsobjects;

import com.github.ryanbrainard.richsobjects.api.model.SObjectDescription;

import java.util.Iterator;

/**
 * @author Ryan Brainard
 */
public interface RichSObject extends Iterable<RichSObject.RichField> {

    SObjectDescription getMetadata();

    RichField get(String fieldName);

    Iterator<RichField> getFields();

    public interface RichField {

        RichSObject getParent();

        SObjectDescription.Field getMetadata();

        Object getValue();

    }
}
