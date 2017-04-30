package org.europa.together.domain;

/**
 * Available Resource Types.
 */
public enum ResourceType {

    CLASSPATH("classpath"),
    DATABASE("database"),
    FILE("file"),
    JNDI("jndi"),
    LDAP("ldap");

    private final String value;

    private ResourceType(final String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }
};
