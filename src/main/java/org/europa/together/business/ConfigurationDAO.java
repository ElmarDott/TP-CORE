package org.europa.together.business;

import java.util.List;
import org.apiguardian.api.API;
import static org.apiguardian.api.API.Status.STABLE;
import org.europa.together.domain.ConfigurationDO;
import org.europa.together.exceptions.DAOException;
import org.springframework.stereotype.Component;

/**
 * The ConfigurationDAO provides all functionality for an application wide
 * configuration. An configuration entry is a simple key=value pair in context
 * of a module. The domain object supports features for versioning the
 * entries.<br>
 *
 * Domain Object: ID, CONF_KEY, CONF_VALUE, DEFAULT_VALUE, CONF_SET, MODUL_NAME,
 * MODUL_VERSION, DEPRECATED, COMMENT<br>
 *
 * The keys are stored as SHA-256 hash, to protect the database against direct
 * editing.
 *
 * @author elmar.dott@gmail.com
 * @version 1.2
 * @since 1.0
 */
@API(status = STABLE, since = "1.0", consumers = "ConfigurationHbmDAO")
@Component
public interface ConfigurationDAO extends GenericDAO<ConfigurationDO, String> {

    /**
     * Identifier for the given feature.
     */
    @API(status = STABLE, since = "1.2")
    String FEATURE_ID = "CM-05";

    /**
     * Get the whole configuration object by a given key, module and the version
     * of the module.
     *
     * @param key as String
     * @param module as String
     * @param version as String
     * @return configuration as Object
     */
    @API(status = STABLE, since = "1.0")
    ConfigurationDO getConfigurationByKey(String key, String module, String version);

    /**
     * Return a List with all configuration objects of a ConfigurationSet.A
     * ConfigurationSet is a collection of all configuration entries of one
     * version for a service like email inside a module.
     *
     * @param module as String
     * @param version as String
     * @param configSet as Sting
     * @return ConfigurationSet as List&lt;Configuration&gt;
     */
    @API(status = STABLE, since = "1.0")
    List<ConfigurationDO>
            getAllConfigurationSetEntries(String module, String version, String configSet);

    /**
     * Get all configuration entries for a module.
     *
     * @param module as String
     * @return ConfigurationSet as List&lt;Configuration&gt;
     */
    @API(status = STABLE, since = "1.0")
    List<ConfigurationDO> getAllModuleEntries(String module);

    /**
     * Return a List of all deprecated ConfigurationDO.
     *
     * @return deprecated as List&lt;Configuration&gt;
     */
    @API(status = STABLE, since = "1.0")
    List<ConfigurationDO> getAllDeprecatedEntries();

    /**
     * In the case that for a module exist more versions than one. For example
     * after some upgrades, this method supports a history function of the
     * previous configuration.
     *
     * @param module as String
     * @param key as String
     * @param configSet as String
     * @return history as List&lt;Configuration&gt;
     */
    @API(status = STABLE, since = "1.0")
    List<ConfigurationDO> getHistoryOfAEntry(String module, String key, String configSet);

    /**
     * Return the value of a key from a module. The hashing of the key will be
     * done in this function, so the usage is more easy. The identifier for a
     * configuration entry is a combination of the key itself, the module-name
     * and the module-version were the DAO stored who use the module. e. g.:
     * E-Mail Configuration is implemented in core, since version 1.0
     *
     * In the case a entry exist but the value is empty, then the default value
     * will used.
     *
     * @param key as String
     * @param module as String
     * @param version as String
     * @return value as String
     */
    @API(status = STABLE, since = "1.0")
    String getValueByKey(String key, String module, String version);

    /**
     * Restore a single entry to his default value.
     *
     * @param entry as Configuration
     * @throws org.europa.together.exceptions.DAOException
     */
    @API(status = STABLE, since = "1.0")
    void restoreKeyToDefault(ConfigurationDO entry)
            throws DAOException;

    /**
     * Update a List of existing configuration entries.
     *
     * @param configuration as List&lt;Configuration&gt;
     * @throws org.europa.together.exceptions.DAOException
     */
    @API(status = STABLE, since = "1.0")
    void updateConfigurationEntries(List<ConfigurationDO> configuration)
            throws DAOException;
}
