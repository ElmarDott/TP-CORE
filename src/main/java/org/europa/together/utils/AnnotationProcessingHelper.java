package org.europa.together.utils;

import java.util.ArrayList;
import java.util.List;
import javax.lang.model.element.Element;
import org.europa.together.domain.AnnotatedClass;

/**
 * During the processing of annotations some simple functions are needed, wich
 * implemented for reuse in this class.
 *
 * Processing Annotations have achive several challenges. As first, everythin is
 * releated to annotation processing need to be compiled befor the code
 * manipulation is started. For that reason, tools like logges or template
 * engine are just present, when this library is used as annotation processor,
 * not for it's functionality inside. This circumstances are reasons why we have
 * a explicit print method instead of the Logger.
 */
public class AnnotationProcessingHelper {

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
    public List<AnnotatedClass> mergedAnnotatedElements(final List<AnnotatedClass> collection) {

        if (!collection.isEmpty()) {
            //collect Clazz & Enum
            List<AnnotatedClass> firstRound
                    = this.processClazzTypes(collection);

            if (!firstRound.isEmpty()) {
                //update Clazz with Constructors & Methods
                List<AnnotatedClass> secondRound
                        = this.updateClazzByMethods(firstRound);

                if (!secondRound.isEmpty()) {
                    //merge Methods to Classes
                    List<AnnotatedClass> thirdRound
                            = this.mergeMethodNames(secondRound);

                    if (!thirdRound.isEmpty()) {
                        for (AnnotatedClass item : thirdRound) {
                            annotations.add(item);
                        }
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

    private List<AnnotatedClass> mergeMethodNames(final List<AnnotatedClass> collection) {
        List<AnnotatedClass> temp = new ArrayList<>();

        //Copy List
        List<AnnotatedClass> orgin = new ArrayList<>();
        List<AnnotatedClass> merge = new ArrayList<>();
        for (AnnotatedClass item : collection) {
            merge.add(item);
            orgin.add(item);
        }

        for (AnnotatedClass element : orgin) {
            List<AnnotatedClass> skip = new ArrayList<>();
            merge.remove(element);

            if (merge.isEmpty()) {
                temp.add(element);
                break;
            }

            for (AnnotatedClass item : merge) {
                if (item.equals(element)) {
                    if (item.getType().equals("CONSTRUCTOR")) {
                        element.addMethodName(item.getClazzName());
                    } else {
                        element.addMethodName(item.getMethodNames());
                    }
                    this.annotations.add(element);
                    skip.add(item);
                }
            }

            if (!skip.isEmpty()) {
                for (AnnotatedClass item : skip) {
                    merge.remove(item);
                }
            } else {
                temp.add(element);
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
                    if (element.getType().equals("CONSTRUCTOR")) {
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
            if (element.getType().equals("ENUM")
                    || element.getType().equals("CLASS")) {
                this.annotations.add(element);
            } else {
                temp.add(element);
            }
        }
        return temp;
    }

}
