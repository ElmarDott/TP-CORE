package org.europa.together.application;

import java.util.ArrayList;
import java.util.List;
import org.europa.together.business.Logger;
import org.europa.together.business.TreeWalker;
import org.europa.together.domain.LogLevel;
import org.europa.together.domain.TreeNode;
import org.europa.together.exceptions.MisconfigurationException;
import org.europa.together.utils.StringUtils;
import org.springframework.stereotype.Repository;

/**
 * Implementation of a TreeWalker.
 *
 * @param <T> define the TreeType
 */
@Repository
public class ListTree<T> implements TreeWalker<T> {

    private static final long serialVersionUID = 9L;
    private static final Logger LOGGER = new LogbackLogger(ListTree.class);

    private List<TreeNode<T>> tree = new ArrayList<>();
    private String rootUuid;

    /**
     * Constructor.
     */
    public ListTree() {
        LOGGER.log("instance class", LogLevel.INFO);
    }

    /**
     * Constructor.
     *
     * @param root as TreeNode
     */
    public ListTree(final TreeNode<T> root) {
        this.addRoot(root);
        LOGGER.log("instance class (" + root.getNodeName() + ")", LogLevel.INFO);
    }

    @Override
    public boolean addRoot(final TreeNode<T> root) {
        boolean success = false;
        if (StringUtils.isEmpty(rootUuid)) {
            this.rootUuid = root.getUuid();
            root.setParent("-1");
            tree.add(root);
            success = true;
            LOGGER.log("ROOT: [" + this.rootUuid + "] added.", LogLevel.DEBUG);
        } else {
            LOGGER.log("Root node [" + this.rootUuid + "] for this tree already exist.",
                    LogLevel.WARN);
        }
        return success;
    }

    @Override
    public boolean isElementOfTree(final TreeNode<T> node) {
        boolean success = false;
        if (!tree.isEmpty()) {
            for (TreeNode<T> element : this.tree) {
                if (node.equals(element)) {
                    success = true;
                }
            }
        } else {
            LOGGER.log("Operation not possible, because Tree is empty.", LogLevel.WARN);
        }
        return success;
    }

    @Override
    public boolean isEmpty() {
        return tree.isEmpty();
    }

    @Override
    public boolean isLeaf(final TreeNode<T> leaf) {
        boolean isLeaf = false;
        if (!tree.isEmpty()) {
            boolean hasParent = false;
            for (TreeNode<T> check : this.tree) {
                if (leaf.getUuid().equals(check.getParent())) {
                    hasParent = true;
                    break;
                }
            }
            if (!hasParent) {
                isLeaf = true;
            }
        } else {
            LOGGER.log("Operation not possible, because Tree is empty.", LogLevel.WARN);
        }
        return isLeaf;
    }

    @Override
    public boolean removeNode(final TreeNode<T> node) {
        boolean success = false;
        if (this.isLeaf(node)) {
            this.tree.remove(node);
            success = true;
            LOGGER.log("Node " + node.getNodeName() + " removed.", LogLevel.DEBUG);
        } else {
            LOGGER.log("Node " + node.getNodeName()
                    + " can't removed. First you need to remove all childe node(s)",
                    LogLevel.WARN);
        }
        return success;
    }

    @Override
    public int countNodes() {
        return this.tree.size();
    }

    @Override
    public int isNameUnique(final String nodeName) {
        int count = 0;
        if (!tree.isEmpty()) {
            for (TreeNode<T> node : this.tree) {
                if (node.getNodeName().equals(nodeName)) {
                    count++;
                }
            }
        } else {
            LOGGER.log("Operation not possible, because Tree is empty.", LogLevel.WARN);
        }
        return count;
    }

    @Override
    public List<TreeNode<T>> getElementByName(final String nodeName) {
        List<TreeNode<T>> elements = new ArrayList<>();
        if (!tree.isEmpty()) {
            for (TreeNode<T> node : this.tree) {
                if (node.getNodeName().equals(nodeName)) {
                    elements.add(node);
                }
            }

            if (elements.isEmpty()) {
                LOGGER.log("No Node(s) with the name " + nodeName + " found.", LogLevel.DEBUG);
            }
        } else {
            LOGGER.log("Operation not possible, because Tree is empty.", LogLevel.WARN);
        }
        return elements;
    }

    @Override
    public List<TreeNode<T>> getLeafs() {
        List<TreeNode<T>> leafs = new ArrayList<>();
        for (TreeNode<T> node : tree) {
            if (isLeaf(node)) {
                leafs.add(node);
            }
        }
        LOGGER.log(leafs.size() + " Leaf Nodes in the tree detected.", LogLevel.DEBUG);
        return leafs;
    }

    @Override
    public List<TreeNode<T>> getTree() {
        return List.copyOf(tree);
    }

    @Override
    public TreeNode<T> getNodeByUuid(final String uuid) {
        TreeNode<T> search = null;
        if (!tree.isEmpty()) {
            for (TreeNode<T> node : this.tree) {
                if (node.getUuid().equals(uuid)) {
                    search = node;
                    break;
                }
            }
        } else {
            LOGGER.log("Operation not possible, because Tree is empty.", LogLevel.WARN);
        }
        return search;
    }

    @Override
    public TreeNode<T> getRoot() {
        TreeNode<T> root = null;
        if (this.rootUuid != null) {
            for (TreeNode<T> node : this.tree) {
                if (node.getUuid().equals(this.rootUuid)) {
                    root = node;
                }
            }
        }
        return root;
    }

    @Override
    public boolean addNode(final TreeNode<T> node) {
        boolean add = true;
        if (node != null) {
            for (TreeNode<T> check : this.tree) {
                if (node.getUuid().equals(check.getUuid())) {
                    LOGGER.log("Node with this UUID already exist.", LogLevel.WARN);
                    add = false;
                    break;
                }
                if (!StringUtils.isEmpty(node.getParent())
                        && node.getParent().equals(check.getParent())
                        && node.getNodeName().equals(check.getNodeName())) {
                    LOGGER.log("Node with same name AND parent  already exist.",
                            LogLevel.WARN);
                    add = false;
                    break;
                }
            }
            if (add) {
                tree.add(node);
                LOGGER.log("Node [" + node.getNodeName() + "] added.",
                        LogLevel.DEBUG);
            }
        } else {
            add = false;
        }
        return add;
    }

    @Override
    public void clear() {
        this.tree.clear();
        this.rootUuid = "";
        LOGGER.log("Tree successful reseted.", LogLevel.TRACE);
    }

    @Override
    public void prune(final TreeNode<T> cutNode) throws MisconfigurationException {
        List<TreeNode<T>> stack = new ArrayList<>();
        LOGGER.log("Cut Tree by Node "
                + cutNode.getNodeName() + " ["
                + cutNode.getUuid() + "]", LogLevel.DEBUG);
        stack.addAll(tree);
        if (stack.isEmpty()) {
            throw new MisconfigurationException("prune: tree is empty nothing to do");
        } else if (cutNode.equals(getRoot())) {
            clear();
        } else if (isLeaf(cutNode)) {
            removeNode(cutNode);
        } else {
            TreeNode<T> node = cutNode;
            List<TreeNode<T>> helper = new ArrayList<>();
            List<TreeNode<T>> cutted = new ArrayList<>();
            cutted.add(node);
            for (int i = 0; i < tree.size(); i++) {
                for (TreeNode<T> compare : stack) {
                    if (i != 0) {
                        node = compare;
                    }
                    for (int j = 0; j < tree.size(); j++) {
                        if (node.getUuid().equals(compare.getUuid())) {
                            cutted.add(compare);
                            if (!isLeaf(compare)) {
                                helper.add(compare);
                            }
                            break;
                        }
                    }
                }
                if (!helper.isEmpty()) {
                    stack.clear();
                    for (TreeNode<T> compare : helper) {
                        for (TreeNode<T> element : this.tree) {
                            if (compare.getUuid().equals(element.getParent())) {
                                stack.add(element);
                            }
                        }
                    }
                    cutted.addAll(stack);
                    helper.clear();
                } else {
                    break;
                }
            }
            for (TreeNode<T> item : cutted) {
                this.tree.remove(item);
            }
        }
    }

    @Override
    public void merge(final String parentUuid, final TreeWalker<T> appendingTree) {
        if (appendingTree != null && !appendingTree.isEmpty()) {
            TreeNode<T> appendingRoot = appendingTree.getRoot();
            appendingRoot.setParent(parentUuid);
            List<TreeNode<T>> newTree = new ArrayList<>();
            newTree.addAll(appendingTree.getTree());
            this.tree.addAll(newTree);
            LOGGER.log("Append " + newTree.size()
                    + " Nodes to the new Tree.", LogLevel.DEBUG);
        } else {
            LOGGER.log("Merging tree is empty - no merge applied.", LogLevel.DEBUG);
        }
    }

    @Override
    public boolean validateTree(final List<TreeNode<T>> collection)
            throws MisconfigurationException {
        boolean isTree = false;
        int marker = 0;
        int counter = 0;
        int root = 0;
        if (!collection.isEmpty()) {
            marker = collection.size() - 1;
            List<TreeNode<T>> comperator = new ArrayList<>();
            comperator.addAll(collection);
            for (TreeNode<T> node : collection) {
                if (StringUtils.isEmpty(node.getParent())) {
                    counter++;
                    LOGGER.log("Element with empty parent: " + node.getNodeName(), LogLevel.DEBUG);
                }
                if (!StringUtils.isEmpty(node.getParent()) && node.getParent().equals("-1")) {
                    root++;
                    LOGGER.log("ROOT element: " + node.getNodeName(), LogLevel.DEBUG);
                }
                for (TreeNode<T> element : comperator) {
                    if (!StringUtils.isEmpty(node.getParent())
                            && !node.getParent().equals("-1")
                            && !element.equals(node)
                            && element.getUuid().equals(node.getParent())) {
                        marker--;
                        LOGGER.log("Parent of (" + node.getNodeName() + ") : " + node.getUuid()
                                + " : (" + element.getNodeName() + ") " + element.getParent(),
                                LogLevel.DEBUG);
                    }
                }
            }
        }
        if (root == 0) {
            LOGGER.log("Tree has no ROOT element.", LogLevel.DEBUG);
        } else if (root > 1) {
            LOGGER.log("Tree has muliple ROOT elements.", LogLevel.DEBUG);
        }
        if (marker != 0) {
            LOGGER.log("Tree has " + marker + " disconnected Elemets.", LogLevel.DEBUG);
        }
        if (counter == 0
                && root == 1
                && marker == 0) {
            isTree = true;
        }
        return isTree;
    }

    @Override
    public String toString() {
        StringBuilder out = new StringBuilder();
        out.append("TreeWalkerImpl{\n")
                .append("\t Root: ")
                .append(getRoot().getNodeName())
                .append("\n")
                .append("\t Nodes: ")
                .append(tree.size())
                .append("\n TREE >>>\n");
        for (TreeNode<T> node : tree) {
            out.append("\t")
                    .append(node.toString())
                    .append("\n");
        }
        out.append("}");
        return out.toString();
    }
}
