package com.github.ryanbrainard.richsobjects.api.model;

import java.util.List;

/**
 * @author Ryan Brainard
 */
public interface SObjectDescription extends BasicSObjectDescription {

    List<? extends Field> getFields();

    List<? extends ChildEntity> getChildRelationships();

    static interface Field {
        Integer getLength();

        String getName();

        String getType();

        String getDefaultValue();

        String getLabel();

        Boolean isUpdateable();

        Boolean isCalculated();

        Boolean isUnique();

        Boolean isNillable();

        Boolean isCaseSensitive();

        String getInlineHelpText();

        Boolean isNameField();

        Boolean isExternalId();

        Boolean isIdLookup();

        Boolean isFilterable();

        Boolean isCreateable();

        Boolean isDeprecatedAndHidden();

        Boolean isAutoNumber();

        Boolean isDefaultedOnCreate();

        Boolean isGroupable();

        String getRelationshipName();

        List<String> getReferenceToEntity();

        Boolean isRestrictedPicklist();

        Boolean isNamePointing();

        Boolean isCustom();

        Boolean isHtmlFormatted();

        Boolean isDependentPicklist();

        Boolean isWriteRequiresMasterRead();

        Boolean isSortable();
    }

    static interface ChildEntity {
        String getField();

        String getChildSObject();

        String getRelationshipName();

        Boolean isDeprecatedAndHidden();

        Boolean isCascadeDelete();
    }
}
