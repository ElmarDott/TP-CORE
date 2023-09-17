package org.europa.together.utils;

import java.nio.file.Paths;
import org.europa.together.application.LoggerImpl;
import org.europa.together.business.Logger;
import org.europa.together.domain.LogLevel;

/**
 * Constraints for the package CORE.
 */
public final class Constraints {

    private static final Logger LOGGER = new LoggerImpl(StringUtils.class);

    /**
     * Constructor.
     */
    private Constraints() {
        throw new UnsupportedOperationException();
    }

    /**
     * Module name.
     */
    public static final String MODULE_NAME = "core";

    /**
     * Version of the module.
     */
    public static final String MODULE_VERSION = "1.0";

    /**
     * A short description of the module.
     */
    public static final String MODULE_DESCRIPTION = "Basic Library";

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
     * Byte Devisor is the magic number of 1024. This number you always need to
     * calculate from one dimension to the next higher dimension. e. g. 1024
     * kilo Byte = 1 mega Byte
     */
    public static final int BYTE_DEVISOR = 1024;

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
        String moduleInfo = "CORE Constraints DEBUG Info."
                + "\n\t Module Name: " + MODULE_NAME
                + "\n\t Module Version: " + MODULE_VERSION
                + "\n\t Module Description: " + MODULE_DESCRIPTION
                + "\n\t Operating System: " + SYSTEM_OS
                + "\n\t User Home DIR: " + SYSTEM_USER_HOME_DIR
                + "\n\t Application DIR: " + SYSTEM_APP_DIR
                + "\n\t BYTE DEVISOR: " + BYTE_DEVISOR
                + "\n\t HEX 255: " + HEX_255
                + "\n\t HEX 256: " + HEX_256;

        LOGGER.log(moduleInfo, LogLevel.DEBUG);
        return moduleInfo;
    }
}
