package com.github.ryanbrainard.richsobjects.api.model;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author Ryan Brainard
 */
public interface BasicSObjectInformation extends Serializable {
    BasicSObjectDescription getBasicObjectDescribe();

    List<Map<String, ?>> getRecentItems();
}
