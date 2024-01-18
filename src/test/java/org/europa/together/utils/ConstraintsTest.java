package org.europa.together.utils;

import java.lang.reflect.Constructor;
import org.europa.together.JUnit5Preperator;
import org.europa.together.application.LogbackLogger;
import org.europa.together.business.Logger;
import org.europa.together.domain.LogLevel;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@SuppressWarnings("unchecked")
@ExtendWith({JUnit5Preperator.class})
public class ConstraintsTest {

    private static final Logger LOGGER = new LogbackLogger(ConstraintsTest.class);

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
        LOGGER.log(info, LogLevel.DEBUG);
        assertNotNull(info);
    }
}
