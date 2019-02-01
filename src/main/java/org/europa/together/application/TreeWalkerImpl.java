package org.europa.together.application;

import java.util.ArrayList;
import java.util.List;
import org.europa.together.business.FeatureToggle;
import org.europa.together.business.Logger;
import org.europa.together.business.TreeWalker;
import org.europa.together.domain.LogLevel;
import org.europa.together.domain.TreeNode;
import org.europa.together.utils.StringUtils;
import org.springframework.stereotype.Repository;

/**
 * Implementation of a TreeWalker.
 */
@Repository
public final class TreeWalkerImpl implements TreeWalker {

    private static final long serialVersionUID = 9L;
    private static final Logger LOGGER = new LoggerImpl(TreeWalkerImpl.class);

    private List<TreeNode> tree;
    private String rootUuid;

    /**
     * Constructor.
     */
    @FeatureToggle(featureID = FEATURE_ID)
    public TreeWalkerImpl() {
        this.tree = new ArrayList<>();
        LOGGER.log("instance class", LogLevel.INFO);
    }

    /**
     * Constructor.
     *
     * @param root as TreeNode
     */
    @FeatureToggle(featureID = FEATURE_ID)
    public TreeWalkerImpl(final TreeNode root) {
        this.tree = new ArrayList<>();
        this.addRoot(root);
        LOGGER.log("instance class (" + root.getNodeName() + ")", LogLevel.INFO);
    }

    @Override
    public boolean addRoot(final TreeNode root) {
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
    public boolean isElementOfTree(final TreeNode node) {
        boolean success = false;
        if (!tree.isEmpty()) {
            for (TreeNode element : this.tree) {
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
    public boolean isLeaf(final TreeNode leaf) {
        boolean isLeaf = false;
        if (!tree.isEmpty()) {

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

        } else {
            LOGGER.log("Operation not possible, because Tree is empty.", LogLevel.WARN);
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
    public int isNameUnique(final String nodeName) {
        int count = 0;
        if (!tree.isEmpty()) {
            for (TreeNode node : this.tree) {
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
    public List<TreeNode> getElementByName(final String nodeName) {
        List<TreeNode> elements = new ArrayList<>();

        if (!tree.isEmpty()) {
            for (TreeNode node : this.tree) {
                if (node.getNodeName().equals(nodeName)) {
                    elements.add(node);
                }
            }
        } else {
            LOGGER.log("Operation not possible, because Tree is empty.", LogLevel.WARN);
        }
        if (elements.isEmpty()) {
            LOGGER.log("No Node(s) with the name " + nodeName + " found.", LogLevel.DEBUG);
        }
        return elements;
    }

    @Override
    public List<TreeNode> getLeafs() {
        List<TreeNode> leafs = new ArrayList<>();
        for (TreeNode node : tree) {
            if (isLeaf(node)) {
                leafs.add(node);
            }
        }
        LOGGER.log(leafs.size() + " Leaf Nodes in the tree detected.", LogLevel.DEBUG);
        return leafs;
    }

    @Override
    public List<TreeNode> getTree() {
        return this.tree;
    }

    @Override
    public TreeNode getNodeByUuid(final String uuid) {
        TreeNode search = null;
        if (!tree.isEmpty()) {
            for (TreeNode node : this.tree) {
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
    public TreeNode getRoot() {
        TreeNode root = null;
        if (this.rootUuid != null && !tree.isEmpty()) {
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
                    break;

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

    @Override
    public void clear() {
        this.tree.clear();
        this.rootUuid = "";
        LOGGER.log("Tree successful reseted.", LogLevel.TRACE);
    }

    @Override
    public void prune(final TreeNode cutNode) {
        try {
            List<TreeNode> stack = new ArrayList<>();
            LOGGER.log("Cut Tree by Node "
                    + cutNode.getNodeName() + " ["
                    + cutNode.getUuid() + "]", LogLevel.DEBUG);

            stack.addAll(tree);
            if (cutNode.equals(getRoot())) {
                clear();
            } else if (isElementOfTree(cutNode) && isLeaf(cutNode)) {
                removeNode(cutNode);
            } else {

                TreeNode node = cutNode;
                List<TreeNode> cutted = new ArrayList<>();
                List<TreeNode> helper = new ArrayList<>();
                cutted.add(node);

                int loop = this.tree.size();
                for (int i = 0; i < loop; i++) {

                    if (!stack.isEmpty()) {
                        for (TreeNode compare : stack) {
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
                    }

                    if (!helper.isEmpty()) {
                        stack.clear();
                        for (TreeNode compare : helper) {
                            for (TreeNode element : this.tree) {
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

                if (!cutted.isEmpty()) {
                    for (TreeNode item : cutted) {
                        this.tree.remove(item);
                    }
                }

            }
        } catch (Exception ex) {
            LOGGER.catchException(ex);
        }
    }

    @Override
    public void merge(final String parentUuid, final TreeWalker appendingTree) {
        if (appendingTree != null && !appendingTree.isEmpty()) {

            TreeNode appendingRoot = appendingTree.getRoot();
            appendingRoot.setParent(parentUuid);

            List<TreeNode> newTree = new ArrayList<>();
            newTree.addAll(appendingTree.getTree());

            this.tree.addAll(newTree);
            LOGGER.log("Append " + newTree.size()
                    + " Nodes to the new Tree.", LogLevel.DEBUG);
        } else {
            LOGGER.log("Merging tree is empty - no merge applied.", LogLevel.DEBUG);

        }
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

        for (TreeNode node : tree) {
            out.append("\t")
                    .append(node.toString())
                    .append("\n");
        }

        out.append("}");
        return out.toString();
    }

}
