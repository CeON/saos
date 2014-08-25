package pl.edu.icm.saos.api.links;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import pl.edu.icm.saos.api.ApiConfiguration;
import pl.edu.icm.saos.api.judgments.JudgmentController;
import pl.edu.icm.saos.api.judgments.JudgmentsController;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

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
        String actualUrl = linksBuilder.linkToJudgment(someJudgmentId);

        //then
        assertTrue(actualUrl.endsWith("/api/judgments/"+someJudgmentId));
    }

    @Test
    public void itShouldReturnUrlToGivenDivision(){
        //given
        int someDivisionId = 123;

        //when
        String actualUrl = linksBuilder.linkToDivision(someDivisionId);

        //then
        assertTrue(actualUrl.endsWith("/api/divisions/"+someDivisionId));
    }

    @Test
    public void itShouldReturnUrlToGivenCourt(){
        //given
        int someCourtId = 321321;

        //when
        String actualUrl = linksBuilder.linkToCourt(someCourtId);

        //then
        assertTrue(actualUrl.endsWith("/api/courts/"+someCourtId));
    }



}