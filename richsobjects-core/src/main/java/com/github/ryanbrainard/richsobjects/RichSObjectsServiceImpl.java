package com.github.ryanbrainard.richsobjects;

import com.github.ryanbrainard.richsobjects.api.client.SfdcApiClient;
import com.github.ryanbrainard.richsobjects.api.client.SfdcApiLoader;
import com.github.ryanbrainard.richsobjects.api.model.BasicSObjectDescription;
import com.github.ryanbrainard.richsobjects.api.model.QueryResult;
import com.github.ryanbrainard.richsobjects.api.model.SObjectDescription;
import com.github.ryanbrainard.richsobjects.filters.UpdateableFieldsOnly;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Ryan Brainard
 */
public class RichSObjectsServiceImpl implements RichSObjectsService {

    private static final Pattern SOQL_TYPE_PATTERN = Pattern.compile(".*FROM\\s+(\\w+).*", Pattern.CASE_INSENSITIVE);

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
        return api().describeSObject(type.toUpperCase());
    }

    @Override
    public RichSObject of(String type) {
        return of(type, new HashMap<String, Object>());
    }

    @Override
    public RichSObject of(String type, String recordId) {
        return of(type, Collections.singletonMap("Id", recordId));
    }
    
    @Override
    public RichSObject of(String type, Map<String, ?> record) {
        return new ImmutableRichSObject(this, describe(type), record);
    }

    @Override
    public RichSObject insert(RichSObject record) {
        String type = record.getMetadata().getName();
        final String id = api().createSObject(type, record.getRaw());
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

        String type = record.getMetadata().getName();
        String id = record.getField("id").asString();
        api().updateSObject(type, id, updateableFields);
        return fetch(type, id);
    }

    @Override
    public void delete(RichSObject record) {
        api().deleteSObject(record.getMetadata().getName(), record.getField("id").asString());
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
        final Matcher matcher = SOQL_TYPE_PATTERN.matcher(soql);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Could not determine type from SOQL query: " + soql);
        }
        final String type = matcher.group(1);
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
