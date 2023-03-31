package org.europa.together.domain;

import java.io.Serializable;
import java.util.Date;
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

    @Column(name = "INT_VALUE")
    private Integer int_number;

    @Column(name = "FLOAT_VALUE")
    private Float float_number;

    @Column(name = "DATE_VALUE")
    private Date currentDate;

    @Column(name = "CONF_KEY")
    private String key;

    @Column(name = "CONF_VALUE")
    private String value;

    @Column(name = "MODUL_NAME")
    private String modulName;

    @Column(name = "CONF_SET")
    private String configurationSet;

    @Column(name = "DEPRECATED")
    private boolean deprecated;

    /**
     * Constructor.
     */
    public PaginationTestDO() {
        this.uuid = StringUtils.generateUUID();
    }

    //<editor-fold defaultstate="collapsed" desc="Getter / Setter">
    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Integer getInt_number() {
        return int_number;
    }

    public void setInt_number(Integer int_number) {
        this.int_number = int_number;
    }

    public Float getFloat_number() {
        return float_number;
    }

    public void setFloat_number(Float float_number) {
        this.float_number = float_number;
    }

    public Date getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(Date currentDate) {
        this.currentDate = currentDate;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getModulName() {
        return modulName;
    }

    public void setModulName(String modulName) {
        this.modulName = modulName;
    }

    public String getConfigurationSet() {
        return configurationSet;
    }

    public void setConfigurationSet(String configurationSet) {
        this.configurationSet = configurationSet;
    }

    public boolean isDeprecated() {
        return deprecated;
    }

    public void setDeprecated(boolean deprecated) {
        this.deprecated = deprecated;
    }
    //</editor-fold>
}
