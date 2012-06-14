package com.github.ryanbrainard.richsobjects.filters;

import com.github.ryanbrainard.richsobjects.RichSObject;

/**
 * @author Ryan Brainard
 */
class FieldFilterUtil {

    static boolean isPersonAccountField(RichSObject.RichField maybeNext) {
        if ("Account".equals(maybeNext.getParent().getMetadata().getName())) {
            final String fieldName = maybeNext.getMetadata().getName();
            if ("Salutation".equals(fieldName) || "FirstName".equals(fieldName) || "LastName".equals(fieldName) || fieldName.endsWith("__pc")) {
                return true;
            }
        }

        return false;
    }

}
