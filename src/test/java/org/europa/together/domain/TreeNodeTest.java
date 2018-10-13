package org.europa.together.domain;

import static com.google.code.beanmatchers.BeanMatchers.*;
import org.europa.together.application.LoggerImpl;
import org.europa.together.business.Logger;
import org.europa.together.utils.StringUtils;
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
        LOGGER.log("### TEST SUITE INICIATED.", LogLevel.TRACE);
        LOGGER.log("Assumption terminated. TestSuite will be executed.\n", LogLevel.TRACE);
    }

    @AfterAll
    static void tearDown() {
        LOGGER.log("### TEST SUITE TERMINATED.", LogLevel.TRACE);
    }

    @BeforeEach
    void testCaseInitialization() {
    }

    @AfterEach
    void testCaseTermination() {
        LOGGER.log("TEST CASE TERMINATED.\n", LogLevel.TRACE);
    }
    //</editor-fold>

    @Test
    void testDomainObject() {
        LOGGER.log("TEST CASE: domainObject()", LogLevel.DEBUG);

        assertThat(TreeNode.class, hasValidBeanConstructor());
        assertThat(TreeNode.class, hasValidBeanHashCodeFor("nodeName", "parent"));
    }

    @Test
    void testEqualByClone() {
        LOGGER.log("TEST CASE: equalByClone()", LogLevel.DEBUG);

        TreeNode node_01 = new TreeNode("node 1");
        node_01.setNodeName("node 1");
        node_01.setParent(StringUtils.generateUUID());
        LOGGER.log(node_01.toString(), LogLevel.DEBUG);

        TreeNode node_02 = node_01;
        node_02.setNodeName("node 2");
        node_01.setParent(StringUtils.generateUUID());
        LOGGER.log(node_02.toString(), LogLevel.DEBUG);

        assertEquals(node_01, node_02);
        assertTrue(node_01.equals(node_02));
        assertTrue(node_02.equals(node_01));
    }

    @Test
    void testNotEqual() {
        LOGGER.log("TEST CASE: notEqual()", LogLevel.DEBUG);

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

        LOGGER.log("ASSERT FALSE BY wrong Object Type", LogLevel.DEBUG);
        assertFalse(node_01.equals(new Object()));
    }

    @Test
    void testToString() {
        LOGGER.log("TEST CASE: toString()", LogLevel.DEBUG);

        TreeNode node = new TreeNode("NODE");
        String out = node.toString();
        LOGGER.log("toString() " + out, LogLevel.DEBUG);
        assertEquals(out, node.toString());
    }

    @Test
    void testCreateDomainObjectBySetter() {
        LOGGER.log("TEST CASE: createDomainObjectBySetter()", LogLevel.DEBUG);

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
        LOGGER.log("TEST CASE: correctBeanConstruction()", LogLevel.DEBUG);

        TreeNode node_00 = new TreeNode();
        assertNotNull(node_00.getUuid());

        TreeNode node_01 = new TreeNode("Node");
        assertNotNull(node_01.getUuid());
        assertEquals("Node", node_01.getNodeName());
    }
}
