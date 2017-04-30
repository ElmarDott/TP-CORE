package org.europa.together.application;

import java.util.HashMap;
import java.util.Map;
import org.europa.together.business.VelocityRenderer;
import static org.junit.Assert.*;
import org.junit.Test;

@SuppressWarnings("unchecked")
public class VelocityRendererImplTest {

    VelocityRenderer instance = new VelocityRendererImpl();
    Map<String, String> properties = new HashMap<>();

    @Test
    public void testGenerateContentByClasspathResource() {

        String resource = "org/europa/together/velocity";
        if (properties != null) {
            properties.clear();
        }
        properties.put("property_key", "value");

        //plain template
        String result_00
                = instance.generateContentByClasspathResource(resource + "/template.vm", properties);
        String result_01
                = instance.generateContentByClasspathResource(resource + "/template.vm", null);
        //with properties
        String result_02
                = instance.generateContentByClasspathResource(resource + "/template_1.vm", properties);
        String result_03
                = instance.generateContentByClasspathResource(resource + "/template_1.vm", null);

        assertEquals("Hello World?", result_00);
        assertEquals("Hello World?", result_01);
        assertEquals("Hello World? : value", result_02);
        assertEquals("Hello World? : $property_key", result_03);
    }

    @Test
    public void testGenerateContentByDatabaseResource() {
    }

    @Test
    public void testGenerateContentByFileResource() {
    }

}
