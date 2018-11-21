package org.europa.together.utils;

import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.Element;
import org.apiguardian.api.API;
import static org.apiguardian.api.API.Status.INTERNAL;
import org.europa.together.application.LoggerImpl;
import org.europa.together.business.Logger;
import org.europa.together.domain.LogLevel;
import org.ff4j.FF4j;
import org.ff4j.conf.XmlConfig;
import org.ff4j.core.Feature;
import org.ff4j.exception.FeatureAccessException;
import org.ff4j.exception.FeatureNotFoundException;

/**
 * PreProcessor for the @FeatureToggle Annotation.
 */
@SupportedAnnotationTypes("org.europa.together.business.FeatureToggle")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class TogglePreProcessor extends AbstractProcessor {

    private static final Logger LOGGER = new LoggerImpl(TogglePreProcessor.class);
    private FF4j toggles;
    private final String configurationFile
            = "org/europa/together/configuration/FeatureToggles.xml";

    /**
     * Constructor.
     */
    public TogglePreProcessor() {
        toggles = new FF4j(configurationFile);
        LOGGER.log("instance class", LogLevel.INFO);
    }

    @Override
    public boolean process(final Set<? extends TypeElement> annotations,
            final RoundEnvironment roundEnv) {

        for (TypeElement annotation : annotations) {
            for (Element element : roundEnv.getElementsAnnotatedWith(annotation)) {

                try {
                    String featureID = "CM-0000";
//annotation.getAnnotation(FeatureToggle.class).featureID();
                    Feature feature = toggle(featureID);
                    String message = "@FeatureToggle(" + feature.getUid() + ") at: "
                            + element.toString();
                    //
                    // TODO: @FeatureToggle: Code generation
                    //
                    LOGGER.log(message, LogLevel.INFO);

                } catch (Exception ex) {
                    LOGGER.catchException(ex);
                }
            }
        }
        return true;
    }

    /**
     * Extends the internal TP-CORE configuration by another configuration file.
     *
     * @param configurationFile as String
     */
    @API(status = INTERNAL, since = "1.2")
    public void loadConfigurationFile(final String configurationFile) {

        XmlConfig config = toggles.parseXmlConfig(configurationFile);
        toggles.getFeatureStore().importFeatures(config.getFeatures().values());
        toggles.getPropertiesStore().importProperties(config.getProperties().values());

        LOGGER.log("Config File overwritten by: " + configurationFile, LogLevel.DEBUG);
    }

    /**
     * Toggel to deactivate test case execution.
     *
     * @param featureId as string
     * @return true on success
     */
    @API(status = INTERNAL, since = "1.2")
    public boolean testCaseActivator(final String featureId) {
        boolean activate = false;
        try {
            activate = toggle(featureId).isEnable();

        } catch (Exception ex) {
            LOGGER.catchException(ex);
        }
        return activate;
    }

    /**
     * The feature toggle (Feature Flag) to throw a exception in the case a
     * feature not exist otherwise it returns the feature object.
     *
     * @param featureId as String
     * @return Feature object
     * @throws FeatureAccessException if the Feature is deactivated
     * @throws FeatureNotFoundException if the feature not exist
     */
    @API(status = INTERNAL, since = "1.2")
    public Feature toggle(final String featureId)
            throws FeatureAccessException, FeatureNotFoundException {

        Feature feature = null;
        if (!toggles.exist(featureId)) {
            throw new FeatureNotFoundException(featureId + " dosn't exist.");
        }

        feature = toggles.getFeature(featureId);
        LOGGER.log("Feature: " + feature.getDescription()
                + " (" + feature.getUid() + ") " + feature.isEnable(), LogLevel.DEBUG);
        return feature;
    }
}
