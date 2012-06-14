package com.github.ryanbrainard.richsobjects;

import com.github.ryanbrainard.richsobjects.api.client.SfdcApiClient;
import com.github.ryanbrainard.richsobjects.api.client.SfdcApiLoader;
import com.github.ryanbrainard.richsobjects.api.model.BasicSObjectDescription;
import com.github.ryanbrainard.richsobjects.api.model.QueryResult;
import com.github.ryanbrainard.richsobjects.api.model.SObjectDescription;
import com.github.ryanbrainard.richsobjects.filters.UpdateableFieldsOnly;

import java.util.*;

/**
 * @author Ryan Brainard
 */
public class RichSObjectsServiceImpl implements RichSObjectsService {

    @Override
    public SfdcApiClient api() {
        return SfdcApiLoader.get(24.0);
    }

    @Override
    public List<BasicSObjectDescription> types() {
        final List<BasicSObjectDescription> metadata = api().describeGlobal().getSObjects();
        Collections.sort(metadata, new Comparator<BasicSObjectDescription>() {
            @Override
            public int compare(BasicSObjectDescription o1, BasicSObjectDescription o2) {
                return o1.getLabel().compareTo(o2.getLabel());
            }
        });
        return Collections.unmodifiableList(metadata);
    }

    @Override
    public SObjectDescription describe(String type) {
        return api().describeSObject(type);
    }

    @Override
    public RichSObject unpopulated(String type) {
        return new ImmutableRichSObject(this, describe(type), new HashMap<String, Object>());
    }

    @Override
    public RichSObject insert(String type, Map<String, ?> record) {
        final String id = api().createSObject(type, record);
        return fetch(type, id);
    }

    @Override
    public RichSObject insert(RichSObject record) {
        return insert(record.getMetadata().getName(), record.getRaw());
    }

    @Override
    public RichSObject update(String type, String id, Map<String, ?> record) {
        api().updateSObject(type, id, record);
        return fetch(type, id);
    }

    @Override
    public RichSObject update(RichSObject record) {
        final Map<String, Object> rawRecords = record.getRaw();
        final Map<String, Object> updateableFields = new HashMap<String, Object>(rawRecords.size());
        for (RichSObject.RichField f : new UpdateableFieldsOnly(record.getFields())) {
            final String fieldName = f.getMetadata().getName().toUpperCase();
            if (rawRecords.containsKey(fieldName)) {
                updateableFields.put(fieldName, rawRecords.get(fieldName));
            }
        }
        
        return update(record.getMetadata().getName(), record.get("id").asString(), updateableFields);
    }

    @Override
    public void delete(String type, String id) {
        api().deleteSObject(type, id);
    }

    @Override
    public void delete(RichSObject record) {
        delete(record.getMetadata().getName(), record.get("id").asString());
    }

    @Override
    public RichSObject fetch(String type, String id) {
        return new ImmutableRichSObject(this, describe(type), api().getSObject(type, id));
    }

    @Override
    public Iterator<RichSObject> recentItems(String type) {
        final SObjectDescription metadata = describe(type);
        final Iterator<Map<String,?>> rawRecentItems = api().describeSObjectBasic(type).getRecentItems().iterator();

        return new Iterator<RichSObject>() {
            @Override
            public boolean hasNext() {
                return rawRecentItems.hasNext();
            }

            @Override
            public RichSObject next() {
                return new ImmutableRichSObject(RichSObjectsServiceImpl.this, metadata, rawRecentItems.next());
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
        final SObjectDescription metadata = describe(type);
        
        return new Iterator<RichSObject>() {
            QueryResult queryResult = api().query(soql);
            Iterator<Map<String, ?>> queryResultItr = queryResult.getRecords().iterator();
            
            @Override
            public boolean hasNext() {
                return queryResultItr.hasNext() || !queryResult.isDone();
            }

            @Override
            public RichSObject next() {
                if (queryResultItr.hasNext()) {
                    return new ImmutableRichSObject(RichSObjectsServiceImpl.this, metadata, queryResultItr.next());
                } else if (!queryResult.isDone()) {
                    queryResult = api().queryMore(queryResult.getNextRecordsUrl());
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
