package org.europa.together.utils;

import com.google.auto.service.AutoService;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import org.apiguardian.api.API;
import static org.apiguardian.api.API.Status.INTERNAL;
import org.europa.together.business.FeatureToggle;
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
@AutoService(Processor.class)
public final class TogglePreProcessor extends AbstractProcessor {

    private FF4j toggles;

    /**
     * Constructor.
     */
    public TogglePreProcessor() {

        try {
            String configuration = this.loadToggleConfiguration();

            //test mode
//            configuration = Paths.get("").toAbsolutePath().toString()
//                    + "/src/test/resources/org/europa/together/configuration/DeactivatedFeatureToggles.xml";
            print("INFO: FeatureToggle configuration: " + configuration);
            InputStream source = new FileInputStream(configuration);
            toggles = new FF4j(source);
            print("INFO: FeatureToggle Annotation Processor inctanciated.");

        } catch (Exception ex) {
            print("ERROR: Instanciation of Feature Toggle Processor failed.");
            print(ex.getMessage());
            ex.printStackTrace();
        }
    }

    /**
     * Toggel to deactivate the test case execution.
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
            print(ex.getMessage());
        }
        return activate;
    }

    /**
     * Extends the internal TP-CORE configuration by another configuration file.
     *
     * @param configurationFile as String
     */
    @API(status = INTERNAL, since = "1.2")
    public void loadConfigurationFile(final String configurationFile) {

        try {
            XmlConfig config = toggles.parseXmlConfig(configurationFile);
            toggles.getFeatureStore().importFeatures(config.getFeatures().values());
            toggles.getPropertiesStore().importProperties(config.getProperties().values());
        } catch (Exception ex) {
            print(ex.getMessage());
        }
    }

    @Override
    public boolean process(final Set<? extends TypeElement> annotations,
            final RoundEnvironment roundEnv) {

        boolean successs = true;
        List<Element> deactivatedFeatures = new ArrayList<>();
        try {

            for (TypeElement annotation : annotations) {
                for (Element element : roundEnv.getElementsAnnotatedWith(annotation)) {

                    if (annotation == null) {
                        print("WARN: no @FeatureToggle annotations detected.");
                        break;
                    }
                    if (element == null) {
                        print("WARN: no anotation element");
                        break;
                    }
                    if (FeatureToggle.class == null) {
                        print("ERROR: annotation class not exist");
                        break;
                    }

                    String featureID = element.getAnnotation(FeatureToggle.class).featureID();
                    Feature feature = toggle(featureID);
                    if (!feature.isEnable()) {
                        deactivatedFeatures.add(element);
                    }
                }
            }

            this.replacement(deactivatedFeatures);

        } catch (Exception ex) {
            successs = false;
            print("ERROR: Process annotation @FeatureToggle failed.");
            print(ex.getMessage());
            ex.printStackTrace();
        }
        return successs;
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
        try {
            feature = toggles.getFeature(featureId);
            print("Feature: " + feature.getDescription()
                    + " (" + feature.getUid() + ") " + feature.isEnable());
        } catch (Exception ex) {
            print(ex.getMessage());
        }
        return feature;
    }

    private void print(final String message) {
        System.out.println(message);
    }

    private String loadToggleConfiguration() {

        String configuration = null;

        String path = Paths.get("").toAbsolutePath().toString();
        String execution = "/target/classes";
        String configurationFile
                = "/org/europa/together/configuration/FeatureToggles.xml";

        //guess where the toggle config is located
        File check = new File(configurationFile);
        if (check.exists()) {
            configuration = configurationFile;
            print("INFO: FeatureToggle configuration [FILE] detected.");

        } else {

            configuration = path + configurationFile;
            if (new File(configuration).exists()) {
                print("INFO: FeatureToggle configuration [PATH + FILE] detected.");

            } else {

                configuration = path + execution + configurationFile;
                if (new File(configuration).exists()) {
                    print("INFO: FeatureToggle configuration [PATH + BUILD_DIR + FILE] detected."
                            + configuration);

                } else {
                    print("ERROR: " + configuration);
                    print("ERROR: FeatureToggle configuration file not found.");
                }
            }
        }
        return configuration;
    }

    private void replacement(final List<Element> elements) {

        if (elements.size() < 1) {
            print("No decatvated features detected. Processor quit.");
        } else {

            for (Element element : elements) {

                Element constructorElement = element.getEnclosingElement();
                Element packageElement = constructorElement.getEnclosingElement();

                print("INFO : deactivated element is " + element.getKind().name());
                String clazzName = null;
                String packageName = null;
                List<String> methodName = new ArrayList<>();

                if (packageElement.getKind() == ElementKind.PACKAGE) {
                    packageName = packageElement.toString();
                }

                if (element.getKind() == ElementKind.CONSTRUCTOR) {
                    clazzName = element.toString().replace("()", "");
                }
                if (element.getKind() == ElementKind.METHOD) {
                    clazzName = constructorElement.getSimpleName().toString();
                    methodName.add(element.getSimpleName().toString());
                }

                print("INFO : \t Package: " + packageName);
                print("INFO : \t Clazz: " + clazzName);
                print("INFO : \t Method: " + methodName.toString());
            }

        }
    }
}
