package com.github.ryanbrainard.richsobjects;


import com.github.ryanbrainard.richsobjects.api.client.SfdcApiClient;
import com.github.ryanbrainard.richsobjects.api.model.BasicSObjectDescription;
import com.github.ryanbrainard.richsobjects.api.model.SObjectDescription;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author Ryan Brainard
 */
public interface RichSObjectsService {

    /**
     * Underlying API client. This may or not be cached.
     */
    SfdcApiClient api();

    /**
     * List of all standard and custom object types with basic metadata
     */
    List<BasicSObjectDescription> types();

    /**
     * Detailed metadata about a type
     */
    SObjectDescription describe(String type);

    /**
     * Create an empty, in-memory RichSObject of a given type
     * with metadata, but fields left unpopulated.
     */
    RichSObject unpopulated(String type);

    /**
     * Insert a record of a given type into Salesforce
     *
     * @return latest version of RichSObject
     */
    RichSObject insert(String type, Map<String, ?> record);

    /**
     * Insert a record into Salesforce
     *
     * @return latest version of RichSObject
     */
    RichSObject insert(RichSObject record);

    /**
     * Fetch a record of a given type from Salesforce
     */
    RichSObject fetch(String type, String id);

    /**
     * Update a record of a given type in Salesforce
     *
     * @return latest version of RichSObject
     */
    RichSObject update(String type, String id, Map<String, ?> record);

    /**
     * Update a record in Salesforce
     *
     * @return latest version of RichSObject
     */
    RichSObject update(RichSObject record);

    /**
     * Delete a record of a given type in Salesforce
     */
    void delete(String type, String id);

    /**
     * Delete a record in Salesforce
     */
    void delete(RichSObject record);

    /**
     * Recently viewed items of a given type.
     * A tab must exist for this type for recent items to be tracked.
     */
    Iterator<RichSObject> recentItems(String type);

    /**
     * Perform a SOQL query against Salesforce
     */
    Iterator<RichSObject> query(String soql);

}
