package pl.edu.icm.saos.api.parameters;

import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.LocalDate;
import org.springframework.stereotype.Component;
import pl.edu.icm.saos.api.exceptions.WrongRequestParameterException;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.removeStart;
import static pl.edu.icm.saos.api.ApiConstants.*;

/**
 * @author pavtel
 */
@Component("parametersExtractor")
public class ParametersExtractor {

    private static final Splitter SPLITTER = Splitter.on(",").trimResults().omitEmptyStrings();

    private static final Splitter DATE_SPLITTER = Splitter.on("-").trimResults().omitEmptyStrings();

    private static final Pattern DATE_PATTERN = Pattern.compile("^[1-9]\\d{3}-(1[0-2]|0[1-9])-(0[1-9]|[1-2]\\d|3[0-1])$");

    private int defaultLimit = 20; //TODO move into properties file
    private int maxLimit = 100; //TODO move into properties file

    public ParametersExtractor() {
    }

    public RequestParameters extractRequestParameter(String expandValue, int limit, int offset) throws WrongRequestParameterException {
        Pagination pagination = extractAndValidatePagination(limit, offset);
        JoinedParameter expandParameter = extractJoinedParameter(expandValue);

        return new RequestParameters(expandParameter, pagination);
    }

    public JoinedParameter extractJoinedParameter(String parameterValue){
        String joinedValue = StringUtils.trimToEmpty(parameterValue);

        Iterable<String> splitedExpand = SPLITTER.split(joinedValue);
        String[] arrayValues = Iterables.toArray(splitedExpand, String.class);

        List<String> values = Arrays.asList(arrayValues);

        Collections.sort(values);


        return new JoinedParameter(joinedValue, values);
    }


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


        LocalDate localDate = new LocalDate(year, month, day);

        return localDate;
    }

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


    // ******** setters ******

    public void setDefaultLimit(int defaultLimit) {
        this.defaultLimit = defaultLimit;
    }

    public void setMaxLimit(int maxLimit) {
        this.maxLimit = maxLimit;
    }
}
