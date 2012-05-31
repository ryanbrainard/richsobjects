package com.github.ryanbrainard.richobjects;

import com.github.ryanbrainard.richsobjects.api.model.SObjectDescription;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

import java.util.List;
import java.util.Map;

/**
 * @author Ryan Brainard
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SObjectDescriptionImpl implements SObjectDescription {

    @JsonProperty private String name;
    @JsonProperty private String label;
    @JsonProperty private Boolean custom;
    @JsonProperty private String keyPrefix;
    @JsonProperty private String labelPlural;
    @JsonProperty private Boolean layoutable;
    @JsonProperty private Boolean activateable;
    @JsonProperty private Boolean updateable;
    @JsonProperty private Map<String, String> urls;
    @JsonProperty private Boolean createable;
    @JsonProperty private Boolean deletable;
    @JsonProperty private Boolean feedEnabled;
    @JsonProperty private Boolean queryable;
    @JsonProperty private Boolean replicateable;
    @JsonProperty private Boolean retrieveable;
    @JsonProperty private Boolean undeletable;
    @JsonProperty private Boolean triggerable;
    @JsonProperty private Boolean mergeable;
    @JsonProperty private Boolean deprecatedAndHidden;
    @JsonProperty private Boolean customSetting;
    @JsonProperty private Boolean searchable;
    @JsonProperty private List<FieldImpl> fields;
    @JsonProperty private List<ChildEntityImpl> childRelationships;

    public String getName() {
        return name;
    }

    public String getLabel() {
        return label;
    }

    public Boolean isCustom() {
        return custom;
    }

    public String getKeyPrefix() {
        return keyPrefix;
    }

    public String getLabelPlural() {
        return labelPlural;
    }

    public Boolean isLayoutable() {
        return layoutable;
    }

    public Boolean isActivateable() {
        return activateable;
    }

    public Boolean isUpdateable() {
        return updateable;
    }

    public Map<String, String> getUrls() {
        return urls;
    }

    public Boolean isCreateable() {
        return createable;
    }

    public Boolean isDeletable() {
        return deletable;
    }

    public Boolean isFeedEnabled() {
        return feedEnabled;
    }

    public Boolean isQueryable() {
        return queryable;
    }

    public Boolean isReplicateable() {
        return replicateable;
    }

    public Boolean isRetrieveable() {
        return retrieveable;
    }

    public Boolean isUndeletable() {
        return undeletable;
    }

    public Boolean isTriggerable() {
        return triggerable;
    }

    public Boolean isMergeable() {
        return mergeable;
    }

    public Boolean isDeprecatedAndHidden() {
        return deprecatedAndHidden;
    }

    public Boolean isCustomSetting() {
        return customSetting;
    }

    public Boolean isSearchable() {
        return searchable;
    }

    public List<? extends Field> getFields() {
        return fields;
    }

    public List<? extends ChildEntity> getChildRelationships() {
        return childRelationships;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class FieldImpl implements Field {
        @JsonProperty private Integer length;
        @JsonProperty private String name;
        @JsonProperty private String type;
        @JsonProperty private String defaultValue;
        @JsonProperty private String label;
        @JsonProperty private Boolean updateable;
        @JsonProperty private Boolean calculated;
        @JsonProperty private Boolean unique;
        @JsonProperty private Boolean nillable;
        @JsonProperty private Boolean caseSensitive;
        @JsonProperty private String inlineHelpText;
        @JsonProperty private Boolean nameField;
        @JsonProperty private Boolean externalId;
        @JsonProperty private Boolean idLookup;
        @JsonProperty private Boolean filterable;
        // soapType;
        @JsonProperty private Boolean createable;
        @JsonProperty private Boolean deprecatedAndHidden;
        // picklistValues;
        @JsonProperty private Boolean autoNumber;
        @JsonProperty private Boolean defaultedOnCreate;
        @JsonProperty private Boolean groupable;
        @JsonProperty private String relationshipName;
        @JsonProperty private List<String> referenceTo;
        // relationshipOrder;
        @JsonProperty private Boolean restrictedPicklist;
        @JsonProperty private Boolean namePointing;
        @JsonProperty private Boolean custom;
        @JsonProperty private Boolean htmlFormatted;
        @JsonProperty private Boolean dependentPicklist;
        @JsonProperty private Boolean writeRequiresMasterRead;
        @JsonProperty private Boolean sortable;

        public Integer getLength() {
            return length;
        }

        public String getName() {
            return name;
        }

        public String getType() {
            return type;
        }

        public String getDefaultValue() {
            return defaultValue;
        }

        public String getLabel() {
            return label;
        }

        public Boolean isUpdateable() {
            return updateable;
        }

        public Boolean isCalculated() {
            return calculated;
        }

        public Boolean isUnique() {
            return unique;
        }

        public Boolean isNillable() {
            return nillable;
        }

        public Boolean isCaseSensitive() {
            return caseSensitive;
        }

        public String getInlineHelpText() {
            return inlineHelpText;
        }

        public Boolean isNameField() {
            return nameField;
        }

        public Boolean isExternalId() {
            return externalId;
        }

        public Boolean isIdLookup() {
            return idLookup;
        }

        public Boolean isFilterable() {
            return filterable;
        }

        public Boolean isCreateable() {
            return createable;
        }

        public Boolean isDeprecatedAndHidden() {
            return deprecatedAndHidden;
        }

        public Boolean isAutoNumber() {
            return autoNumber;
        }

        public Boolean isDefaultedOnCreate() {
            return defaultedOnCreate;
        }

        public Boolean isGroupable() {
            return groupable;
        }

        public String getRelationshipName() {
            return relationshipName;
        }

        public List<String> getReferenceToEntity() {
            return referenceTo;
        }

        public Boolean isRestrictedPicklist() {
            return restrictedPicklist;
        }

        public Boolean isNamePointing() {
            return namePointing;
        }

        public Boolean isCustom() {
            return custom;
        }

        public Boolean isHtmlFormatted() {
            return htmlFormatted;
        }

        public Boolean isDependentPicklist() {
            return dependentPicklist;
        }

        public Boolean isWriteRequiresMasterRead() {
            return writeRequiresMasterRead;
        }

        public Boolean isSortable() {
            return sortable;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ChildEntityImpl implements ChildEntity {
        @JsonProperty private String field;
        @JsonProperty private String childSObject;
        @JsonProperty private String relationshipName;
        @JsonProperty private Boolean deprecatedAndHidden;
        @JsonProperty private Boolean cascadeDelete;

        public String getField() {
            return field;
        }

        public String getChildSObject() {
            return childSObject;
        }

        public String getRelationshipName() {
            return relationshipName;
        }

        public Boolean isDeprecatedAndHidden() {
            return deprecatedAndHidden;
        }

        public Boolean isCascadeDelete() {
            return cascadeDelete;
        }
    }
}
