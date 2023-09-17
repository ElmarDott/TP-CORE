package org.europa.together.utils;

import java.lang.reflect.Constructor;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

@RunWith(JUnitPlatform.class)
@SuppressWarnings("unchecked")
public class ConstraintsTest {

    @Test//(expected = Exception.class)
    void testPrivateConstructor() throws Exception {
        Constructor<Constraints> clazz
                = Constraints.class.getDeclaredConstructor();
        clazz.setAccessible(true);
        assertThrows(Exception.class, () -> {
            Constraints call = clazz.newInstance();
        });
    }

    @Test
    void testToString() {
        String info = Constraints.printConstraintInfo();
        assertNotNull(info);
    }
}
