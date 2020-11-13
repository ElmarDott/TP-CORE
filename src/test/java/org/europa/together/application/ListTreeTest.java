package org.europa.together.application;

import static com.google.code.beanmatchers.BeanMatchers.*;
import java.util.ArrayList;
import java.util.List;
import org.europa.together.business.Logger;
import org.europa.together.business.TreeWalker;
import org.europa.together.domain.LogLevel;
import org.europa.together.domain.TreeNode;
import org.europa.together.exceptions.MisconfigurationException;
import org.europa.together.utils.StringUtils;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

@SuppressWarnings("unchecked")
@RunWith(JUnitPlatform.class)
public class ListTreeTest {

    private static final Logger LOGGER = new LogbackLogger(ListTreeTest.class);
    private TreeWalker treeWalker
            = new ListTree(new TreeNode("Root Node"));

    //<editor-fold defaultstate="collapsed" desc="Test Preparation">
    @BeforeAll
    static void setUp() {
        LOGGER.log("### TEST SUITE INICIATED.", LogLevel.TRACE);
        boolean check = true;
        String out = "executed";
        FF4jProcessor feature = new FF4jProcessor();

        boolean toggle = feature.deactivateUnitTests(TreeWalker.FEATURE_ID);
        if (!toggle) {
            out = "skiped.";
            check = false;
        }
        LOGGER.log("Assumption terminated. TestSuite will be " + out, LogLevel.TRACE);
        Assumptions.assumeTrue(check);
    }

    @AfterAll
    static void tearDown() {
        LOGGER.log("### TEST SUITE TERMINATED.\n", LogLevel.TRACE);
    }

    @BeforeEach
    void testCaseInitialization() {
        treeWalker.addRoot(new TreeNode("Root Node"));
    }

    @AfterEach
    void testCaseTermination() {
        treeWalker.clear();
        LOGGER.log("TEST CASE TERMINATED.", LogLevel.TRACE);
    }
    //</editor-fold>

    @Test
    void testConstructor() {
        LOGGER.log("TEST CASE: constructor", LogLevel.DEBUG);

        assertThat(ListTree.class, hasValidBeanConstructor());
    }

    @Test
    void testAddRoot() throws MisconfigurationException {
        LOGGER.log("TEST CASE: addRoot()", LogLevel.DEBUG);

        TreeNode root = new TreeNode();
        root.setNodeName("ROOT");
        TreeWalker walker = new ListTree(root);

        assertEquals("ROOT", walker.getRoot().getNodeName());
    }

    @Test
    void testFailAddRootTwice() {
        LOGGER.log("TEST CASE: failAddRoot()", LogLevel.DEBUG);

        TreeNode root = new TreeNode();
        root.setNodeName("ROOT");
        TreeWalker walker = new ListTree(root);

        assertFalse(walker.addRoot(root));
    }

    @Test
    void testFailAddSecondRoot() {
        LOGGER.log("TEST CASE: failAddRoot()", LogLevel.DEBUG);

        TreeNode root = new TreeNode();
        root.setNodeName("ROOT");
        TreeWalker walker = new ListTree(root);

        assertFalse(walker.addRoot(new TreeNode("node")));
    }

    @Test
    void testGetRoot() throws MisconfigurationException {
        LOGGER.log("TEST CASE: getRoot()", LogLevel.DEBUG);

        TreeWalker walker
                = new ListTree(new TreeNode("Root Node"));

        assertEquals("Root Node", walker.getRoot().getNodeName());
        assertEquals("-1", walker.getRoot().getParent());
    }

    @Test
    void testFailGetRoot() throws MisconfigurationException {
        LOGGER.log("TEST CASE: failGetRoot()", LogLevel.DEBUG);

        TreeWalker walker = new ListTree();
        assertThrows(MisconfigurationException.class, () -> {
            assertNull(walker.getRoot());
        });
    }

    @Test
    void testCountNodes() {
        LOGGER.log("TEST CASE: countNodes()", LogLevel.DEBUG);

        TreeWalker walker = new ListTree();
        assertEquals(0, walker.countNodes());
    }

    @Test
    void testAddNode() throws MisconfigurationException {
        LOGGER.log("TEST CASE: addNode()", LogLevel.DEBUG);

        buildTree();

        TreeNode add = new TreeNode("add");
        List<TreeNode> node = treeWalker.getElementByName("06");
        add.setParent(node.get(0).getUuid());
        treeWalker.addNode(add);

        assertEquals(13, treeWalker.countNodes());
    }

    @Test
    void testFailAddNodeOfNameAndParent() throws MisconfigurationException {
        LOGGER.log("TEST CASE: failAddNodeOfNameAndParent()", LogLevel.DEBUG);

        buildTree();
        int count = treeWalker.countNodes();

        TreeNode node = new TreeNode("01");
        node.setParent(treeWalker.getRoot().getUuid());

        LOGGER.log("CHK: " + treeWalker.getElementByName("01").get(0).toString(), LogLevel.DEBUG);
        LOGGER.log("ADD: " + node.toString(), LogLevel.DEBUG);

        assertFalse(treeWalker.addNode(node));
        assertEquals(count, treeWalker.countNodes());
    }

    @Test
    void testFailAddNodeOfSameUuid() throws MisconfigurationException {
        LOGGER.log("TEST CASE: failAddNodeOfSameUuid()", LogLevel.DEBUG);

        buildTree();
        int count = treeWalker.countNodes();

        TreeNode node = treeWalker.getElementByName("05").get(0);
        TreeNode copy = node.copy(node);
        copy.setNodeName("changed");

        LOGGER.log("CHK: " + treeWalker.getElementByName("05").get(0).toString(), LogLevel.DEBUG);
        LOGGER.log("ADD: " + node.toString(), LogLevel.DEBUG);

        assertFalse(treeWalker.addNode(copy));
        assertEquals(count, treeWalker.countNodes());
    }

    @Test
    void testIsLeaf() throws MisconfigurationException {
        LOGGER.log("TEST CASE: isLeaf()", LogLevel.DEBUG);

        buildTree();
        List<TreeNode> tree = treeWalker.getTree();
        List<TreeNode> leaf = new ArrayList<>();

        for (TreeNode node : tree) {
            if (treeWalker.isLeaf(node)) {
                leaf.add(node);
            }
        }

        assertEquals(5, leaf.size());
        for (TreeNode node : leaf) {
            assertTrue(treeWalker.isLeaf(node));
        }
    }

    @Test
    void testFailIsLeaf() {
        LOGGER.log("TEST CASE: failIsLeaf()", LogLevel.DEBUG);

        TreeWalker emptyTree = new ListTree();
        assertFalse(emptyTree.isLeaf(new TreeNode("mock")));

    }

    @Test
    void testGetLeafs() throws MisconfigurationException {
        LOGGER.log("TEST CASE: getLeafs()", LogLevel.DEBUG);

        buildTree();
        assertEquals(5, treeWalker.getLeafs().size());
    }

    @Test
    void testGetNodeByUuid() throws MisconfigurationException {
        LOGGER.log("TEST CASE: getNodeByUuid()", LogLevel.DEBUG);

        buildTree();
        TreeNode node = this.treeWalker.getTree().get(5);
        String uuid = node.getUuid();

        TreeNode fetched = treeWalker.getNodeByUuid(uuid);

        assertEquals(node.getUuid(), fetched.getUuid());
        assertEquals(node.getNodeName(), fetched.getNodeName());
        assertEquals(node.getParent(), fetched.getParent());
    }

    @Test
    void testFailGetNodeByUuid() {
        LOGGER.log("TEST CASE: failGetNodeByUuid()", LogLevel.DEBUG);

        TreeWalker emptyTree = new ListTree();
        assertNull(emptyTree.getNodeByUuid("uuid"));
    }

    @Test
    void testIsNodeElementOfTree() throws MisconfigurationException {
        LOGGER.log("TEST CASE: isNodeElementOfTree()", LogLevel.DEBUG);

        buildTree();
        TreeNode node = treeWalker.getElementByName("07").get(0);
        assertTrue(treeWalker.isElementOfTree(node));
    }

    @Test
    void testFailNodeIsElementOfTree() {
        LOGGER.log("TEST CASE: failNodeIsElementOfTree()", LogLevel.DEBUG);

        TreeWalker emptyTree = new ListTree();
        assertFalse(emptyTree.isElementOfTree(new TreeNode("mock")));
    }

    @Test
    void testRemoveNode() throws MisconfigurationException {
        LOGGER.log("TEST CASE: removeNode()", LogLevel.DEBUG);

        buildTree();
        assertFalse(treeWalker.removeNode(treeWalker.getTree().get(0)));
    }

    @Test
    void testIsNameUnique() throws MisconfigurationException {
        LOGGER.log("TEST CASE: isNameUnique()", LogLevel.DEBUG);

        buildTree();
        assertEquals(1, treeWalker.isNameUnique("05"));
        assertEquals(0, treeWalker.isNameUnique("00"));

        TreeNode node = new TreeNode("05");
        node.setParent(treeWalker.getTree().get(3).getUuid());
        treeWalker.addNode(node);

        assertEquals(2, treeWalker.isNameUnique("05"));
    }

    @Test
    void testFailIsNameUnique() {
        LOGGER.log("TEST CASE: failIsNameUnique()", LogLevel.DEBUG);

        TreeWalker emptyTree = new ListTree();
        assertEquals(0, emptyTree.isNameUnique("node"));
    }

    @Test
    void testGetElementsByName() throws MisconfigurationException {
        LOGGER.log("TEST CASE: getElementsByName()", LogLevel.DEBUG);

        buildTree();

        TreeNode nodeA = new TreeNode("05");
        nodeA.setParent(treeWalker.getTree().get(3).getUuid());
        treeWalker.addNode(nodeA);
        TreeNode nodeB = new TreeNode("05");
        nodeB.setParent(treeWalker.getTree().get(8).getUuid());
        treeWalker.addNode(nodeB);
        TreeNode nodeC = new TreeNode("05");
        nodeC.setParent(treeWalker.getTree().get(2).getUuid());
        treeWalker.addNode(nodeC);

        List<TreeNode> elements = treeWalker.getElementByName("05");
        assertEquals(4, elements.size());

        assertEquals("05", elements.get(0).getNodeName());
        assertEquals("05", elements.get(1).getNodeName());
        assertEquals("05", elements.get(2).getNodeName());
        assertEquals("05", elements.get(3).getNodeName());
    }

    @Test
    void testGetElementByNameNotExist() throws MisconfigurationException {
        LOGGER.log("TEST CASE: getElementsByNameNotExist()", LogLevel.DEBUG);

        buildTree();
        assertTrue(treeWalker.getElementByName("foo").isEmpty());
    }

    @Test
    void testGetElementByNameInEmptyTree() {
        LOGGER.log("TEST CASE: getElementsByNameInEmptyTree()", LogLevel.DEBUG);

        TreeWalker emptyTree = new ListTree();
        assertTrue(emptyTree.getElementByName("foo").isEmpty());
    }

    @Test
    void testPrune() throws MisconfigurationException {
        LOGGER.log("TEST CASE: prune()", LogLevel.DEBUG);

        buildTree();

        treeWalker.prune(treeWalker.getElementByName("01").get(0));
        assertEquals(8, treeWalker.countNodes());
        LOGGER.log("\n" + treeWalker.toString(), LogLevel.TRACE);

        treeWalker.prune(treeWalker.getElementByName("10").get(0));
        assertEquals(7, treeWalker.countNodes());
        LOGGER.log("\n" + treeWalker.toString(), LogLevel.TRACE);
    }

    @Test
    void testPruneRoot() throws MisconfigurationException {
        LOGGER.log("TEST CASE: pruneRoot()", LogLevel.DEBUG);

        buildTree();
        treeWalker.prune(treeWalker.getRoot());

        assertEquals(0, treeWalker.countNodes());
    }

    @Test
    void testFailPrune() throws Exception {
        LOGGER.log("TEST CASE: failPrune()", LogLevel.DEBUG);

        buildTree();

        assertThrows(Exception.class, () -> {
            treeWalker.prune(null);
        });
    }

    @Test
    void testMerge() throws MisconfigurationException {
        LOGGER.log("TEST CASE: merge()", LogLevel.DEBUG);

        buildTree();

        TreeNode mergeID = treeWalker.getElementByName("03").get(0);
        treeWalker.merge(mergeID.getUuid(), appendTree());
        LOGGER.log("\n" + treeWalker.toString(), LogLevel.TRACE);

        assertEquals(16, treeWalker.countNodes());
    }

    @Test
    void testFailMerge() throws MisconfigurationException {
        LOGGER.log("TEST CASE: failMerge()", LogLevel.DEBUG);

        buildTree();

        treeWalker.merge("node", null);
        assertEquals(12, treeWalker.countNodes());

        treeWalker.merge("node", new ListTree());
        assertEquals(12, treeWalker.countNodes());
    }

    @Test
    void testPassValidateTree() throws MisconfigurationException {
        LOGGER.log("TEST CASE: passValidateTree()", LogLevel.DEBUG);

        buildTree();

        List<TreeNode> tree = treeWalker.getTree();
        assertTrue(treeWalker.validateTree(tree));
    }

    @Test
    void testFailValidateTreeRemoveNode() throws MisconfigurationException {
        LOGGER.log("TEST CASE: failValidateTreeRemoveNode()", LogLevel.DEBUG);

        buildTree();
        assertFalse(treeWalker.removeNode(treeWalker.getElementByName("05").get(0)));
        assertTrue(treeWalker.validateTree(treeWalker.getTree()));
    }

    @Test
    void testFailValidateTreeNodeWithEmptyParent() throws MisconfigurationException {
        LOGGER.log("TEST CASE: failValidateTreeNodeWithEmptyParent()", LogLevel.DEBUG);

        buildTree();
        TreeNode lost_01 = new TreeNode("lost");
        treeWalker.addNode(lost_01);

        assertFalse(treeWalker.validateTree(treeWalker.getTree()));
    }

    @Test
    void testFailValidateTreeNodeWithDisconnectedParent() throws MisconfigurationException {
        LOGGER.log("TEST CASE: failValidateTreeNodeWithDisconnectedParent()", LogLevel.DEBUG);

        buildTree();
        TreeNode lost_01 = new TreeNode("diconnected");
        lost_01.setParent(StringUtils.generateUUID());
        treeWalker.addNode(lost_01);

        TreeNode lost_02 = new TreeNode("sub node 01");
        lost_02.setParent(lost_01.getUuid());
        treeWalker.addNode(lost_02);
        TreeNode lost_03 = new TreeNode("sub node 02");
        lost_03.setParent(lost_01.getUuid());
        treeWalker.addNode(lost_03);

        assertFalse(treeWalker.validateTree(treeWalker.getTree()));
    }

    @Test
    void testFailValidateTreeHasNoRoot() throws MisconfigurationException {
        LOGGER.log("TEST CASE: failValidateTreeHasNoRoot()", LogLevel.DEBUG);

        treeWalker.clear();

        TreeWalker tree = new ListTree();
        TreeNode n_01 = new TreeNode("01");
        tree.addNode(n_01);
        TreeNode n_04 = new TreeNode("04");
        n_04.setParent(n_01.getUuid());
        tree.addNode(n_04);
        TreeNode n_05 = new TreeNode("05");
        n_05.setParent(n_01.getUuid());
        tree.addNode(n_05);

        assertFalse(treeWalker.validateTree(tree.getTree()));
    }

    @Test
    void testFailValidateTreeHasMulitpleRoot() throws MisconfigurationException {
        LOGGER.log("TEST CASE: failValidateTreeHasMulitpleRoot()", LogLevel.DEBUG);

        TreeNode node = new TreeNode("second ROOT");
        node.setParent("-1");
        treeWalker.addNode(node);

        assertFalse(treeWalker.validateTree(treeWalker.getTree()));
    }

    private void buildTree() throws MisconfigurationException {
        //P:0
        TreeNode n_01 = new TreeNode("01");
        n_01.setParent(treeWalker.getRoot().getUuid());
        treeWalker.addNode(n_01);
        TreeNode n_02 = new TreeNode("02");
        n_02.setParent(treeWalker.getRoot().getUuid());
        treeWalker.addNode(n_02);
        TreeNode n_03 = new TreeNode("03");
        n_03.setParent(treeWalker.getRoot().getUuid());
        treeWalker.addNode(n_03);
        //P:1
        TreeNode n_04 = new TreeNode("04");
        n_04.setParent(n_01.getUuid());
        treeWalker.addNode(n_04);
        TreeNode n_05 = new TreeNode("05");
        n_05.setParent(n_01.getUuid());
        treeWalker.addNode(n_05);
        //P:2
        TreeNode n_06 = new TreeNode("06");
        n_06.setParent(n_02.getUuid());
        treeWalker.addNode(n_06);
        //P:3
        TreeNode n_07 = new TreeNode("07");
        n_07.setParent(n_03.getUuid());
        treeWalker.addNode(n_07);
        //P:5
        TreeNode n_08 = new TreeNode("08");
        n_08.setParent(n_05.getUuid());
        treeWalker.addNode(n_08);
        //P:7
        TreeNode n_09 = new TreeNode("09");
        n_09.setParent(n_07.getUuid());
        treeWalker.addNode(n_09);
        TreeNode n_10 = new TreeNode("10");
        n_10.setParent(n_07.getUuid());
        treeWalker.addNode(n_10);
        //P:9
        TreeNode n_11 = new TreeNode("11");
        n_11.setParent(n_09.getUuid());
        treeWalker.addNode(n_11);
    }

    private TreeWalker appendTree() throws MisconfigurationException {
        LOGGER.log("TEST CASE: constructor", LogLevel.DEBUG);

        TreeWalker append = new ListTree(new TreeNode("sub tree"));
        //P:0
        TreeNode n_01 = new TreeNode("S-01");
        n_01.setParent(append.getRoot().getUuid());
        append.addNode(n_01);
        TreeNode n_02 = new TreeNode("S-02");
        n_02.setParent(append.getRoot().getUuid());
        append.addNode(n_02);
        TreeNode n_03 = new TreeNode("S-03");
        n_03.setParent(append.getRoot().getUuid());
        append.addNode(n_03);

        return append;
    }
}
