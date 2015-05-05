package pl.edu.icm.saos.api.search.parameters;

import static org.apache.commons.lang3.StringUtils.removeStart;
import static pl.edu.icm.saos.api.ApiConstants.PAGE_NUMBER;
import static pl.edu.icm.saos.api.ApiConstants.PAGE_SIZE;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.api.services.dates.DatesFactory;
import pl.edu.icm.saos.api.services.exceptions.WrongRequestParameterException;

import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;

/**
 * Provides functionality for extracting request parameters.
 * @author pavtel
 */
@Service("parametersExtractor")
public class ParametersExtractor {

    private static final Splitter SPLITTER = Splitter.on(",").trimResults().omitEmptyStrings();

    private static final Splitter DATE_SPLITTER = Splitter.on("-").trimResults().omitEmptyStrings();

    private static final Pattern DATE_PATTERN = Pattern.compile("^[1-9]\\d{3}-(1[0-2]|0[1-9])-(0[1-9]|[1-2]\\d|3[0-1])$");

    private int defaultPageSize=20;

    @Value("${restful.api.max.page.size}")
    private int maxPageSize=100;
    
    @Value("${restful.api.min.page.size}")
    private int minPageSize=10;


    //------------------------ LOGIC --------------------------

    /**
     * Extracts 'joined' request parameter.
     * If paramValue="valA,ValB", then joinedParameter=JoinedParameter("valA,valB", ["valA", "valB"])
     * @param parameterValue to process.
     * @return joinedParameter
     */
    public JoinedParameter extractJoinedParameter(String parameterValue){
        String joinedValue = StringUtils.trimToEmpty(parameterValue);

        Iterable<String> splitedExpand = SPLITTER.split(joinedValue);
        String[] arrayValues = Iterables.toArray(splitedExpand, String.class);

        List<String> values = Arrays.asList(arrayValues);

        Collections.sort(values);


        return new JoinedParameter(joinedValue, values);
    }


    /**
     * Extracts date in the format "yyyy-MM-DD" into {@link org.joda.time.LocalDate localDate}.
     * @param value date to process in the format "yyyy-MM-DD".
     * @param paramName used for exception.
     * @return localDate representation of value.
     * @throws WrongRequestParameterException if value has incorrect format.
     */
    public LocalDate extractLocalDate(String value, String paramName) throws WrongRequestParameterException {
        if(StringUtils.isBlank(value))
            return null;

        if(!DATE_PATTERN.matcher(value).matches()){
            throw new WrongRequestParameterException(paramName, "should have format yyyy-MM-DD");
        }

        List<Integer> integers = DATE_SPLITTER
                .splitToList(value)
                .stream()
                .map(s -> removeStart(s, "0"))
                .map(Integer::valueOf)
                .collect(Collectors.toList())
                ;

        int year = integers.get(0);
        int month = integers.get(1);
        int day = integers.get(2);


        LocalDate localDate = DatesFactory.utcLocalDate(year, month, day);

        return localDate;
    }

    /**
     * Constructs Pagination object (sets default values for offset and limit if necessary)
     * @param pageSize to process.
     * @param pageNumber to process.
     * @return pagination
     * @throws WrongRequestParameterException if limit and offset values are incorrect.
     */
    public Pagination extractAndValidatePagination(int pageSize, int pageNumber) throws WrongRequestParameterException {
        int currentPageSize = normalizeAndValidatePageSize(pageSize);
        int currentPageNumber = normalizeAndValidatePageNumber(pageNumber);


        return new Pagination(currentPageSize, currentPageNumber);
    }

    private int normalizeAndValidatePageNumber(int pageNumber) throws WrongRequestParameterException {
        validate(pageNumber, (n) -> n<0, PAGE_NUMBER, "can't be negative");

        return pageNumber;
    }


    private int normalizeAndValidatePageSize(int pageSize) throws WrongRequestParameterException {
        int currentPageSize = normalizePageSize(pageSize);

        validate(currentPageSize, (n) -> n<=0, PAGE_SIZE, "may not be negative");
        validate(currentPageSize, (n) -> n>maxPageSize, PAGE_SIZE, "may not be greater than "+maxPageSize);
        validate(currentPageSize, (n) -> n<minPageSize, PAGE_SIZE, "may not be less than "+minPageSize);

        return currentPageSize;
    }

    private void validate(int value, Predicate<Integer> predicate, String parameterName, String exMsg) throws WrongRequestParameterException {
        if(predicate.test(value)){
            throw new WrongRequestParameterException(parameterName, exMsg);
        }
    }

    private int normalizePageSize(int pageSize){
        return pageSize != 0 ? pageSize : defaultPageSize;
    }
    
    
    //------------------------ GETTERS --------------------------
    
    public int getMaxPageSize() {
        return maxPageSize;
    }

    public int getMinPageSize() {
        return minPageSize;
    }


    //------------------------ SETTERS --------------------------

    public void setDefaultPageSize(int defaultPageSize) {
        this.defaultPageSize = defaultPageSize;
    }

    public void setMaxPageSize(int maxPageSize) {
        this.maxPageSize = maxPageSize;
    }

    public void setMinPageSize(int minPageSize) {
        this.minPageSize = minPageSize;
    }

}
