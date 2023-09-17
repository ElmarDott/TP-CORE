package org.europa.together.business;

import java.util.List;
import org.apiguardian.api.API;
import static org.apiguardian.api.API.Status.STABLE;
import org.europa.together.domain.TreeNode;
import org.europa.together.exceptions.MisconfigurationException;
import org.springframework.stereotype.Component;

/**
 * Implementation of the TREE data structure. The domain object TreeNode
 * represent a node: <br>
 * <li>UUID (String): auto generated by constructor
 * <li>name (String): a human readable description of the node (e. g. folder)
 * <li>value (Object): represent the content of a node (e. g. file)
 * <li>parent (String): reference of the UUID to the parent node
 *
 * @param <T> define the TreeType
 *
 * @author elmar.dott@gmail.com
 * @version 1.2
 * @since 1.0
 */
@API(status = STABLE, since = "1.0", consumers = "ListTree")
@Component
public interface TreeWalker<T> {

    /**
     * Identifier for the given feature.
     */
    @API(status = STABLE, since = "1.2")
    String FEATURE_ID = "CM-09";

    /**
     * Add the root node to the tree.
     *
     * @param root as TreeNode
     * @return true on success
     */
    @API(status = STABLE, since = "1.0")
    boolean addRoot(TreeNode<T> root);

    /**
     * Test if a node is a element of the tree. <b>This function do not validate
     * if all tree elements are well connected.</b> This tests returns true if
     * the element is added to the tree list.
     *
     * @param node as TreeNode
     * @return true on success
     */
    @API(status = STABLE, since = "1.0")
    boolean isElementOfTree(TreeNode<T> node);

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
    boolean isLeaf(TreeNode<T> node);

    /**
     * Removes a Node (leaf) from the tree. This method allows just to cut
     * elements from the tree which has no children. In the case it is necessary
     * to cut a subtree use the prune() method.
     *
     * @param node as TreeNode
     * @return true on success
     */
    @API(status = STABLE, since = "1.0")
    boolean removeNode(TreeNode<T> node);

    /**
     * Count the nodes of the tree.
     *
     * @return count as int
     */
    @API(status = STABLE, since = "1.0")
    int countNodes();

    /**
     * Test if a element name exist more than one time. Useful to get to know
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
    List<TreeNode<T>> getElementByName(String nodeName);

    /**
     * Get all leaf nodes of the tree.
     *
     * @return leafs as List
     */
    @API(status = STABLE, since = "1.0")
    List<TreeNode<T>> getLeafs();

    /**
     * Get the full tree.
     *
     * @return tree as List
     */
    @API(status = STABLE, since = "1.0")
    List<TreeNode<T>> getTree();

    /**
     * Get a TreeNode by his UUID.
     *
     * @param uuid as String
     * @return node as TreeNode
     */
    @API(status = STABLE, since = "1.0")
    TreeNode<T> getNodeByUuid(String uuid);

    /**
     * Get the root TreeNode. If no root is set the return will be NULL.
     *
     * @return root as TreeNode
     */
    @API(status = STABLE, since = "3.0")
    TreeNode<T> getRoot();

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
    boolean addNode(TreeNode<T> node);

    /**
     * Reset all internal data of the TreeWalker.
     */
    @API(status = STABLE, since = "1.0")
    void clear();

    /**
     * Remove an element and all his child nodes from the tree.
     *
     * @param cutNode as TreeNode
     * @throws org.europa.together.exceptions.MisconfigurationException
     */
    @API(status = STABLE, since = "1.0")
    void prune(TreeNode<T> cutNode) throws MisconfigurationException;

    /**
     * Merge another Tree (TreeWalker) into the present tree. After choosing a
     * node from the present tree the whole new three (including) the ROOT
     * element will apped on the chosen merge node.
     *
     * @param parentUuid as String
     * @param appendingTree as TreeWalker
     */
    @API(status = STABLE, since = "1.0")
    void merge(String parentUuid, TreeWalker<T> appendingTree);

    /**
     * Check if the tree is valid. A valid tree has no "unconnected" elements
     * (treeNodes). There are three failure types:<br>
     * <li>1: Single TreeNode elements without a parent</li>
     * <li>2: Connected TreeNode fragments without a root</li>
     * <li>3: Multiple Trees with root element</li>
     * Those three types can occur in various combinations.
     *
     * @param tree as List
     * @return true on success
     * @throws org.europa.together.exceptions.MisconfigurationException
     */
    @API(status = STABLE, since = "2.1")
    boolean validateTree(List<TreeNode<T>> tree) throws MisconfigurationException;
}
