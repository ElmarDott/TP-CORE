package org.europa.together.application;

import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.europa.together.business.ConfigurationDAO;
import org.europa.together.business.CryptoTools;
import org.europa.together.business.Logger;
import org.europa.together.domain.ConfigurationDO;
import org.europa.together.domain.HashAlgorithm;
import org.europa.together.domain.LogLevel;
import org.europa.together.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of the ConfigurationDAO.
 */
@Repository
@Transactional
public class ConfigurationHbmDAO extends GenericHbmDAO<ConfigurationDO, String>
        implements ConfigurationDAO {

    private static final long serialVersionUID = 5L;
    private static final Logger LOGGER = new LogbackLogger(ConfigurationHbmDAO.class);

    @Autowired
    private transient CryptoTools cryptoTools;

    /**
     * Constructor.
     */
    public ConfigurationHbmDAO() {
        super();
        LOGGER.log("instance class", LogLevel.INFO);
    }

    @Override
    public void updateConfigurationEntries(final List<ConfigurationDO> configuration) {
        for (ConfigurationDO entry : configuration) {
            this.update(entry.getUuid(), entry);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ConfigurationDO getConfigurationByKey(final String key,
            final String module, final String version) {

        String hash = cryptoTools.calculateHash(key, HashAlgorithm.SHA256);
        CriteriaBuilder builder = mainEntityManagerFactory.getCriteriaBuilder();
        CriteriaQuery<ConfigurationDO> query = builder.createQuery(ConfigurationDO.class);
        // create Criteria
        Root<ConfigurationDO> root = query.from(ConfigurationDO.class);
        query.where(builder.equal(root.get("key"), hash),
                builder.equal(root.get("modulName"), module),
                builder.equal(root.get("version"), version));

        ConfigurationDO entry = mainEntityManagerFactory.createQuery(query).getSingleResult();
        LOGGER.log("getValueByKey() : " + entry.toString(), LogLevel.DEBUG);
        return entry;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ConfigurationDO> getAllConfigurationSetEntries(final String module,
            final String version, final String configSet) {
        CriteriaBuilder builder = mainEntityManagerFactory.getCriteriaBuilder();
        CriteriaQuery<ConfigurationDO> query = builder.createQuery(ConfigurationDO.class);
        // create Criteria
        Root<ConfigurationDO> root = query.from(ConfigurationDO.class);
        query.where(builder.equal(root.get("modulName"), module),
                builder.equal(root.get("version"), version),
                builder.equal(root.get("configurationSet"), configSet));

        return mainEntityManagerFactory.createQuery(query).getResultList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ConfigurationDO> getAllModuleEntries(final String module) {
        CriteriaBuilder builder = mainEntityManagerFactory.getCriteriaBuilder();
        CriteriaQuery<ConfigurationDO> query = builder.createQuery(ConfigurationDO.class);
        // create Criteria
        Root<ConfigurationDO> root = query.from(ConfigurationDO.class);
        query.where(builder.equal(root.get("modulName"), module));
        query.orderBy(builder.asc(root.get("version")));

        return mainEntityManagerFactory.createQuery(query).getResultList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ConfigurationDO> getAllDepecatedEntries() {
        CriteriaBuilder builder = mainEntityManagerFactory.getCriteriaBuilder();
        CriteriaQuery<ConfigurationDO> query = builder.createQuery(ConfigurationDO.class);
        // create Criteria
        Root<ConfigurationDO> root = query.from(ConfigurationDO.class);
        query.where(builder.equal(root.get("depecated"), Boolean.TRUE));
        query.orderBy(builder.asc(root.get("version")));

        return mainEntityManagerFactory.createQuery(query).getResultList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ConfigurationDO> getHistoryOfAEntry(final String module,
            final String key, final String configSet) {
        String hash = cryptoTools.calculateHash(key, HashAlgorithm.SHA256);
        CriteriaBuilder builder = mainEntityManagerFactory.getCriteriaBuilder();
        CriteriaQuery<ConfigurationDO> query = builder.createQuery(ConfigurationDO.class);
        // create Criteria
        Root<ConfigurationDO> root = query.from(ConfigurationDO.class);
        query.where(builder.equal(root.get("key"), hash),
                builder.equal(root.get("modulName"), module),
                builder.equal(root.get("configurationSet"), configSet));
        query.orderBy(builder.asc(root.get("version")));

        return mainEntityManagerFactory.createQuery(query).getResultList();
    }

    @Override
    @Transactional(readOnly = true)
    public String getValueByKey(final String key, final String module, final String version) {

        String value = null;
        LOGGER.log("Module: " + module + " :: Version: " + version + " :: Key: " + key,
                LogLevel.DEBUG);
        ConfigurationDO entry = getConfigurationByKey(key, module, version);
        value = entry.getValue();

        if (StringUtils.isEmpty(value)) {
            value = entry.getDefaultValue();
            LOGGER.log("getValueByKey() returns the defaultValue " + value, LogLevel.DEBUG);
        }
        return value;
    }

    @Override
    public void restoreKeyToDefault(final ConfigurationDO entry) {
        ConfigurationDO change = this.find(entry.getUuid());
        change.setValue(change.getDefaultValue());
        this.update(change.getUuid(), change);
        LOGGER.log(change.getKey() + " reset to default.", LogLevel.DEBUG);
    }
}
