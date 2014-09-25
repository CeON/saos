package pl.edu.icm.saos.api.parameters;

import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.LocalDate;
import org.springframework.stereotype.Component;
import pl.edu.icm.saos.api.exceptions.WrongRequestParameterException;
import pl.edu.icm.saos.api.utils.DatesFactory;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.removeStart;
import static pl.edu.icm.saos.api.ApiConstants.LIMIT;
import static pl.edu.icm.saos.api.ApiConstants.OFFSET;

/**
 * Provides functionality for extracting request parameters.
 * @author pavtel
 */
@Component("parametersExtractor")
public class ParametersExtractor {

    //********* fields *********
    private static final Splitter SPLITTER = Splitter.on(",").trimResults().omitEmptyStrings();

    private static final Splitter DATE_SPLITTER = Splitter.on("-").trimResults().omitEmptyStrings();

    private static final Pattern DATE_PATTERN = Pattern.compile("^[1-9]\\d{3}-(1[0-2]|0[1-9])-(0[1-9]|[1-2]\\d|3[0-1])$");

    private int defaultLimit = 20; //TODO move into properties file
    private int maxLimit = 100; //TODO move into properties file

    //********** END fields **********

    //************* business methods **********

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
        if(value == null || StringUtils.isBlank(value))
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


        LocalDate localDate = DatesFactory.warsawLocalDate(year, month, day);

        return localDate;
    }


    /**
     * Constructs Pagination object (sets default values for offset and limit if necessary)
     * @param limit to process.
     * @param offset to process.
     * @return pagination
     * @throws WrongRequestParameterException if limit and offset values are incorrect.
     */
    public Pagination extractAndValidatePagination(int limit, int offset) throws WrongRequestParameterException {
        int currentLimit = normalizeAndValidateLimit(limit);
        int currentOffset = normalizeAndValidateOffset(offset);


        return new Pagination(currentLimit, currentOffset);
    }

    private int normalizeAndValidateOffset(int offset) throws WrongRequestParameterException {
        validate(offset, (n) -> n<0, OFFSET, "can't be negative");

        return offset;
    }


    private int normalizeAndValidateLimit(int limit) throws WrongRequestParameterException {
        int currentLimit = normalizeLimit(limit);

        validate(currentLimit, (n) -> n<=0, LIMIT, "should be positive");
        validate(currentLimit, (n) -> n>maxLimit, LIMIT, "can't be bigger than "+maxLimit);

        return currentLimit;
    }

    private void validate(int value, Predicate<Integer> predicate, String parameterName, String exMsg) throws WrongRequestParameterException {
        if(predicate.test(value)){
            throw new WrongRequestParameterException(parameterName, exMsg);
        }
    }

    private int normalizeLimit(int limit){
        return limit != 0 ? limit : defaultLimit;
    }

    //******* END business methods **********


    // ******** setters ******

    public void setDefaultLimit(int defaultLimit) {
        this.defaultLimit = defaultLimit;
    }

    public void setMaxLimit(int maxLimit) {
        this.maxLimit = maxLimit;
    }
}
