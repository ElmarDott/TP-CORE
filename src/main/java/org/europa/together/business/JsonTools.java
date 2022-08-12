package org.europa.together.business;

import org.europa.together.exceptions.JsonProcessingException;
import java.util.List;
import org.apiguardian.api.API;
import static org.apiguardian.api.API.Status.STABLE;
import org.springframework.stereotype.Component;

/**
 *
 * @param <T>
 */
@API(status = STABLE, since = "3.0", consumers = "JacksonJsonTools")
@Component
public interface JsonTools<T> {

    /**
     * Identifier for the given feature.
     */
    @API(status = STABLE, since = "3.0")
    String FEATURE_ID = "CM-15";

    @API(status = STABLE, since = "3.0")
    String serializeAsJson(final T object)
            throws JsonProcessingException;

    @API(status = STABLE, since = "3.0")
    T deserializeJsonAsObject(final String json, final Class<T> object)
            throws JsonProcessingException, ClassNotFoundException;

    @API(status = STABLE, since = "3.0")
    List<T> deserializeJsonAsList(final String json, final Class<T> object)
            throws JsonProcessingException, ClassNotFoundException;

}
