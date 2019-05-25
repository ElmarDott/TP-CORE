package org.europa.together.domain;

import java.util.Objects;

/**
 * Data structure to storrage annotaed elements. Implements te functionality to
 * compare and sort the elements to process a code generation.
 */
public class AnnotatedClass {

    private final String annotation;
    private final String packageName;
    private final String clazzName;
    private final String constructorName;
    private final String methodName;

    /**
     * Constructor.
     *
     * @param type as String
     * @param namespace as String
     * @param clazz as String
     * @param constructor as String
     * @param method as String
     */
    public AnnotatedClass(final String type, final String namespace, final String clazz,
            final String constructor, final String method) {

        annotation = type;
        packageName = namespace;
        clazzName = clazz;
        constructorName = constructor;
        methodName = method;
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
                + ", Constructor=" + constructorName
                + ", Method=" + methodName + "}";
    }

    /**
     * Get the annotation type.
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
     * Get the constructor.
     *
     * @return constructorName as String
     */
    public String getConstructorName() {
        return constructorName;
    }

    /**
     * Get the method.
     *
     * @return methodName as String
     */
    public String getMethodName() {
        return methodName;
    }
}
