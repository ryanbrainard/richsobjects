package com.github.ryanbrainard.richsobjects.api.model;

import java.util.List;
import java.util.Map;

/**
 * @author Ryan Brainard
 */
public interface BasicSObjectInformation {
    BasicSObjectDescription getBasicObjectDescribe();

    List<Map<String, ?>> getRecentItems();
}
