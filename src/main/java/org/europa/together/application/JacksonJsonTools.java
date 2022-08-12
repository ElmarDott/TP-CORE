package org.europa.together.application;

import com.fasterxml.jackson.core.type.TypeReference;
import org.europa.together.exceptions.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.europa.together.business.JsonTools;
import org.europa.together.business.Logger;
import org.europa.together.domain.LogLevel;
import org.springframework.stereotype.Repository;

/**
 * Implementation of JavaScript Object Notation (JSON) processing.
 *
 * @param <T>
 */
@Repository
public class JacksonJsonTools<T> implements JsonTools<T> {

    private static final long serialVersionUID = 15L;
    private static final Logger LOGGER = new LogbackLogger(JacksonJsonTools.class);

    /**
     * Constructor.
     */
    public JacksonJsonTools() {
        LOGGER.log("instance class", LogLevel.INFO);
    }

    @Override
    public String serializeAsJson(final T object) throws JsonProcessingException {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(object);

        } catch (com.fasterxml.jackson.core.JsonProcessingException ex) {
            throw new JsonProcessingException(ex.getOriginalMessage());
        }
    }

    @Override
    public T deserializeJsonAsObject(final String json, final Class<T> object)
            throws JsonProcessingException, ClassNotFoundException {

        try {
            ObjectMapper mapper = new ObjectMapper();
            return (T) mapper.readValue(json, Class.forName(object.getCanonicalName()));

        } catch (com.fasterxml.jackson.core.JsonProcessingException ex) {
            throw new JsonProcessingException(ex.getOriginalMessage());
        }
    }

    @Override
    public List<T> deserializeJsonAsList(final String json, final Class<T> object)
            throws JsonProcessingException, ClassNotFoundException {

        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(json, new TypeReference<List<T>>() {
            });

        } catch (com.fasterxml.jackson.core.JsonProcessingException ex) {
            throw new JsonProcessingException(ex.getOriginalMessage());
        }
    }
}
