package pl.edu.icm.saos.api.services.links;

import org.junit.Before;
import org.junit.Test;
import org.springframework.hateoas.Link;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import static org.hamcrest.Matchers.*;
import static pl.edu.icm.saos.api.ApiConstants.*;

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
        String actualUrl = linksBuilder.urlToCcDivision(someDivisionId);

        //then
        assertTrue(actualUrl.endsWith("/api/ccDivisions/"+someDivisionId));
    }

    @Test
    public void itShouldReturnUrlToGivenCourt(){
        //given
        int someCourtId = 321321;

        //when
        String actualUrl = linksBuilder.urlToCommonCourt(someCourtId);

        //then
        assertTrue(actualUrl.endsWith("/api/commonCourts/"+someCourtId));
    }

    @Test
    public void itShouldReturnLinkToGivenJudgment(){
        //given
        int someJudgmentId = 454;

        //when
        Link link = linksBuilder.linkToJudgment(someJudgmentId);

        //then
        assertThat(link.getHref(), endsWith("/api/judgments/"+someJudgmentId));
        assertThat(link.getRel(), is(SELF));
    }

    @Test
    public void itShouldReturnLinkToGivenDivision(){
        //given
        int someDivisionId = 689;

        //when
        Link link = linksBuilder.linkToCcDivision(someDivisionId);

        //then
        assertThat(link.getHref(), endsWith("/api/ccDivisions/"+someDivisionId));
        assertThat(link.getRel(), is(SELF));
    }

    @Test
    public void itShouldReturnLinkToGivenCourt(){
        //given
        int someCourtId = 909;

        //when
        Link link = linksBuilder.linkToCommonCourt(someCourtId);

        //then
        assertThat(link.getHref(), endsWith("/api/commonCourts/"+someCourtId));
        assertThat(link.getRel(), is(SELF));
    }

    @Test
    public void itShouldReturnLinkToGivenCourtWithGivenRel(){
        //given
        int someCourtId = 234;
        String someRelName = "someCourtRelName";

        //when
        Link link = linksBuilder.linkToCommonCourt(someCourtId, someRelName);

        //then
        assertThat(link.getHref(), endsWith("/api/commonCourts/"+someCourtId));
        assertThat(link.getRel(), is(someRelName));
    }



}