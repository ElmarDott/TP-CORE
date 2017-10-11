package org.europa.together.business;

import java.io.Serializable;
import java.util.List;

/**
 * GenericDAO interface primary for CRUD Database operations. To use the DAO by
 * your own configuration, you need to load the spring-dao.xml into your Spring
 * context. For Desktop application you can load the spring context by following
 * code:<br>
 * private transient ApplicationContext context = new
 * ClassPathXmlApplicationContext("classpath:eu/freeplace/newsletter/config/spring-dao.xml");
 * <br><br>
 * This configuration us a MySQL Database connection to local host with the
 * following account: <br>
 * - jdbc.main.schema=collab<br>
 * - jdbc.main.user=collab<br>
 * - jdbc.main.password=collab
 *
 * @param <T> the Entity to Save
 * @param <PK> the Primary Key
 *
 * @author elmar.dott@gmail.com
 * @version 1.0
 */
public interface GenericDAO<T, PK extends Serializable> extends Serializable {

    /**
     * Persist a new Entity and return TRUE if it was successful. In the case
     * that the entity is already existing or the persist pobject is not a valid
     * entity the method return FALSE.
     *
     * @param object of an Entity
     * @return true on success
     */
    boolean create(T object);

    /**
     * Search for an persistent entity. If it's already existent then it will be
     * delete, otherwise the Method return FALSE.
     *
     * @param id as Object
     * @return true on success
     */
    boolean delete(PK id);

    /**
     * Search an entity in the persistence context and update it, if it's exist.
     * If is not a valid Entity or not existent the method return FALSE.
     *
     * @param id as object
     * @param object of an Entity
     * @return true on success
     */
    boolean update(PK id, T object);

    /**
     * Count the entries of an database table. THis function call native SQL
     * code, to be efficient in the performance of the execution time. This is
     * designed for huge datasets. SQL: <br>
     * <code>SELECT COUNT(*) FROM &lt;TABLE&gt;</code>
     *
     * @param table as String
     * @return resultSet as int
     */
    long countEntries(String table);

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
     * Tried to create a Object from a given JSON String.
     *
     * @param json as String
     * @return Entity as String
     */
    T deserializeAsObject(String json);

    /**
     * try to find a persitence Object.
     *
     * @param id as Object
     * @return object
     */
    T find(PK id);

    /**
     * Delete all Entries of a Database Table.
     *
     * @param tableName as String
     */
    void flushTable(String tableName);
}
