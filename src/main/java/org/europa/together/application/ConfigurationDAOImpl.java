package org.europa.together.application;

import java.util.List;
import org.europa.together.business.ConfigurationDAO;
import org.europa.together.business.Logger;
import org.europa.together.domain.ConfigurationDO;
import org.europa.together.domain.HashAlgorithm;
import org.europa.together.domain.LogLevel;
import org.europa.together.utils.StringUtils;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of the ConfigurationDAO.
 */
@Repository
public class ConfigurationDAOImpl extends GenericDAOImpl<ConfigurationDO, String>
        implements ConfigurationDAO {

    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = new LoggerImpl(ConfigurationDAO.class);

    /**
     * Constructor.
     */
    public ConfigurationDAOImpl() {
        super();
        LOGGER.log("instance class", LogLevel.INFO);
    }

    @Override
    @Transactional(readOnly = true)
    public ConfigurationDO getConfigurationByKey(final String key,
            final String module, final String version) {

        ConfigurationDO entry = null;
        String hash = StringUtils.calculateHash(key, HashAlgorithm.SHA256);
        Session session = mainEntityManagerFactory.unwrap(Session.class);
        entry = (ConfigurationDO) session.createCriteria(ConfigurationDO.class)
                .add(Restrictions.eq("key", hash))
                .add(Restrictions.eq("modulName", module))
                .add(Restrictions.eq("version", version))
                .uniqueResult();

        if (entry != null) {
            LOGGER.log("getValueByKey() : " + entry.toString(), LogLevel.DEBUG);
        } else {
            LOGGER.log("ConfigurationByKey() : key: " + key + " for "
                    + module + " not found.", LogLevel.WARN);
        }
        return entry;
    }

    @Override
    @Transactional(readOnly = true)
    public String getValueByKey(final String key, final String module, final String version) {

        String value = null;
        LOGGER.log("Module: " + module + " :: Version: " + version + " :: Key: " + key,
                LogLevel.DEBUG);
        ConfigurationDO entry = getConfigurationByKey(key, module, version);

        if (entry != null) {
            value = entry.getValue();

            if (StringUtils.isEmpty(value)) {
                value = entry.getDefaultValue();
                LOGGER.log("getValueByKey() returns the defaultValue " + value, LogLevel.DEBUG);
            }
        }
        return value;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ConfigurationDO> getAllConfigurationSetEntries(final String module,
            final String configSet) {
        //TODO: getAllConfigurationSetEntries() inplement me.
        throw new UnsupportedOperationException("TODO: Not supported yet.");
    }

    @Override
    public boolean updateConfigurationEntries(final List<ConfigurationDO> configuration) {
        //TODO: updateConfigurationEntries() inplement me.
        throw new UnsupportedOperationException("TODO: Not supported yet.");
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> compareConfigurationSetWithDefault(final List<ConfigurationDO> configSet) {
        //TODO: compareConfigurationSetWithDefault() inplement me.
        throw new UnsupportedOperationException("TODO: Not supported yet.");
    }

    @Override
    public boolean restoreKeyToDefault(final ConfigurationDO entry) {
        //TODO: restoreKeyToDefault() inplement me.
        throw new UnsupportedOperationException("TODO: Not supported yet.");
    }

    @Override
    @Transactional(readOnly = true)
    public List<ConfigurationDO> getHistoryOfAEntry(final String module, final String key) {
        //TODO: getHistoryOfAEntry() inplement me.
        throw new UnsupportedOperationException("TODO: Not supported yet.");
    }

    @Override
    @Transactional(readOnly = true)
    public String exportEntireConfiguration() {
        //TODO: exportEntireConfiguration() inplement me.
        throw new UnsupportedOperationException("TODO: Not supported yet.");
    }

    @Override
    @Transactional(readOnly = true)
    public String exportConfigurationOfAModule(final String module) {
        //TODO: exportConfigurationOfAModule() inplement me.
        throw new UnsupportedOperationException("TODO: Not supported yet.");
    }

    @Override
    @Transactional(readOnly = true)
    public String exportConfigurationSet(final String module, final String configSet) {
        //TODO: exportConfigurationSet() inplement me.
        throw new UnsupportedOperationException("TODO: Not supported yet.");
    }

    @Override
    public boolean importConfiguration(final String configuration) {
        //TODO: importConfiguration() inplement me.
        throw new UnsupportedOperationException("TODO: Not supported yet.");
    }

}
