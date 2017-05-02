package org.europa.together.business;

import java.util.Map;
import org.springframework.stereotype.Component;

/**
 * Velocity Template Renderer.
 *
 * @author elmar.dott@gmail.com
 */
@Component
public interface VelocityRenderer {

    /**
     * Generate a String from a Template in the classpath and a Map with
     * Variables.
     *
     * @param resource as String
     * @param properties as Map
     * @return processedTemplate as String
     */
    String generateContentByClasspathResource(String resource,
            Map<String, String> properties);

    /**
     * Generate a String from a Template in a database and a Map with Variables.
     *
     * @param resource as String
     * @param properties as Map
     * @return processedTemplate as String
     */
    String generateContentByDatabaseResource(String resource,
            Map<String, String> properties);

    /**
     * Generate a String from a Template from a external File and a Map with
     * Variables.
     *
     * @param resourcePath as String
     * @param template as String
     * @param properties as Map
     * @return processedTemplate as String
     */
    String generateContentByFileResource(String resourcePath, String template,
            Map<String, String> properties);
}
