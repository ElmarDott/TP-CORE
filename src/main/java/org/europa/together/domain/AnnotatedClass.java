package org.europa.together.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Data structure to storage annotated FeatureToggle elements. Implements the
 * functionality to compare and sort the elements to process a code generation.
 * Classname & constructor name are the same. Types: CLASS | ENUM | CONSTRUCTOR
 * | METHOD
 */
public class AnnotatedClass {

    public static final String CLASS = "CLASS";
    public static final String ENUM = "ENUM";
    public static final String CONSTRUCTOR = "CONSTRUCTOR";
    public static final String METHOD = "METHOD";

    private final String annotation;
    private final String packageName;
    private final String clazzName;
    private final List<String> methodNames = new ArrayList<>();

    /**
     * Constructor.
     *
     * @param type as String
     * @param namespace as String
     * @param clazz as String
     * @param methods as List
     */
    public AnnotatedClass(final String type, final String namespace, final String clazz,
            final List<String> methods) {

        annotation = type;
        packageName = namespace;
        clazzName = clazz;

        if (methods != null && !methods.isEmpty()) {
            for (String method : methods) {
                methodNames.add(method);
            }
        }
    }

    @Override
    public int hashCode() {
        int hash = Objects.hashCode(this.packageName);
        hash += Objects.hashCode(this.clazzName);
        return hash;
    }

    @Override
    public boolean equals(final Object object) {

        boolean success = false;
        if (object != null && object instanceof AnnotatedClass) {

            if (this == object) {
                success = true;
            } else {

                final AnnotatedClass other = (AnnotatedClass) object;
                if (Objects.equals(this.packageName, other.packageName)
                        && Objects.equals(this.clazzName, other.clazzName)) {
                    success = true;
                }
            }
        }
        return success;
    }

    @Override
    public String toString() {
        return "AnnotatedClass{"
                + "TYPE=" + annotation
                + ", Package=" + packageName
                + ", Class=" + clazzName
                + ", Method=[" + getMethodNames() + "]}";
    }

    /**
     * Get the annotation type. <br/>
     * CLASS | ENUM | CONSTRUCTOR | METHOD
     *
     * @return annotation as String
     */
    public String getType() {
        return annotation;
    }

    /**
     * Get the package.
     *
     * @return packageName as String
     */
    public String getPackageName() {
        return packageName;
    }

    /**
     * Get the class.
     *
     * @return clazzName as String
     */
    public String getClazzName() {
        return clazzName;
    }

    /**
     * Get the method.
     *
     * @return methodName as String
     */
    public String getMethodNames() {
        return String.join(" ", this.methodNames);
    }

    /**
     * Extend the List of Methods.
     *
     * @param methodName as String
     */
    public void addMethodName(final String methodName) {
        this.methodNames.add(methodName);
    }
}
