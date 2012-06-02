package com.github.ryanbrainard.richsobjects.api.model;

import java.util.List;
import java.util.Map;

/**
 * @author Ryan Brainard
 */
public interface QueryResult {

    public int getTotalSize();

    public boolean isDone();

    public List<Map<String, ?>> getRecords();

    public String getNextRecordsUrl();
}
