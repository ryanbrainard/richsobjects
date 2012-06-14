package com.github.ryanbrainard.richsobjects.api.client;

import com.github.ryanbrainard.richsobjects.api.model.BasicSObjectInformation;
import com.github.ryanbrainard.richsobjects.api.model.GlobalDescription;
import com.github.ryanbrainard.richsobjects.api.model.QueryResult;
import com.github.ryanbrainard.richsobjects.api.model.SObjectDescription;

import java.util.Map;

/**
 * @author Ryan Brainard
 */
@SuppressWarnings("UnusedDeclaration")
class SimpleInMemoryUserCache implements SfdcApiClient {
    
    private final SfdcApiClient api;
    private GlobalDescription cachedGlobalDescription;
    private Map<String,SObjectDescription> cachedSObjectDescriptions;
    private Map<String, Map<String, ?>> cachedSObjects;

    SimpleInMemoryUserCache(SfdcApiClient api) {
        this.api = api;
        cachedGlobalDescription = null;
        cachedSObjectDescriptions = LruMap.newSync(5);
        cachedSObjects = LruMap.newSync(10);
    }

    public GlobalDescription describeGlobal() {
        if (cachedGlobalDescription != null) {
            return cachedGlobalDescription;
        } else {
            cachedGlobalDescription = api.describeGlobal();
            return cachedGlobalDescription;
        }
    }

    public BasicSObjectInformation describeSObjectBasic(String type) {
        return api.describeSObjectBasic(type);
    }

    public SObjectDescription describeSObject(String type) {
        if (cachedSObjectDescriptions.containsKey(type)) {
            return cachedSObjectDescriptions.get(type);
        } else {
            final SObjectDescription value = api.describeSObject(type);
            cachedSObjectDescriptions.put(type, value);
            return value;
        }
    }

    public String createSObject(String type, Map<String, ?> record) {
        return api.createSObject(type, record);
    }

    public void updateSObject(String type, String id, Map<String, ?> record) {
        cachedSObjects.remove(id);
        api.updateSObject(type, id, record);
    }

    public void deleteSObject(String type, String id) {
        cachedSObjects.remove(id);
        api.deleteSObject(type, id);
    }

    public Map<String, ?> getSObject(String type, String id) {
        if (cachedSObjects.containsKey(id)) {
            return cachedSObjects.get(id);
        } else {
            final Map<String, ?> value = api.getSObject(type, id);
            cachedSObjects.put(id, value);
            return value;
        }
    }

    public QueryResult query(String soql) {
        return api.query(soql);
    }

    public QueryResult queryMore(String nextRecordsUrl) {
        return api.queryMore(nextRecordsUrl);
    }

    public String getRawBase64Content(String contentUrl) {
        return api.getRawBase64Content(contentUrl);
    }
}
