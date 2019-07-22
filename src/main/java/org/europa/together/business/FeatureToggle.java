package org.europa.together.business;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.apiguardian.api.API;
import static org.apiguardian.api.API.Status.STABLE;

/**
 * Feature Toggle Annotation to activate or deactivate PUBLIC classes,
 * constructors and metods.<br>
 * When a Constructor get deactivated the whole class is deactivated, no matter
 * how many Constructors defined. In the case it exist an overloaded method, all
 * those methods get deactivated.
 *
 * @author elmar.dott@gmail.com
 * @version 1.0
 * @since 1.2
 */
@Target({ElementType.TYPE, ElementType.CONSTRUCTOR, ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.SOURCE)
@Documented
@API(status = STABLE, since = "1.2")
public @interface FeatureToggle {

    /**
     * Indicate the value for featureID.
     *
     * @return featureId as String
     */
    String featureID();
}
