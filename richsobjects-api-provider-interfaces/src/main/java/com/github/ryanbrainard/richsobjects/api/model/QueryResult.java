package com.github.ryanbrainard.richsobjects.api.model;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author Ryan Brainard
 */
public interface QueryResult extends Serializable {

    public int getTotalSize();

    public boolean isDone();

    public List<Map<String, ?>> getRecords();

    public String getNextRecordsUrl();
}
