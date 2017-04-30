package org.europa.together.business;

import java.io.Serializable;
import java.util.List;

/**
 * GenericDAO interface primary for CRUD Database operations. To use the DAO by
 * your own configuration, you need to load the spring-dao.xml into your Spring
 * context. For Desktop application you can load the spring context by following
 * code: * private transient ApplicationContext context = new
 * ClassPathXmlApplicationContext("classpath:eu/freeplace/newsletter/config/spring-dao.xml");
 *
 * This configuration us a MySQL Database connection to local host with the
 * following account: - jdbc.main.schema=collab<br>
 * - jdbc.main.user=collab<br>
 * - jdbc.main.password=collab
 *
 * @param <T> the Entity to Save
 * @param <PK> the Primary Key
 */
public interface GenericDAO<T, PK extends Serializable> extends Serializable {

    /**
     * Persist a new Entity and return TRUE if it was successful. In the case
     * that the entity is already existing or the persist pobject is not a valid
     * entity the method return FALSE.
     *
     * @param object of an Entity
     * @return return success as boolean
     */
    boolean create(T object);

    /**
     * Search for an persitend entity. If it's alredy existent then it will be
     * delete, otherwise the Method return FALSE.
     *
     * @param id as Object
     * @return success as boolean
     */
    boolean delete(PK id);

    /**
     * Search an entity in the persistence context and update it, if it's exist.
     * If is not a valid Entity or not existent the method return FALSE.
     *
     * @param id as object
     * @param object of an Entity
     * @return success as boolean
     */
    boolean update(PK id, T object);

    /**
     * Tried to create a Object from a given JSON String.
     *
     * @param json as String
     * @return Entity as String
     */
    T deserializeAsObject(String json);

    /**
     * Get all persited entries of an Entity.
     *
     * @return List of Entity Objects
     */
    List<T> listAllElements();

    /**
     * Get the primary key of an Object.
     *
     * @param object as Object
     * @return id as Object
     */
    PK getPrimaryKeyOfObject(T object);

    /**
     * Check if the Entity is not NULL and try to create a JSON Object as
     * String, otherwise the String will be empty.
     *
     * @param object of an Entity
     * @return JSON object as String
     */
    String serializeAsJson(T object);

    /**
     * try to find a persitence Object.
     *
     * @param id as Object
     * @return object
     */
    T find(PK id);
}
