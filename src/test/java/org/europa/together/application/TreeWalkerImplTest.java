package org.europa.together.application;

import static com.google.code.beanmatchers.BeanMatchers.*;
import java.util.ArrayList;
import java.util.List;
import org.europa.together.business.Logger;
import org.europa.together.business.TreeWalker;
import org.europa.together.domain.LogLevel;
import org.europa.together.domain.TreeNode;
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
public class TreeWalkerImplTest {

    private static final Logger LOGGER = new LoggerImpl(TreeWalkerImplTest.class);
    private TreeWalker treeWalker
            = new TreeWalkerImpl(new TreeNode("Root Node"));

    //<editor-fold defaultstate="collapsed" desc="Test Preparation">
    @BeforeAll
    static void setUp() {

        LOGGER.log("### TEST SUITE INICIATED.", LogLevel.TRACE);

        FF4jProcessor feature = new FF4jProcessor();
        boolean toggle = feature.deactivateUnitTests(TreeWalker.FEATURE_ID);
        LOGGER.log("PERFORM TESTS :: FeatureToggle", LogLevel.TRACE);

        boolean check;
        String out;
        if (!toggle) {
            out = "skiped.";
            check = false;
        } else {
            out = "executed.";
            check = true;
        }
        LOGGER.log("Assumption terminated. TestSuite will be " + out, LogLevel.TRACE);
        Assumptions.assumeTrue(check);

    }

    @AfterAll
    static void tearDown() {
        LOGGER.log("### TEST SUITE TERMINATED.", LogLevel.TRACE);
    }

    @BeforeEach
    void testCaseInitialization() {
        treeWalker.addRoot(new TreeNode("Root Node"));
    }

    @AfterEach
    void testCaseTermination() {
        treeWalker.clear();
        LOGGER.log("TEST CASE TERMINATED.\n", LogLevel.TRACE);
    }
    //</editor-fold>

    @Test
    void testConstructor() {
        LOGGER.log("TEST CASE: constructor", LogLevel.DEBUG);

        assertThat(TreeWalkerImpl.class, hasValidBeanConstructor());
    }

    @Test
    void testAddRoot() {
        LOGGER.log("TEST CASE: addRoot()", LogLevel.DEBUG);

        TreeNode root = new TreeNode();
        root.setNodeName("ROOT");
        TreeWalker walker = new TreeWalkerImpl(root);

        assertFalse(walker.addRoot(root));
        //new UUID && new name
        assertFalse(walker.addRoot(new TreeNode("node")));
    }

    @Test
    void testGetRoot() {
        LOGGER.log("TEST CASE: getRoot()", LogLevel.DEBUG);

        TreeWalker walker
                = new TreeWalkerImpl(new TreeNode("Root Node"));

        assertEquals("Root Node", walker.getRoot().getNodeName());
        assertEquals("-1", walker.getRoot().getParent());
    }

    @Test
    void testCountNodes() {
        LOGGER.log("TEST CASE: countNodes()", LogLevel.DEBUG);

        TreeWalker walker = new TreeWalkerImpl();
        assertEquals(0, walker.countNodes());
    }

    @Test
    void testAddNode() {
        LOGGER.log("TEST CASE: addNode()", LogLevel.DEBUG);

        buildTree();
        assertEquals(12, treeWalker.countNodes());
    }

    @Test
    void testIsLeaf() {
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

        TreeWalker emptyTree = new TreeWalkerImpl();
        assertFalse(emptyTree.isLeaf(new TreeNode("mock")));

    }

    @Test
    void testGetLeafs() {
        LOGGER.log("TEST CASE: getLeafs()", LogLevel.DEBUG);

        buildTree();
        assertEquals(5, treeWalker.getLeafs().size());
    }

    @Test
    void testGetNodeByUuid() {
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

        TreeWalker emptyTree = new TreeWalkerImpl();
        assertNull(emptyTree.getNodeByUuid("uuid"));
    }

    @Test
    void testIsNodeElementOfTree() {
        LOGGER.log("TEST CASE: isNodeElementOfTree()", LogLevel.DEBUG);

        buildTree();
        TreeNode node = treeWalker.getElementByName("07").get(0);
        assertTrue(treeWalker.isElementOfTree(node));
    }

    @Test
    void testFailNodeIsElementOfTree() {
        LOGGER.log("TEST CASE: failNodeIsElementOfTree()", LogLevel.DEBUG);

        TreeWalker emptyTree = new TreeWalkerImpl();
        assertFalse(emptyTree.isElementOfTree(new TreeNode("mock")));
    }

    @Test
    void testRemoveNode() {
        LOGGER.log("TEST CASE: removeNode()", LogLevel.DEBUG);

        buildTree();
        assertFalse(treeWalker.removeNode(treeWalker.getTree().get(0)));
    }

    @Test
    void testIsNameUnique() {
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

        TreeWalker emptyTree = new TreeWalkerImpl();
        assertEquals(0, emptyTree.isNameUnique("node"));
    }

    @Test
    void testGetElementsByName() {
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
    void testPrune() {
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
    void testMerge() {
        LOGGER.log("TEST CASE: merge()", LogLevel.DEBUG);

        buildTree();

        TreeNode mergeID = treeWalker.getElementByName("03").get(0);
        treeWalker.merge(mergeID.getUuid(), appendTree());
        LOGGER.log("\n" + treeWalker.toString(), LogLevel.TRACE);

        assertEquals(16, treeWalker.countNodes());
    }

    @Test
    void testFailMerge() {
        LOGGER.log("TEST CASE: failMerge()", LogLevel.DEBUG);

        buildTree();

        treeWalker.merge("node", null);
        assertEquals(12, treeWalker.countNodes());

        treeWalker.merge("node", new TreeWalkerImpl());
        assertEquals(12, treeWalker.countNodes());
    }

    private void buildTree() {
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

    private TreeWalker appendTree() {
        LOGGER.log("TEST CASE: constructor", LogLevel.DEBUG);

        TreeWalker append = new TreeWalkerImpl(new TreeNode("sub tree"));
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
