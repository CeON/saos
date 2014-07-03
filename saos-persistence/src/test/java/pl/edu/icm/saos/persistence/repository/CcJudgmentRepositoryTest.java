package pl.edu.icm.saos.persistence.repository;

import javax.transaction.Transactional;

import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import pl.edu.icm.saos.persistence.PersistenceTestSupport;
import pl.edu.icm.saos.persistence.model.CommonCourtJudgment;

/**
 * @author Łukasz Dumiszewski
 */

public class CcJudgmentRepositoryTest extends PersistenceTestSupport {

    @Autowired
    private CcJudgmentRepository ccJudgmentRepository;
    
    @Test
    @Transactional
    public void getMaxSourcePublicationDate() {
        CommonCourtJudgment ccJudgment20121221_2112 = createCcJudgment(new LocalDateTime(2012, 12, 21, 21, 12, 13).toDateTime());
        CommonCourtJudgment ccJudgment20131101_2311 = createCcJudgment(new LocalDateTime(2013, 11, 1, 23, 11, 12).toDateTime());
        CommonCourtJudgment ccJudgment20131101_2312 = createCcJudgment(new LocalDateTime(2013, 11, 1, 23, 12, 12).toDateTime());
        
        getDbUtils().persist(ccJudgment20121221_2112, ccJudgment20131101_2311, ccJudgment20131101_2312);
        
        Assert.assertEquals(ccJudgment20131101_2312.getJudgmentSource().getPublicationDate(), ccJudgmentRepository.getMaxSourcePublicationDate());
    }
    
    
    //------------------------ PRIVATE --------------------------
    
    private CommonCourtJudgment createCcJudgment(DateTime sourcePublicationDate) {
        CommonCourtJudgment ccJudgment = new CommonCourtJudgment();
        ccJudgment.getJudgmentSource().setPublicationDate(sourcePublicationDate);
        return ccJudgment;
    }
    
}
