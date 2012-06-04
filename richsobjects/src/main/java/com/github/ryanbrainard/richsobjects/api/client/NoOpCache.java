package com.github.ryanbrainard.richsobjects.api.client;

import com.github.ryanbrainard.richsobjects.api.model.BasicSObjectInformation;
import com.github.ryanbrainard.richsobjects.api.model.GlobalDescription;
import com.github.ryanbrainard.richsobjects.api.model.QueryResult;
import com.github.ryanbrainard.richsobjects.api.model.SObjectDescription;

import java.util.Map;

/**
 * @author Ryan Brainard
 */
public class NoOpCache implements SfdcApiCache {

    private final SfdcApiClient api;

    public NoOpCache(SfdcApiClient api) {
        this.api = api;
    }

    @Override
    public GlobalDescription describeGlobal() {
        return api.describeGlobal();
    }

    @Override
    public BasicSObjectInformation describeSObjectBasic(String type) {
        return api.describeSObjectBasic(type);
    }

    @Override
    public SObjectDescription describeSObject(String type) {
        return api.describeSObject(type);
    }

    @Override
    public String createSObject(String type, Map<String, ?> record) {
        return api.createSObject(type, record);
    }

    @Override
    public void updateSObject(String type, String id, Map<String, ?> record) {
        api.updateSObject(type, id, record);
    }

    @Override
    public void deleteSObject(String type, String id) {
        api.deleteSObject(type, id);
    }

    @Override
    public Map<String, ?> getSObject(String type, String id) {
        return api.getSObject(type, id);
    }

    @Override
    public QueryResult query(String soql) {
        return api.query(soql);
    }

    @Override
    public QueryResult queryMore(String nextRecordsUrl) {
        return api.queryMore(nextRecordsUrl);
    }

    @Override
    public String getRawBase64Content(String contentUrl) {
        return api.getRawBase64Content(contentUrl);
    }

    @Override
    public void clear() {
        // no-op
    }
}
