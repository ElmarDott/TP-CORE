package org.europa.together.business;

import java.util.Map;
import org.apiguardian.api.API;
import static org.apiguardian.api.API.Status.STABLE;
import org.springframework.stereotype.Component;

/**
 * Velocity Template Renderer.
 *
 * @author elmar.dott@gmail.com
 * @version 1.2
 * @since 1.0
 */
@API(status = STABLE, since = "1.0", consumers = "VelocityRenderer")
@Component
public interface TemplateRenderer {

    /**
     * Identifier for the given feature to enable toggles.
     */
    @API(status = STABLE, since = "1.2")
    String FEATURE_ID = "CM-0003";

    /**
     * Generate a String from a Template in the classpath and a Map with
     * Variables.
     *
     * @param resourcePath as String
     * @param template as String
     * @param properties as Map
     * @return processedTemplate as String
     */
    @API(status = STABLE, since = "1.0")
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
    @API(status = STABLE, since = "1.0")
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
    @API(status = STABLE, since = "1.0")
    String loadContentByStringResource(String resource, Map<String, String> properties);
}
