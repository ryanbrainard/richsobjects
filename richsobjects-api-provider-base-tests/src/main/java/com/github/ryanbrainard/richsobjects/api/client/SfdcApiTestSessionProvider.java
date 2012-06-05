package com.github.ryanbrainard.richsobjects.api.client;

/**
 * @author Ryan Brainard
 */
public class SfdcApiTestSessionProvider implements SfdcApiSessionProvider {
    
    @Override
    public String getAccessToken() {
        return getSystemPropertyOrEnvVar("sfdc.test.sessionId", "SFDC_TEST_SESSION_ID");
    }

    @Override
    public String getApiEndpoint() {
        return getSystemPropertyOrEnvVar("sfdc.test.endpoint", "SFDC_TEST_ENDPOINT");
    }

    private static String getSystemPropertyOrEnvVar(String systemProperty, String envVar) {
        if (System.getProperties().containsKey(systemProperty)) {
            return  System.getProperty(systemProperty);
        } else if (System.getenv().containsKey(envVar)) {
            return System.getenv(envVar);
        } else {
            throw new RuntimeException("Could not log system property [" + systemProperty + "]" +
                    " or environment variable [" + envVar + "]");
        }
    }
}
