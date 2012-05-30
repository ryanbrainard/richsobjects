package com.github.ryanbrainard.richsobjects.service;


import com.github.ryanbrainard.richsobjects.RichSObject;
import com.github.ryanbrainard.richsobjects.api.model.BasicSObjectDescription;
import com.github.ryanbrainard.richsobjects.api.model.SObjectDescription;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author Ryan Brainard
 */
public interface RichSObjectsService {

    List<BasicSObjectDescription> listSObjectTypes();

    Iterator<RichSObject> getRecentItems(String type);

    RichSObject getSObject(String type, String id);

    SObjectDescription describeSObjectType(String type);

    RichSObject newSObject(String type);

    RichSObject existingSObject(String type, Map<String, ?> record);

    void updateSObject(String type, String id, Map<String, ?> record);

    String createSObject(String type, Map<String, ?> record);

    void deleteSObject(String type, String id);

}
