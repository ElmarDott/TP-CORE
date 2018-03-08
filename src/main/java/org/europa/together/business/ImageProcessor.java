package org.europa.together.business;

import java.util.List;
import org.apiguardian.api.API;
import static org.apiguardian.api.API.Status.*;
import org.europa.together.exceptions.UnsupportedVersionException;
import org.springframework.stereotype.Component;

/**
 * A simple Image Processor with some usful basic functionality in applications.
 *
 * @author elmar.dott@gmail.com
 * @version 1.0
 * @since 1.0
 */
@API(status = STABLE, since = "1.0")
@Component
public interface ImageProcessor {

    @API(status = EXPERIMENTAL, since = "1.0")
    default void crop(long x, long y, long height, long width) throws UnsupportedVersionException {
        throw new UnsupportedVersionException("Method not implemnted in this Version.");
    }

    @API(status = EXPERIMENTAL, since = "1.0")
    default void compress(int percentage) throws UnsupportedVersionException {
        throw new UnsupportedVersionException("Method not implemnted in this Version.");
    }

    @API(status = EXPERIMENTAL, since = "1.0")
    default void rezize(long height, long width) throws UnsupportedVersionException {
        throw new UnsupportedVersionException("Method not implemnted in this Version.");
    }

    @API(status = EXPERIMENTAL, since = "1.0")
    default void rotateRight() throws UnsupportedVersionException {
        throw new UnsupportedVersionException("Method not implemnted in this Version.");
    }

    @API(status = EXPERIMENTAL, since = "1.0")
    default void setMetaData() throws UnsupportedVersionException {
        throw new UnsupportedVersionException("Method not implemnted in this Version.");
    }

    @API(status = EXPERIMENTAL, since = "1.0")
    default List<String> getMetaData() throws UnsupportedVersionException {
        throw new UnsupportedVersionException("Method not implemnted in this Version.");
    }

}
