package com.github.ryanbrainard.richobjects;

import com.github.ryanbrainard.richsobjects.api.client.SfdcRestApiClient;
import com.github.ryanbrainard.richsobjects.api.model.BasicSObjectInformation;
import com.github.ryanbrainard.richsobjects.api.model.GlobalDescription;
import com.github.ryanbrainard.richsobjects.api.model.SObjectDescription;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;

import javax.ws.rs.core.MediaType;
import java.util.Map;

/**
 * @author Ryan Brainard
 */
public class SfdcRestApiJerseyClient implements SfdcRestApiClient {

    private final WebResource baseResource;
    private final WebResource sobjectsResource;

    {
        final ClientConfig config = new DefaultClientConfig();
        config.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);

        final Client jerseyClient = Client.create(config);
        jerseyClient.addFilter(new AuthorizationHeaderFilter(System.getProperty("sfdc.test.sessionId")));
        baseResource = jerseyClient.resource(System.getProperty("sfdc.test.endpoint") + "/services/data/v24.0"); // TODO: versioning
        sobjectsResource = baseResource.path("/sobjects");
    }

    @Override
    public GlobalDescription describeGlobal() {
        return sobjectsResource.get(GlobalDescription.class);
    }

    @Override
    public BasicSObjectInformation describeSObjectBasic(String type) {
        return sobjectsResource.path("/" + type).get(BasicSObjectInformation.class);
    }

    @Override
    public SObjectDescription describeSObject(String type) {
        return sobjectsResource.path("/" + type + "/describe").get(SObjectDescriptionImpl.class);
    }

    @Override
    public String createSObject(String type, Map<String, ?> record) {
        return sobjectsResource.path("/" + type)
                .entity(record, MediaType.APPLICATION_JSON_TYPE)
                .post(Map.class).get("id").toString();
    }

    @Override
    public void updateSObject(String type, String id, Map<String, ?> record) {
        sobjectsResource.path("/" + type + "/" + id).queryParam("_HttpMethod", "PATCH")
                .entity(record, MediaType.APPLICATION_JSON_TYPE)
                .post();
    }

    @Override
    public void deleteSObject(String type, String id) {
        sobjectsResource.path("/" + type + "/" + id).delete();
    }

    @Override
    public Map<String, ?> getSObject(String type, String id) {
        //noinspection unchecked
        return (Map<String, ?>) sobjectsResource.path("/" + type + "/" + id).get(Map.class);
    }
}
