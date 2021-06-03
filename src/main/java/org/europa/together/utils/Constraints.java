package org.europa.together.utils;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.TimeZone;
import org.europa.together.application.LogbackLogger;
import org.europa.together.application.PropertyFileReader;
import org.europa.together.business.Logger;
import org.europa.together.business.PropertyReader;

/**
 * Constraints for the package CORE.
 */
public final class Constraints {

    private static final Logger LOGGER = new LogbackLogger(Constraints.class);
    private static final String FILE
            = "org/europa/together/configuration/module.properties";

    /**
     * Constructor.
     */
    private Constraints() {
        throw new UnsupportedOperationException();
    }

    /**
     * Module name used for ConfigurationDAO.
     */
    public static final String MODULE_NAME = getAppInfo("module");

    /**
     * Version of the module. This Version is autogenerated by the Maven POM
     * file version entry.
     */
    public static final String MODULE_VERSION = getAppInfo("version");

    /**
     * The Software License of the artifact.
     */
    public static final String LICENSE = "Apache License 2.0";

    /**
     * A short description of the module.
     */
    public static final String MODULE_DESCRIPTION = getAppInfo("description");

    /* ###################################################################### */
    /**
     * The default timezone is used by the artifact.
     */
    public static final TimeZone SYSTEM_DEFAULT_TIMEZONE = TimeZone.getTimeZone("UTC");

    /**
     * Detect the Operating System (OS) where the application is running.
     */
    public static final String SYSTEM_OS = System.getProperty("os.name");

    /**
     * Detect the Home Directory of the user in the OS.
     */
    public static final String SYSTEM_USER_HOME_DIR = System.getProperty("user.home");

    /**
     * Detect the Directory where the application is running.
     */
    public static final String SYSTEM_APP_DIR = Paths.get("").toAbsolutePath().toString();

    /**
     * Magic Constant.
     */
    public static final int INT_512 = 512;

    /**
     * *
     * Magic Constant.
     */
    public static final int INT_1024 = 1024;

    /**
     * Magic Constant.
     */
    public static final int INT_4096 = 4096;

    /**
     * Hexadecimal representation of 255.
     */
    public static final int HEX_255 = 0xff;

    /**
     * Hexadecimal representation of 256.
     */
    public static final int HEX_256 = 0x100;

    /**
     * Implements a static version of toString();.
     *
     * @return Constraints as String
     */
    public static String printConstraintInfo() {
        return "CORE Constraints DEBUG Info."
                + "\n\t Module Name: " + MODULE_NAME
                + "\n\t Module Version: " + MODULE_VERSION
                + "\n\t Module Description: " + MODULE_DESCRIPTION
                + "\n\t Software License: " + LICENSE
                + "\n\t Operating System: " + SYSTEM_OS
                + "\n\t User Home DIR: " + SYSTEM_USER_HOME_DIR
                + "\n\t Application DIR: " + SYSTEM_APP_DIR
                + "\n\t BYTE DEVISOR: " + INT_1024
                + "\n\t HEX 255: " + HEX_255
                + "\n\t HEX 256: " + HEX_256;
    }

    /* ###################################################################### */
    private static String getAppInfo(final String propertyName) {
        PropertyReader propertyReader = new PropertyFileReader();
        try {
            propertyReader.appendPropertiesFromClasspath(FILE);
        } catch (IOException ex) {
            LOGGER.catchException(ex);
        }
        return propertyReader.getPropertyAsString(propertyName);
    }
}
