package org.europa.together.utils;

import java.lang.reflect.Constructor;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SuppressWarnings("unchecked")
@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {"/applicationContext.xml"})
public class ConstraintsTest {

    @Test
    void privateConstructor() throws Exception {
        Constructor<Constraints> clazz
                = Constraints.class.getDeclaredConstructor();
        clazz.setAccessible(true);
        assertThrows(Exception.class, () -> {
            Constraints call = clazz.newInstance();
        });
    }

    @Test
    void generateToString() {
        String info = Constraints.printConstraintInfo();
        assertNotNull(info);
    }
}
