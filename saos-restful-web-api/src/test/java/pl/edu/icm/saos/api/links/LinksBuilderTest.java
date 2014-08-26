package pl.edu.icm.saos.api.links;

import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import static org.junit.Assert.*;

public class LinksBuilderTest {

    private LinksBuilder linksBuilder = new LinksBuilder();

    @Before
    public void setUp(){
        ServletRequestAttributes requestAttributes = new ServletRequestAttributes(new MockHttpServletRequest());
        RequestContextHolder.setRequestAttributes(requestAttributes);
    }

    @Test
    public void itShouldReturnUrlToGivenJudgment(){
        //given
        int someJudgmentId = 333;

        //when
        String actualUrl = linksBuilder.urlToJudgment(someJudgmentId);

        //then
        assertTrue(actualUrl.endsWith("/api/judgments/"+someJudgmentId));
    }

    @Test
    public void itShouldReturnUrlToGivenDivision(){
        //given
        int someDivisionId = 123;

        //when
        String actualUrl = linksBuilder.urlToDivision(someDivisionId);

        //then
        assertTrue(actualUrl.endsWith("/api/divisions/"+someDivisionId));
    }

    @Test
    public void itShouldReturnUrlToGivenCourt(){
        //given
        int someCourtId = 321321;

        //when
        String actualUrl = linksBuilder.urlToCourt(someCourtId);

        //then
        assertTrue(actualUrl.endsWith("/api/courts/"+someCourtId));
    }



}