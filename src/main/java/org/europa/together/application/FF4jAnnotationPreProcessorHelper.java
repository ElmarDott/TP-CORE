package org.europa.together.application;

import java.util.ArrayList;
import java.util.List;
import javax.lang.model.element.Element;
import org.europa.together.business.FeatureToggle;
import org.europa.together.domain.AnnotatedClass;
import org.europa.together.utils.StringUtils;

/**
 * During the processing of annotations some simple functions are needed, wich
 * implemented for reuse in this class.
 *
 * Processing Annotations have achive several challenges. As first, everything
 * is related to annotation processing need to be compiled before the code
 * manipulation is started. For that reason, tools like loggers or template
 * engine are just present, when this library is used as annotation processor,
 * not for it's functionality inside. This circumstances are reasons why we have
 * a explicit print method instead of the Logger.
 */
@FeatureToggle(featureID = "CM-0013.H01")
public class FF4jAnnotationPreProcessorHelper {

    private List<AnnotatedClass> annotations = new ArrayList<>();

    /**
     * Collect annotation Elements as List from Annotation Processor and
     * transform them into AnnotatedClass.
     *
     * @param elements as List
     * @return AnnotatedClass as List
     */
    public List<AnnotatedClass> prepareCollection(final List<Element> elements) {

        List<AnnotatedClass> collection = new ArrayList<>();
        for (Element element : elements) {
            String type = null;
            String packageName = null;
            String clazzName = null;
            List<String> methodNames = new ArrayList<>();

            if (element.getKind() != null) {
                switch (element.getKind()) {
                    case ENUM:
                        type = AnnotatedClass.ENUM;
                        packageName = element.getEnclosingElement().toString();
                        clazzName = element.getSimpleName().toString();
                        break;
                    case CLASS:
                        type = AnnotatedClass.CLASS;
                        packageName = element.getEnclosingElement().toString();
                        clazzName = element.getSimpleName().toString();
                        break;
                    case CONSTRUCTOR:
                        type = AnnotatedClass.CONSTRUCTOR;
                        packageName = element.getEnclosingElement().getEnclosingElement()
                                .toString();
                        clazzName = element.getEnclosingElement().getSimpleName().toString();
                        break;
                    case METHOD:
                        type = AnnotatedClass.METHOD;
                        packageName = element.getEnclosingElement().getEnclosingElement()
                                .toString();
                        clazzName = element.getEnclosingElement().getSimpleName().toString();
                        methodNames.add(element.getSimpleName().toString());
                        break;

                    default:
                        break;
                }
            }

            AnnotatedClass entry = new AnnotatedClass(type, packageName,
                    clazzName, methodNames);

            collection.add(entry);
        }
        return collection;
    }

    /**
     * Merge the different annotations together, to allow the code generating
     * for deactivated classes, methods & etc.
     *
     * @param collection as List
     * @return annotatedElements as List
     */
    public List<AnnotatedClass> mergeAnnotatedElements(final List<AnnotatedClass> collection) {

        if (!collection.isEmpty()) {
            //collect Clazz & Enum
            List<AnnotatedClass> initial
                    = this.processClazzTypes(collection);

            if (!initial.isEmpty()) {
                //update Clazz with Constructors & Methods
                List<AnnotatedClass> firstRound
                        = this.updateClazzByMethods(initial);

                if (!firstRound.isEmpty()) {
                    //merge Methods & Constructors together
                    List<AnnotatedClass> secondRound
                            = this.mergeMethodsAndConstructors(firstRound);

                    if (!secondRound.isEmpty()) {
                        // add merge methods
                        annotations.addAll(
                                this.mergeDuplications(secondRound));
                    }
                }
            }
        }
        return this.annotations;
    }

    /**
     * Write logging information on the command line.
     *
     * @param message as String
     */
    public static void print(final String message) {
        System.out.println(message);
    }

    /**
     * Process the content of a java class file and replace implementations by
     * RegEx.
     *
     * @param source as String
     * @param regex as String
     *
     * @return processedText as String
     */
    public static String textProcessor(final String source, final String regex) {
        String input = StringUtils.shrink(source);

        String imports = "import .*?;";
        input = input.replaceAll(imports, " ");

        return input.replaceAll(regex, " ");
    }

    //##########################################################################
    private List<AnnotatedClass> mergeDuplications(final List<AnnotatedClass> collection) {
        List<AnnotatedClass> result = new ArrayList<>();
        //copy elements
        List<AnnotatedClass> orgin = new ArrayList<>();
        List<AnnotatedClass> temp = new ArrayList<>();
        for (AnnotatedClass item : collection) {
            orgin.add(item);
            temp.add(item);
        }

        for (AnnotatedClass element : orgin) {

            temp.remove(element);
            List<AnnotatedClass> skip = new ArrayList<>();
            for (AnnotatedClass item : temp) {
                if (element.equals(item)) {
                    element.addMethodName(item.getMethodNames());
                    skip.add(item);
                }
            }
            if (!skip.isEmpty()) {
                for (AnnotatedClass del : skip) {
                    temp.remove(del);
                }
            }
            result.add(element);
            if (temp.isEmpty()) {
                break;
            }
        }
        return result;
    }

    private List<AnnotatedClass> mergeMethodsAndConstructors(
            final List<AnnotatedClass> collection) {

        List<AnnotatedClass> temp = new ArrayList<>();

        //split constructors & methods
        List<AnnotatedClass> constructors = new ArrayList<>();
        List<AnnotatedClass> methods = new ArrayList<>();
        for (AnnotatedClass item : collection) {
            if (AnnotatedClass.CONSTRUCTOR.equals(item.getType())) {
                constructors.add(item);
            } else {
                methods.add(item);
            }
        }

        if (!constructors.isEmpty() && methods.isEmpty()) {
            temp.addAll(constructors);
        } else if (!methods.isEmpty() && constructors.isEmpty()) {
            temp.addAll(methods);
        } else {
            //MERGE
            for (AnnotatedClass element : constructors) {
                List<AnnotatedClass> skip = new ArrayList<>();
                if (!methods.isEmpty()) {
                    for (AnnotatedClass item : methods) {
                        if (element.equals(item)) {
                            element.addMethodName(item.getMethodNames());
                            skip.add(item);
                        }
                    }
                    if (!skip.isEmpty()) {
                        element.addMethodName(element.getClazzName());
                        for (AnnotatedClass del : skip) {
                            methods.remove(del);
                        }
                    }
                }
                annotations.add(element);
            }

            if (!methods.isEmpty()) {
                temp.addAll(methods);
            }
        }

        return temp;
    }

    private List<AnnotatedClass> updateClazzByMethods(final List<AnnotatedClass> collection) {
        List<AnnotatedClass> temp = new ArrayList<>();
        for (AnnotatedClass element : collection) {

            boolean skip = false;
            for (AnnotatedClass item : this.annotations) {
                if (item.equals(element)) {
                    if (AnnotatedClass.CONSTRUCTOR.equals(element.getType())) {
                        item.addMethodName(element.getClazzName());
                    } else {
                        item.addMethodName(element.getMethodNames());
                    }
                    skip = true;
                }
            }

            if (!skip) {
                temp.add(element);
            }
        }
        return temp;
    }

    private List<AnnotatedClass> processClazzTypes(final List<AnnotatedClass> collection) {
        List<AnnotatedClass> temp = new ArrayList<>();
        for (AnnotatedClass element : collection) {
            if (element.getType().equals(AnnotatedClass.ENUM)
                    || element.getType().equals(AnnotatedClass.CLASS)) {
                this.annotations.add(element);
            } else {
                temp.add(element);
            }
        }
        return temp;
    }

}
