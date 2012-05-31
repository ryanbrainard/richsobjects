package com.github.ryanbrainard.richsobjects.api.client;

/**
 * @author Ryan Brainard
 */
public class SfdcRestApiTestSessionProvider implements SfdcRestApiSessionProvider {
    
    @Override
    public String getAccessToken() {
        return System.getProperty("sfdc.test.sessionId");
    }

    @Override
    public String getApiEndpoint() {
        return System.getProperty("sfdc.test.endpoint");
    }
}
