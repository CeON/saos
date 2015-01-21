package pl.edu.icm.saos.enrichment.upload;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;
import static pl.edu.icm.saos.common.json.JsonNormalizer.normalizeJson;
import static pl.edu.icm.saos.enrichment.upload.EnrichmentTagUploadResponseMessages.ERROR_AUTHENTICATION_FAILED;
import static pl.edu.icm.saos.enrichment.upload.EnrichmentTagUploadResponseMessages.ERROR_EMPTY_DATA;
import static pl.edu.icm.saos.enrichment.upload.EnrichmentTagUploadResponseMessages.ERROR_INVALID_DATA;
import static pl.edu.icm.saos.enrichment.upload.EnrichmentTagUploadResponseMessages.ERROR_INVALID_JSON_FORMAT;
import static pl.edu.icm.saos.enrichment.upload.EnrichmentTagUploadResponseMessages.ERROR_MAX_UPLOAD_SIZE_EXCEEDED;
import static pl.edu.icm.saos.enrichment.upload.EnrichmentTagUploadResponseMessages.ERROR_SAME_TAG_ALREADY_UPLOADED;
import static pl.edu.icm.saos.enrichment.upload.EnrichmentTagUploadResponseMessages.ERROR_UNSUPPORTED_HTTP_CONTENT_TYPE;
import static pl.edu.icm.saos.enrichment.upload.EnrichmentTagUploadResponseMessages.ERROR_UNSUPPORTED_HTTP_METHOD;
import static pl.edu.icm.saos.enrichment.upload.EnrichmentTagUploadResponseMessages.OK_MESSAGE;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.codec.binary.Base64;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.context.WebApplicationContext;

import pl.edu.icm.saos.common.service.ServiceExecutionStatus;
import pl.edu.icm.saos.common.testcommon.category.SlowTest;
import pl.edu.icm.saos.persistence.common.TestPersistenceObjectFactory;
import pl.edu.icm.saos.persistence.enrichment.EnrichmentTagRepository;
import pl.edu.icm.saos.persistence.enrichment.UploadEnrichmentTagRepository;
import pl.edu.icm.saos.persistence.enrichment.model.EnrichmentTag;
import pl.edu.icm.saos.persistence.model.Judgment;
import pl.edu.icm.saos.webapp.WebappTestSupport;

/**
 * 
 * @author Łukasz Dumiszewski
 *
 */
@Category(SlowTest.class)
@WebAppConfiguration
public class EnrichmentTagUploadControllerTest extends WebappTestSupport {

    @Autowired
    private WebApplicationContext webApplicationCtx;

    @Autowired
    private FilterChainProxy springSecurityFilterChain;

    @Autowired
    private EnrichmentTagRepository enrichmentTagRepository;

    @Autowired
    private UploadEnrichmentTagRepository uploadEnrichmentTagRepository;

    @Autowired
    private EnrichmentTagUploadService enrichmentTagUploadService;

    @Autowired
    private TestPersistenceObjectFactory testPersistenceObjectFactory;

    @Autowired
    private EnrichmentTagUploadController enrichmentTagUploadController;

    
    @Value("${enrichment.enricher.login}")
    private String enricherLogin;

    private String enricherPassword = "pass";

    private static final String URL = "/api/enrichment/tags";

    private MockMvc mvc;

    
    @Before
    public void setUp() throws IOException {

        mvc = webAppContextSetup(webApplicationCtx).addFilter(springSecurityFilterChain).build();

        assertEquals(0, enrichmentTagRepository.count());
        assertEquals(0, uploadEnrichmentTagRepository.count());

    }

    // ------------------------ TESTS --------------------------

    @Test
    public void uploadEnrichmentTags_Unauthorized_NoAuthHeader() throws Exception {

        // execute
        ResultActions result = mvc.perform(post(URL));

        // assert
        assertUnauthorized(result);
    }

    
    @Test
    public void uploadEnrichmentTags_Unauthorized_InvalidUsername() throws Exception {

        // given
        String basicAuth = "Basic " + new String(Base64.encodeBase64((enricherLogin + "_badSuffix:pass").getBytes()));

        // execute & assert
        ResultActions result = mvc.perform(post(URL).header("Authorization", basicAuth));

        // assert
        assertUnauthorized(result);
    }

    
    @Test
    public void uploadEnrichmentTags_Unauthorized_InvalidPassword() throws Exception {

        // given
        String basicAuth = "Basic " + new String(Base64.encodeBase64((enricherLogin + ":invalidPassword").getBytes()));

        // execute
        ResultActions result = mvc.perform(post(URL).header("Authorization", basicAuth));

        // assert
        assertUnauthorized(result);
    }

    
    @Test
    public void uploadEnrichmentTags_MethodNotAllowed() throws Exception {

        // given
        String basicAuth = "Basic " + new String(Base64.encodeBase64((enricherLogin + ":" + enricherPassword).getBytes()));

        // execute
        ResultActions result = mvc.perform(post(URL).header("Authorization", basicAuth));

        // assert
        assertError(result, HttpStatus.METHOD_NOT_ALLOWED, ERROR_UNSUPPORTED_HTTP_METHOD);
    }

    
    @Test
    public void uploadEnrichmentTags_UnsupportedMediaType() throws Exception {

        // given
        String basicAuth = "Basic " + new String(Base64.encodeBase64((enricherLogin + ":" + enricherPassword).getBytes()));

        // execute
        ResultActions result = mvc.perform(put(URL).header("Authorization", basicAuth).contentType(MediaType.TEXT_PLAIN));

        // assert
        assertError(result, HttpStatus.UNSUPPORTED_MEDIA_TYPE, ERROR_UNSUPPORTED_HTTP_CONTENT_TYPE);
    }

    
    @Test
    public void uploadEnrichmentTags_EmptyData_NoBody() throws Exception {

        // given
        String basicAuth = "Basic " + new String(Base64.encodeBase64((enricherLogin + ":" + enricherPassword).getBytes()));

        // execute
        ResultActions result = mvc.perform(put(URL).header("Authorization", basicAuth).contentType(MediaType.APPLICATION_JSON));

        // assert
        assertError(result, HttpStatus.BAD_REQUEST, ERROR_EMPTY_DATA);
    }

    
    @Test
    public void uploadEnrichmentTags_EmptyData_EmptyBody() throws Exception {

        // execute
        ResultActions result = performPut("");

        // assert
        assertError(result, HttpStatus.BAD_REQUEST, ERROR_EMPTY_DATA);
    }

    
    @Test
    public void uploadEnrichmentTags_InvalidJsonFormat_NoArrayTagAtTheBeginning() throws Exception {

        // execute
        ResultActions result = performPut(normalizeJson("{'xxx':'yyy'}"));

        // assert
        assertError(result, HttpStatus.BAD_REQUEST, ERROR_INVALID_JSON_FORMAT);
    }

    
    @Test
    public void uploadEnrichmentTags_InvalidJsonFormat() throws Exception {

        // execute
        ResultActions result = performPut("[{\"'xxx\"'}]");

        // assert
        assertError(result, HttpStatus.BAD_REQUEST, ERROR_INVALID_JSON_FORMAT);
    }
    

    @Test
    public void uploadEnrichmentTags_InvalidData_EmptyTagType() throws Exception {

        // execute
        ResultActions result = performPut("[" + createJsonTag(12, "", "{'caseNo':'1234'}") + "]");

        // assert
        assertError(result, HttpStatus.BAD_REQUEST, ERROR_INVALID_DATA);
    }
    

    @Test
    public void uploadEnrichmentTags_InvalidData_NoJsonValue() throws Exception {

        // execute
        ResultActions result = performPut("[" + createJsonTag(12, "", "'YYY'") + "]");

        // assert
        assertError(result, HttpStatus.BAD_REQUEST, ERROR_INVALID_JSON_FORMAT);
    }

    
    @Test
    public void uploadEnrichmentTags_MaxUploadSizeExceeded() throws Exception {

        // given
        long maxUploadSize = enrichmentTagUploadService.getEnrichmentTagMaxUploadSize();
        enrichmentTagUploadService.setEnrichmentTagMaxUploadSize(10);

        // execute
        ResultActions result = performPut("[" + createJsonTag(12, "REFERENCED_REGULATIONS", "{'CCC':'YYY'}") + "]");

        enrichmentTagUploadService.setEnrichmentTagMaxUploadSize(maxUploadSize);

        // assert
        assertError(result, HttpStatus.REQUEST_ENTITY_TOO_LARGE, ERROR_MAX_UPLOAD_SIZE_EXCEEDED);

    }

    
    @Test
    public void uploadEnrichmentTags_SameTagAlreadyUploaded() throws Exception {

        // execute
        ResultActions result = performPut("[" + createJsonTag(12, "REFERENCED_REGULATIONS", "{'XXX':'YYY'}") + ","
                + createJsonTag(12, "REFERENCED_REGULATIONS", "{'ABC':'YYY'}") + "]");

        // assert
        assertError(result, HttpStatus.BAD_REQUEST, ERROR_SAME_TAG_ALREADY_UPLOADED);

        assertEquals(0, enrichmentTagRepository.count());
        assertEquals(0, uploadEnrichmentTagRepository.count());
    }
    

    @Test
    public void uploadEnrichmentTags_InternalServerError() throws Exception {

        // given
        EnrichmentTagUploadService mockEnrichmentTagUploadService = mock(EnrichmentTagUploadService.class);
        Mockito.doThrow(Exception.class).when(mockEnrichmentTagUploadService).uploadEnrichmentTags(Mockito.any(InputStream.class));
        enrichmentTagUploadController.setEnrichmentTagUploadService(mockEnrichmentTagUploadService);

        // execute
        ResultActions result = performPut("[" + createJsonTag(12, "REFERENCED_REGULATIONS", "{'CCC':'YYY'}") + "]");

        enrichmentTagUploadController.setEnrichmentTagUploadService(enrichmentTagUploadService);

        // assert
        assertError(result, HttpStatus.INTERNAL_SERVER_ERROR, EnrichmentTagUploadResponseMessages.ERROR_INTERVAL_SERVER_ERROR);
    }

    
    @Test
    public void uploadEnrichmentTags_OK() throws Exception {

        // given

        Judgment ccJudgment = testPersistenceObjectFactory.createCcJudgment();
        Judgment scJudgment = testPersistenceObjectFactory.createScJudgment();

        String jsonValue1 = normalizeJson("{'XXX':'YYY'}");
        String jsonValue2 = normalizeJson("{'caseNumbers':['YYY','xx-345']}");
        String jsonValue3 = normalizeJson("{'caseNumbers':['YYY','xx-345','YYY-2222']}");

        int nonExistingJudgmentId = scJudgment.getId() + ccJudgment.getId();

        // execute

        ResultActions result = performPut("[" + createJsonTag(ccJudgment.getId(), "REFERENCED_REGULATIONS", jsonValue1) + ","
                + createJsonTag(ccJudgment.getId(), "REFERENCED_CASE_NUMBERS", jsonValue2) + ","
                + createJsonTag(nonExistingJudgmentId, "REFERENCED_CASE_NUMBERS", jsonValue2) + "," // non-existing judgment shouldn't ne copied to production tags,
                + createJsonTag(scJudgment.getId(), "REFERENCED_CASE_NUMBERS", jsonValue3) + "]");

        // assert

        result.andExpect(status()
                .isOk())
                .andExpect(header().string("content-type", Matchers.containsString("application/json")))
                .andExpect(jsonPath("$.status").value(ServiceExecutionStatus.OK.name()))
                .andExpect(jsonPath("$.message").value(OK_MESSAGE))
                .andDo(print());

        assertEquals(4, uploadEnrichmentTagRepository.count());

        Thread.sleep(700);
        assertEquals(3, enrichmentTagRepository.count());

        EnrichmentTag expectedEnrichmentTag1 = createEnrichmentTag(ccJudgment.getId(), "REFERENCED_REGULATIONS", jsonValue1);
        EnrichmentTag expectedEnrichmentTag2 = createEnrichmentTag(ccJudgment.getId(), "REFERENCED_CASE_NUMBERS", jsonValue2);
        EnrichmentTag expectedEnrichmentTag3 = createEnrichmentTag(scJudgment.getId(), "REFERENCED_CASE_NUMBERS", jsonValue3);

        assertThat(enrichmentTagRepository.findAll(), containsInAnyOrder(expectedEnrichmentTag1, expectedEnrichmentTag2, expectedEnrichmentTag3));
    }

    
    
    
    // ------------------------ PRIVATE --------------------------

    private void assertUnauthorized(ResultActions result) throws Exception {
        assertError(result, HttpStatus.UNAUTHORIZED, ERROR_AUTHENTICATION_FAILED);
    }

    private void assertError(ResultActions result, HttpStatus httpStatus, String enrichmentTagUploadResponseMessage) throws Exception {
        result.andExpect(status().is(httpStatus.value()))
                .andExpect(header().string("content-type", Matchers.containsString("application/json")))
                .andExpect(jsonPath("$.status").value(ServiceExecutionStatus.ERROR.name()))
                .andExpect(jsonPath("$.message").value(enrichmentTagUploadResponseMessage)).andDo(print());
    }

    private ResultActions performPut(String content) throws Exception {

        String basicAuth = "Basic " + new String(Base64.encodeBase64((enricherLogin + ":" + enricherPassword).getBytes()));

        return mvc.perform(put(URL).header("Authorization", basicAuth).contentType(MediaType.APPLICATION_JSON).content(content.getBytes()));
    }

    private String createJsonTag(int judgmentId, String tagType, String jsonValue) {
        String jsonTag = "{'judgmentId':'" + judgmentId + "'," + " 'tagType':'" + tagType + "'," + " 'value':" + jsonValue + "}";
        return normalizeJson(jsonTag);
    }

    private EnrichmentTag createEnrichmentTag(int judgmentId, String tagType, String tagValue) {
        EnrichmentTag enrichmentTag = new EnrichmentTag();
        enrichmentTag.setJudgmentId(judgmentId);
        enrichmentTag.setTagType(tagType);
        enrichmentTag.setValue(tagValue);
        return enrichmentTag;
    }

}
