package org.europa.together.application;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.ParameterExpression;
import jakarta.persistence.criteria.Root;
import org.europa.together.business.ConfigurationDAO;
import org.europa.together.business.CryptoTools;
import org.europa.together.business.Logger;
import org.europa.together.domain.ConfigurationDO;
import org.europa.together.domain.HashAlgorithm;
import org.europa.together.domain.JpaPagination;
import org.europa.together.domain.LogLevel;
import org.europa.together.exceptions.DAOException;
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
    private CryptoTools cryptoTools;

    /**
     * Constructor.
     */
    public ConfigurationHbmDAO() {
        super();
        LOGGER.log("instance class", LogLevel.INFO);
    }

    @Override
    public void updateConfigurationEntries(final List<ConfigurationDO> configuration)
            throws DAOException {
        try {
            for (ConfigurationDO entry : configuration) {
                this.update(entry.getUuid(), entry);
            }
        } catch (DAOException ex) {
            LOGGER.catchException(ex);
            throw new DAOException("EntityNotFound: " + ex.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ConfigurationDO getConfigurationByKey(final String key,
            final String module, final String version) {
        ConfigurationDO entry;
        String hash = cryptoTools.calculateHash(key, HashAlgorithm.SHA256);
        CriteriaBuilder builder = mainEntityManagerFactory.getCriteriaBuilder();
        CriteriaQuery<ConfigurationDO> query = builder.createQuery(ConfigurationDO.class);
        // create Criteria
        Root<ConfigurationDO> root = query.from(ConfigurationDO.class);
        //Criteria SQL Parameters
        ParameterExpression<String> paramKey = builder.parameter(String.class);
        ParameterExpression<String> paramModulName = builder.parameter(String.class);
        ParameterExpression<String> paramVersion = builder.parameter(String.class);

        query.where(builder.equal(root.get("key"), paramKey),
                builder.equal(root.get("modulName"), paramModulName),
                builder.equal(root.get("version"), paramVersion));
        query.orderBy(builder.asc(root.get("uuid")));
        // wire queries tog parametersether with parameters
        TypedQuery<ConfigurationDO> result = mainEntityManagerFactory.createQuery(query);
        result.setParameter(paramKey, hash);
        result.setParameter(paramModulName, module);
        result.setParameter(paramVersion, version);

        entry = result.getSingleResult();
        LOGGER.log("getValueByKey() : " + entry.toString(), LogLevel.DEBUG);
        return entry;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ConfigurationDO> getAllConfigurationSetEntries(final String module,
            final String version, final String configSet) {
        List<ConfigurationDO> result = new ArrayList<>();
        Map<String, String> filter = new HashMap<>();
        filter.put("modulName", module);
        filter.put("version", version);
        filter.put("configurationSet", configSet);

        JpaPagination pagination = new JpaPagination("uuid");
        pagination.setFilterStringCriteria(filter);
        result.addAll(listAllElements(pagination));
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ConfigurationDO> getAllModuleEntries(final String module) {
        List<ConfigurationDO> result = new ArrayList<>();
        Map<String, String> filter = new HashMap<>();
        filter.put("modulName", module);

        JpaPagination pagination = new JpaPagination("uuid");
        pagination.setFilterStringCriteria(filter);
        pagination.setAdditionalOrdering("version");
        result.addAll(listAllElements(pagination));
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ConfigurationDO> getAllDeprecatedEntries() {
        List<ConfigurationDO> result = new ArrayList<>();
        Map<String, Boolean> filter = new HashMap<>();
        filter.put("deprecated", Boolean.TRUE);

        JpaPagination pagination = new JpaPagination("uuid");
        pagination.setFilterBooleanCriteria(filter);
        pagination.setAdditionalOrdering("version");
        result.addAll(listAllElements(pagination));
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ConfigurationDO> getHistoryOfAEntry(final String module,
            final String key, final String configSet) {
        List<ConfigurationDO> result = new ArrayList<>();
        String hash = cryptoTools.calculateHash(key, HashAlgorithm.SHA256);
        Map<String, String> filter = new HashMap<>();
        filter.put("key", hash);
        filter.put("modulName", module);
        filter.put("configurationSet", configSet);

        JpaPagination pagination = new JpaPagination("uuid");
        pagination.setFilterStringCriteria(filter);
        pagination.setAdditionalOrdering("version");
        result.addAll(listAllElements(pagination));
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public String getValueByKey(final String key, final String module, final String version) {
        String value = "";
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
    public void restoreKeyToDefault(final ConfigurationDO entry)
            throws DAOException {
        ConfigurationDO change = this.find(entry.getUuid());
        if (change != null) {
            change.setValue(change.getDefaultValue());
            this.update(change.getUuid(), change);
            LOGGER.log(change.getKey() + " reset to default.", LogLevel.DEBUG);
        } else {
            LOGGER.log("EntityNotFound " + entry.toString(), LogLevel.WARN);
            throw new DAOException("EntityNotFound: " + entry.toString());
        }
    }
}
