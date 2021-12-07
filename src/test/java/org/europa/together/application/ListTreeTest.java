package org.europa.together.application;

import static com.google.code.beanmatchers.BeanMatchers.*;
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
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SuppressWarnings("unchecked")
@RunWith(JUnitPlatform.class)
@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {"/applicationContext.xml"})
public class ListTreeTest {

    private static final Logger LOGGER = new LogbackLogger(ListTreeTest.class);

    @Autowired
    private TreeWalker<Boolean> treeWalker;

    //<editor-fold defaultstate="collapsed" desc="Test Preparation">
    @BeforeAll
    static void setUp() {
        Assumptions.assumeTrue(true);

        LOGGER.log("### TEST SUITE INICIATED.", LogLevel.TRACE);
    }

    @AfterAll
    static void tearDown() {
        LOGGER.log("### TEST SUITE TERMINATED.\n", LogLevel.TRACE);
    }

    @BeforeEach
    void testCaseInitialization() {
        treeWalker.addRoot(new TreeNode<Boolean>("Root Node"));
    }

    @AfterEach
    void testCaseTermination() {
        treeWalker.clear();
        LOGGER.log("TEST CASE TERMINATED.", LogLevel.TRACE);
    }
    //</editor-fold>

    @Test
    void constructor() {
        LOGGER.log("TEST CASE: constructor", LogLevel.DEBUG);

        assertThat(ListTree.class, hasValidBeanConstructor());
    }

    @Test
    void addRoot() throws MisconfigurationException {
        LOGGER.log("TEST CASE: addRoot", LogLevel.DEBUG);

        TreeNode<Boolean> root = new TreeNode<Boolean>();
        root.setNodeName("ROOT");
        TreeWalker<Boolean> walker = new ListTree<Boolean>(root);

        assertEquals("ROOT", walker.getRoot().getNodeName());
    }

    @Test
    void failAddRootTwice() {
        LOGGER.log("TEST CASE: failAddRoot", LogLevel.DEBUG);

        TreeNode<Boolean> root = new TreeNode<>("ROOT");
        TreeWalker<Boolean> walker = new ListTree<Boolean>(root);

        assertFalse(walker.addRoot(root));
    }

    @Test
    void getRoot() throws MisconfigurationException {
        LOGGER.log("TEST CASE: getRoot", LogLevel.DEBUG);

        TreeWalker<Boolean> walker
                = new ListTree<Boolean>(new TreeNode<Boolean>("Root Node"));

        TreeNode<Boolean> root = walker.getRoot();

        assertEquals("Root Node", walker.getRoot().getNodeName());
        assertEquals("-1", walker.getRoot().getParent());
    }

    @Test
    void failGetRootNotExist() {
        LOGGER.log("TEST CASE: failGetRoot", LogLevel.DEBUG);

        TreeWalker<Boolean> walker = new ListTree<Boolean>();
        assertNull(walker.getRoot());
    }

    @Test
    void countNodes() throws MisconfigurationException {
        LOGGER.log("TEST CASE: countNodes", LogLevel.DEBUG);

        buildTree();
        assertEquals(12, treeWalker.countNodes());
    }

    @Test
    void countNodesEmptyTree() {
        LOGGER.log("TEST CASE: countNodes", LogLevel.DEBUG);

        assertEquals(0, new ListTree<Boolean>().countNodes());
    }

    @Test
    void addNode() throws MisconfigurationException {
        LOGGER.log("TEST CASE: addNode", LogLevel.DEBUG);

        buildTree();

        TreeNode<Boolean> node = new TreeNode<>("06.1");
        node.setParent(
                treeWalker.getElementByName("06").get(0).getUuid()
        );
        treeWalker.addNode(node);

        assertEquals(13, treeWalker.countNodes());
    }

    @Test
    void failAddEmptyNode() throws MisconfigurationException {
        LOGGER.log("TEST CASE: failAddEmptyNode", LogLevel.DEBUG);

        buildTree();
        assertFalse(treeWalker.addNode(null));
    }

    @Test
    void failAddNodeOfSameNameAndParent() throws MisconfigurationException {
        LOGGER.log("TEST CASE: failAddNodeOfSameNameAndParent", LogLevel.DEBUG);

        TreeWalker<Boolean> walker = new ListTree<Boolean>();

        TreeNode<Boolean> root = new TreeNode<>("Root");
        TreeNode<Boolean> node_01 = new TreeNode<>("Node");
        node_01.setParent(root.getUuid());
        TreeNode<Boolean> node_02 = new TreeNode<>("Node");
        node_02.setParent(root.getUuid());

        assertTrue(walker.addRoot(root));
        assertTrue(walker.addNode(node_01));
        assertFalse(walker.addNode(node_02));

        assertEquals(2, walker.countNodes());
    }

    @Test
    void failAddNodeOfSameUuid() throws MisconfigurationException {
        LOGGER.log("TEST CASE: failAddNodeOfSameUuid", LogLevel.DEBUG);

        TreeWalker<Boolean> walker = new ListTree<Boolean>();
        TreeNode<Boolean> node_01 = new TreeNode<>("01");
        TreeNode<Boolean> node_02 = node_01.copy(node_01);
        node_02.setNodeName("02");

        assertEquals(node_01.getUuid(), node_02.getUuid());
        assertEquals("01", node_01.getNodeName());
        assertTrue(walker.addNode(node_01));
        assertEquals("02", node_02.getNodeName());
        assertFalse(walker.addNode(node_02));
    }

    @Test
    void getLeafCount() throws MisconfigurationException {
        LOGGER.log("TEST CASE: getLeafCount", LogLevel.DEBUG);

        buildTree();
        assertEquals(5, treeWalker.getLeafs().size());
    }

    @Test
    void isLeaf() throws MisconfigurationException {
        LOGGER.log("TEST CASE: isLeaf", LogLevel.DEBUG);

        buildTree();
        assertTrue(treeWalker.isLeaf(
                treeWalker.getElementByName("11").get(0))
        );
    }

    @Test
    void failIsLeaf() throws MisconfigurationException {
        LOGGER.log("TEST CASE: failIsLeaf", LogLevel.DEBUG);

        buildTree();
        assertFalse(treeWalker.isLeaf(
                treeWalker.getElementByName("01").get(0))
        );
    }

    @Test
    void getNodeByUuid() throws MisconfigurationException {
        LOGGER.log("TEST CASE: getNodeByUuid", LogLevel.DEBUG);

        buildTree();
        String uuid = this.treeWalker.getTree().get(5).getUuid();
        TreeNode<Boolean> fetched = treeWalker.getNodeByUuid(uuid);

        assertEquals(uuid, fetched.getUuid());
        assertEquals("05", fetched.getNodeName());
    }

    @Test
    void failIsLeafByEmptyTree() {
        LOGGER.log("TEST CASE: failIsLeafByEmptyTree", LogLevel.DEBUG);

        TreeWalker<Boolean> emptyTree = new ListTree<Boolean>();
        assertFalse(emptyTree.isLeaf(new TreeNode<Boolean>("mock")));
    }

    @Test
    void failGetNodeByUuidByEmptyTree() {
        LOGGER.log("TEST CASE: failGetNodeByUuidByEmptyTree", LogLevel.DEBUG);

        TreeWalker<Boolean> emptyTree = new ListTree<Boolean>();
        assertNull(emptyTree.getNodeByUuid("uuid"));
    }

    @Test
    void failGetNodeByUuidByUuidNotExist() throws MisconfigurationException {
        LOGGER.log("TEST CASE: failGetNodeByUuidByUuidNotExist", LogLevel.DEBUG);

        buildTree();
        assertNull(treeWalker.getNodeByUuid("NotExist"));
    }

    @Test
    void isNodeElementOfTree() throws MisconfigurationException {
        LOGGER.log("TEST CASE: isNodeElementOfTree", LogLevel.DEBUG);

        buildTree();
        TreeNode<Boolean> node = treeWalker.getElementByName("07").get(0);
        assertTrue(treeWalker.isElementOfTree(node));
    }

    @Test
    void failNodeIsElementOfTree() {
        LOGGER.log("TEST CASE: failNodeIsElementOfTree", LogLevel.DEBUG);

        assertFalse(new ListTree<Boolean>().isElementOfTree(new TreeNode<>("mock")));
    }

    @Test
    void failRemoveNode() throws MisconfigurationException {
        LOGGER.log("TEST CASE: failRemoveNode", LogLevel.DEBUG);

        buildTree();
        assertFalse(treeWalker.removeNode(treeWalker.getTree().get(0)));
    }

    @Test
    void isNameUnique() throws MisconfigurationException {
        LOGGER.log("TEST CASE: isNameUnique", LogLevel.DEBUG);

        buildTree();
        assertEquals(1, treeWalker.isNameUnique("05"));
        assertEquals(0, treeWalker.isNameUnique("00"));

        TreeNode<Boolean> node = new TreeNode<>("05");
        node.setParent(treeWalker.getTree().get(3).getUuid());
        treeWalker.addNode(node);

        assertEquals(2, treeWalker.isNameUnique("05"));
    }

    @Test
    void failIsNameUniqueByEmptyTree() {
        LOGGER.log("TEST CASE: failIsNameUniqueByEmptyTree", LogLevel.DEBUG);

        TreeWalker<Boolean> emptyTree = new ListTree<Boolean>();
        assertEquals(0, emptyTree.isNameUnique("node"));
    }

    @Test
    void getElementsByName() throws MisconfigurationException {
        LOGGER.log("TEST CASE: getElementsByName", LogLevel.DEBUG);

        buildTree();

        TreeNode<Boolean> nodeA = new TreeNode<>("05");
        nodeA.setParent(treeWalker.getTree().get(3).getUuid());
        treeWalker.addNode(nodeA);
        TreeNode<Boolean> nodeB = new TreeNode<>("05");
        nodeB.setParent(treeWalker.getTree().get(8).getUuid());
        treeWalker.addNode(nodeB);
        TreeNode<Boolean> nodeC = new TreeNode<>("05");
        nodeC.setParent(treeWalker.getTree().get(2).getUuid());
        treeWalker.addNode(nodeC);

        List<TreeNode<Boolean>> elements = treeWalker.getElementByName("05");
        assertEquals(4, elements.size());

        assertEquals("05", elements.get(0).getNodeName());
        assertEquals("05", elements.get(1).getNodeName());
        assertEquals("05", elements.get(2).getNodeName());
        assertEquals("05", elements.get(3).getNodeName());
    }

    @Test
    void getElementByNameNotExist() throws MisconfigurationException {
        LOGGER.log("TEST CASE: getElementsByNameNotExist", LogLevel.DEBUG);

        buildTree();
        assertTrue(treeWalker.getElementByName("foo").isEmpty());
    }

    @Test
    void getElementByNameInEmptyTree() {
        LOGGER.log("TEST CASE: getElementsByNameInEmptyTree", LogLevel.DEBUG);

        TreeWalker<Boolean> emptyTree = new ListTree<Boolean>();
        assertTrue(emptyTree.getElementByName("foo").isEmpty());
    }

    @Test
    void pruneRoot() throws MisconfigurationException {
        LOGGER.log("TEST CASE: pruneRoot", LogLevel.DEBUG);

        buildTree();
        treeWalker.prune(treeWalker.getRoot());

        assertEquals(0, treeWalker.countNodes());
    }

    @Test
    void pruneLeaf() throws MisconfigurationException {
        LOGGER.log("TEST CASE: pruneLeaf", LogLevel.DEBUG);

        buildTree();

        TreeNode<Boolean> cuttingNode = treeWalker.getElementByName("11").get(0);
        assertTrue(treeWalker.isLeaf(cuttingNode));
        assertEquals(12, treeWalker.countNodes());

        treeWalker.prune(cuttingNode);
        assertEquals(11, treeWalker.countNodes());
    }

    @Test
    void pruneElementIsNotInTree() throws MisconfigurationException {
        LOGGER.log("TEST CASE: pruneElementIsNotInTree", LogLevel.DEBUG);

        buildTree();
        assertEquals(12, treeWalker.countNodes());

        treeWalker.addNode(new TreeNode<Boolean>("the lonley outsider"));
        assertEquals(13, treeWalker.countNodes());
        assertFalse(treeWalker.validateTree(treeWalker.getTree()));

        treeWalker.prune(treeWalker.getElementByName("the lonley outsider").get(0));
        assertEquals(12, treeWalker.countNodes());
    }

    @Test
    void prune() throws MisconfigurationException {
        LOGGER.log("TEST CASE: prune", LogLevel.DEBUG);

        buildTree();

        treeWalker.prune(treeWalker.getElementByName("01").get(0));
        assertEquals(8, treeWalker.countNodes());
        LOGGER.log("\n" + treeWalker.toString(), LogLevel.TRACE);
    }

    @Test
    void failPruneByEmptyTree() throws Exception {
        LOGGER.log("TEST CASE: failPruneByEmptyTree", LogLevel.DEBUG);

        TreeWalker<Boolean> emptyTree = new ListTree<Boolean>();
        assertEquals(0, emptyTree.countNodes());
        assertThrows(Exception.class, () -> {
            emptyTree.prune(new TreeNode<Boolean>("cut me"));
        });
    }

    @Test
    void failPruneByEmptyNode() throws Exception {
        LOGGER.log("TEST CASE: failPruneByEmptyNode", LogLevel.DEBUG);

        buildTree();
        assertThrows(Exception.class, () -> {
            treeWalker.prune(null);
        });
    }

    @Test
    void merge() throws MisconfigurationException {
        LOGGER.log("TEST CASE: merge", LogLevel.DEBUG);

        buildTree();

        TreeNode<Boolean> mergeID = treeWalker.getElementByName("03").get(0);
        treeWalker.merge(mergeID.getUuid(), appendTree());
        LOGGER.log("\n" + treeWalker.toString(), LogLevel.TRACE);

        assertEquals(16, treeWalker.countNodes());
    }

    @Test
    void failMerge() throws MisconfigurationException {
        LOGGER.log("TEST CASE: failMerge", LogLevel.DEBUG);

        buildTree();

        treeWalker.merge("node", null);
        assertEquals(12, treeWalker.countNodes());

        treeWalker.merge("node", new ListTree<Boolean>());
        assertEquals(12, treeWalker.countNodes());
    }

    @Test
    void passValidateTree() throws MisconfigurationException {
        LOGGER.log("TEST CASE: passValidateTree", LogLevel.DEBUG);

        buildTree();

        List<TreeNode<Boolean>> tree = treeWalker.getTree();
        assertTrue(treeWalker.validateTree(tree));
    }

    @Test
    void failValidateByEmptyTree() throws MisconfigurationException {
        LOGGER.log("TEST CASE: failValidateByTreeRemoveNode", LogLevel.DEBUG);

        TreeWalker<Boolean> emptyTree = new ListTree<Boolean>();
        assertFalse(emptyTree.validateTree(emptyTree.getTree()));
    }

    @Test
    void failValidateTreeRemoveNode() throws MisconfigurationException {
        LOGGER.log("TEST CASE: failValidateTreeRemoveNode", LogLevel.DEBUG);

        buildTree();
        assertFalse(treeWalker.removeNode(treeWalker.getElementByName("05").get(0)));
        assertTrue(treeWalker.validateTree(treeWalker.getTree()));
    }

    @Test
    void failValidateTreeNodeWithEmptyParent() throws MisconfigurationException {
        LOGGER.log("TEST CASE: failValidateTreeNodeWithEmptyParent", LogLevel.DEBUG);

        buildTree();
        TreeNode<Boolean> lost_01 = new TreeNode<>("lost");
        treeWalker.addNode(lost_01);

        assertFalse(treeWalker.validateTree(treeWalker.getTree()));
    }

    @Test
    void failValidateTreeNodeWithDisconnectedParent() throws MisconfigurationException {
        LOGGER.log("TEST CASE: failValidateTreeNodeWithDisconnectedParent", LogLevel.DEBUG);

        buildTree();
        TreeNode<Boolean> lost_01 = new TreeNode<>("diconnected");
        lost_01.setParent(StringUtils.generateUUID());
        treeWalker.addNode(lost_01);

        TreeNode<Boolean> lost_02 = new TreeNode<>("sub node 01");
        lost_02.setParent(lost_01.getUuid());
        treeWalker.addNode(lost_02);
        TreeNode<Boolean> lost_03 = new TreeNode<>("sub node 02");
        lost_03.setParent(lost_01.getUuid());
        treeWalker.addNode(lost_03);

        assertFalse(treeWalker.validateTree(treeWalker.getTree()));
    }

    @Test
    void failValidateTreeHasNoRoot() throws MisconfigurationException {
        LOGGER.log("TEST CASE: failValidateTreeHasNoRoot", LogLevel.DEBUG);

        treeWalker.clear();

        TreeWalker<Boolean> tree = new ListTree<Boolean>();
        TreeNode<Boolean> n_01 = new TreeNode<>("01");
        tree.addNode(n_01);
        TreeNode<Boolean> n_04 = new TreeNode<>("04");
        n_04.setParent(n_01.getUuid());
        tree.addNode(n_04);
        TreeNode<Boolean> n_05 = new TreeNode<>("05");
        n_05.setParent(n_01.getUuid());
        tree.addNode(n_05);

        assertFalse(treeWalker.validateTree(tree.getTree()));
    }

    @Test
    void failValidateTreeHasMulitpleRoot() throws MisconfigurationException {
        LOGGER.log("TEST CASE: failValidateTreeHasMulitpleRoot", LogLevel.DEBUG);

        TreeNode<Boolean> node = new TreeNode<>("second ROOT");
        node.setParent("-1");
        treeWalker.addNode(node);

        assertFalse(treeWalker.validateTree(treeWalker.getTree()));
    }

    private void buildTree() throws MisconfigurationException {
        //P:0
        TreeNode<Boolean> n_01 = new TreeNode<>("01");
        n_01.setParent(treeWalker.getRoot().getUuid());
        treeWalker.addNode(n_01);
        TreeNode<Boolean> n_02 = new TreeNode<>("02");
        n_02.setParent(treeWalker.getRoot().getUuid());
        treeWalker.addNode(n_02);
        TreeNode<Boolean> n_03 = new TreeNode<>("03");
        n_03.setParent(treeWalker.getRoot().getUuid());
        treeWalker.addNode(n_03);
        //P:1
        TreeNode<Boolean> n_04 = new TreeNode<>("04");
        n_04.setParent(n_01.getUuid());
        treeWalker.addNode(n_04);
        TreeNode<Boolean> n_05 = new TreeNode<>("05");
        n_05.setParent(n_01.getUuid());
        treeWalker.addNode(n_05);
        //P:2
        TreeNode<Boolean> n_06 = new TreeNode<>("06");
        n_06.setParent(n_02.getUuid());
        treeWalker.addNode(n_06);
        //P:3
        TreeNode<Boolean> n_07 = new TreeNode<>("07");
        n_07.setParent(n_03.getUuid());
        treeWalker.addNode(n_07);
        //P:5
        TreeNode<Boolean> n_08 = new TreeNode<>("08");
        n_08.setParent(n_05.getUuid());
        treeWalker.addNode(n_08);
        //P:7
        TreeNode<Boolean> n_09 = new TreeNode<>("09");
        n_09.setParent(n_07.getUuid());
        treeWalker.addNode(n_09);
        TreeNode<Boolean> n_10 = new TreeNode<>("10");
        n_10.setParent(n_07.getUuid());
        treeWalker.addNode(n_10);
        //P:9
        TreeNode<Boolean> n_11 = new TreeNode<>("11");
        n_11.setParent(n_09.getUuid());
        treeWalker.addNode(n_11);
    }

    private TreeWalker<Boolean> appendTree() throws MisconfigurationException {
        TreeWalker<Boolean> append = new ListTree<Boolean>(new TreeNode<>("sub tree"));
        //P:0
        TreeNode<Boolean> n_01 = new TreeNode<>("S-01");
        n_01.setParent(append.getRoot().getUuid());
        append.addNode(n_01);
        TreeNode<Boolean> n_02 = new TreeNode<>("S-02");
        n_02.setParent(append.getRoot().getUuid());
        append.addNode(n_02);
        TreeNode<Boolean> n_03 = new TreeNode<>("S-03");
        n_03.setParent(append.getRoot().getUuid());
        append.addNode(n_03);

        return append;
    }
}
