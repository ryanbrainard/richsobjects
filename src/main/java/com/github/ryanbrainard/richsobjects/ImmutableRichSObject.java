package com.github.ryanbrainard.richsobjects;

import com.github.ryanbrainard.richsobjects.api.model.SObjectDescription;

import java.util.*;

/**
 * @author Ryan Brainard
 */
public class ImmutableRichSObject implements RichSObject {

    private final SObjectDescription metadata;
    private final Map<String, Object> record;
    private final Map<String, SObjectDescription.Field> indexedFieldMetadata;
    private final List<SObjectDescription.Field> sortedFieldMetadata;

    public ImmutableRichSObject(SObjectDescription metadata, Map<String, ?> record) {
        this(metadata, record, ORDER_BY_FIELD_LABEL);
    }

    public ImmutableRichSObject(SObjectDescription metadata, Map<String, ?> record, Comparator<SObjectDescription.Field> sortFieldsBy) {
        this.metadata = metadata;

        final Map<String, Object> tmpRecord = new HashMap<String, Object>(record.size());
        for (Map.Entry<String, ?> field : record.entrySet()) {
            tmpRecord.put(field.getKey().toUpperCase(), field.getValue());
        }
        this.record = Collections.unmodifiableMap(tmpRecord);

        final Map<String,SObjectDescription.Field> tmpIndexedFieldMetadata = new HashMap<String,SObjectDescription.Field>(metadata.getFields().size());
        for (SObjectDescription.Field f : metadata.getFields()) {
            tmpIndexedFieldMetadata.put(f.getName().toUpperCase(), f);
        }
        this.indexedFieldMetadata = Collections.unmodifiableMap(tmpIndexedFieldMetadata);

        final List<SObjectDescription.Field> tmpSortedFieldMetadata = new ArrayList<SObjectDescription.Field>(metadata.getFields());
        Collections.sort(tmpSortedFieldMetadata, sortFieldsBy);
        this.sortedFieldMetadata = Collections.unmodifiableList(tmpSortedFieldMetadata);
    }

    @Override
    public SObjectDescription getMetadata() {
        return metadata;
    }

    @Override
    public RichField get(String fieldName) {
        return new ImmutableRichField(fieldName.toUpperCase());
    }

    @Override
    public Iterator<RichField> iterator() {
        return getFields();
    }

    @Override
    public Iterator<RichField> getFields() {
        return new Iterator<RichField>() {
            private final Iterator<SObjectDescription.Field> fieldIterator = sortedFieldMetadata.iterator();

            @Override
            public boolean hasNext() {
                return fieldIterator.hasNext();
            }

            @Override
            public RichField next() {
                return get(fieldIterator.next().getName());
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("Fields cannot be removed");
            }
        };
    }

    public class ImmutableRichField implements RichField {

        private final String fieldName;

        public ImmutableRichField(String fieldName) {
            this.fieldName = fieldName;
        }

        @Override
        public RichSObject getParent() {
            return ImmutableRichSObject.this;
        }

        @Override
        public SObjectDescription.Field getMetadata() {
            return indexedFieldMetadata.get(fieldName);
        }

        @Override
        public Object getValue() {
            // TODO: add type casting based on types in metadata
            return record.get(fieldName);
        }
    }

    public static Comparator<SObjectDescription.Field> ORDER_BY_FIELD_LABEL = new Comparator<SObjectDescription.Field>() {
        @Override
        public int compare(SObjectDescription.Field o1, SObjectDescription.Field o2) {
            return o1.getLabel().compareTo(o2.getLabel());
        }
    };
}
