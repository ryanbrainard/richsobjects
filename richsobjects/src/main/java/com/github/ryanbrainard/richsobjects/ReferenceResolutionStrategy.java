package com.github.ryanbrainard.richsobjects;

/**
* @author Ryan Brainard
*/
public interface ReferenceResolutionStrategy {
    Object resolve(RichSObject.RichField field);
}
