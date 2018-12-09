package org.europa.together.domain;

import java.util.Objects;
import org.europa.together.application.LoggerImpl;
import org.europa.together.business.FeatureToggle;
import org.europa.together.business.Logger;
import org.europa.together.exceptions.MisconfigurationException;
import org.europa.together.utils.Validator;

/**
 * Data class for program version numbers, based on semantic versioning. This
 * implementation allwos to compare and sort version numbers.<br>
 * Pattern: Major.Minor.Patch-Label (Patch and Label are optional)<br>
 * <code>
 *  true:  equals(1.2.3-SNAPSHOT : 1.2.3-SNAPSHOT);
 *  true:  equals(1.0 : 1.0.0);
 *  false: equals(1.0 : 1.0.1);
 *  false: equals(1.0-LABEL : 1.0);
 *  A greater B: compareTo(2.0 : 1.0);
 *  A greater B: compareTo(1.1 : 1.0);
 *  A greater B: compareTo(1.0.1 : 1.0.0);
 *  A greater B: compareTo(2.1 : 1.1);
 *  A equal B:   compareTo(2.1.1 : 2.1.1-SNAPSHOT);
 * </code> The only recomennded label is SNAPSHOT, because labels are not part
 * of comparing.
 *
 * <b>Definition:</b><br>
 * A version of a software artifact is always uniqe. Artifacts can have
 * different versions (1:n relation). That means when an artifact a exist two
 * times and both are not identical, the first artifact has a different version
 * than the second artifact. Both artifacts can not have the same version. If
 * both artifacts with the same name have the same version it is impossible to
 * distinguish them.
 */
public class Version implements Comparable<Version> {

    private static final Logger LOGGER = new LoggerImpl(Version.class);

    private final int MAJOR;
    private final int MINOR;
    private int PATCH = -1;
    private String LABEL = null;

    /**
     * Constructor.
     *
     * @param version as String
     * @throws org.europa.together.exceptions.MisconfigurationException
     */
    @FeatureToggle(featureID = "CM-0005.DO02")
    public Version(String version) throws MisconfigurationException {

        if (!Validator.validate(version, Validator.VERSION_NUMBER)) {
            throw new MisconfigurationException("The version number " + version
                    + " do not match the Pattern: [000].[000].[000]-[LABEL]");
        }

        String[] fragments = version.split(".");

        MAJOR = new Integer(fragments[0]);
        MINOR = new Integer(fragments[1]);

        if (fragments.length > 2) {
            String[] optional = fragments[3].split("-");

            PATCH = new Integer(optional[0]);
            if (optional.length == 2) { //prevent index out of bound exception
                LABEL = optional[1];
            }
        }
    }

    /**
     * Return the Major section of a version number as int. MANDANTORY.
     *
     * @return Major as int
     */
    public int getMajor() {
        return MAJOR;
    }

    /**
     * Return the Minor section of a version number as int. MANDANTORY.
     *
     * @return Minor as int
     */
    public int getMinor() {
        return MINOR;
    }

    /**
     * Return the Patch Level section of a version number as int. OPTIONAL. If
     * the patch level not exist the method return -1.
     *
     * @return Patch as int
     */
    public int getPatch() {
        return PATCH;
    }

    /**
     * Return the Label section of a version number as String. OPTIONAL. If the
     * lable not exist the method return null.
     *
     * @return Label as String
     */
    public String getLabel() {
        return LABEL;
    }

    @Override
    public int hashCode() {
        return 91;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Version other = (Version) obj;
        if (this.MAJOR != other.MAJOR) {
            return false;
        }
        if (this.MINOR != other.MINOR) {
            return false;
        }
        if (this.PATCH != other.PATCH) {
            return false;
        }
        if (!Objects.equals(this.LABEL, other.LABEL)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return MAJOR + "." + MINOR + "." + PATCH + "-" + LABEL;
    }

    @Override
    public int compareTo(Version o) {
        // -1:smaller | 0:equal | 1:greater
        int compare;

        if (this.equals(o)) {
            compare = 0;
        } else if (this.MAJOR > o.MAJOR) {
            compare = 1;
        } else if (this.MAJOR < o.MAJOR) {
            compare = -1;
        } else {

            if (this.MINOR > o.MINOR) {
                compare = 1;
            } else if (this.MINOR < o.MINOR) {
                compare = -1;
            } else {

                if (this.PATCH > o.PATCH) {
                    compare = 1;
                } else if (this.PATCH < o.PATCH) {
                    compare = -1;
                } else {
                    //equal, because lables will not sorted
                    compare = 0;
                }
            }
        }
        return compare;
    }
}
