package com.github.ryanbrainard.richsobjects;

import java.util.*;

public class RichSObjectsServiceImpl implements RichSObjectsService {
    
    private ForceApi getForceApi() {
        return null; // TODO: Service Loader
    }

    @Override
    public List<DescribeSObject> listSObjectTypes() {
        final List<DescribeSObject> describeSObjects = getForceApi().describeGlobal().getSObjects();
        Collections.sort(describeSObjects, new Comparator<DescribeSObject>() {
            @Override
            public int compare(DescribeSObject o1, DescribeSObject o2) {
                return o1.getLabel().compareTo(o2.getLabel());
            }
        });
        return Collections.unmodifiableList(describeSObjects);
    }


    @Override
    public DescribeSObject describeSObjectType(String type) {
        return getForceApi().describeSObject(type);
    }

    @Override
    public RichSObject newSObject(String type) {
        //noinspection unchecked
        return new ImmutableRichSObject(describeSObjectType(type), Collections.EMPTY_MAP);
    }

    @Override
    public RichSObject existingSObject(String type, Map<String, String> record) {
        return new ImmutableRichSObject(describeSObjectType(type), record);
    }
    
    @Override
    public String createSObject(String type, Map<String, String> record) {
        return getForceApi().createSObject(type, record);
    }

    @Override
    public void updateSObject(String type, String id, Map<String, String> record) {
        getForceApi().updateSObject(type, id, record);
    }

    @Override
    public void deleteSObject(String type, String id) {
        getForceApi().deleteSObject(type, id);
    }

    @Override
    public RichSObject getSObject(String type, String id) {
        return new ImmutableRichSObject(describeSObjectType(type), getRawSObject(type, id));
    }

    private Map<String, String> getRawSObject(String sobject, String id) {
        return getForceApi().getSObject(sobject, id);
    }

    @Override
    public Iterator<RichSObject> getRecentItems(String type) {
        final DescribeSObject describeSObject = describeSObjectType(type);
        final Iterator<Map<String,String>> rawRecentItems = getForceApi().getRecentItems(type).iterator();

        return new Iterator<RichSObject>() {
            @Override
            public boolean hasNext() {
                return rawRecentItems.hasNext();
            }

            @Override
            public RichSObject next() {
                //noinspection unchecked
                return new ImmutableRichSObject(describeSObject, rawRecentItems.next());
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("Recent items cannot be removed");
            }
        };
    }
}
