package org.europa.together.utils;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import org.europa.together.application.FF4jProcessor;
import org.europa.together.application.LoggerImpl;
import org.europa.together.business.Logger;
import org.europa.together.domain.AnnotatedClass;
import org.europa.together.domain.LogLevel;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

@SuppressWarnings("unchecked")
@RunWith(JUnitPlatform.class)
public class AnnotationProcessingHelperTest {

    private static final Logger LOGGER = new LoggerImpl(AnnotationProcessingHelperTest.class);

    //<editor-fold defaultstate="collapsed" desc="Test Preparation">
    @BeforeAll
    static void setUp() {
        LOGGER.log("### TEST SUITE INICIATED.", LogLevel.TRACE);

        FF4jProcessor feature = new FF4jProcessor();
        boolean toggle = feature.deactivateUnitTests("CM-0013");
        LOGGER.log("PERFORM TESTS :: FeatureToggle", LogLevel.TRACE);

        boolean check;
        String out;
        if (!toggle) {
            out = "skiped.";
            check = false;
        } else {
            out = "executed.";
            check = true;
        }
        LOGGER.log("Assumption terminated. TestSuite will be " + out, LogLevel.TRACE);
        Assumptions.assumeTrue(check);

    }

    @AfterAll
    static void tearDown() {
        LOGGER.log("### TEST SUITE TERMINATED.", LogLevel.TRACE);
    }

    @BeforeEach
    void testCaseInitialization() {
    }

    @AfterEach
    void testCaseTermination() {
        LOGGER.log("TEST CASE TERMINATED.\n", LogLevel.TRACE);
    }
    //</editor-fold>

    @Test
    @Disabled
    public void testPerformComplexCombinations() {
        LOGGER.log("TEST CASE: performComplexCombinations", LogLevel.DEBUG);

        List<AnnotatedClass> set = new ArrayList<>();
        set.addAll(this.annotatedEnums());
        set.addAll(this.annotatedClasses());
        set.addAll(this.annotatedConstructors());
        set.addAll(this.annotatedMethods());
        set.addAll(this.updateAnnotatedClasses());
        set.addAll(this.mergeConstructorsOfOneClass());
        set.addAll(this.mergeMetodsOfOneClass());
        set.addAll(this.mergeConstructorAndMethod());

        AnnotationProcessingHelper helper = new AnnotationProcessingHelper();
        List<AnnotatedClass> merged = helper.mergedAnnotatedElements(set);

        for (AnnotatedClass item : merged) {
            LOGGER.log(item.toString(), LogLevel.DEBUG);
        }
        assertEquals(15, merged.size());
    }

    @Test
    public void testMergeConstructorsAndMethods() {
        LOGGER.log("TEST CASE: mergeConstructorsAndMethods", LogLevel.DEBUG);

        List<AnnotatedClass> set = new ArrayList<>();
        set.addAll(this.mergeConstructorAndMethod());

        AnnotationProcessingHelper helper = new AnnotationProcessingHelper();
        List<AnnotatedClass> merged = helper.mergedAnnotatedElements(set);

        for (AnnotatedClass item : merged) {
            LOGGER.log(item.toString(), LogLevel.DEBUG);
        }
        assertEquals(1, merged.size());
        assertEquals(AnnotatedClass.CONSTRUCTOR, merged.get(0).getType());
    }

    @Test
    public void testMergeMethodsToOneElement() {
        LOGGER.log("TEST CASE: mergeMethodsToOneElement", LogLevel.DEBUG);

        List<AnnotatedClass> set = new ArrayList<>();
        set.addAll(this.mergeMetodsOfOneClass());

        AnnotationProcessingHelper helper = new AnnotationProcessingHelper();
        List<AnnotatedClass> merged = helper.mergedAnnotatedElements(set);

        for (AnnotatedClass item : merged) {
            LOGGER.log(item.toString(), LogLevel.DEBUG);
        }
        assertEquals(1, merged.size());
        assertEquals("methodA methodB methodC", merged.get(0).getMethodNames());
    }

    @Test
    public void testMergeConstructorsToOneElement() {
        LOGGER.log("TEST CASE: mergeConstructorsToOneElement", LogLevel.DEBUG);

        List<AnnotatedClass> set = new ArrayList<>();
        set.addAll(this.mergeConstructorsOfOneClass());

        AnnotationProcessingHelper helper = new AnnotationProcessingHelper();
        List<AnnotatedClass> merged = helper.mergedAnnotatedElements(set);

        for (AnnotatedClass item : merged) {
            LOGGER.log(item.toString(), LogLevel.DEBUG);
        }
        assertEquals(1, merged.size());
//        assertEquals("methodA methodB methodC", merged.get(0).getMethodNames());
    }

    @Test
    public void testMergeMethodsAndConstructorsToClass() {
        LOGGER.log("TEST CASE: mergeMethodsAndConstructorsToClass", LogLevel.DEBUG);

        List<AnnotatedClass> set = new ArrayList<>();
        set.addAll(this.updateAnnotatedClasses());

        AnnotationProcessingHelper helper = new AnnotationProcessingHelper();
        List<AnnotatedClass> merged = helper.mergedAnnotatedElements(set);

        for (AnnotatedClass item : merged) {
            LOGGER.log(item.toString(), LogLevel.DEBUG);
        }
        assertEquals(3, merged.size());
    }

    @Test
    public void testSimpleMethods() {
        LOGGER.log("TEST CASE: simpleMethods", LogLevel.DEBUG);

        List<AnnotatedClass> set = new ArrayList<>();
        set.addAll(this.annotatedMethods());

        AnnotationProcessingHelper helper = new AnnotationProcessingHelper();
        List<AnnotatedClass> merged = helper.mergedAnnotatedElements(set);

        for (AnnotatedClass item : merged) {
            LOGGER.log(item.toString(), LogLevel.DEBUG);
        }
        assertEquals(set.size(), merged.size());

    }

    @Test
    public void testSimpleConstructors() {
        LOGGER.log("TEST CASE: simpleConstructors", LogLevel.DEBUG);

        List<AnnotatedClass> set = new ArrayList<>();
        set.addAll(this.annotatedConstructors());

        AnnotationProcessingHelper helper = new AnnotationProcessingHelper();
        List<AnnotatedClass> merged = helper.mergedAnnotatedElements(set);

        for (AnnotatedClass item : merged) {
            LOGGER.log(item.toString(), LogLevel.DEBUG);
        }
        assertEquals(set.size(), merged.size());
    }

    @Test
    public void testSimpleEnums() {
        LOGGER.log("TEST CASE: simpleEnums", LogLevel.DEBUG);

        List<AnnotatedClass> set = new ArrayList<>();
        set.addAll(this.annotatedEnums());

        AnnotationProcessingHelper helper = new AnnotationProcessingHelper();
        List<AnnotatedClass> merged = helper.mergedAnnotatedElements(set);

        for (AnnotatedClass item : merged) {
            LOGGER.log(item.toString(), LogLevel.DEBUG);
        }
        assertEquals(set.size(), merged.size());
    }

    @Test
    public void testSimpleClasses() {
        LOGGER.log("TEST CASE: simpleClasses", LogLevel.DEBUG);

        List<AnnotatedClass> set = new ArrayList<>();
        set.addAll(this.annotatedClasses());

        AnnotationProcessingHelper helper = new AnnotationProcessingHelper();
        List<AnnotatedClass> merged = helper.mergedAnnotatedElements(set);

        for (AnnotatedClass item : merged) {
            LOGGER.log(item.toString(), LogLevel.DEBUG);
        }
        assertEquals(set.size(), merged.size());
    }

    @Test
    public void testFailMergeAnnotations() {
        LOGGER.log("TEST CASE: failMergeAnnotations", LogLevel.DEBUG);

        AnnotationProcessingHelper helper = new AnnotationProcessingHelper();
        assertEquals(0, helper.mergedAnnotatedElements(new ArrayList()).size());
    }

    @Test
    public void testPrint() {
        LOGGER.log("TEST CASE: print", LogLevel.DEBUG);

        PrintStream originalOut = System.out;
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();

        System.setOut(new PrintStream(outContent));

        AnnotationProcessingHelper.print("H311o W0r1d.");
        String test = outContent.toString();

        //restore stream
        System.setOut(originalOut);

        assertEquals("H311o W0r1d.\n", test);
        LOGGER.log("out stream restored.", LogLevel.INFO);
    }

    private List<AnnotatedClass> annotatedClasses() {
        List<AnnotatedClass> set = new ArrayList<>();

        set.add(new AnnotatedClass(AnnotatedClass.CLASS, "org.sample.test", "ClassA", null));
        set.add(new AnnotatedClass(AnnotatedClass.CLASS, "org.sample.test", "ClassB", null));
        set.add(new AnnotatedClass(AnnotatedClass.CLASS, "org.sample.test.a", "ClassA", null));
        return set;
    }

    private List<AnnotatedClass> annotatedEnums() {
        List<AnnotatedClass> set = new ArrayList<>();

        set.add(new AnnotatedClass(AnnotatedClass.ENUM, "org.sample.test", "Enum", null));
        set.add(new AnnotatedClass(AnnotatedClass.ENUM, "org.sample.test.a", "Enum", null));
        return set;
    }

    private List<AnnotatedClass> annotatedConstructors() {
        List<AnnotatedClass> set = new ArrayList<>();

        set.add(new AnnotatedClass(AnnotatedClass.CONSTRUCTOR, "org.sample.test", "ConstructorA", null));
        set.add(new AnnotatedClass(AnnotatedClass.CONSTRUCTOR, "org.sample.test", "ConstructorB", null));
        set.add(new AnnotatedClass(AnnotatedClass.CONSTRUCTOR, "org.sample.test.a", "ConstructorA", null));
        return set;
    }

    private List<AnnotatedClass> annotatedMethods() {
        List<AnnotatedClass> set = new ArrayList<>();

        List<String> methodName = new ArrayList<>();
        methodName.add("methodName");

        set.add(new AnnotatedClass(AnnotatedClass.METHOD, "org.sample.test", "MethodA", methodName));
        set.add(new AnnotatedClass(AnnotatedClass.METHOD, "org.sample.test", "MethodB", methodName));
        set.add(new AnnotatedClass(AnnotatedClass.METHOD, "org.sample.test.a", "MethodA", methodName));
        return set;
    }

    private List<AnnotatedClass> updateAnnotatedClasses() {
        List<AnnotatedClass> set = new ArrayList<>();

        set.add(new AnnotatedClass(AnnotatedClass.ENUM, "org.sample.test", "Enum", null));
        set.add(new AnnotatedClass(AnnotatedClass.CLASS, "org.sample.test", "Class", null));
        set.add(new AnnotatedClass(AnnotatedClass.CLASS, "org.sample.test.sub", "Class", null));

        set.add(new AnnotatedClass(AnnotatedClass.CONSTRUCTOR, "org.sample.test", "Class", null));

        List<String> methodName_1 = new ArrayList<>();
        methodName_1.add("methodA");
        List<String> methodName_2 = new ArrayList<>();
        methodName_2.add("methodB");
        List<String> methodName_3 = new ArrayList<>();
        methodName_3.add("methodC");

        set.add(new AnnotatedClass(AnnotatedClass.METHOD, "org.sample.test", "Class", methodName_1));
        set.add(new AnnotatedClass(AnnotatedClass.METHOD, "org.sample.test", "Class", methodName_2));
        set.add(new AnnotatedClass(AnnotatedClass.METHOD, "org.sample.test", "Class", methodName_3));
        return set;
    }

    private List<AnnotatedClass> mergeConstructorsOfOneClass() {
        List<AnnotatedClass> set = new ArrayList<>();
        set.add(new AnnotatedClass(AnnotatedClass.CONSTRUCTOR, "org.sample.test", "Constructor", null));
        set.add(new AnnotatedClass(AnnotatedClass.CONSTRUCTOR, "org.sample.test", "Constructor", null));
        set.add(new AnnotatedClass(AnnotatedClass.CONSTRUCTOR, "org.sample.test", "Constructor", null));
        return set;
    }

    private List<AnnotatedClass> mergeMetodsOfOneClass() {
        List<AnnotatedClass> set = new ArrayList<>();

        List<String> methodName_1 = new ArrayList<>();
        methodName_1.add("methodA");
        List<String> methodName_2 = new ArrayList<>();
        methodName_2.add("methodB");
        List<String> methodName_3 = new ArrayList<>();
        methodName_3.add("methodC");

        set.add(new AnnotatedClass(AnnotatedClass.METHOD, "org.sample.test", "Method", methodName_1));
        set.add(new AnnotatedClass(AnnotatedClass.METHOD, "org.sample.test", "Method", methodName_2));
        set.add(new AnnotatedClass(AnnotatedClass.METHOD, "org.sample.test", "Method", methodName_3));
        return set;
    }

    private List<AnnotatedClass> mergeConstructorAndMethod() {
        List<AnnotatedClass> set = new ArrayList<>();

        List<String> methodName = new ArrayList<>();
        methodName.add("method");

        set.add(new AnnotatedClass(AnnotatedClass.CONSTRUCTOR, "org.sample.test", "ConstuctorMethod", null));
        set.add(new AnnotatedClass(AnnotatedClass.METHOD, "org.sample.test", "ConstuctorMethod", methodName));
        return set;
    }
}
