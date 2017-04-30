package org.europa.together.utils;

import java.lang.reflect.Constructor;
import org.junit.Test;

@SuppressWarnings("unchecked")
public class ConstraintsTest {

    @Test(expected = Exception.class)
    public void testPrivateConstructor() throws Exception {
        Constructor<Constraints> clazz
                = Constraints.class.getDeclaredConstructor();
        clazz.setAccessible(true);
        Constraints call = clazz.newInstance();
    }

}
