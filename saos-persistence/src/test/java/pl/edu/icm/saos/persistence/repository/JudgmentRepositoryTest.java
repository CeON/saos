package pl.edu.icm.saos.persistence.repository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

import org.hibernate.LazyInitializationException;
import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.google.common.collect.Lists;

import pl.edu.icm.saos.common.testcommon.category.SlowTest;
import pl.edu.icm.saos.persistence.PersistenceTestSupport;
import pl.edu.icm.saos.persistence.common.TestInMemoryObjectFactory;
import pl.edu.icm.saos.persistence.common.TestPersistenceObjectFactory;
import pl.edu.icm.saos.persistence.model.CommonCourtJudgment;
import pl.edu.icm.saos.persistence.model.ConstitutionalTribunalJudgment;
import pl.edu.icm.saos.persistence.model.ConstitutionalTribunalJudgmentDissentingOpinion;
import pl.edu.icm.saos.persistence.model.CourtCase;
import pl.edu.icm.saos.persistence.model.Judge;
import pl.edu.icm.saos.persistence.model.Judge.JudgeRole;
import pl.edu.icm.saos.persistence.model.Judgment;
import pl.edu.icm.saos.persistence.model.JudgmentSourceInfo;
import pl.edu.icm.saos.persistence.model.JudgmentTextContent;
import pl.edu.icm.saos.persistence.model.SourceCode;
import pl.edu.icm.saos.persistence.model.SupremeCourtJudgment;

/**
 * @author Łukasz Dumiszewski
 */
@Category(SlowTest.class)
public class JudgmentRepositoryTest extends PersistenceTestSupport {

    @Autowired
    private JudgmentRepository judgmentRepository;

    @Autowired
    private TestPersistenceObjectFactory testPersistenceObjectFactory;
    
    
   
    //------------------------ TESTS --------------------------
    
    @Test
    public void testSaveAndGet() {
        
        // given
        Assert.assertEquals(0, judgmentRepository.count());
        
        CommonCourtJudgment judgment = new CommonCourtJudgment();
        judgment.addCourtCase(new CourtCase("222"));
        judgment.getSourceInfo().setSourceCode(SourceCode.SUPREME_COURT);
        judgment.getSourceInfo().setSourceJudgmentId("11111");
        
        // execute
        judgmentRepository.save(judgment);
        
        // assert
        Assert.assertEquals(1, judgmentRepository.count());
    }
    
    @Test
    public void save_iterable() {
        
        // given
        
        CommonCourtJudgment judgment1 = new CommonCourtJudgment();
        judgment1.addCourtCase(new CourtCase("AAA1"));
        judgment1.getSourceInfo().setSourceCode(SourceCode.COMMON_COURT);
        judgment1.getSourceInfo().setSourceJudgmentId("1");
        
        CommonCourtJudgment judgment2 = new CommonCourtJudgment();
        judgment2.addCourtCase(new CourtCase("AAA2"));
        judgment2.getSourceInfo().setSourceCode(SourceCode.COMMON_COURT);
        judgment2.getSourceInfo().setSourceJudgmentId("2");
        
        DateTime beforeSave = new DateTime();
        waitForTimeChange();
        
        
        // execute
        
        judgmentRepository.save(Lists.newArrayList(judgment1, judgment2));
        
        
        // assert
        
        assertEquals(2, judgmentRepository.count());
        
        Judgment retJudgment1 = judgmentRepository.findOne(judgment1.getId());
        Judgment retJudgment2 = judgmentRepository.findOne(judgment2.getId());
        
        assertTrue(retJudgment1.getModificationDate().isAfter(beforeSave));
        assertTrue(retJudgment2.getModificationDate().isAfter(beforeSave));
    }
    
    @Test
    public void saveAndFlush() {
        
        // given
        
        CommonCourtJudgment judgment = new CommonCourtJudgment();
        judgment.addCourtCase(new CourtCase("AAA1"));
        judgment.getSourceInfo().setSourceCode(SourceCode.COMMON_COURT);
        judgment.getSourceInfo().setSourceJudgmentId("1");
        
        DateTime beforeSave = new DateTime();
        waitForTimeChange();
        
        
        // execute
        
        judgmentRepository.saveAndFlush(judgment);
        
        
        // assert
        
        assertEquals(1, judgmentRepository.count());
        
        Judgment retJudgment = judgmentRepository.findOne(judgment.getId());
        
        assertTrue(retJudgment.getModificationDate().isAfter(beforeSave));
    }
    
    
    @Test
    public void count_WITH_CLASS() {
        // when
        createCcJudgment(SourceCode.COMMON_COURT, "1", "AAA1");
        createCcJudgment(SourceCode.COMMON_COURT, "2", "AAA2");
        createScJudgment(SourceCode.SUPREME_COURT, "3", "AAA3");
        
        // execute
        long actualCount = judgmentRepository.count(CommonCourtJudgment.class);
        
        // then
        assertEquals(2, actualCount);
    }
    
    @Test
    public void findAllIds() {
        // given
        Judgment judgment1 = createCcJudgment(SourceCode.COMMON_COURT, "1", "AAA1");
        Judgment judgment2 = createCcJudgment(SourceCode.COMMON_COURT, "2", "AAA2");
        Judgment judgment3 = createScJudgment(SourceCode.SUPREME_COURT, "3", "AAA3");
        
        // execute
        List<Long> judgmentIds = judgmentRepository.findAllIds();
        
        // assert
        assertThat(judgmentIds, containsInAnyOrder(judgment1.getId(), judgment2.getId(), judgment3.getId()));
    }
    
    @Test
    public void findOneBySourceCodeAndSourceJudgmentId_NOT_FOUND() {
        
        // execute
        Judgment ccJudgment = judgmentRepository.findOneBySourceCodeAndSourceJudgmentId(SourceCode.COMMON_COURT, "111122");
        
        // assert
        assertNull(ccJudgment);
        
    }
    
    @Test
    public void findOneBySourceCodeAndSourceJudgmentId_FOUND() {
        
        // given
        
        CommonCourtJudgment ccJudgment = TestInMemoryObjectFactory.createSimpleCcJudgment();
        JudgmentSourceInfo sourceInfo = new JudgmentSourceInfo();
        sourceInfo.setSourceCode(SourceCode.COMMON_COURT);
        sourceInfo.setSourceJudgmentId("1123");
        ccJudgment.setSourceInfo(sourceInfo);
        
        judgmentRepository.save(ccJudgment);
        
        // execute
        
        Judgment dbCcJudgment = judgmentRepository.findOneBySourceCodeAndSourceJudgmentId(SourceCode.COMMON_COURT, "1123");
        
        
        // assert
        assertNotNull(dbCcJudgment);
        assertEquals(ccJudgment.getId(), dbCcJudgment.getId());
    }
    
    @Test
    public void findOneBySourceCodeAndSourceJudgmentId_WITH_CLASS_NOT_FOUND() {
        
        // given
        SupremeCourtJudgment scJudgment = testPersistenceObjectFactory.createScJudgment();
        
        // execute
        CommonCourtJudgment ccJudgment = judgmentRepository.findOneBySourceCodeAndSourceJudgmentId(
                scJudgment.getSourceInfo().getSourceCode(), scJudgment.getSourceInfo().getSourceJudgmentId(), CommonCourtJudgment.class);
        
        // assert
        assertNull(ccJudgment);
    }
    
    @Test
    public void findOneBySourceCodeAndSourceJudgmentId_WITH_CLASS_FOUND() {
        
        // given
        CommonCourtJudgment ccJudgment = testPersistenceObjectFactory.createCcJudgment();
        JudgmentSourceInfo sourceInfo = ccJudgment.getSourceInfo();
        
        // execute
        CommonCourtJudgment dbCcJudgment = judgmentRepository.findOneBySourceCodeAndSourceJudgmentId(
                SourceCode.COMMON_COURT, sourceInfo.getSourceJudgmentId(), CommonCourtJudgment.class);
        
        // assert
        assertNotNull(dbCcJudgment);
        assertEquals(ccJudgment.getId(), dbCcJudgment.getId());
    }
    
    
    @Test
    public void findBySourceCodeAndCaseNumber_NOT_FOUND() {
        
        // execute
        List<Judgment> ccJudgments = judgmentRepository.findBySourceCodeAndCaseNumber(SourceCode.COMMON_COURT, "111122");
        
        // assert
        assertEquals(0, ccJudgments.size());
    }
    
    @Test
    public void findBySourceCodeAndCaseNumber_FOUND() {
        
        // given
        CommonCourtJudgment ccJudgment1 = createCcJudgment(SourceCode.COMMON_COURT, "1", "AAA1");
        createCcJudgment(SourceCode.COMMON_COURT, "2", "AAA2");
        createCcJudgment(SourceCode.COMMON_COURT, "3", "AAA3");
        createCcJudgment(SourceCode.ADMINISTRATIVE_COURT, "1", "AAA1");
        
        // execute
        List<Judgment> ccJudgments = judgmentRepository.findBySourceCodeAndCaseNumber(SourceCode.COMMON_COURT, ccJudgment1.getCaseNumbers().get(0));
        
        // assert
        assertEquals(1, ccJudgments.size());
        assertEquals(ccJudgment1.getId(), ccJudgments.get(0).getId());
      
    }
    
    @Test
    public void findBySourceCodeAndCaseNumber_FOUND_MultiCases() {
        
        //given
        CommonCourtJudgment ccJudgment1 = createCcJudgment(SourceCode.COMMON_COURT, "1", "AAA1");
        createCcJudgment(SourceCode.COMMON_COURT, "2", "AAA2");
        createCcJudgment(SourceCode.COMMON_COURT, "3", "AAA3");
        createCcJudgment(SourceCode.ADMINISTRATIVE_COURT, "1", "AAA1");
        
        ccJudgment1.addCourtCase(new CourtCase("BBB1"));
        
        // execute
        List<Judgment> ccJudgments = judgmentRepository.findBySourceCodeAndCaseNumber(SourceCode.COMMON_COURT, ccJudgment1.getCaseNumbers().get(0));
        
        // assert
        assertEquals(1, ccJudgments.size());
        assertEquals(ccJudgment1.getId(), ccJudgments.get(0).getId());
      
    }
    
    @Test
    public void findOneAndInitialize_Judgment() {
        
        // given
        Judgment ccJudgment = testPersistenceObjectFactory.createCcJudgment();
        
        // execute
        Judgment dbJudgment = judgmentRepository.findOneAndInitialize(ccJudgment.getId());
        
        // assert
        assertNotNull(dbJudgment);
        dbJudgment.getJudges().size();
        dbJudgment.getJudges().get(0).getSpecialRoles().size();
        dbJudgment.getReferencedRegulations().size();
        dbJudgment.getReferencedRegulations().get(0).getLawJournalEntry().getTitle();
        dbJudgment.getCourtReporters().size();
        dbJudgment.getLegalBases().size();
        
    }

    @Test
    public void findOneAndInitialize_CommonCourtJudgment() {
        
        // given
        Judgment ccJudgment = testPersistenceObjectFactory.createCcJudgment();
        
        // execute
        CommonCourtJudgment dbJudgment = judgmentRepository.findOneAndInitialize(ccJudgment.getId());
        
        // assert
        assertNotNull(dbJudgment);
        dbJudgment.getCourtDivision().getCourt().getCode();
        dbJudgment.getKeywords().size();
    }


    @Test
    public void findOneAndInitialize_SupremeCourtJudgment() {
        
        // given
        Judgment scJudgment = testPersistenceObjectFactory.createScJudgment();
        
        // execute
        SupremeCourtJudgment dbJudgment = judgmentRepository.findOneAndInitialize(scJudgment.getId());
        
        // assert
        assertNotNull(dbJudgment);
        dbJudgment.getScChamberDivision().getName();
        dbJudgment.getScChambers().size();
        dbJudgment.getScJudgmentForm().getName();
    }

    
    @Test(expected=LazyInitializationException.class)
    public void findOne_Uninitialized() {
        
        // given
        Judgment ccJudgment = testPersistenceObjectFactory.createCcJudgment();
        
        // execute
        Judgment dbJudgment = judgmentRepository.findOne(ccJudgment.getId());
        
        // assert
        assertNotNull(dbJudgment);
        dbJudgment.getJudges().size();
    }
    
    @Test(expected=EntityNotFoundException.class)
    public void findOneAndInitialize_NOT_FOUND() {
        
        // execute
        judgmentRepository.findOneAndInitialize(987654l);
    }
    

    
    @Test
    public void markAsNotIndexedBySourceCode_ONLY_CC_JUDGMENTS() {
        
        // given
        
        CommonCourtJudgment ccJudgment = createCcJudgment(SourceCode.COMMON_COURT, "1", "AAA1");
        SupremeCourtJudgment scJudgment = createScJudgment(SourceCode.SUPREME_COURT, "2", "AAA2");
        judgmentRepository.markAsIndexed(ccJudgment.getId());
        judgmentRepository.markAsIndexed(scJudgment.getId());
        
        
        // execute
        
        judgmentRepository.markAsNotIndexedBySourceCode(SourceCode.COMMON_COURT);
        
        
        // assert
        
        Judgment actualCcJudgment = judgmentRepository.findOne(ccJudgment.getId());
        Judgment actualScJudgment = judgmentRepository.findOne(scJudgment.getId());
        
        assertFalse(actualCcJudgment.isIndexed());
        assertTrue(actualScJudgment.isIndexed());
    }
    
    @Test
    public void markAsNotIndexedBySourceCode_ALL_JUDGMENTS() {
        
        // given
        
        CommonCourtJudgment ccJudgment = createCcJudgment(SourceCode.COMMON_COURT, "1", "AAA1");
        SupremeCourtJudgment scJudgment = createScJudgment(SourceCode.SUPREME_COURT, "2", "AAA2");
        judgmentRepository.markAsIndexed(ccJudgment.getId());
        judgmentRepository.markAsIndexed(scJudgment.getId());
        
        
        // execute
        
        judgmentRepository.markAsNotIndexedBySourceCode(null);
        
        
        // assert
        
        Judgment actualCcJudgment = judgmentRepository.findOne(ccJudgment.getId());
        Judgment actualScJudgment = judgmentRepository.findOne(scJudgment.getId());
        
        assertFalse(actualCcJudgment.isIndexed());
        assertFalse(actualScJudgment.isIndexed());
    }

    @Test
    @Transactional
    public void it_should_update_modification_date_on_judgment_change(){
        
        //given
        CommonCourtJudgment judgment = createCcJudgment(SourceCode.COMMON_COURT, "1", "AAA1");
        judgmentRepository.save(judgment);
        DateTime firstModificationDate = judgment.getModificationDate();

        //when
        judgment.setDecision("some decision");
        waitForTimeChange();
        judgmentRepository.save(judgment);
        DateTime secondModificationDate = judgmentRepository.findOne(judgment.getId()).getModificationDate();

        //then
        assertNotNull(firstModificationDate);
        assertNotNull(secondModificationDate);
        assertTrue(secondModificationDate.isAfter(firstModificationDate));
    }
    
    @Test
    @Transactional
    public void it_should_update_modification_date_on_judgment_collection_change(){
        
        //given
        CommonCourtJudgment judgment = createCcJudgment(SourceCode.COMMON_COURT, "1", "AAA1");
        judgmentRepository.save(judgment);
        DateTime firstModificationDate = judgment.getModificationDate();

        //when
        judgment.addJudge(new Judge("John Doe", JudgeRole.PRESIDING_JUDGE));
        waitForTimeChange();
        judgmentRepository.save(judgment);
        DateTime secondModificationDate = judgmentRepository.findOne(judgment.getId()).getModificationDate();

        //then
        assertNotNull(firstModificationDate);
        assertNotNull(secondModificationDate);
        assertTrue(secondModificationDate.isAfter(firstModificationDate));
    }
    
    
    @Test
    public void delete_JudgmentIds_All() {
        
        // given
        Judgment ccJudgment = testPersistenceObjectFactory.createCcJudgment();
        Judgment scJudgment = testPersistenceObjectFactory.createScJudgment();
        Judgment ctJudgment = testPersistenceObjectFactory.createCtJudgment();
        Judgment nacJudgment = testPersistenceObjectFactory.createNacJudgment();
        
        assertEquals(4, judgmentRepository.count());
        
        
        // execute
        judgmentRepository.delete(Lists.newArrayList(ccJudgment.getId(), scJudgment.getId(), ctJudgment.getId(), nacJudgment.getId()));
        
        // assert
        assertEquals(0, judgmentRepository.count());
        
    }
    
    
    @Test
    public void delete_JudgmentIds_AFew() {
        
        // given
        Judgment ccJudgment = testPersistenceObjectFactory.createCcJudgment();
        Judgment scJudgment = testPersistenceObjectFactory.createScJudgment();
        Judgment ctJudgment = testPersistenceObjectFactory.createCtJudgment();
        Judgment nacJudgment = testPersistenceObjectFactory.createNacJudgment();
        
        assertEquals(4, judgmentRepository.count());
        
        
        // execute
        judgmentRepository.delete(Lists.newArrayList(ctJudgment.getId(), nacJudgment.getId()));
        
        // assert
        assertEquals(2, judgmentRepository.count());
        
        List<Long> judgmentIds = judgmentRepository.findAll().stream().map(j->j.getId()).collect(Collectors.toList());
        assertThat(judgmentIds, containsInAnyOrder(scJudgment.getId(), ccJudgment.getId()));
    }
    
    
    @Test
    public void delete() {
        
        // given
        Judgment ccJudgment = testPersistenceObjectFactory.createCcJudgment();
        Judgment scJudgment = testPersistenceObjectFactory.createScJudgment();
        Judgment ctJudgment = testPersistenceObjectFactory.createCtJudgment();
        Judgment nacJudgment = testPersistenceObjectFactory.createNacJudgment();
        
        assertEquals(4, judgmentRepository.count());
        
        
        // execute
        judgmentRepository.delete(ccJudgment);
        judgmentRepository.delete(scJudgment);
        judgmentRepository.delete(ctJudgment);
        judgmentRepository.delete(nacJudgment);
        
        // assert
        assertEquals(0, judgmentRepository.count());
        
    }
    
    
    @Test
    public void delete_ById() {
        
        // given
        Judgment ccJudgment = testPersistenceObjectFactory.createCcJudgment();
        Judgment scJudgment = testPersistenceObjectFactory.createScJudgment();
        Judgment ctJudgment = testPersistenceObjectFactory.createCtJudgment();
        Judgment nacJudgment = testPersistenceObjectFactory.createNacJudgment();
        
        assertEquals(4, judgmentRepository.count());
        
        
        // execute
        judgmentRepository.delete(ccJudgment.getId());
        judgmentRepository.delete(scJudgment.getId());
        judgmentRepository.delete(ctJudgment.getId());
        judgmentRepository.delete(nacJudgment.getId());
        
        // assert
        assertEquals(0, judgmentRepository.count());
        
    }

    @Test
    public void deleteAll() {
        
        // given
        testPersistenceObjectFactory.createCcJudgment();
        testPersistenceObjectFactory.createScJudgment();
        testPersistenceObjectFactory.createCtJudgment();
        testPersistenceObjectFactory.createNacJudgment();
        
        assertEquals(4, judgmentRepository.count());
        
        
        // execute
        judgmentRepository.deleteAll();
        
        // assert
        assertEquals(0, judgmentRepository.count());
        
    }
    
    @Test
    public void filterIdsToExisting() {
        
        // given
        long judgmentId1 = createCcJudgment(SourceCode.COMMON_COURT, "1", "AA1").getId();
        long judgmentId2 = createCcJudgment(SourceCode.COMMON_COURT, "2", "AA2").getId();
        long notExistingJudgmentId = Math.max(judgmentId1, judgmentId2) + 1;
        
        // execute 
        List<Long> filtered = judgmentRepository.filterIdsToExisting(Lists.newArrayList(judgmentId1, judgmentId2, notExistingJudgmentId));
        
        // assert
        assertThat(filtered, containsInAnyOrder(judgmentId1, judgmentId2));
    }
    
    
    
    @Test
    public void ctJudgment_opinions_should_be_initialized(){
        
        //given
        ConstitutionalTribunalJudgment tribunalJudgment =testPersistenceObjectFactory.createCtJudgment();

        //when
        ConstitutionalTribunalJudgment ctJudgment = judgmentRepository.findOneAndInitialize(tribunalJudgment.getId());

        //then
        ConstitutionalTribunalJudgmentDissentingOpinion actual = ctJudgment.getDissentingOpinions().get(0);
        ConstitutionalTribunalJudgmentDissentingOpinion expected = tribunalJudgment.getDissentingOpinions().get(0);
        assertThat("opinion id ", actual.getId(), is(expected.getId()));
        assertThat("opinion ", actual, is(expected));
    }
    
    @Test
    public void getJudgment_NOT_NULL_TEXT_CONTENT() {
        // given
        Judgment judgment = TestInMemoryObjectFactory.createSimpleNacJudgment();
        
        JudgmentTextContent textContent = new JudgmentTextContent();
        textContent.setRawTextContent("raw content");
        judgment.setTextContent(textContent);
        
        judgmentRepository.save(judgment);
        
        // execute
        Judgment actualJudgment = judgmentRepository.findOneAndInitialize(judgment.getId());
        
        
        // assert
        assertNotNull("judgment text content is null", actualJudgment.getTextContent());
        assertEquals("raw content", actualJudgment.getTextContent().getRawTextContent());
    }
    
    @Test
    public void markAsIndexed() {
        // given
        Judgment judgment1 = createCcJudgment(SourceCode.COMMON_COURT, "1", "AA1");
        waitForTimeChange();
        DateTime beforeIndexed = new DateTime();
        
        // execute
        waitForTimeChange();
        judgmentRepository.markAsIndexed(judgment1.getId());
        waitForTimeChange();
        
        DateTime afterIndexed = new DateTime();
        
        // assert
        Judgment actualJudgment = judgmentRepository.findOne(judgment1.getId());
        assertTrue(actualJudgment.isIndexed());
        assertTrue(actualJudgment.getIndexedDate().isAfter(beforeIndexed));
        assertTrue(actualJudgment.getIndexedDate().isBefore(afterIndexed));
        assertTrue(actualJudgment.getModificationDate().isBefore(beforeIndexed));
        
    }
    
    @Test
    public void markAsNotIndexed() {
        // given
        Judgment judgment1 = createCcJudgment(SourceCode.COMMON_COURT, "1", "AA1");
        Judgment judgment2 = createCcJudgment(SourceCode.COMMON_COURT, "2", "AA2");
        Judgment judgment3 = createCcJudgment(SourceCode.COMMON_COURT, "3", "AA3");

        judgmentRepository.markAsIndexed(judgment1.getId());
        judgmentRepository.markAsIndexed(judgment3.getId());


        // execute
        judgmentRepository.markAsNotIndexed(Lists.newArrayList(judgment1.getId(), judgment2.getId()));

        // assert
        assertFalse(judgmentRepository.findOne(judgment1.getId()).isIndexed());
        assertFalse(judgmentRepository.findOne(judgment2.getId()).isIndexed());
        assertTrue(judgmentRepository.findOne(judgment3.getId()).isIndexed());
    }
    
    @Test
    public void findAllNotIndexed_FOUND() {
        
        // given
        createCcJudgment(SourceCode.COMMON_COURT, "1", "AAA1");
        
        // execute
        Page<Judgment> judgments = judgmentRepository.findAllNotIndexed(new PageRequest(0, 10));
        
        // assert
        assertEquals(1, judgments.getTotalElements());
    }
    
    
    @Test
    public void findAllNotIndexed_NOT_FOUND() {
        
        // given
        CommonCourtJudgment judgment = createCcJudgment(SourceCode.COMMON_COURT, "1", "AAA1");
        judgmentRepository.markAsIndexed(judgment.getId());
        
        // execute
        Page<Judgment> judgments = judgmentRepository.findAllNotIndexed(new PageRequest(0, 10));
        
        // assert
        assertEquals(0, judgments.getTotalElements());
    }
    
    @Test
    public void findAllNotIndexedIds_FOUND() {
        
        // given
        Judgment firstJudgment = createCcJudgment(SourceCode.COMMON_COURT, "1", "AAA1");
        Judgment secondJudgment = createCcJudgment(SourceCode.COMMON_COURT, "2", "AAA2");
        Judgment thirdJudgment = createCcJudgment(SourceCode.COMMON_COURT, "3", "AAA3");
        
        // execute
        List<Long> notIndexed = judgmentRepository.findAllNotIndexedIds();
        
        // assert
        assertThat(notIndexed, containsInAnyOrder(firstJudgment.getId(), secondJudgment.getId(), thirdJudgment.getId()));
    }
    
    @Test
    public void findAllNotIndexedIds_NOT_FOUND() {
        
        // given
        CommonCourtJudgment judgment = createCcJudgment(SourceCode.COMMON_COURT, "1", "AAA1");
        judgmentRepository.markAsIndexed(judgment.getId());
        
        // execute
        List<Long> notIndexed = judgmentRepository.findAllNotIndexedIds();
        
        // assert
        assertEquals(0, notIndexed.size());
    }
    
    
    
    //------------------------ PRIVATE --------------------------
    
    private CommonCourtJudgment createCcJudgment(SourceCode sourceCode, String sourceJudgmentId, String caseNumber) {
        CommonCourtJudgment ccJudgment = new CommonCourtJudgment();
        JudgmentSourceInfo sourceInfo = new JudgmentSourceInfo();
        sourceInfo.setSourceCode(sourceCode);
        sourceInfo.setSourceJudgmentId(sourceJudgmentId);
        ccJudgment.setSourceInfo(sourceInfo);
        ccJudgment.addCourtCase(new CourtCase(caseNumber));
        judgmentRepository.save(ccJudgment);
        return ccJudgment;
    }
    
    private SupremeCourtJudgment createScJudgment(SourceCode sourceCode, String sourceJudgmentId, String caseNumber) {
        SupremeCourtJudgment scJudgment = new SupremeCourtJudgment();
        JudgmentSourceInfo sourceInfo = new JudgmentSourceInfo();
        sourceInfo.setSourceCode(sourceCode);
        sourceInfo.setSourceJudgmentId(sourceJudgmentId);
        scJudgment.setSourceInfo(sourceInfo);
        scJudgment.addCourtCase(new CourtCase(caseNumber));
        judgmentRepository.save(scJudgment);
        return scJudgment;
    }
    
    private void waitForTimeChange() {
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {

        }
    }
}
