package org.europa.together.domain;

public class JacksonObjectTest {

    private String id;
    private String key;
    private int value;
    private boolean activation;

    @Override
    public String toString() {
        return "JacksonObjectTest{" + "id=" + id
                + ", key=" + key
                + ", value=" + value
                + ", activation=" + activation
                + '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public boolean isActivation() {
        return activation;
    }

    public void setActivation(boolean activation) {
        this.activation = activation;
    }
}
