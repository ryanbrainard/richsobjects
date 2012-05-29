package com.github.ryanbrainard.richsobjects.api.client;

import com.github.ryanbrainard.richsobjects.api.model.GlobalDescription;
import com.github.ryanbrainard.richsobjects.api.model.SObjectDescription;
import com.github.ryanbrainard.richsobjects.api.model.BasicSObjectInformation;

import java.util.Map;

/**
 * @author Ryan Brainard
 */
public interface SfdcRestApiClient {

    GlobalDescription describeGlobal();

    BasicSObjectInformation describeSObjectBasic(String type);

    SObjectDescription describeSObject(String type);

    String createSObject(String type, Map<String, String> record);

    String updateSObject(String type, String id, Map<String, String> record);

    void deleteSObject(String type, String id);

    Map<String, String> getSObject(String sobject, String id);
}
