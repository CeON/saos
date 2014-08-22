package pl.edu.icm.saos.api.parameters;

import org.junit.Before;
import org.junit.Test;
import pl.edu.icm.saos.api.exceptions.WrongRequestParameterException;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public class ParametersExtractorTest {

    private ParametersExtractor parametersExtractor;

    private static final int MAX_LIMIT = 77;
    private static final int DEFAULT_LIMIT = 33;

    @Before
    public void setUp(){
        parametersExtractor = new ParametersExtractor();
        parametersExtractor.setMaxLimit(MAX_LIMIT);
        parametersExtractor.setDefaultLimit(DEFAULT_LIMIT);
    }


    @Test
    public void itShouldReturnDefaultLimitValueIfZeroIsGiven() throws Exception {
        //given
        int zero = 0;
        int someOffset = 11;

        //when
        Pagination pagination = parametersExtractor.extractAndValidatePagination(zero, someOffset);

        //then
        assertEquals(DEFAULT_LIMIT, pagination.getLimit());
    }

    @Test
    public void itShouldReturnGivenOffsetValue() throws Exception {
        //given
        int someLimit = 1;
        int givenOffset = 11;

        //when
        Pagination pagination = parametersExtractor.extractAndValidatePagination(someLimit, givenOffset);

        //then
        assertEquals(givenOffset, pagination.getOffset());
    }

    @Test (expected = WrongRequestParameterException.class)
    public void itShouldThrowWRPExceptionIfOffsetIsNegative() throws Exception {
        //given
        int someLimit = 1;
        int negativeOffset = - 20;

        //when
        parametersExtractor.extractAndValidatePagination(someLimit, negativeOffset);
    }

    @Test (expected = WrongRequestParameterException.class)
    public void itShouldThrowWRPExceptionIfLimitIsGreaterThanMaxLimit() throws Exception {
        //given
        int bigLimit = MAX_LIMIT +1;
        int someOffset = 1;

        //when
        parametersExtractor.extractAndValidatePagination(bigLimit, someOffset);
    }

    @Test (expected = WrongRequestParameterException.class)
    public void itShouldThrowWRPExceptionIfLimitIsNegative() throws Exception {
        //given
        int negativeLimit = -1;
        int someOffset = 10;

        //when
        parametersExtractor.extractAndValidatePagination(negativeLimit, someOffset);
    }

    @Test
    public void itShouldReturnEmptyValuesForBlankString() throws Exception {
        //given
        String blankValue = null;

        //when
        JoinedParameter actual = parametersExtractor.extractJoinedParameter(blankValue);

        //then
        JoinedParameter expected = new JoinedParameter("", Collections.emptyList());
        assertEquals(expected, actual);
    }

    @Test
    public void itShouldReturnValuesSortedByNames() throws Exception {
        //given
        String first = "court";
        String second = "all";
        String parameterValue = first+","+second;

        //when
        JoinedParameter actual = parametersExtractor.extractJoinedParameter(parameterValue);

        //then
        JoinedParameter expected = new JoinedParameter(parameterValue, Arrays.asList(second, first));
        assertEquals(expected, actual);
    }



}