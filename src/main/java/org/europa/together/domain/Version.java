package org.europa.together.domain;

import java.util.Objects;
import org.europa.together.application.LogbackLogger;
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
 *  true:  equals(1.2.3-LABLE : 1.2.3-SNAPSHOT);
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
@FeatureToggle(featureID = "CM-0005.DO02")
public class Version implements Comparable<Version> {

    private static final Logger LOGGER = new LogbackLogger(Version.class);

    private int major = -1;
    private int minor = -1;
    private int patch = -1;
    private String label = null;

    /**
     * Constructor.
     *
     * @param version as String
     */
    public Version(final String version) {

        try {

            if (!Validator.validate(version, Validator.VERSION_NUMBER)) {
                String msg = "The version number " + version
                        + " do not match the Pattern: [000].[000].[000]-[LABEL].";
                throw new MisconfigurationException(msg);
            }

            String[] fragments = version.split("\\.");
            LOGGER.log("Fragments: " + fragments.length, LogLevel.DEBUG);

            major = Integer.parseInt(fragments[0]);

            if (!fragments[1].contains("-")) {
                minor = Integer.parseInt(fragments[1]);
            } else {
                LOGGER.log("Lable after minor.", LogLevel.DEBUG);

                String[] optional = fragments[1].split("-");
                minor = Integer.parseInt(optional[0]);
                label = optional[1];
            }

            if (fragments.length > 2) {
                String[] optional = fragments[2].split("-");

                patch = Integer.parseInt(optional[0]);
                if (optional.length == 2) { //prevent index out of bound exception
                    label = optional[1];
                }
            }

            LOGGER.log("Version: " + toString(), LogLevel.DEBUG);

        } catch (Exception ex) {
            LOGGER.catchException(ex);
        }

    }

    //<editor-fold defaultstate="collapsed" desc="Getter / Setter">
    /**
     * Return the Major section of a version number as int. MANDANTORY.
     *
     * @return Major as int
     */
    public int getMajor() {
        return major;
    }

    /**
     * Return the Minor section of a version number as int. MANDANTORY.
     *
     * @return Minor as int
     */
    public int getMinor() {
        return minor;
    }

    /**
     * Return the Patch Level section of a version number as int. OPTIONAL. If
     * the patch level not exist the method return -1.
     *
     * @return Patch as int
     */
    public int getPatch() {
        return patch;
    }

    /**
     * Return the Label section of a version number as String. OPTIONAL. If the
     * lable not exist the method return null.
     *
     * @return Label as String
     */
    public String getLabel() {
        return label;
    }

    /**
     * Return the whole version number as String.
     *
     * @return version as String
     */
    public String getVersion() {

        String version = null;
        version = major + "." + minor;
        if (patch != -1) {
            version = version + "." + patch;
        }
        if (label != null) {
            version = version + "-" + label;
        }
        return version;
    }
    //</editor-fold>

    @Override
    public int compareTo(final Version o) {
        // -1:smaller | 0:equal | 1:greater
        int compare;

        if (this.major > o.major) {
            compare = 1;
        } else if (this.major < o.major) {
            compare = -1;
        } else {

            if (this.minor > o.minor) {
                compare = 1;
            } else if (this.minor < o.minor) {
                compare = -1;
            } else {

                if (this.patch > o.patch) {
                    compare = 1;
                } else if (this.patch < o.patch) {
                    compare = -1;
                } else {
                    compare = 0;
                }
            }
        }
        return compare;
    }

    @Override
    @SuppressWarnings("PMD.CollapsibleIfStatements")
    public boolean equals(final Object object) {

        boolean success = false;
        if (object != null && object instanceof Version) {

            if (this == object) {
                success = true;
            } else {

                final Version other = (Version) object;
                if (Objects.equals(this.major, other.major)
                        && Objects.equals(this.minor, other.minor)) {

                    if ((this.patch == -1 || this.patch == 0)
                            && (other.patch == -1 || other.patch == 0)
                            || Objects.equals(this.patch, other.patch)) {
                        success = true;
                    }
                }
            }
        }
        return success;
    }

    @Override
    public int hashCode() {
        int hash = Objects.hashCode(this.major);
        hash += Objects.hashCode(this.minor);
        hash += Objects.hashCode(this.patch);
        return hash;
    }

    @Override
    public String toString() {
        return "Version{" + getVersion() + "}";
    }
}
