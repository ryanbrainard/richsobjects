package com.github.ryanbrainard.richsobjects.api.model;

import java.util.List;

/**
 * @author Ryan Brainard
 */
public interface GlobalDescription {
    String getEncoding();

    Integer getMaxBatchSize();

    List<SObjectDescription> getSObjects();
}
