package com.github.ryanbrainard.richsobjects.service;

import com.github.ryanbrainard.richsobjects.ImmutableRichSObject;
import com.github.ryanbrainard.richsobjects.RichSObject;
import com.github.ryanbrainard.richsobjects.api.client.SfdcApiClient;
import com.github.ryanbrainard.richsobjects.api.client.SfdcApiLoader;
import com.github.ryanbrainard.richsobjects.api.model.BasicSObjectDescription;
import com.github.ryanbrainard.richsobjects.api.model.QueryResult;
import com.github.ryanbrainard.richsobjects.api.model.SObjectDescription;

import java.util.*;

/**
 * @author Ryan Brainard
 */
public class RichSObjectsServiceImpl implements RichSObjectsService {

    protected SfdcApiClient getApiClient() {
        return SfdcApiLoader.get(24.0);
    }

    @Override
    public List<BasicSObjectDescription> listSObjectTypes() {
        final List<BasicSObjectDescription> metadata = getApiClient().describeGlobal().getSObjects();
        Collections.sort(metadata, new Comparator<BasicSObjectDescription>() {
            @Override
            public int compare(BasicSObjectDescription o1, BasicSObjectDescription o2) {
                return o1.getLabel().compareTo(o2.getLabel());
            }
        });
        return Collections.unmodifiableList(metadata);
    }

    @Override
    public SObjectDescription describeSObjectType(String type) {
        return getApiClient().describeSObject(type);
    }

    @Override
    public RichSObject newSObject(String type) {
        //noinspection unchecked
        return new ImmutableRichSObject(describeSObjectType(type), Collections.EMPTY_MAP);
    }

    @Override
    public RichSObject existingSObject(String type, Map<String, ?> record) {
        return new ImmutableRichSObject(describeSObjectType(type), record);
    }
    
    @Override
    public String createSObject(String type, Map<String, ?> record) {
        return getApiClient().createSObject(type, record);
    }

    @Override
    public void updateSObject(String type, String id, Map<String, ?> record) {
        getApiClient().updateSObject(type, id, record);
    }

    @Override
    public void deleteSObject(String type, String id) {
        getApiClient().deleteSObject(type, id);
    }

    @Override
    public RichSObject getSObject(String type, String id) {
        return new ImmutableRichSObject(describeSObjectType(type), getRawSObject(type, id));
    }

    private Map<String, ?> getRawSObject(String sobject, String id) {
        return getApiClient().getSObject(sobject, id);
    }

    @Override
    public Iterator<RichSObject> getRecentItems(String type) {
        final SObjectDescription metadata = describeSObjectType(type);
        final Iterator<Map<String,?>> rawRecentItems = getApiClient().describeSObjectBasic(type).getRecentItems().iterator();

        return new Iterator<RichSObject>() {
            @Override
            public boolean hasNext() {
                return rawRecentItems.hasNext();
            }

            @Override
            public RichSObject next() {
                return new ImmutableRichSObject(metadata, rawRecentItems.next());
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("Recent items cannot be removed");
            }
        };
    }

    @Override
    public Iterator<RichSObject> query(final String soql) {
        final String type = soql.replaceFirst(".*FROM\\s+(\\w+).*", "$1");
        final SObjectDescription metadata = describeSObjectType(type);
        
        return new Iterator<RichSObject>() {
            QueryResult queryResult = getApiClient().query(soql);
            Iterator<Map<String, ?>> queryResultItr = queryResult.getRecords().iterator();
            
            @Override
            public boolean hasNext() {
                return queryResultItr.hasNext() || !queryResult.isDone();
            }

            @Override
            public RichSObject next() {
                if (queryResultItr.hasNext()) {
                    return new ImmutableRichSObject(metadata, queryResultItr.next());
                } else if (!queryResult.isDone()) {
                    queryResult = getApiClient().queryMore(queryResult.getNextRecordsUrl());
                    queryResultItr = queryResult.getRecords().iterator();
                    return next();
                } else {
                    throw new NoSuchElementException();
                }
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("Query results cannot be removed");
            }
        };
    }

}
