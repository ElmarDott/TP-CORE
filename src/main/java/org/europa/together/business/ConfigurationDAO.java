package org.europa.together.business;

import java.util.List;
import org.europa.together.domain.Configuration;
import org.springframework.stereotype.Component;

/**
 * The ConfigurationDAO provides all functionality for an application wide
 * configuration. An configuration entry is a simple key=value pair in context
 * of a module. The domain object supports features for versioning the
 * entries.<br>
 *
 * Domain Object: ID, CONF_KEY, CONF_VALUE, DEFAULT_VALUE, CONF_SET, MODUL_NAME,
 * MODUL_VERSION, DEPECATED, COMMENT<br>
 *
 * The keys are stored as SHA-256 hash, to protect the Database against direct
 * editing.
 */
@Component
public interface ConfigurationDAO extends GenericDAO<Configuration, Long> {

    /**
     * Get the whole configuration object by a given key, module and the version
     * of the module.
     *
     * @param key as String
     * @param module as String
     * @param version as String
     * @return configuration as Object
     */
    Configuration getConfigurationByKey(String key, String module, String version);

    /**
     * Return the value of a key from a module. The hashing of the key will be
     * done in this function, so the usage is more easy. The identifier for a
     * configuration entry is a combination of the key itself, the module-name
     * and the module-version were the DAO stored who use the module. e. g.:
     * E-Mail Configuration is implemented in core, since version 1.0
     *
     * In the case a entry exist but the value is empty, then the default value
     * will be use.
     *
     * @param key as String
     * @param module as String
     * @param version as String
     * @return value as String
     */
    String getValueByKey(String key, String module, String version);

    /**
     * Return a List with all configuration Objects of a ConfigurationSet. A
     * ConfigurationSet is a collection of all configuration Entries for a
     * special service like email inside a module.
     *
     * @param module as String
     * @param configSet as Sting
     * @return ConfigurationSet as List&gt;Configuration&lt;
     */
    List<Configuration> getAllConfigurationSetEntries(String module, String configSet);

    /**
     * Update existing configuration entries.
     *
     * @param configuration as List&gt;Configuration&lt;
     * @return true on success
     */
    boolean updateConfigurationEntries(List<Configuration> configuration);

    /**
     * Compare from a given collection all entries with the default values. The
     * result will be a list of Strings.<br>
     * &nbsp; &nbsp; [Key] value (default)
     *
     * @param configSet as List
     * @return Configurations as List
     */
    List<String> compareConfigurationSetWithDefault(List<Configuration> configSet);

    /**
     * Restore a single Entry to his default value.
     *
     * @param entry as Configuration
     * @return true on success
     */
    boolean restoreKeyToDefault(Configuration entry);

    /**
     * In the case that for a module exist more versions, for example after some
     * upgrades, this method supports a history function of the previous
     * configuration.
     *
     * @param module as String
     * @param key as String
     * @return Configuration as List
     */
    List<Configuration> getHistoryOfAEntry(String module, String key);

    /**
     * Export the entire configuration as JSON String.
     *
     * @return JSON as String
     */
    String exportEntireConfiguration();

    /**
     * Export a full module as JSON String.
     *
     * @param module as Sting
     * @return JSON as String
     */
    String exportConfigurationOfAModule(String module);

    /**
     * Export a ConfigurationSet like E-Mail as JSON String.
     *
     * @param module as Sting
     * @param configSet as Sting
     * @return JSON as String
     */
    String exportConfigurationSet(String module, String configSet);

    /**
     * Allows the import of configuations as JSON objects.
     *
     * @param configuration as Sting
     * @return true on success
     */
    boolean importConfiguration(String configuration);
}
