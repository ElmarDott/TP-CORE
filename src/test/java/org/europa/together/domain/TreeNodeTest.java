package org.europa.together.domain;

import static com.google.code.beanmatchers.BeanMatchers.*;
import org.europa.together.application.LoggerImpl;
import org.europa.together.business.Logger;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

@SuppressWarnings("unchecked")
@RunWith(JUnitPlatform.class)
public class TreeNodeTest {

    private static final Logger LOGGER = new LoggerImpl(TreeNodeTest.class);

    //<editor-fold defaultstate="collapsed" desc="Test Preparation">
    @BeforeAll
    static void setUp() {
    }

    @AfterAll
    static void tearDown() {
        LOGGER.log("TEST SUITE TERMINATED.", LogLevel.TRACE);
    }

    @BeforeEach
    void testCaseInitialization() {
    }

    @AfterEach
    void testCaseTermination() {
        LOGGER.log("TEST CASE TERMINATED. \n", LogLevel.TRACE);
    }
    //</editor-fold>

    @Test
    void testDomainObject() {
        assertThat(TreeNode.class, hasValidBeanConstructor());
        assertThat(TreeNode.class, hasValidBeanHashCodeFor("nodeName", "parent"));
    }

    @Test
    void testEqual() {
        TreeNode node_00 = new TreeNode("node 1");
        node_00.setNodeName("node 1");
        assertNotNull(node_00.getUuid());

        TreeNode node_01 = new TreeNode("node 2");
        assertNotNull(node_01.getUuid());

        TreeNode node_03 = node_00;
        node_03.setNodeName("node 3");
        assertNotNull(node_03.getUuid());
        assertEquals("node 3", node_03.getNodeName());

        assertNotEquals(node_01, node_00);
        assertNotEquals(node_01, node_03);
        assertEquals(node_00, node_03);

        assertFalse(node_00.equals(null));
        assertFalse(node_00.equals(new Object()));
        assertFalse(node_00.equals(new TreeNode("node 4")));
        assertFalse(node_00.equals(node_01));

        assertTrue(node_03.equals(node_00));
    }

    @Test
    void testToString() {
        TreeNode node = new TreeNode("NODE");
        String out = node.toString();
        LOGGER.log("toString() " + out, LogLevel.DEBUG);
        assertEquals(out, node.toString());
    }

    @Test
    void testCreateDomainObjectBySetter() {
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
    void testCorrectBeanConstruction() {
        TreeNode node_00 = new TreeNode();
        assertNotNull(node_00.getUuid());

        TreeNode node_01 = new TreeNode("Node");
        assertNotNull(node_01.getUuid());
        assertEquals("Node", node_01.getNodeName());
    }
}
