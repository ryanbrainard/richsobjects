package com.github.ryanbrainard.richsobjects;

import java.util.List;
import java.util.Map;

interface DescribeSObject {

    String getName();

    String getLabel();

    Boolean isCustom();

    String getKeyPrefix();

    String getLabelPlural();

    Boolean isLayoutable();

    Boolean isActivateable();

    Boolean isUpdateable();

    Map<String, String> getUrls();

    Boolean isCreateable();

    Boolean isDeletable();

    Boolean isFeedEnabled();

    Boolean isQueryable();

    Boolean isReplicateable();

    Boolean isRetrieveable();

    Boolean isUndeletable();

    Boolean isTriggerable();

    Boolean isMergeable();

    Boolean isDeprecatedAndHidden();

    Boolean isCustomSetting();

    Boolean isSearchable();

    List<Field> getFields();

    List<ChildEntity> getChildRelationships();

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
