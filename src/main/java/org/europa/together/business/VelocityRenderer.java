package org.europa.together.business;

import java.util.Map;
import org.springframework.stereotype.Component;

/**
 * Velocity Template Renderer.
 *
 * @author elmar.dott@gmail.com
 * @version 1.0
 */
@Component
public interface VelocityRenderer {

    /**
     * Generate a String from a Template in the classpath and a Map with
     * Variables.
     *
     * @param resourcePath as String
     * @param template as String
     * @param properties as Map
     * @return processedTemplate as String
     */
    String loadContentByClasspathResource(String resourcePath, String template,
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
    String loadContentByFileResource(String resourcePath, String template,
            Map<String, String> properties);

    /**
     * Render a Template (resource) with a Map of Variables from an input String
     * and process the output also as String.
     *
     * @param resource as String
     * @param properties as Map
     * @return processedTemplate as String
     */
    String loadContentByStringResource(String resource, Map<String, String> properties);
}
