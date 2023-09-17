package org.europa.together.domain;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import org.europa.together.utils.StringUtils;

@Entity
@Table(name = "PAGINATION_TEST")
public class PaginationTestDO implements Serializable {

    private static final long serialVersionUID = 9999L;

    /**
     * The name of the used database table for this domain object.
     */
    public static final String TABLE_NAME = "PAGINATION_TEST";

    @Id //validate uuid
    @Column(name = "IDX")
    private String uuid;

    @Column(name = "CONF_KEY")
    private String key;

    @Column(name = "CONF_VALUE")
    private String value;

    @Column(name = "DEFAULT_VALUE")
    private String defaultValue;

    @Column(name = "MODUL_NAME")
    private String modulName;

    @Column(name = "SERVICE_VERSION")
    private String version;

    @Column(name = "CONF_SET")
    private String configurationSet;

    @Column(name = "DEPRECATED")
    private boolean deprecated;

    @Column(name = "MANDATORY")
    private boolean mandatory;

    /**
     * Constructor.
     */
    public PaginationTestDO() {
        this.uuid = StringUtils.generateUUID();
    }

    //<editor-fold defaultstate="collapsed" desc="Getter / Setter">
    public boolean isDeprecated() {
        return deprecated;
    }

    public boolean isMandatory() {
        return mandatory;
    }

    public void setConfigurationSet(final String configurationSet) {
        this.configurationSet = configurationSet;
    }

    public void setDefaultValue(final String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public void setDeprecated(final boolean deprecated) {
        this.deprecated = deprecated;
    }

    public void setMandatory(final boolean mandatory) {
        this.mandatory = mandatory;
    }

    public void setKey(final String key) {
        this.key = key;
    }

    public void setModulName(final String modulName) {
        this.modulName = modulName;
    }

    public void setUuid(final String uuid) {
        this.uuid = uuid;
    }

    public void setValue(final String value) {
        this.value = value;
    }

    public void setVersion(final String version) {
        this.version = version;
    }

    public String getConfigurationSet() {
        return configurationSet;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public String getKey() {
        return key;
    }

    public String getModulName() {
        return modulName;
    }

    public String getUuid() {
        return uuid;
    }

    public String getValue() {
        return value;
    }

    public String getVersion() {
        return version;
    }
    //</editor-fold>

}
