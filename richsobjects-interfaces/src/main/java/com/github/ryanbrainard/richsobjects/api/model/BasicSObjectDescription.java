package com.github.ryanbrainard.richsobjects.api.model;

import java.io.Serializable;
import java.util.Map;

/**
 * @author Ryan Brainard
 */
public interface BasicSObjectDescription extends Serializable {

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

}
