package com.github.ryanbrainard.richsobjects.api.model;

import java.io.Serializable;
import java.util.List;

/**
 * @author Ryan Brainard
 */
public interface GlobalDescription extends Serializable {
    String getEncoding();

    Integer getMaxBatchSize();

    List<BasicSObjectDescription> getSObjects();
}
