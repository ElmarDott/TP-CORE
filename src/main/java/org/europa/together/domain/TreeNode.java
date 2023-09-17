package org.europa.together.domain;

import java.util.Objects;
import org.europa.together.application.LogbackLogger;
import org.europa.together.business.Logger;
import org.europa.together.utils.StringUtils;

/**
 * Data structure for a tree.A node is unique by is UUID, but in real
 * environments is a more specified definition necessary. A parent node can not
 * have two child nods with the same name. for a user is then hard to
 * distinguish which child node is the right one. (e.g. Files and Folders)
 *
 * @param <T> as generic Class
 */
public final class TreeNode<T> {

    private static final Logger LOGGER = new LogbackLogger(TreeNode.class);
    private static final int HASH = 97;

    private String uuid;
    private String nodeName;
    private String parent;
    private T value;

    /**
     * Constructor.
     */
    public TreeNode() {
        this.uuid = StringUtils.generateUUID();
    }

    /**
     * Constructor.
     *
     * @param name as String
     */
    public TreeNode(final String name) {
        this.uuid = StringUtils.generateUUID();
        this.nodeName = name;
    }

    //<editor-fold defaultstate="collapsed" desc="Getter / Setter">
    /**
     * Get the name of the node.
     *
     * @return name as String
     */
    public String getNodeName() {
        return nodeName;
    }

    /**
     * Set the name of the node.
     *
     * @param nodeName as String
     */
    public void setNodeName(final String nodeName) {
        this.nodeName = nodeName;
    }

    /**
     * Get the UUID of the parent node.
     *
     * @return UUID as String
     */
    public String getParent() {
        return parent;
    }

    /**
     * Set the UUID of the parent node.
     *
     * @param parent as String
     */
    public void setParent(final String parent) {
        this.parent = parent;
    }

    /**
     * Get the value of a node.
     *
     * @return value as Object
     */
    public T getValue() {
        return value;
    }

    /**
     * Set the value of a node.
     *
     * @param value as Object
     */
    public void setValue(final T value) {
        this.value = value;
    }

    /**
     * Get the UUID of the node.
     *
     * @return uuid as String
     */
    public String getUuid() {
        return uuid;
    }
    //</editor-fold>

    /**
     * Clone (copy) a TreeNode to a we instance.
     *
     * @param node as TreeNode
     * @return a copy of the TreeNode
     */
    public TreeNode<T> copy(final TreeNode<T> node) {
        TreeNode<T> copy = new TreeNode<>();
        copy.uuid = node.uuid;
        copy.nodeName = node.nodeName;
        copy.parent = node.parent;
        copy.value = node.value;
        return copy;
    }

    @Override
    public String toString() {
        return "TreeNode{" + "uuid=" + uuid
                + ", nodeName=" + nodeName
                + ", parent=" + parent
                + ", value=" + value + '}';
    }

    @Override
    public int hashCode() {
        return HASH + Objects.hashCode(this.nodeName);
    }

    @Override
    public boolean equals(final Object obj) {
        boolean success = false;
        if (obj != null && obj instanceof TreeNode) {
            if (this == obj) {
                success = true;
            } else {
                final TreeNode<?> other = (TreeNode<?>) obj;
                if (Objects.equals(this.parent, other.parent)
                        && Objects.equals(this.nodeName, other.nodeName)) {
                    success = true;
                }
            }
        }
        if (success) {
            LOGGER.log(this.toString() + " == " + obj.toString(), LogLevel.DEBUG);
        }
        return success;
    }
}
