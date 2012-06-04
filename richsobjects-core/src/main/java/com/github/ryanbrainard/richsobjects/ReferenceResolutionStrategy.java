package com.github.ryanbrainard.richsobjects;

/**
* @author Ryan Brainard
*/
interface ReferenceResolutionStrategy {
    Object resolve(RichSObject.RichField field);
}
