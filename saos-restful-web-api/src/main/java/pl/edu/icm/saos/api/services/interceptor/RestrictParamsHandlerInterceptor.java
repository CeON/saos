package pl.edu.icm.saos.api.services.interceptor;

import com.google.common.base.Objects;
import com.google.common.collect.Sets;
import org.springframework.beans.BeanUtils;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import pl.edu.icm.saos.api.services.exceptions.WrongRequestParameterException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.Set;

/**
 * Checks if request in controller's method annotated with {@link RestrictParamsNames}
 * contains only allowed parameters names.
 *
 * @see RestrictParamsNames
 *
 * @author pavtel
 */
public class RestrictParamsHandlerInterceptor extends HandlerInterceptorAdapter {

    //------------------------ LOGIC --------------------------

    /**
     * Checks if request in controller's method annotated with {@link RestrictParamsNames}
     * contains only allowed parameters names.
     * Allowed parameters names are extracted from method arguments' fields
     * (arguments which have {@link  RequestParam} annotation or {@link ModelAttribute}) annotation.
     * @param request to process.
     * @param response  skipped.
     * @param handler to process.
     * @return always <code>true</code>
     * @throws WrongRequestParameterException if request contains not allowed parameter name.
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        if(handler instanceof HandlerMethod){
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            RestrictParamsNames restrictParamsNames = handlerMethod.getMethodAnnotation(RestrictParamsNames.class);

            if(shouldHandle(restrictParamsNames)){
                checkIfRequestContainNotAllowedParameter(request, handlerMethod, restrictParamsNames);
            }

        }

        return true;
    }

    //------------------------ PRIVATE --------------------------

    private boolean shouldHandle(RestrictParamsNames restrictParamsNames) {
        return restrictParamsNames != null;
    }

    private void checkIfRequestContainNotAllowedParameter(HttpServletRequest request,
                                                           HandlerMethod handlerMethod,
                                                           RestrictParamsNames restrictParamsNames )
                                                    throws WrongRequestParameterException{

        Set<String> allowedNames = extractAllowedParameterNamesFor(handlerMethod);
        Set<String> requestNames = Sets.newHashSet(request.getParameterMap().keySet());

        requestNames.removeAll(allowedNames);

        if(!requestNames.isEmpty()){
            final String[] allowedPrefixes = extractAllowedPrefixes(restrictParamsNames);
            checkThatRequestNamesStartWith(allowedPrefixes, requestNames);
        }

    }

    private String[] extractAllowedPrefixes(RestrictParamsNames restrictParamsNames){
        String[] prefixes = restrictParamsNames.allowedPrefixes();
        if(prefixes == null){
            prefixes = new String[0];
        }

        return prefixes;
    }

    private void checkThatRequestNamesStartWith(final String[] allowedPrefixes, Set<String> requestNames)
                                                throws WrongRequestParameterException {
        requestNames.stream()
                .forEach(name -> checkAllowedRequestName(name, allowedPrefixes));
    }


    private void checkAllowedRequestName(String requestName, String[] allowedPrefixes) throws WrongRequestParameterException{
        for(String prefix: allowedPrefixes){
            if(requestName.startsWith(prefix)){
                return;
            }
        }

        throw new WrongRequestParameterException(requestName, " name is incorrect");
    }

    private Set<String> extractAllowedParameterNamesFor(HandlerMethod handlerMethod){
        Set<String> allowedParamsNames = Sets.newHashSet();

        MethodParameter[] parameters = handlerMethod.getMethodParameters();
        for(MethodParameter methodParameter: parameters){
            Set<String> allowedNames = extractAllowedParametersNamesFor(methodParameter);
            allowedParamsNames.addAll(allowedNames);
        }


        return allowedParamsNames;
    }

    private Set<String> extractAllowedParametersNamesFor(MethodParameter param){
        Annotation[] annotations =  param.getParameterAnnotations();

        for(Annotation annotation: annotations){
            if(annotation instanceof RequestParam){
                RequestParam requestParam = (RequestParam) annotation;
                String parameterName = requestParam.value();

                return Sets.newHashSet(parameterName);
            } else if (annotation instanceof ModelAttribute){
                Class<?> parameterType = param.getParameterType();
                Set<String> names = extractReadablePropertiesNamesFor(parameterType);

                return names;
            }
        }

        return Collections.emptySet();
    }

    private Set<String> extractReadablePropertiesNamesFor(Class<?> beanClass){
        Set<String> propertiesNames = Sets.newHashSet();

        PropertyDescriptor[] descriptors = BeanUtils.getPropertyDescriptors(beanClass);
        if(descriptors == null){
            descriptors = new PropertyDescriptor[0];
        }

        for(PropertyDescriptor propertyDescriptor: descriptors){
            PropertyNameWrapper nameWrapper = extractName(propertyDescriptor);
            if(nameWrapper.isWritableProperty()){
                propertiesNames.add(nameWrapper.getName());
            }
        }

        return propertiesNames;
    }

    private PropertyNameWrapper extractName(PropertyDescriptor propertyDescriptor) {
        String name = propertyDescriptor.getName();
        boolean isWritable = (propertyDescriptor.getWriteMethod() != null);
        return new PropertyNameWrapper(isWritable, name);
    }

    private static class PropertyNameWrapper {
        private boolean writableProperty;
        private String name;

        //------------------------ CONSTRUCTORS --------------------------
        public PropertyNameWrapper(boolean writableProperty, String name) {
            this.writableProperty = writableProperty;
            this.name = name;
        }

        //------------------------ GETTERS --------------------------
        public boolean isWritableProperty() {
            return writableProperty;
        }

        public String getName() {
            return name;
        }

        //------------------------ toString --------------------------

        @Override
        public String toString() {
            return Objects.toStringHelper(this)
                    .add("writableProperty", writableProperty)
                    .add("name", name)
                    .toString();
        }
    }
}