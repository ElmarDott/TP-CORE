package org.europa.together.application;

import java.io.StringWriter;
import java.util.Map;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.apache.velocity.runtime.resource.loader.StringResourceLoader;
import org.apache.velocity.runtime.resource.util.StringResourceRepository;
import org.europa.together.business.Logger;
import org.europa.together.domain.LogLevel;
import org.springframework.stereotype.Repository;
import org.europa.together.business.TemplateRenderer;

/**
 * Implementation of a VelocityRenderer.
 */
@Repository
public class VelocityRenderer implements TemplateRenderer {

    private static final long serialVersionUID = 3L;
    private static final Logger LOGGER = new LogbackLogger(VelocityRenderer.class);

    private VelocityEngine engine = new VelocityEngine();

    /**
     * Constructor.
     */
    public VelocityRenderer() {
        LOGGER.log("instance class", LogLevel.INFO);
    }

    @Override
    public String loadContentByClasspathResource(final String resourcePath,
            final String template,
            final Map<String, String> properties) {

        engine.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
        engine.setProperty("classpath.resource.loader.class",
                ClasspathResourceLoader.class.getName());
        engine.init();
        return processContent(resourcePath + template, properties);
    }

    @Override
    public String loadContentByFileResource(final String resourcePath,
            final String template,
            final Map<String, String> properties) {

        engine.setProperty(RuntimeConstants.FILE_RESOURCE_LOADER_PATH, resourcePath);
        engine.init();
        return processContent(template, properties);
    }

    @Override
    public String loadContentByStringResource(final String resource,
            final Map<String, String> properties) {

        engine.setProperty(Velocity.RESOURCE_LOADER, "string");
        engine.addProperty("string.resource.loader.class",
                StringResourceLoader.class.getName());
        engine.addProperty("string.resource.loader.repository.static", "false");
        engine.init();

        StringResourceRepository templateRepository
                = (StringResourceRepository) engine
                        .getApplicationAttribute(StringResourceLoader.REPOSITORY_NAME_DEFAULT);
        templateRepository.putStringResource("stringResource", resource);

        return processContent("stringResource", properties);
    }

    private String processContent(final String resource,
            final Map<String, String> properties) {

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
