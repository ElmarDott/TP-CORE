package org.europa.together.application;

import java.io.StringWriter;
import java.util.Map;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.europa.together.business.Logger;
import org.europa.together.business.VelocityRenderer;
import org.europa.together.domain.LogLevel;
import org.springframework.stereotype.Repository;

/**
 * Implementation of a VelocityRenderer.
 */
@Repository
public class VelocityRendererImpl implements VelocityRenderer {

    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = new LoggerImpl(VelocityRendererImpl.class);

    private final VelocityEngine engine = new VelocityEngine();

    /**
     * Constructor.
     */
    public VelocityRendererImpl() {
        LOGGER.log("instance class", LogLevel.INFO);
    }

    @Override
    public String generateContentByClasspathResource(final String resource,
            final Map<String, String> properties) {

        engine.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
        engine.setProperty("classpath.resource.loader.class",
                ClasspathResourceLoader.class.getName());
        engine.init();

        return this.proccesResource(resource, properties);
    }

    @Override
    public String generateContentByDatabaseResource(final String resource,
            final Map<String, String> properties) {
        //TODO: generateContentByDatabaseResource() implement me
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String generateContentByFileResource(final String resource,
            final Map<String, String> properties) {
        //TODO: generateContentByFileResource() implement me
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private String proccesResource(String resource, Map<String, String> properties) {
        Template template = engine.getTemplate(resource);

        StringWriter writer = new StringWriter();
        writer.flush();

        VelocityContext context = new VelocityContext();
        if (properties != null) {
            for (Map.Entry<String, String> entry : properties.entrySet()) {
                context.put(entry.getKey(), entry.getValue());
            }
        } else {
            context.put("__init__", "");
        }

        template.merge(context, writer);
        return writer.toString();
    }
}
