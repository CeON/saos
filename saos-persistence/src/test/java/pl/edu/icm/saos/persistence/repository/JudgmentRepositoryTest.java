package pl.edu.icm.saos.persistence.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.hibernate.LazyInitializationException;
import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import pl.edu.icm.saos.common.testcommon.category.SlowTest;
import pl.edu.icm.saos.persistence.PersistenceTestSupport;
import pl.edu.icm.saos.persistence.common.TestJudgmentFactory;
import pl.edu.icm.saos.persistence.model.CommonCourtJudgment;
import pl.edu.icm.saos.persistence.model.CourtCase;
import pl.edu.icm.saos.persistence.model.Judgment;
import pl.edu.icm.saos.persistence.model.JudgmentSourceInfo;
import pl.edu.icm.saos.persistence.model.SourceCode;
import pl.edu.icm.saos.persistence.model.SupremeCourtJudgment;

import com.google.common.collect.Lists;

/**
 * @author Łukasz Dumiszewski
 */
@Category(SlowTest.class)
public class JudgmentRepositoryTest extends PersistenceTestSupport {

    private static Logger log = LoggerFactory.getLogger(JudgmentRepositoryTest.class);
    
    @Autowired
    private JudgmentRepository judgmentRepository;
    
    @Autowired
    private TestJudgmentFactory testJudgmentFactory;
    
    
    
    
    @Test
    public void testSaveAndGet() {
        Assert.assertEquals(0, judgmentRepository.count());
        
        CommonCourtJudgment judgment = new CommonCourtJudgment();
        judgment.addCourtCase(new CourtCase("222"));
        judgmentRepository.save(judgment);
        
        Assert.assertEquals(1, judgmentRepository.count());
    }
    
    @Test
    public void findOneBySourceCodeAndSourceJudgmentId_NOT_FOUND() {
        log.info("======= findOneBySourceCodeAndSourceJudgmentId");
        Judgment ccJudgment = judgmentRepository.findOneBySourceCodeAndSourceJudgmentId(SourceCode.COMMON_COURT, "111122");
        assertNull(ccJudgment);
        log.info("======= END OF findOneBySourceCodeAndSourceJudgmentId");
        
    }
    
    @Test
    public void findOneBySourceCodeAndSourceJudgmentId_FOUND() {
        CommonCourtJudgment ccJudgment = TestJudgmentFactory.createSimpleCcJudgment();
        JudgmentSourceInfo sourceInfo = new JudgmentSourceInfo();
        sourceInfo.setSourceCode(SourceCode.COMMON_COURT);
        sourceInfo.setSourceJudgmentId("1123");
        ccJudgment.setSourceInfo(sourceInfo);
        judgmentRepository.save(ccJudgment);
        Judgment dbCcJudgment = judgmentRepository.findOneBySourceCodeAndSourceJudgmentId(SourceCode.COMMON_COURT, "1123");
        assertNotNull(dbCcJudgment);
        assertEquals(ccJudgment.getId(), dbCcJudgment.getId());
    }
    
    
    @Test
    public void findBySourceCodeAndCaseNumber_NOT_FOUND() {
        List<Judgment> ccJudgments = judgmentRepository.findBySourceCodeAndCaseNumber(SourceCode.COMMON_COURT, "111122");
        assertEquals(0, ccJudgments.size());
    }
    
    @Test
    public void findBySourceCodeAndCaseNumber_FOUND() {
        CommonCourtJudgment ccJudgment1 = createCcJudgment(SourceCode.COMMON_COURT, "1", "AAA1");
        createCcJudgment(SourceCode.COMMON_COURT, "2", "AAA2");
        createCcJudgment(SourceCode.COMMON_COURT, "3", "AAA3");
        createCcJudgment(SourceCode.ADMINISTRATIVE_COURT, "1", "AAA1");
        
        List<Judgment> ccJudgments = judgmentRepository.findBySourceCodeAndCaseNumber(SourceCode.COMMON_COURT, ccJudgment1.getCaseNumbers().get(0));
        assertEquals(1, ccJudgments.size());
        assertEquals(ccJudgment1.getId(), ccJudgments.get(0).getId());
      
    }
    
    @Test
    public void findBySourceCodeAndCaseNumber_FOUND_MultiCases() {
        CommonCourtJudgment ccJudgment1 = createCcJudgment(SourceCode.COMMON_COURT, "1", "AAA1");
        createCcJudgment(SourceCode.COMMON_COURT, "2", "AAA2");
        createCcJudgment(SourceCode.COMMON_COURT, "3", "AAA3");
        createCcJudgment(SourceCode.ADMINISTRATIVE_COURT, "1", "AAA1");
        
        ccJudgment1.addCourtCase(new CourtCase("BBB1"));
        
        List<Judgment> ccJudgments = judgmentRepository.findBySourceCodeAndCaseNumber(SourceCode.COMMON_COURT, ccJudgment1.getCaseNumbers().get(0));
        assertEquals(1, ccJudgments.size());
        assertEquals(ccJudgment1.getId(), ccJudgments.get(0).getId());
      
    }
    
    @Test
    public void findOneAndInitialize_Judgment() {
        Judgment ccJudgment = testJudgmentFactory.createFullCcJudgment(true);
        Judgment dbJudgment = judgmentRepository.findOneAndInitialize(ccJudgment.getId());
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
        Judgment ccJudgment = testJudgmentFactory.createFullCcJudgment(true);
        CommonCourtJudgment dbJudgment = judgmentRepository.findOneAndInitialize(ccJudgment.getId());
        assertNotNull(dbJudgment);
        dbJudgment.getCourtDivision().getCourt().getCode();
        dbJudgment.getKeywords().size();
    }


    @Test
    public void findOneAndInitialize_SupremeCourtJudgment() {
        Judgment scJudgment = testJudgmentFactory.createFullScJudgment(true);
        SupremeCourtJudgment dbJudgment = judgmentRepository.findOneAndInitialize(scJudgment.getId());
        assertNotNull(dbJudgment);
        dbJudgment.getScChamberDivision().getName();
        dbJudgment.getScChambers().size();
        dbJudgment.getScJudgmentForm().getName();
    }

    
    @Test(expected=LazyInitializationException.class)
    public void findOne_Uninitialized() {
        Judgment ccJudgment = testJudgmentFactory.createFullCcJudgment(true);
        Judgment dbJudgment = judgmentRepository.findOne(ccJudgment.getId());
        assertNotNull(dbJudgment);
        dbJudgment.getJudges().size();
    }
    
    @Test
    public void findAllToIndex_FOUND() {
        createCcJudgment(SourceCode.COMMON_COURT, "1", "AAA1");
        
        Page<Judgment> judgments = judgmentRepository.findAllNotIndexed(new PageRequest(0, 10));
        
        assertEquals(1, judgments.getTotalElements());
    }
    
    @Test
    public void findAllToIndex_NOT_FOUND() {
        CommonCourtJudgment judgment = createCcJudgment(SourceCode.COMMON_COURT, "1", "AAA1");
        judgment.markAsIndexed();
        judgmentRepository.save(judgment);
        
        Page<Judgment> judgments = judgmentRepository.findAllNotIndexed(new PageRequest(0, 10));
        
        assertEquals(0, judgments.getTotalElements());
    }
    
    @Test
    public void findAllToIndex_CHECK_INDEXED_DATE() {
        CommonCourtJudgment judgment = createCcJudgment(SourceCode.COMMON_COURT, "1", "AAA1");
        
        DateTime beforeIndexed = new DateTime();
        waitForTimeChange();
        
        judgment.markAsIndexed();
        judgmentRepository.save(judgment);
        
        waitForTimeChange();
        DateTime afterIndexed = new DateTime();
        
        Judgment actualJudgment = judgmentRepository.findOne(judgment.getId());
        
        assertNotNull(actualJudgment);
        assertNotNull(actualJudgment.getIndexedDate());
        assertTrue(beforeIndexed.isBefore(actualJudgment.getIndexedDate()));
        assertTrue(afterIndexed.isAfter(actualJudgment.getCreationDate()));
    }
    
    @Test
    public void findAllNotIndexedIds_FOUND() {
        createCcJudgment(SourceCode.COMMON_COURT, "1", "AAA1");
        
        List<Integer> notIndexed = judgmentRepository.findAllNotIndexedIds();
        
        assertEquals(1, notIndexed.size());
    }
    
    @Test
    public void findAllNotIndexedIds_NOT_FOUND() {
        CommonCourtJudgment judgment = createCcJudgment(SourceCode.COMMON_COURT, "1", "AAA1");
        judgment.markAsIndexed();
        judgmentRepository.save(judgment);
        
        List<Integer> notIndexed = judgmentRepository.findAllNotIndexedIds();
        
        assertEquals(0, notIndexed.size());
    }

    @Test
    public void findOne_it_should_return_correct_modification_date_value(){
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
    public void delete_JudgmentIds() {
        Judgment ccJudgment = testJudgmentFactory.createFullCcJudgment(true);
        Judgment scJudgment = testJudgmentFactory.createFullScJudgment(true);
        
        judgmentRepository.delete(Lists.newArrayList(ccJudgment.getId(), scJudgment.getId()));
        
        
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
    
    private void waitForTimeChange() {
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {

        }
    }
}
