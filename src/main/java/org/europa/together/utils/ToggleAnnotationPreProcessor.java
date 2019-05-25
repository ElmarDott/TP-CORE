package org.europa.together.utils;

import org.europa.together.application.FF4jProcessor;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import org.europa.together.business.FeatureToggle;
import org.europa.together.domain.AnnotatedClass;
import org.ff4j.core.Feature;

/**
 * PreProcessor for the @FeatureToggle Annotation.
 *
 * This Class can not use the Logger because of the pre compiling mode
 */
@SupportedAnnotationTypes("org.europa.together.business.FeatureToggle")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@FeatureToggle(featureID = "CM-0013.H01")
public final class ToggleAnnotationPreProcessor extends AbstractProcessor {

    private FF4jProcessor ff4j;

    /**
     * Constructor.
     */
    public ToggleAnnotationPreProcessor() {
        ff4j = new FF4jProcessor();
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
                        ff4j.print("WARN: no @FeatureToggle annotations detected.");
                        break;
                    }
                    if (element == null) {
                        ff4j.print("WARN: no anotation element");
                        break;
                    }
                    if (FeatureToggle.class == null) {
                        ff4j.print("ERROR: annotation class not exist");
                        break;
                    }

                    String featureID = element.getAnnotation(FeatureToggle.class).featureID();
                    Feature feature = ff4j.scanToggels(featureID);
                    if (!feature.isEnable()) {
                        deactivatedFeatures.add(element);
                    }
                }
            }
            List<AnnotatedClass> collection = this.prepareCollection(deactivatedFeatures);

        } catch (Exception ex) {
            successs = false;
            ff4j.print("ERROR: Process annotation @FeatureToggle failed.");
            ff4j.print(ex.getMessage());
        }
        return successs;
    }

    private List<AnnotatedClass> prepareCollection(final List<Element> elements) {

        List<AnnotatedClass> collection = new ArrayList<>();
        if (elements.size() < 1) {
            ff4j.print("No decatvated features detected. Processor quit.");
        } else {

            int count = 1;
            for (Element element : elements) {

                String type = null;
                String packageName = null;
                String clazzName = null;
                String constructorName = null;
                String methodName = null;

                if (null != element.getKind()) {
                    switch (element.getKind()) {
                        case ENUM:
                            type = "ENUM";
                            packageName = element.getEnclosingElement().toString();
                            clazzName = element.getSimpleName().toString();
                            break;
                        case CLASS:
                            type = "CLASS";
                            packageName = element.getEnclosingElement().toString();
                            clazzName = element.getSimpleName().toString();
                            constructorName = clazzName;
                            break;
                        case CONSTRUCTOR:
                            type = "CONSTRUCTOR";
                            packageName = element.getEnclosingElement().getEnclosingElement()
                                    .toString();
                            clazzName = element.getEnclosingElement().getSimpleName().toString();
                            constructorName = clazzName;
                            break;
                        case METHOD:
                            type = "METHOD";
                            packageName = element.getEnclosingElement().getEnclosingElement()
                                    .toString();
                            clazzName = element.getEnclosingElement().getSimpleName().toString();
                            constructorName = clazzName;
                            methodName = element.getSimpleName().toString();
                            break;
                        default:
                            break;
                    }
                }

                AnnotatedClass entry = new AnnotatedClass(type, packageName,
                        clazzName, constructorName, methodName);
                collection.add(entry);
                ff4j.print("INFO : (" + count + ") " + entry.toString());
                count++;
            }
            ff4j.print("\nINFO : " + collection.size() + " elements added.");
        }
        return collection;
    }
}
