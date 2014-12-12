package pl.edu.icm.saos.api.services.interceptor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation which in conjugation with {@link RestrictParamsHandlerInterceptor}
 * restricts request parameters names.
 * If you need to restrict parameters that can be used in request for
 * controller's method just annotate method with this annotation.
 * Allowed parameters names are extracted from method arguments' fields
 * (arguments which have annotation {@link org.springframework.web.bind.annotation.RequestParam RequestParam}
 * or {@link org.springframework.web.bind.annotation.ModelAttribute ModelAttribute}).
 *
 * @see RestrictParamsHandlerInterceptor
 *
 * @author pavtel
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RestrictParamsNames {

    /**
     * If request parameter's name starts from
     * one of defined prefixes then {@link RestrictParamsHandlerInterceptor}
     * skips parameter processing
     */
    String[] allowedPrefixes() default {"_", "!"};

}
