package org.europa.together.utils;

import java.net.Socket;
import org.europa.together.application.LoggerImpl;
import org.europa.together.business.Logger;
import org.europa.together.domain.LogLevel;

/**
 * Helper Class to iniciate timeouts in connections e.g. Databases, SMTP Servers
 * and so on.
 */
public final class SocketTimeout {

    private static final Logger LOGGER = new LoggerImpl(FileUtils.class);

    /**
     * Constructor.
     */
    private SocketTimeout() {
        throw new UnsupportedOperationException();
    }

    /**
     * Initiate a timeout after the given time in milliseconds are passed. 1
     * Second = 1000 Milliseconds. Useful values to interrupt connection are
     * between 3000 and 5000.<br>
     * The method return a success true when a connection can be established.
     *
     * @param milliseconds as long
     * @param uri as String
     * @param port as int
     * @return true on success
     */
    public static boolean timeout(final int milliseconds, final String uri, final int port) {

        boolean success = false;
        for (int i = 1; i <= 3; i++) {
            try {
                Socket socket = new Socket(uri, port);
                socket.setSoTimeout(milliseconds);
                socket.close();

                success = true;
                LOGGER.log("[After " + i + " tries] Socket connection to " + uri
                        + ":" + port + " can be established.",
                        LogLevel.DEBUG);
                break;

            } catch (Exception ex) {
                LOGGER.catchException(ex);
            }
        }
        return success;
    }
}
