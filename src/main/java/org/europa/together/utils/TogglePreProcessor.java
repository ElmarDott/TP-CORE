package org.europa.together.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.nio.file.Paths;
import java.util.Set;
import javax.annotation.processing.*;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.JavaFileObject;
import org.apiguardian.api.API;
import static org.apiguardian.api.API.Status.INTERNAL;
import org.europa.together.business.FeatureToggle;
import org.ff4j.FF4j;
import org.ff4j.conf.XmlConfig;
import org.ff4j.core.Feature;
import org.ff4j.exception.FeatureAccessException;
import org.ff4j.exception.FeatureNotFoundException;
import sun.reflect.generics.scope.Scope;

/**
 * PreProcessor for the @FeatureToggle Annotation.
 */
@SupportedAnnotationTypes("org.europa.together.business.FeatureToggle")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class TogglePreProcessor extends AbstractProcessor {

    private FF4j toggles;

    /**
     * Constructor.
     */
    public TogglePreProcessor() {

        String path = Paths.get("").toAbsolutePath().toString();
        String execution = "/target/classes";
        String configurationFile
                = "/org/europa/together/configuration/FeatureToggles.xml";

        String configuration;
        try {

            //guess where the toggle config is located
            File check = new File(configurationFile);
            if (check.exists()) {
                configuration = configurationFile;
                print("INFO: FeatureToggle configuration file detected.\n\t" + configuration);

            } else {

                configuration = path + configurationFile;
                if (new File(configuration).exists()) {
                    print("INFO: FeatureToggle configuration file detected.\n\t" + configuration);

                } else {

                    configuration = path + execution + configurationFile;
                    if (new File(configuration).exists()) {
                        print("INFO: FeatureToggle configuration file detected.\n\t"
                                + configuration);

                    } else {
                        print("ERROR: " + configuration);
                        print("ERROR: FeatureToggle configuration file not found.");
                    }
                }
            }

            InputStream source = new FileInputStream(configuration);
            toggles = new FF4j(source);
            print("INFO: FeatureToggle Annotation Processor inctanciated.");

        } catch (Exception ex) {
            print("ERROR: Instanciation of Feature Toggle Processor failed.");
            print(ex.getMessage());
            ex.printStackTrace();
        }
    }

    @Override
    public boolean process(final Set<? extends TypeElement> annotations,
            final RoundEnvironment roundEnv) {

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

                    //
                    // TODO: @FeatureToggle: Code generation
                    //
                    print("@FeatureToggle(" + feature.getUid() + ") at: " + element.toString());
                }
            }

        } catch (Exception ex) {
            print("ERROR: Process annotation @FeatureToggle failed.");
            print(ex.getMessage());
            ex.printStackTrace();
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

        try {
            XmlConfig config = toggles.parseXmlConfig(configurationFile);
            toggles.getFeatureStore().importFeatures(config.getFeatures().values());
            toggles.getPropertiesStore().importProperties(config.getProperties().values());
        } catch (Exception ex) {
            print(ex.getMessage());
        }
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
            print(ex.getMessage());
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

    private void classWriter(Element element, Scope scope) throws IOException {
        Filer filer = processingEnv.getFiler();
        JavaFileObject fileObject
                = filer.createSourceFile(scope.getTargetClassNameWithPackage(), element);
        try (Writer writer = fileObject.openWriter()) {
            template.execute(writer, scope);
        }
    }

    private String replacement(String featureId) {
        StringBuilder builder = new StringBuilder();
        builder.append("throw new FeatureNotFoundException(featureId + \" dosn't exist.\");");

        return builder.toString();
    }

}
