package com.github.ryanbrainard.richsobjects.api.client;

/**
 * @author Ryan Brainard
 */
public class SfdcApiTestSessionProvider implements SfdcApiSessionProvider {
    
    @Override
    public String getAccessToken() {
        return System.getProperty("sfdc.test.sessionId");
    }

    @Override
    public String getApiEndpoint() {
        return System.getProperty("sfdc.test.endpoint");
    }
}
