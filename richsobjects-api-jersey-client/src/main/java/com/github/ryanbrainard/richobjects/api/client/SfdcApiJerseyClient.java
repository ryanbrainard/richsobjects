package com.github.ryanbrainard.richobjects.api.client;

import com.github.ryanbrainard.richsobjects.api.client.SfdcApiClient;
import com.github.ryanbrainard.richsobjects.api.model.BasicSObjectInformation;
import com.github.ryanbrainard.richsobjects.api.model.GlobalDescription;
import com.github.ryanbrainard.richsobjects.api.model.QueryResult;
import com.github.ryanbrainard.richsobjects.api.model.SObjectDescription;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import java.util.Map;

/**
 * @author Ryan Brainard
 */
public class SfdcApiJerseyClient implements SfdcApiClient {

    private final String accessToken;
    private final WebResource baseResource;
    private final WebResource versionedDateResource;
    private final WebResource sobjectsResource;

    SfdcApiJerseyClient(Client pooledClient, String accessToken, String apiEndpoint, String version){
        this.accessToken = accessToken;
        baseResource = pooledClient.resource(apiEndpoint);
        versionedDateResource = pooledClient.resource(apiEndpoint + "/services/data/" + version);
        sobjectsResource = versionedDateResource.path("/sobjects");
    }

    private WebResource.Builder auth(WebResource resource) {
        return resource.header(HttpHeaders.AUTHORIZATION, "OAuth " + accessToken);
    }
    
    @Override
    public GlobalDescription describeGlobal() {
        return auth(sobjectsResource).get(GlobalDescription.class);
    }

    @Override
    public BasicSObjectInformation describeSObjectBasic(String type) {
        return auth(sobjectsResource.path("/" + type)).get(BasicSObjectInformation.class);
    }

    @Override
    public SObjectDescription describeSObject(String type) {
        return auth(sobjectsResource.path("/" + type + "/describe")).get(SObjectDescription.class);
    }

    @Override
    public String createSObject(String type, Map<String, ?> record) {
        return auth(sobjectsResource.path("/" + type))
                .entity(record, MediaType.APPLICATION_JSON_TYPE)
                .post(Map.class).get("id").toString();
    }

    @Override
    public void updateSObject(String type, String id, Map<String, ?> record) {
        auth(sobjectsResource.path("/" + type + "/" + id).queryParam("_HttpMethod", "PATCH"))
                .entity(record, MediaType.APPLICATION_JSON_TYPE)
                .post();
    }

    @Override
    public void deleteSObject(String type, String id) {
        auth(sobjectsResource.path("/" + type + "/" + id)).delete();
    }

    @Override
    public Map<String, ?> getSObject(String type, String id) {
        //noinspection unchecked
        return (Map<String, ?>) auth(sobjectsResource.path("/" + type + "/" + id)).get(Map.class);
    }

    @Override
    public QueryResult query(String soql) {
        return auth(versionedDateResource.path("/query").queryParam("q", soql)).get(QueryResult.class);
    }

    @Override
    public QueryResult queryMore(String nextRecordsUrl) {
        return auth(baseResource.path(nextRecordsUrl)).get(QueryResult.class);
    }

    @Override
    public String getRawBase64Content(String contentUrl) {
        return auth(baseResource.path(contentUrl)).get(String.class);
    }
}
