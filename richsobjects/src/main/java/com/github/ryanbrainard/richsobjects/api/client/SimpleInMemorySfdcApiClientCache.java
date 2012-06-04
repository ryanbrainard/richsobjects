package com.github.ryanbrainard.richsobjects.api.client;

import com.github.ryanbrainard.richsobjects.api.model.BasicSObjectInformation;
import com.github.ryanbrainard.richsobjects.api.model.GlobalDescription;
import com.github.ryanbrainard.richsobjects.api.model.QueryResult;
import com.github.ryanbrainard.richsobjects.api.model.SObjectDescription;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Ryan Brainard
 */
class SimpleInMemorySfdcApiClientCache implements SfdcApiCache {
    
    private final SfdcApiClient api;
    private GlobalDescription cachedGlobalDescription;
    private Map<String,BasicSObjectInformation> cachedBasicSObjectInfos;
    private Map<String,SObjectDescription> cachedDescribeSObjectBasics;
    private Map<String, Map<String, ?>> cachedSObjects;

    SimpleInMemorySfdcApiClientCache(SfdcApiClient api) {
        this.api = api;
        clear();
    }

    @Override
    public void clear() {
        cachedGlobalDescription = null;
        cachedBasicSObjectInfos = new HashMap<String, BasicSObjectInformation>();
        cachedDescribeSObjectBasics = new HashMap<String, SObjectDescription>();
        cachedSObjects = new HashMap<String, Map<String, ?>>();
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
        if (cachedBasicSObjectInfos.containsKey(type)) {
            return cachedBasicSObjectInfos.get(type);
        } else {
            final BasicSObjectInformation value = api.describeSObjectBasic(type);
            cachedBasicSObjectInfos.put(type, value);
            return value;
        }
    }

    public SObjectDescription describeSObject(String type) {
        if (cachedDescribeSObjectBasics.containsKey(type)) {
            return cachedDescribeSObjectBasics.get(type);
        } else {
            final SObjectDescription value = api.describeSObject(type);
            cachedDescribeSObjectBasics.put(type, value);
            return value;
        }
    }

    public String createSObject(String type, Map<String, ?> record) {
        cachedBasicSObjectInfos.remove(type);
        return api.createSObject(type, record);
    }

    public void updateSObject(String type, String id, Map<String, ?> record) {
        cachedBasicSObjectInfos.remove(type);
        cachedSObjects.remove(id);
        api.updateSObject(type, id, record);
    }

    public void deleteSObject(String type, String id) {
        cachedBasicSObjectInfos.remove(type);
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
