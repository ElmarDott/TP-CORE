package org.europa.together.domain;

import java.util.Objects;
import org.europa.together.business.FeatureToggle;
import org.europa.together.utils.StringUtils;

/**
 * Data structure for a tree. A node is unique by is UUID, but in real
 * environments is a more specified definition necessary. A parent node can not
 * have two child nods with the same name. for a user is then hard to
 * distinguish which child node is the right one. (e.g. Files and Folders)
 */
public class TreeNode {

    private static final int HASH = 97;

    private final String uuid;
    private String nodeName;
    private String parent;
    private Object attributes;

    /**
     * Constructor.
     */
    @FeatureToggle(featureID = "CM-0009.DO")
    public TreeNode() {
        this.uuid = StringUtils.generateUUID();
    }

    /**
     * Constructor.
     *
     * @param name as String
     */
    @FeatureToggle(featureID = "CM-0009.DO")
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
    public Object getValue() {
        return attributes;
    }

    /**
     * Set the value of a node.
     *
     * @param value as Object
     */
    public void setValue(final Object value) {
        this.attributes = value;
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

    @Override
    public String toString() {
        return "TreeNode{" + "uuid=" + uuid
                + ", nodeName=" + nodeName
                + ", parent=" + parent
                + ", value=" + attributes + '}';
    }

    @Override
    public int hashCode() {
        int hash = HASH * 3;
        hash = hash + Objects.hashCode(this.uuid);
        return hash;
    }

    @Override
    public boolean equals(final Object obj) {
        boolean success = true;

        if (obj == null) {
            success = false;
        } else {

            if (this == obj) {
                success = true;
            } else if (getClass() != obj.getClass()) {
                success = false;
            } else {
                final TreeNode other = (TreeNode) obj;
                if (!Objects.equals(this.uuid, other.uuid)) {
                    success = false;
                }
            }
        }
        return success;
    }

}
