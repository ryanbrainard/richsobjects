package com.github.ryanbrainard.richsobjects;

/**
* @author Ryan Brainard
*/
enum ReferenceResolutionStrategies implements ReferenceResolutionStrategy {

    UNRESOLVED {
        @Override
        public String resolve(RichSObject.RichField field) {
            return field.asString();
        }
    },

    NAME_ONLY {
        @Override
        public String resolve(RichSObject.RichField field) {
            return field.asRef().get("NAME").asString();
        }
    },

    FULLY {
        @Override
        public RichSObject resolve(RichSObject.RichField field) {
            return field.asRef();
        }
    }
}
