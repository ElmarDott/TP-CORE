package org.europa.together.domain;

import static com.google.code.beanmatchers.BeanMatchers.*;
import org.europa.together.application.LogbackLogger;
import org.europa.together.business.Logger;
import org.europa.together.utils.StringUtils;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

@SuppressWarnings("unchecked")
public class TreeNodeTest {

    private static final Logger LOGGER = new LogbackLogger(TreeNodeTest.class);

    @Test
    void createDomainObject() {
        LOGGER.log("TEST CASE: createDomainObject", LogLevel.DEBUG);

        assertThat(TreeNode.class, hasValidBeanConstructor());
        assertThat(TreeNode.class, hasValidBeanHashCodeFor("nodeName"));
        assertThat(TreeNode.class, hasValidBeanEqualsFor("nodeName"));
    }

    @Test
    void correctBeanConstruction() {
        LOGGER.log("TEST CASE: correctBeanConstruction", LogLevel.DEBUG);

        TreeNode<String> node_00 = new TreeNode<>();
        assertNotNull(node_00.getUuid());

        TreeNode<String> node_01 = new TreeNode<>("Node");
        assertNotNull(node_01.getUuid());
        assertEquals("Node", node_01.getNodeName());
    }

    @Test
    void createDomainObjectBySetter() {
        LOGGER.log("TEST CASE: createDomainObjectBySetter", LogLevel.DEBUG);

        TreeNode<Integer> parent = new TreeNode<>();
        TreeNode<Integer> node = new TreeNode<>();
        node.setNodeName("name");
        node.setValue(100);
        node.setParent(parent.getUuid());

        assertEquals("name", node.getNodeName());
        assertEquals(100, node.getValue());
        assertEquals(parent.getUuid(), node.getParent());
    }

    @Test
    void createDomainObjectWithDifferentValueObjects() {
        LOGGER.log("TEST CASE: createDomainObjectWithDifferentValueObjects", LogLevel.DEBUG);

        TreeNode<Integer> parent = new TreeNode<>();
        parent.setValue(404);

        TreeNode<String> node = new TreeNode<>();
        node.setNodeName("name");
        node.setValue("String Object instead of the parent Integer");
        node.setParent(parent.getUuid());

        assertTrue(parent.getValue() instanceof Integer);
        assertTrue(node.getValue() instanceof String);
    }

    @Test
    void generateToString() {
        LOGGER.log("TEST CASE: generateToString", LogLevel.DEBUG);

        TreeNode<String> node = new TreeNode<>("NODE");
        node.setValue("string value.");
        String out = node.toString();
        LOGGER.log("toString() " + out, LogLevel.DEBUG);
        assertEquals(out, node.toString());
    }

    @Test
    void equalByCopy() {
        LOGGER.log("TEST CASE: equalByCopy", LogLevel.DEBUG);

        TreeNode<String> node_01 = new TreeNode<>("cloend");
        node_01.setParent(StringUtils.generateUUID());
        node_01.setValue("string value");

        TreeNode<String> node_02 = node_01.copy(node_01);

        assertEquals(node_01, node_02);
        assertEquals(node_01.getValue(), node_02.getValue());
    }

    @Test
    void notEqualByCopyAndManipulate() {
        LOGGER.log("TEST CASE: notEqualByCopyAndManipulate", LogLevel.DEBUG);

        TreeNode<String> node_01 = new TreeNode<>("node x");
        node_01.setNodeName("node 1");
        node_01.setParent(StringUtils.generateUUID());

        TreeNode<String> node_02 = node_01.copy(node_01);
        node_02.setNodeName("node 2");

        LOGGER.log("Node: " + node_02.toString(), LogLevel.DEBUG);
        LOGGER.log("Node: " + node_01.toString(), LogLevel.DEBUG);

        assertNotEquals(node_01, node_02);
    }

    @Test
    void equalByDiffrentUuid() {
        LOGGER.log("TEST CASE: equalByDiffrentUuid", LogLevel.DEBUG);

        TreeNode<Boolean> a = new TreeNode<>("");
        TreeNode<Boolean> b = new TreeNode<>("");

        assertTrue(a.equals(b));
        assertEquals(a, b);
    }

    @Test
    void notEqualBySameRootAndDiffrentName() {
        LOGGER.log("TEST CASE: notEqualDefaultCompare", LogLevel.DEBUG);

        TreeNode<Boolean> root = new TreeNode<>("root");
        TreeNode<Boolean> node_01 = new TreeNode<>("node A");
        node_01.setParent(root.getUuid());
        TreeNode<Boolean> node_02 = new TreeNode<>("node B");
        node_02.setParent(root.getUuid());

        assertNotEquals(node_01, node_02);
        assertFalse(node_01.equals(node_02));
    }

    @Test
    void notEqualByNull() {
        LOGGER.log("TEST CASE: notEqualByNull", LogLevel.DEBUG);

        TreeNode<?> node_01 = new TreeNode<>("node A");
        assertFalse(node_01.equals(null));
    }

}
