package org.europa.together.application;

import java.util.ArrayList;
import java.util.List;
import org.europa.together.business.Logger;
import org.europa.together.business.TreeWalker;
import org.europa.together.domain.LogLevel;
import org.europa.together.domain.TreeNode;

/**
 * Implemention of a TreeWalker.
 */
public class TreeWalkerImpl implements TreeWalker {

    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = new LoggerImpl(TreeWalkerImpl.class);

    private List<TreeNode> tree;
    private String rootUuid;

    /**
     * Constructor.
     */
    public TreeWalkerImpl() {
        this.tree = new ArrayList<>();
        LOGGER.log("instance class", LogLevel.INFO);
    }

    /**
     * Constructor.
     *
     * @param root as TreeNode
     */
    public TreeWalkerImpl(final TreeNode root) {
        this.tree = new ArrayList<>();
        this.addRoot(root);
        LOGGER.log("instance class (" + root.getNodeName() + ")", LogLevel.INFO);
    }

    @Override
    public boolean addRoot(final TreeNode root) {
        boolean success = false;
        if (this.rootUuid == null) {
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
    public boolean isElementOfTree(final TreeNode node) {
        boolean success = false;
        for (TreeNode element : this.tree) {
            if (element.equals(node)) {
                success = true;
            }
        }
        return success;
    }

    @Override
    public boolean isLeaf(final TreeNode leaf) {
        boolean isLeaf = false;
        boolean hasParent = false;
        for (TreeNode check : this.tree) {
            if (leaf.getUuid().equals(check.getParent())) {
                hasParent = true;
                break;
            }
        }
        if (!hasParent) {
            isLeaf = true;
        }
        return isLeaf;
    }

    @Override
    public boolean removeNode(final TreeNode node) {
        boolean success = false;
        if (this.isLeaf(node)) {
            this.tree.remove(node);
            success = true;
            LOGGER.log("Node " + node.getNodeName() + " removed.", LogLevel.DEBUG);
        } else {
            LOGGER.log("Node \"" + node.getNodeName() + "\" can't removed.", LogLevel.WARN);
        }
        return success;
    }

    @Override
    public int countNodes() {
        return this.tree.size();
    }

    @Override
    public int countLevelsOfTree() {
        //TODO: countLevelsOfTree() : recrsive
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int isNameUnique(final String nodeName) {
        int count = 0;
        for (TreeNode node : this.tree) {
            if (node.getNodeName().equals(nodeName)) {
                count++;
            }
        }
        return count;
    }

    @Override
    public List<TreeNode> getElemtByName(final String nodeName) {
        List<TreeNode> elements = new ArrayList<>();
        for (TreeNode node : this.tree) {
            if (node.getNodeName().equals(nodeName)) {
                elements.add(node);
            }
        }
        return elements;
    }

    @Override
    public List<TreeNode> getTree() {
        return this.tree;
    }

    @Override
    public TreeNode getNodeByUuid(final String uuid) {
        TreeNode search = null;
        for (TreeNode node : this.tree) {
            if (node.getUuid().equals(uuid)) {
                search = node;
                break;
            }
        }
        return search;
    }

    @Override
    public TreeNode getRoot() {
        TreeNode root = null;
        if (this.rootUuid != null) {
            for (TreeNode node : this.tree) {
                if (node.getUuid().equals(this.rootUuid)) {
                    root = node;
                }
            }
        }

        if (root == null) {
            LOGGER.log("No root node is set.", LogLevel.ERROR);
        }
        return root;
    }

    @Override
    public void addNode(final TreeNode node) {
        boolean add = false;
        if (!this.tree.isEmpty()
                && node != null
                && !node.getUuid().equals(this.rootUuid)) {

            for (TreeNode check : this.tree) {

                if (!node.getUuid().equals(check.getUuid())
                        || !node.getParent().equals(check.getParent())
                        && !node.getNodeName().equals(check.getNodeName())) {

                    add = true;
                    LOGGER.log("Node [" + node.getNodeName() + "] added.",
                            LogLevel.DEBUG);

                } else {
                    LOGGER.log("Node with the same name and parent or the same UUID already exist.",
                            LogLevel.WARN);
                }
            }

            if (add) {
                tree.add(node);
            }
        }
    }

    public void prune(final TreeNode cutNode) {
        //TODO: prune()
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void merge(final String parentUuid, final TreeWalker appendingTree) {
        TreeNode root = appendingTree.getRoot();
        appendingTree.removeNode(root);
        root.setParent(parentUuid);
        this.tree.add(root);
        this.tree.addAll(appendingTree.getTree());
        //TODO: merge()
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String toString() {
        return "TreeWalkerImpl{" + "tree=" + tree + '}';
    }

}
