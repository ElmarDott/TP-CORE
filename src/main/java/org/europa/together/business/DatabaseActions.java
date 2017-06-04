package org.europa.together.business;

import java.util.List;
import org.springframework.stereotype.Component;

/**
 * DatabaseActions is a simple helper class to execute SQL queries and other
 * database operations out of the DAO Context.
 *
 * @author elmar.dott@gmail.com
 */
@Component
public interface DatabaseActions {

    /**
     * Execute a plain SQL Query.
     *
     * @param sql as String
     * @return List of Objects
     */
    List<Object> excecuteQuery(String sql);
}
