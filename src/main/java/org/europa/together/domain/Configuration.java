package org.europa.together.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import org.europa.together.application.LoggerImpl;
import org.europa.together.business.Logger;

/**
 * Application wide configuration with key=value entries. For an easier
 * maintenance are entries with module-name, module-version and a deprecated
 * marker extended.
 */
@Entity
@Table(name = "APP_CONFIG", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"MODUL_NAME", "MODUL_VERSION", "CONF_KEY"})
})
public class Configuration implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = new LoggerImpl(Configuration.class);
    private static final int HASH = 43;

    @Id
    @Column(name = "IDX")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String uuid;

    @NotNull(message = "validation.notnull")
    @Column(name = "CONF_KEY", nullable = false)
    private String key;

    @Column(name = "CONF_VALUE")
    private String value;

    @NotNull(message = "validation.notnull")
    @Column(name = "DEFAULT_VALUE")
    private String defaultValue;

    @NotNull(message = "validation.notnull")
    @Column(name = "MODUL_NAME", nullable = false)
    private String modulName;

    @Column(name = "CONF_SET")
    private String configurationSet;

    @NotNull(message = "validation.notnull")
    @Column(name = "MODUL_VERSION", nullable = false)
    private String version;

    @Column(name = "DEPECATED", nullable = false)
    private boolean depecated;

    @Column(name = "COMMENT")
    private String comment;

    /**
     * Constructor.
     */
    public Configuration() {
        /* NOT IN USE */
    }

    /**
     * Actions who have to performed before objects get persisted. e.g. cerate
     * default entries in the database.
     */
    @PrePersist
    public void prePersist() {
        this.depecated = false;
        LOGGER.log("@PrePersist", LogLevel.INFO);
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
     * Set key.
     *
     * @param key as String
     */
    public void setKey(final String key) {
        this.key = key;
    }

    /**
     * Set modul´name.
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
     * Set verson of modul.
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
     * Get Modulname.
     *
     * @return modulname as String
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
     * Get modul version.
     *
     * @return modulversion as String
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
        final Configuration other = (Configuration) obj;
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
        return "Configuration{" + "uuid=" + uuid
                + ", key=" + key
                + ", value=" + value
                + ", defaultValue=" + defaultValue
                + ", modulName=" + modulName
                + ", configurationSet=" + configurationSet
                + ", version=" + version
                + ", depecated=" + depecated
                + ", comment=" + comment + '}';
    }
}
