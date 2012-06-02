package com.github.ryanbrainard.richobjects.api.client;

import com.force.api.ApiSession;
import com.force.api.DescribeGlobal;
import com.force.api.DescribeSObject;
import com.force.api.ForceApi;
import com.github.ryanbrainard.richsobjects.api.client.SfdcApiClient;
import com.github.ryanbrainard.richsobjects.api.client.SfdcApiClientProvider;
import com.github.ryanbrainard.richsobjects.api.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Ryan Brainard
 */
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

                    private final DescribeGlobal describeGlobal = api.describeGlobal();

                    @Override
                    public String getEncoding() {
                        throw new UnsupportedOperationException();
                    }

                    @Override
                    public Integer getMaxBatchSize() {
                        throw new UnsupportedOperationException();
                    }

                    @Override
                    public List<BasicSObjectDescription> getSObjects() {
                        final List<DescribeSObject> srcDescs = api.describeGlobal().getSObjects();
                        final List<BasicSObjectDescription> targetDescs = new ArrayList<BasicSObjectDescription>(srcDescs.size());
                        for (final DescribeSObject d : srcDescs) {
                            targetDescs.add(toSObjectDescription(d));
                        }
                        return targetDescs;
                    }
                };
            }

            @Override
            public BasicSObjectInformation describeSObjectBasic(String type) {
                throw new UnsupportedOperationException();
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
                throw new UnsupportedOperationException();
            }

            @Override
            public QueryResult queryMore(String nextRecordsUrl) {
                throw new UnsupportedOperationException();
            }
        };
    }
    
    private static SObjectDescription toSObjectDescription(final DescribeSObject src) {
        return new SObjectDescription() {
            public String getName() {
                return src.getName();
            }

            @Override
            public String getLabel() {
                return src.getLabel();
            }

            @Override
            public Boolean isCustom() {
                return src.isCustom();
            }

            @Override
            public String getKeyPrefix() {
                return src.getKeyPrefix();
            }

            @Override
            public String getLabelPlural() {
                return src.getLabelPlural();
            }

            @Override
            public Boolean isLayoutable() {
                return src.isLayoutable();
            }

            @Override
            public Boolean isActivateable() {
                return src.isActivateable();
            }

            @Override
            public Boolean isUpdateable() {
                return src.isUpdateable();
            }

            @Override
            public Map<String, String> getUrls() {
                return src.getUrls();
            }

            @Override
            public Boolean isCreateable() {
                return src.isCreateable();
            }

            @Override
            public Boolean isDeletable() {
                return src.isDeletable();
            }

            @Override
            public Boolean isFeedEnabled() {
                return src.isFeedEnabled();
            }

            @Override
            public Boolean isQueryable() {
                return src.isQueryable();
            }

            @Override
            public Boolean isReplicateable() {
                return src.isReplicateable();
            }

            @Override
            public Boolean isRetrieveable() {
                return src.isRetrieveable();
            }

            @Override
            public Boolean isUndeletable() {
                return src.isUndeletable();
            }

            @Override
            public Boolean isTriggerable() {
                return src.isTriggerable();
            }

            @Override
            public Boolean isMergeable() {
                return src.isMergeable();
            }

            @Override
            public Boolean isDeprecatedAndHidden() {
                return src.isDeprecatedAndHidden();
            }

            @Override
            public Boolean isCustomSetting() {
                return src.isCustomSetting();
            }

            @Override
            public Boolean isSearchable() {
                return src.isSearchable();
            }

            @Override
            public List<Field> getFields() {
                final List<Field> fs = new ArrayList<Field>(src.getFields().size());
                for (final DescribeSObject.Field f : src.getFields()) {
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
                List<ChildEntity> ces  = new ArrayList<ChildEntity>(src.getChildRelationships().size());
                for (final DescribeSObject.ChildEntity ce : src.getChildRelationships()) {
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
