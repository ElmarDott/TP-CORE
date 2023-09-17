package org.europa.together.utils;

import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.FileObject;
import javax.tools.JavaFileObject;
import javax.tools.StandardLocation;
import org.europa.together.application.FF4jProcessor;
import org.europa.together.application.internal.AnnotatedClass;
import org.europa.together.application.internal.FF4jAnnotationPreProcessorHelper;
import org.europa.together.business.FeatureToggle;
import org.ff4j.core.Feature;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * PreProcessor for the @FeatureToggle Annotation.
 *
 * This Class can not use the Logger because of the pre compiling mode
 */
@SupportedAnnotationTypes("org.europa.together.business.FeatureToggle")
@SupportedSourceVersion(SourceVersion.RELEASE_11)
@FeatureToggle(featureID = "CM-0013.H01")
public class FF4jAnnotationPreProcessor extends AbstractProcessor {

    private FF4jProcessor ff4j;
    private Filer filer;
    @Autowired
    private FF4jAnnotationPreProcessorHelper annotationHelper;

    /**
     * Constructor.
     */
    public FF4jAnnotationPreProcessor() {

        String testConfigFile = "/src/test/resources"
                + "/org/europa/together/configuration/DeactivatedFeatureToggles.xml";

        ff4j = new FF4jProcessor();
//        ff4j.setConfigFile(testConfigFile);
    }

    @Override
    public void init(final ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        filer = processingEnvironment.getFiler();
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
                        FF4jAnnotationPreProcessorHelper
                                .print("WARN: no @FeatureToggle annotations detected.");
                        break;
                    }
                    if (element == null) {
                        FF4jAnnotationPreProcessorHelper
                                .print("WARN: no anotation element");
                        break;
                    }
                    if (FeatureToggle.class == null) {
                        FF4jAnnotationPreProcessorHelper
                                .print("ERROR: annotation class not exist");
                        break;
                    }

                    String featureID = element.getAnnotation(FeatureToggle.class).featureID();
                    Feature feature = ff4j.scanToggels(featureID);
                    if (!feature.isEnable()) {
                        deactivatedFeatures.add(element);
                    }
                }
            }

            if (!deactivatedFeatures.isEmpty()) {

                List<AnnotatedClass> collection
                        = annotationHelper.prepareCollection(deactivatedFeatures);
                List<AnnotatedClass> orderdElements = annotationHelper
                        .mergeAnnotatedElements(collection);

                FF4jAnnotationPreProcessorHelper
                        .print("DEBUG: Result > \n");

                for (AnnotatedClass elemet : orderdElements) {
                    FF4jAnnotationPreProcessorHelper.print("\t" + elemet.toString());
                    writeFiles(elemet);
                }
            }
        } catch (Exception ex) {
            successs = false;
            FF4jAnnotationPreProcessorHelper
                    .print("ERROR: Process annotation @FeatureToggle failed.");
            FF4jAnnotationPreProcessorHelper
                    .print("ERROR: " + ex.getClass().getSimpleName() + ex.getMessage());
        }
        return successs;
    }

    private void writeFiles(final AnnotatedClass element) {

        filer = processingEnv.getFiler();
        try {
            String clazzName = element.getPackageName() + "." + element.getClazzName();
            FileObject fileReader = filer.getResource(StandardLocation.SOURCE_PATH,
                    element.getPackageName(),
                    element.getClazzName() + ".java");

            JavaFileObject fileBuilder = filer.createSourceFile(clazzName);
            Writer writer = fileBuilder.openWriter();
            writer.append("/*Auto Generated by @FeatureToggle */");
            writer.close();

//            AnnotationProcessingHelper
//                    .print(processFiles(fileReader, element));
        } catch (Exception ex) {
            FF4jAnnotationPreProcessorHelper
                    .print("ERROR: " + ex.getClass().getSimpleName() + ex.getMessage());
        }
    }

    private String processFiles(final FileObject original, final AnnotatedClass element) {

        String content = null;
        try {
            content = original.getCharContent(true).toString();
            if (element.getType().equals(AnnotatedClass.CLASS)) {

                content = content.replaceAll("\\^import*", " ");
            }

        } catch (Exception ex) {
            FF4jAnnotationPreProcessorHelper
                    .print("ERROR: " + ex.getClass().getSimpleName() + ex.getMessage());
        }
        return content;
    }
}
