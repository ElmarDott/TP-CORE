package org.europa.together.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import org.europa.together.application.LoggerImpl;
import org.europa.together.business.Logger;
import org.europa.together.utils.StringUtils;

/**
 * Application wide configuration with key=value entries. For an easier
 * maintenance are entries with module-name, module-version and a deprecated
 * marker extended.
 */
@Entity
@Table(name = "APP_CONFIG",
        indexes = {
            @Index(columnList = "CONF_KEY", name = "configuration_key"),
            @Index(columnList = "MODUL_NAME", name = "modul_name"),
            @Index(columnList = "CONF_SET", name = "configuration_set")
        },
        uniqueConstraints = {
            @UniqueConstraint(columnNames
                    = {"MODUL_NAME", "MODUL_VERSION", "CONF_KEY"})
        }
)
public class ConfigurationDO implements Serializable {

    private static final long serialVersionUID = 102L;
    private static final Logger LOGGER = new LoggerImpl(ConfigurationDO.class);
    private static final int HASH = 43;

    /**
     * The name of the used database table for this domain object.
     */
    public static final String TABLE_NAME = "APP_CONFIG";

    @Id
    @Column(name = "IDX")
    private String uuid;

    @NotNull(message = "{validation.notnull}")
    @Column(name = "CONF_KEY", nullable = false)
    private String key;

    @Column(name = "CONF_VALUE")
    private String value;

    @NotNull(message = "{validation.notnull}")
    @Column(name = "DEFAULT_VALUE", nullable = false)
    private String defaultValue;

    @NotNull(message = "{validation.notnull}")
    @Column(name = "MODUL_NAME", nullable = false)
    private String modulName;

    @NotNull(message = "{validation.notnull}")
    @Column(name = "MODUL_VERSION", nullable = false)
    private String version;

    @NotNull(message = "{validation.notnull}")
    @Column(name = "CONF_SET", nullable = false)
    private String configurationSet;

    @NotNull(message = "{validation.notnull}")
    @Column(name = "DEPECATED", nullable = false)
    private boolean depecated;

    @NotNull(message = "{validation.notnull}")
    @Column(name = "MANDATORY", nullable = false)
    private boolean mandatory;

    @Column(name = "COMMENT")
    private String comment;

    /**
     * Constructor.
     */
    public ConfigurationDO() {
        //PreSet
        this.uuid = StringUtils.generateUUID();
    }

    /**
     * Constructor.
     *
     * @param key as String
     * @param value as String
     * @param modulName as String
     * @param version as String
     */
    public ConfigurationDO(final String key, final String value, final String modulName,
            final String version) {

        //PreSet
        this.uuid = StringUtils.generateUUID();
        //mandatory
        this.modulName = modulName;
        this.version = version;
        this.key = key;
        this.value = value;
        //optional
        this.configurationSet = "default";
        this.defaultValue = "NIL";
        this.depecated = false;
        this.mandatory = false;
        this.comment = "";
    }

    /**
     * Actions who have to performed before objects get persisted. e.g. cerate
     * default entries in the database.
     */
    @PrePersist
    public void prePersist() {
        this.configurationSet = "default";
        this.defaultValue = "NIL";
        this.depecated = false;
        this.mandatory = false;
        LOGGER.log("@PrePersist [ConfigurationDO]", LogLevel.INFO);
    }

    //<editor-fold defaultstate="collapsed" desc="Getter / Setter">
    /**
     * Show if entry is depecated.
     *
     * @return true if is depecated
     */
    public boolean isDepecated() {
        return depecated;
    }

    /**
     * Mark if an configuration entry is mandatory.
     *
     * @return true if is mandatory
     */
    public boolean isMandatory() {
        return mandatory;
    }

    /**
     * Set the comment.
     *
     * @param comment as String
     */
    public void setComment(final String comment) {
        this.comment = comment;
    }

    /**
     * Set the configuration set.
     *
     * @param configurationSet as String
     */
    public void setConfigurationSet(final String configurationSet) {
        this.configurationSet = configurationSet;
    }

    /**
     * Set the default value.
     *
     * @param defaultValue as String
     */
    public void setDefaultValue(final String defaultValue) {
        this.defaultValue = defaultValue;
    }

    /**
     * Set if a entry is depecated.
     *
     * @param depecated as boolean
     */
    public void setDepecated(final boolean depecated) {
        this.depecated = depecated;
    }

    /**
     * Set if a entry is mandatory.
     *
     * @param mandatory as boolean
     */
    public void setMandatory(final boolean mandatory) {
        this.mandatory = mandatory;
    }

    /**
     * Set key.
     *
     * @param key as String
     */
    public void setKey(final String key) {
        this.key = key;
    }

    /**
     * Set module´name.
     *
     * @param modulName as String
     */
    public void setModulName(final String modulName) {
        this.modulName = modulName;
    }

    /**
     * Set the UUID.
     *
     * @param uuid as String
     */
    public void setUuid(final String uuid) {
        this.uuid = uuid;
    }

    /**
     * Set value.
     *
     * @param value as String
     */
    public void setValue(final String value) {
        this.value = value;
    }

    /**
     * Set version of module.
     *
     * @param version as String
     */
    public void setVersion(final String version) {
        this.version = version;
    }

    /**
     * Get the comment.
     *
     * @return comment as String
     */
    public String getComment() {
        return comment;
    }

    /**
     * Get the configuration set.
     *
     * @return ConfigurationSet as String.
     */
    public String getConfigurationSet() {
        return configurationSet;
    }

    /**
     * Get the default value.
     *
     * @return dafaultValue as String
     */
    public String getDefaultValue() {
        return defaultValue;
    }

    /**
     * Get the key.
     *
     * @return key as String
     */
    public String getKey() {
        return key;
    }

    /**
     * Get Modulename.
     *
     * @return modulename as String
     */
    public String getModulName() {
        return modulName;
    }

    /**
     * Get the UUID.
     *
     * @return UUID as String
     */
    public String getUuid() {
        return uuid;
    }

    /**
     * Get value.
     *
     * @return value as String
     */
    public String getValue() {
        return value;
    }

    /**
     * Get module version.
     *
     * @return moduleversion as String
     */
    public String getVersion() {
        return version;
    }
    //</editor-fold>

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ConfigurationDO other = (ConfigurationDO) obj;
        if (!Objects.equals(this.key, other.key)) {
            return false;
        }
        if (!Objects.equals(this.modulName, other.modulName)) {
            return false;
        }
        if (!Objects.equals(this.version, other.version)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = HASH * 5;
        hash = hash + Objects.hashCode(this.key);
        hash = hash + Objects.hashCode(this.modulName);
        hash = hash + Objects.hashCode(this.version);
        return hash;
    }

    @Override
    public String toString() {
        return "ConfigurationDO{" + "uuid=" + uuid
                + ", key=" + key
                + ", value=" + value
                + ", defaultValue=" + defaultValue
                + ", modulName=" + modulName
                + ", configurationSet=" + configurationSet
                + ", version=" + version
                + ", depecated=" + depecated
                + ", mandatory=" + mandatory
                + ", comment=" + comment + '}';
    }
}
