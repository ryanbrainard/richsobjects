package com.github.ryanbrainard.richobjects.api.client;

import com.force.api.*;
import com.github.ryanbrainard.richsobjects.api.client.SfdcApiClient;
import com.github.ryanbrainard.richsobjects.api.client.SfdcApiClientProvider;
import com.github.ryanbrainard.richsobjects.api.model.*;
import com.github.ryanbrainard.richsobjects.api.model.QueryResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Ryan Brainard
 */
@SuppressWarnings("UnusedDeclaration")
public class ForceApiProvider implements SfdcApiClientProvider {
    
    @Override
    public SfdcApiClient get(final String accessToken, final String apiEndpoint, /*TODO*/ final String version) {
        return new SfdcApiClient() {

            private final ForceApi api = new ForceApi(new ApiSession()
                    .setAccessToken(accessToken)
                    .setApiEndpoint(apiEndpoint));

            @Override
            public GlobalDescription describeGlobal() {
                return new GlobalDescription() {

                    private final DescribeGlobal srcDescribeGlobal = api.describeGlobal();

                    @Override
                    public String getEncoding() {
                        return srcDescribeGlobal.getEncoding();
                    }

                    @Override
                    public Integer getMaxBatchSize() {
                        return srcDescribeGlobal.getMaxBatchSize();
                    }

                    @Override
                    public List<BasicSObjectDescription> getSObjects() {
                        final List<DescribeSObjectBasic> srcDescs = api.describeGlobal().getSObjects();
                        final List<BasicSObjectDescription> targetDescs = new ArrayList<BasicSObjectDescription>(srcDescs.size());
                        for (final DescribeSObjectBasic d : srcDescs) {
                            targetDescs.add(toBasicSObjectDescription(d));
                        }
                        return targetDescs;
                    }
                };
            }

            @Override
            public BasicSObjectInformation describeSObjectBasic(String type) {
                final DiscoverSObject<Map> disc = api.discoverSObject(type, Map.class);

                return new BasicSObjectInformation() {
                    @Override
                    public BasicSObjectDescription getBasicObjectDescribe() {
                        return toBasicSObjectDescription(disc.getObjectDescribe());
                    }

                    @Override
                    public List<Map<String, ?>> getRecentItems() {
                        return listDeepCast(disc.getRecentItems());
                    }
                };
            }

            @Override
            public SObjectDescription describeSObject(String type) {
                return toSObjectDescription(api.describeSObject(type));
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
                //noinspection unchecked
                return (Map<String, ?>) api.getSObject(type, id).asMap();
            }

            @Override
            public QueryResult query(String soql) {
                return toQueryResult(api.query(soql));
            }

            @Override
            public QueryResult queryMore(String nextRecordsUrl) {
                return toQueryResult(api.queryMore(nextRecordsUrl));
            }
        };
    }

    protected static QueryResult toQueryResult(final com.force.api.QueryResult<Map> srcQueryResult) {
        return new QueryResult() {
            @Override
            public int getTotalSize() {
                return srcQueryResult.getTotalSize();
            }

            @Override
            public boolean isDone() {
                return srcQueryResult.isDone();
            }

            @Override
            public List<Map<String, ?>> getRecords() {
                return listDeepCast(srcQueryResult.getRecords());
            }

            @Override
            public String getNextRecordsUrl() {
                return srcQueryResult.getNextRecordsUrl();
            }
        };
    }

    private static <S,T> List<T> listDeepCast(List<S> srcRecords) {
        final List<T> tgtRecords = new ArrayList<T>(srcRecords.size());
        for (S record : srcRecords) {
            //noinspection unchecked
            tgtRecords.add((T) record);
        }
        return tgtRecords;
    }

    protected static BasicSObjectDescription toBasicSObjectDescription(final DescribeSObjectBasic src) {
        return _toSObjectDescription(src, null, null);
    }

    protected static SObjectDescription toSObjectDescription(final DescribeSObject src) {
        return _toSObjectDescription(src, src.getFields(), src.getChildRelationships());
    }
    
    private static SObjectDescription _toSObjectDescription(final DescribeSObjectBasic basic, 
                                                            final List<DescribeSObject.Field> fields, 
                                                            final List<DescribeSObject.ChildEntity> childRelationships) {
        return new SObjectDescription() {
            public String getName() {
                return basic.getName();
            }

            @Override
            public String getLabel() {
                return basic.getLabel();
            }

            @Override
            public Boolean isCustom() {
                return basic.isCustom();
            }

            @Override
            public String getKeyPrefix() {
                return basic.getKeyPrefix();
            }

            @Override
            public String getLabelPlural() {
                return basic.getLabelPlural();
            }

            @Override
            public Boolean isLayoutable() {
                return basic.isLayoutable();
            }

            @Override
            public Boolean isActivateable() {
                return basic.isActivateable();
            }

            @Override
            public Boolean isUpdateable() {
                return basic.isUpdateable();
            }

            @Override
            public Map<String, String> getUrls() {
                return basic.getUrls();
            }

            @Override
            public Boolean isCreateable() {
                return basic.isCreateable();
            }

            @Override
            public Boolean isDeletable() {
                return basic.isDeletable();
            }

            @Override
            public Boolean isFeedEnabled() {
                return basic.isFeedEnabled();
            }

            @Override
            public Boolean isQueryable() {
                return basic.isQueryable();
            }

            @Override
            public Boolean isReplicateable() {
                return basic.isReplicateable();
            }

            @Override
            public Boolean isRetrieveable() {
                return basic.isRetrieveable();
            }

            @Override
            public Boolean isUndeletable() {
                return basic.isUndeletable();
            }

            @Override
            public Boolean isTriggerable() {
                return basic.isTriggerable();
            }

            @Override
            public Boolean isMergeable() {
                return basic.isMergeable();
            }

            @Override
            public Boolean isDeprecatedAndHidden() {
                return basic.isDeprecatedAndHidden();
            }

            @Override
            public Boolean isCustomSetting() {
                return basic.isCustomSetting();
            }

            @Override
            public Boolean isSearchable() {
                return basic.isSearchable();
            }

            @Override
            public List<Field> getFields() {
                if (fields == null) {
                    return null;
                }
                
                final List<Field> fs = new ArrayList<Field>(fields.size());
                for (final DescribeSObject.Field f : fields) {
                    fs.add(new Field() {
                        public Integer getLength() {
                            return f.getLength();
                        }

                        public String getName() {
                            return f.getName();
                        }

                        public String getType() {
                            return f.getType();
                        }

                        public String getDefaultValue() {
                            return f.getDefaultValue();
                        }

                        public String getLabel() {
                            return f.getLabel();
                        }

                        public Boolean isUpdateable() {
                            return f.isUpdateable();
                        }

                        public Boolean isCalculated() {
                            return f.isCalculated();
                        }

                        public Boolean isUnique() {
                            return f.isUnique();
                        }

                        public Boolean isNillable() {
                            return f.isNillable();
                        }

                        public Boolean isCaseSensitive() {
                            return f.isCaseSensitive();
                        }

                        public String getInlineHelpText() {
                            return f.getInlineHelpText();
                        }

                        public Boolean isNameField() {
                            return f.isNameField();
                        }

                        public Boolean isExternalId() {
                            return f.isExternalId();
                        }

                        public Boolean isIdLookup() {
                            return f.isIdLookup();
                        }

                        public Boolean isFilterable() {
                            return f.isFilterable();
                        }

                        public Boolean isCreateable() {
                            return f.isCreateable();
                        }

                        public Boolean isDeprecatedAndHidden() {
                            return f.isDeprecatedAndHidden();
                        }

                        public Boolean isAutoNumber() {
                            return f.isAutoNumber();
                        }

                        public Boolean isDefaultedOnCreate() {
                            return f.isDefaultedOnCreate();
                        }

                        public Boolean isGroupable() {
                            return f.isGroupable();
                        }

                        public String getRelationshipName() {
                            return f.getRelationshipName();
                        }

                        public List<String> getReferenceToEntity() {
                            return f.getReferenceToEntity();
                        }

                        public Boolean isRestrictedPicklist() {
                            return f.isRestrictedPicklist();
                        }

                        public Boolean isNamePointing() {
                            return f.isNamePointing();
                        }

                        public Boolean isCustom() {
                            return f.isCustom();
                        }

                        public Boolean isHtmlFormatted() {
                            return f.isHtmlFormatted();
                        }

                        public Boolean isDependentPicklist() {
                            return f.isDependentPicklist();
                        }

                        public Boolean isWriteRequiresMasterRead() {
                            return f.isWriteRequiresMasterRead();
                        }

                        public Boolean isSortable() {
                            return f.isSortable();
                        }
                    });
                }

                return fs;
            }

            @Override
            public List<ChildEntity> getChildRelationships() {
                if (childRelationships == null) {
                    return null;
                }
                
                List<ChildEntity> ces = new ArrayList<ChildEntity>(childRelationships.size());
                for (final DescribeSObject.ChildEntity ce : childRelationships) {
                    ces.add(new ChildEntity() {
                        public String getField() {
                            return ce.getField();
                        }

                        public String getChildSObject() {
                            return ce.getChildSObject();
                        }

                        public String getRelationshipName() {
                            return ce.getRelationshipName();
                        }

                        public Boolean isDeprecatedAndHidden() {
                            return ce.isDeprecatedAndHidden();
                        }

                        public Boolean isCascadeDelete() {
                            return ce.isCascadeDelete();
                        }
                    });
                }

                return ces;
            }
        };
    }
}
