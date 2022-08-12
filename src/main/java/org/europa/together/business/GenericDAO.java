package org.europa.together.business;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityNotFoundException;
import org.apiguardian.api.API;
import static org.apiguardian.api.API.Status.STABLE;
import org.europa.together.domain.JpaPagination;
import org.europa.together.exceptions.DAOException;
import org.europa.together.exceptions.JsonProcessingException;
import org.springframework.stereotype.Component;

/**
 * GenericDAO primary for CRUD database operations. To use the DAO by your own
 * configuration, you need to load the spring-dao.xml into your Spring
 * context.<br>
 * A detailed documentation can found at:
 * https://github.com/ElmarDott/TP-CORE/wiki/%5BCORE-02%5D-generic-Data-Access-Object---DAO
 *
 * @param <T> the entity to save
 * @param <PK> the primary key
 *
 * @author elmar.dott@gmail.com
 * @version 1.4
 * @since 1.0
 */
@API(status = STABLE, since = "1.0", consumers = "GenrericHbmDAO")
@Component
public interface GenericDAO<T, PK extends Serializable> extends Serializable {

    /**
     * Identifier for the given feature.
     */
    @API(status = STABLE, since = "1.2")
    String FEATURE_ID = "CM-02";

    /**
     * Persist a new entity and return TRUE if it was successful.In the case
     * that the entity is already existing or the persist project is not a valid
     * entity the method return FALSE.
     *
     * @param object as Generic
     * @return true on success
     * @throws org.europa.together.exceptions.DAOException
     */
    @API(status = STABLE, since = "1.0")
    boolean create(T object) throws DAOException;

    /**
     * Search for an persistent entity.If it's already existent then it will be
     * delete, otherwise a EntityNotFoundException will thrown.
     *
     * @param id as Generic
     * @throws javax.persistence.EntityNotFoundException
     */
    @API(status = STABLE, since = "1.0")
    void delete(PK id) throws EntityNotFoundException;

    /**
     * Search an entity in the persistence context and update it.If it's not
     * exist a EntityNotFoundException will thrown.
     *
     * @param id as Generic
     * @param object as Generic
     * @throws javax.persistence.EntityNotFoundException
     */
    @API(status = STABLE, since = "1.0")
    void update(PK id, T object) throws EntityNotFoundException;

    /**
     * Count the entries of corresponding table to the domain object is
     * specialized in the DAO.
     *
     * @return count as long
     */
    @API(status = STABLE, since = "3.0")
    long countAllElements();

    /**
     * Get all persited entries of an entity as list.
     *
     * @param seekElement as JpaPagination
     * @return List of Entity Objects
     */
    @API(status = STABLE, since = "1.0")
    List<T> listAllElements(JpaPagination seekElement);

    /**
     * Get the primary key of the DAO entity.
     *
     * @param object as Genric
     * @return id as Genric
     */
    @API(status = STABLE, since = "1.0")
    PK getPrimaryKeyOfObject(T object);

    /**
     * Check if the entity is not NULL and try to create a JSON object as
     * String, otherwise the String will be empty.
     *
     * @param object as Generic
     * @return JSON object as String
     * @throws com.fasterxml.jackson.core.JsonProcessingException
     */
    @API(status = STABLE, since = "1.0")
    String serializeAsJson(T object)
            throws JsonProcessingException;

    /**
     * Tried to create a entity object from a given JSON String.
     *
     * @param json as String
     * @param clazz as Class
     * @return Entity as Generic
     */
    @API(status = STABLE, since = "2.1")
    T deserializeJsonAsObject(String json, Class<T> clazz)
            throws JsonProcessingException, ClassNotFoundException;

    /**
     * Try to find a persited domain object.
     *
     * @param id as Generic
     * @return object as Generic
     */
    @API(status = STABLE, since = "1.0")
    T find(PK id);
}
