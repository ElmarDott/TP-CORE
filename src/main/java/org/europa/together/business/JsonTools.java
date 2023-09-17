package org.europa.together.business;

import org.europa.together.exceptions.JsonProcessingException;
import java.util.List;
import org.apiguardian.api.API;
import static org.apiguardian.api.API.Status.STABLE;
import org.springframework.stereotype.Component;

/**
 * Lightweight wrapper for basic JSON functionality.
 *
 * @param <T> as DomainObject
 */
@API(status = STABLE, since = "3.0", consumers = "JacksonJsonTools")
@Component
public interface JsonTools<T> {

    /**
     * Identifier for the given feature.
     */
    @API(status = STABLE, since = "3.0")
    String FEATURE_ID = "CM-15";

    /**
     * Serialize an DomainObject &gt;T&lt; to an JSON String.
     *
     * @param object as &gt;T&lt; DomainObject
     * @return JSON as String
     * @throws org.europa.together.exceptions.JsonProcessingException
     */
    @API(status = STABLE, since = "3.0")
    String serializeAsJsonObject(T object)
            throws JsonProcessingException;

    /**
     * Create (deserialize) a DomainObject &gt;T&lt; from a JSON String.
     *
     * @param json as String
     * @param object as &gt;T&lt;
     * @return DomainObject as &gt;T&lt;
     * @throws org.europa.together.exceptions.JsonProcessingException
     * @throws java.lang.ClassNotFoundException
     */
    @API(status = STABLE, since = "3.0")
    T deserializeJsonAsObject(String json, Class<T> object)
            throws JsonProcessingException, ClassNotFoundException;

    /**
     * Create (deserialize) a list of DomainObject &gt;T&lt; from a JSON String.
     *
     * @param json as String
     * @return List of DomainObjects as &gt;T&lt;
     * @throws org.europa.together.exceptions.JsonProcessingException
     * @throws java.lang.ClassNotFoundException
     */
    @API(status = STABLE, since = "3.0")
    List<T> deserializeJsonAsList(String json)
            throws JsonProcessingException, ClassNotFoundException;

}
