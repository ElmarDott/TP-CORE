package org.europa.together.domain;

import static com.google.code.beanmatchers.BeanMatchers.*;
import org.europa.together.application.LogbackLogger;
import org.europa.together.business.Logger;
import org.europa.together.utils.StringUtils;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

@SuppressWarnings("unchecked")
@RunWith(JUnitPlatform.class)
public class TreeNodeTest {

    private static final Logger LOGGER = new LogbackLogger(TreeNodeTest.class);

    @Test
    void createDomainObject() {
        LOGGER.log("TEST CASE: createDomainObject", LogLevel.DEBUG);

        assertThat(TreeNode.class, hasValidBeanConstructor());
        assertThat(TreeNode.class, hasValidBeanHashCodeFor("nodeName"));
    }

    @Test
    void equalByCopy() {
        LOGGER.log("TEST CASE: equalByCopy", LogLevel.DEBUG);

        TreeNode node_01 = new TreeNode("cloend");
        node_01.setParent(StringUtils.generateUUID());

        TreeNode node_02 = node_01.copy(node_01);
        assertEquals(node_01, node_02);

        node_01.setNodeName("rename");
        LOGGER.log("Node: " + node_02.toString(), LogLevel.DEBUG);
        LOGGER.log("Node: " + node_01.toString(), LogLevel.DEBUG);
        assertNotEquals(node_01, node_02);
    }

    @Test
    void notEqualByCopy() {
        LOGGER.log("TEST CASE: equalNotByCopy", LogLevel.DEBUG);

        try {
            TreeNode node_01 = new TreeNode("node x");
            node_01.setNodeName("node 1");
            node_01.setParent(StringUtils.generateUUID());

            TreeNode node_02 = node_01.copy(node_01);
            node_02.setNodeName("node 2");
            node_01.setParent(StringUtils.generateUUID());

            LOGGER.log("Node: " + node_02.toString(), LogLevel.DEBUG);
            LOGGER.log("Node: " + node_01.toString(), LogLevel.DEBUG);

            assertNotEquals(node_01, node_02);
            assertFalse(node_01.equals(node_02));
            assertFalse(node_02.equals(node_01));

            assertTrue(node_01.equals(node_01));
            assertTrue(node_02.equals(node_02));

        } catch (Exception ex) {
            LOGGER.catchException(ex);
        }
    }

    @Test
    void notEqual() {
        LOGGER.log("TEST CASE: notEqual", LogLevel.DEBUG);

        TreeNode node_01 = new TreeNode("node A");
        assertNotNull(node_01.getUuid());

        TreeNode node_02 = new TreeNode("node B");
        assertNotNull(node_02.getUuid());

        assertNotEquals(node_01, node_02);

        LOGGER.log("ASSERT FALSE", LogLevel.DEBUG);
        assertFalse(node_01.equals(node_02));

        LOGGER.log("ASSERT FALSE BY <null>", LogLevel.DEBUG);
        assertFalse(node_01.equals(null));

        LOGGER.log("ASSERT FALSE BY new(NODE_NAME)", LogLevel.DEBUG);
        assertFalse(node_01.equals(new TreeNode("node C")));

        LOGGER.log("ASSERT FALSE BY new()", LogLevel.DEBUG);
        assertFalse(node_01.equals(new TreeNode()));

        LOGGER.log("ASSERT FALSE BY new(UUID)", LogLevel.DEBUG);
        assertFalse(node_01.getUuid().equals(new TreeNode("node C").getUuid()));
    }

    @Test
    void generateToString() {
        LOGGER.log("TEST CASE: generateToString", LogLevel.DEBUG);

        TreeNode node = new TreeNode("NODE");
        String out = node.toString();
        LOGGER.log("toString() " + out, LogLevel.DEBUG);
        assertEquals(out, node.toString());
    }

    @Test
    void createDomainObjectBySetter() {
        LOGGER.log("TEST CASE: createDomainObjectBySetter", LogLevel.DEBUG);

        TreeNode parent = new TreeNode();
        TreeNode node = new TreeNode();
        node.setNodeName("name");
        node.setValue("000");
        node.setParent(parent.getUuid());

        assertEquals("name", node.getNodeName());
        assertEquals("000", node.getValue());
        assertEquals(parent.getUuid(), node.getParent());
    }

    @Test
    void correctBeanConstruction() {
        LOGGER.log("TEST CASE: correctBeanConstruction", LogLevel.DEBUG);

        TreeNode node_00 = new TreeNode();
        assertNotNull(node_00.getUuid());

        TreeNode node_01 = new TreeNode("Node");
        assertNotNull(node_01.getUuid());
        assertEquals("Node", node_01.getNodeName());
    }
}
