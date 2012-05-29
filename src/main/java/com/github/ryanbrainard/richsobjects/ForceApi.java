package com.github.ryanbrainard.richsobjects;

import java.util.List;
import java.util.Map;

/**
 * @author Ryan Brainard
 */
public interface ForceApi {
    DescribeGlobal describeGlobal();

    DescribeSObject describeSObject(String type);

    String createSObject(String type, Map<String, String> record);

    String updateSObject(String type, String id, Map<String, String> record);

    void deleteSObject(String type, String id);

    Map<String, String> getSObject(String sobject, String id);

    List<Map<String,String>> getRecentItems(String type);
}
