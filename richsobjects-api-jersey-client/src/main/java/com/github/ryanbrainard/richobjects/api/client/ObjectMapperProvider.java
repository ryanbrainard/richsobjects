package com.github.ryanbrainard.richobjects.api.client;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.mrbean.AbstractTypeMaterializer;

import javax.ws.rs.ext.ContextResolver;

/**
 * @author Ryan Brainard
 */
public class ObjectMapperProvider implements ContextResolver<ObjectMapper> {

    private final ObjectMapper mapper;

    public ObjectMapperProvider() {
        mapper = new ObjectMapper();
        mapper.getDeserializationConfig().setAbstractTypeResolver(new AbstractTypeMaterializer()); // allow deserialzation to interfaces
        mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @Override
    public ObjectMapper getContext(Class<?> type) {
        return mapper;
    }
}
