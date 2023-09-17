package org.europa.together.business;

import java.util.List;
import org.apiguardian.api.API;
import static org.apiguardian.api.API.Status.STABLE;
import org.europa.together.domain.TreeNode;
import org.springframework.stereotype.Component;

/**
 * Implementation of the TREE data structure. The Domain Object TreeNode
 * represent a node: <br>
 * <li>UUID (String): autogenerated by Constructor
 * <li>name (String): a human readable description of the node (e. g. Folder)
 * <li>value (Object): represent the content of a node (e. g. File)
 * <li>parent (String): Reference of the UUID to the parent node
 *
 * @author elmar.dott@gmail.com
 * @version 1.0
 * @since 1.0
 */
@API(status = STABLE, since = "1.0")
@Component
public interface TreeWalker {

    /**
     * Identifier for the given feature to enable toggles.
     */
    @API(status = STABLE, since = "1.2")
    String FEATURE_ID = "CM-0009";

    /**
     * Add the root node to the tree.
     *
     * @param root as TreeNode
     * @return true on success
     */
    @API(status = STABLE, since = "1.0")
    boolean addRoot(TreeNode root);

    /**
     * Test if a node is a element of the tree.
     *
     * @param node as TreeNode
     * @return true on success
     */
    @API(status = STABLE, since = "1.0")
    boolean isElementOfTree(TreeNode node);

    /**
     * Check if the representing tree is empty.
     *
     * @return true n success
     */
    @API(status = STABLE, since = "1.0")
    boolean isEmpty();

    /**
     * Check if a given node is a leaf in the tree. Leafs don't have child
     * nodes. => node.uuid != otherNode.getParent
     *
     * @param node as TreeNode
     * @return true on success
     */
    @API(status = STABLE, since = "1.0")
    boolean isLeaf(TreeNode node);

    /**
     * Removes a Node (Leaf) from the tree. This method allows just to cut
     * elements from the tree which has no children. In the case it is necessary
     * to cut a subtree use the prune() method.
     *
     * @param node as TreeNode
     * @return true on success
     */
    @API(status = STABLE, since = "1.0")
    boolean removeNode(TreeNode node);

    /**
     * Count the nodes of the tree.
     *
     * @return count as int
     */
    @API(status = STABLE, since = "1.0")
    int countNodes();

    /**
     * Test if a Element name exist more than one time. Useful to get to know
     * how often a name is used. Return the count how often the name for this
     * element already appear in the data structure.
     *
     * @param nodeName as String
     * @return count as int
     */
    @API(status = STABLE, since = "1.0")
    int isNameUnique(String nodeName);

    /**
     * Get all nodes of the tree with the same NodeName.
     *
     * @param nodeName as String
     * @return nodes as List
     */
    @API(status = STABLE, since = "1.0")
    List<TreeNode> getElementByName(String nodeName);

    /**
     * Get all leaf nodes of the tree.
     *
     * @return leafs as List
     */
    @API(status = STABLE, since = "1.0")
    List<TreeNode> getLeafs();

    /**
     * Get the full tree.
     *
     * @return tree as List
     */
    @API(status = STABLE, since = "1.0")
    List<TreeNode> getTree();

    /**
     * Get a TreeNode by his UUID.
     *
     * @param uuid as String
     * @return node as TreeNode
     */
    @API(status = STABLE, since = "1.0")
    TreeNode getNodeByUuid(String uuid);

    /**
     * Get the root TreeNode.
     *
     * @return root as TreeNode
     */
    @API(status = STABLE, since = "1.0")
    TreeNode getRoot();

    /**
     * Add the node to the tree. Check if the node is already exist in the tree
     * by comparing the UUID. In the case there exist a node already with the
     * UUID then the new one will not added. Another check is that not exist two
     * nodes with the same name and the same parent. Also for this case the node
     * will not be added to the tree.
     *
     * @param node as TreeNode
     * @return true on success
     */
    @API(status = STABLE, since = "1.0")
    boolean addNode(TreeNode node);

    /**
     * Reset al internal data of the TreeWalker.
     */
    @API(status = STABLE, since = "1.0")
    void clear();

    /**
     * Remove an Element and all his child nodes from the tree.
     *
     * @param cutNode as TreeNode
     */
    @API(status = STABLE, since = "1.0")
    void prune(TreeNode cutNode);

    /**
     * Merge another Tree (TreeWalker) into the present tree. After choosing a
     * node from the present tree the whole new three (including) the ROOT
     * Element will apped on the chosen merge node.
     *
     * @param parentUuid as String
     * @param appendingTree as TreeWalker
     */
    @API(status = STABLE, since = "1.0")
    void merge(String parentUuid, TreeWalker appendingTree);
}
