package pl.edu.icm.saos.batch.core.importer;

import java.util.List;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.persistence.model.CommonCourt;
import pl.edu.icm.saos.persistence.model.CommonCourt.CommonCourtType;
import pl.edu.icm.saos.persistence.model.CommonCourtDivision;
import pl.edu.icm.saos.persistence.model.CommonCourtDivisionType;
import pl.edu.icm.saos.persistence.repository.CommonCourtRepository;

import com.google.common.collect.Lists;

/**
 * @author Łukasz Dumiszewski
 */
@Service("testCommonCourtsGenerator")
public class TestCommonCourtsGenerator {
    
    
    private static final String[] COURT_CODES = new String[] {"15201000", "15100000", "15050500", "15251000", "15450525", "15100500", "15451025", "15150000", "15551500", "15400000"};
    
    private static final String[] DIVISION_CODES = new String[] {"5027", "2512", "0503", "1521", "1503", "1506", "8027", "4027", "1003"};
    
    
    @Autowired
    private CommonCourtRepository commonCourtRepository;
    
    @Autowired
    private EntityManager entityManager;
    
    
    @Transactional
    public void generateCourts() {
        List<CommonCourtDivisionType> divisionTypes = generateDivisionTypes();
        
        for (String courtCode : COURT_CODES) {
            CommonCourt court = new CommonCourt();
            court.setCode(courtCode);
            if (courtCode.endsWith("0000")) {
                court.setType(CommonCourtType.APPEAL);
                court.setName("Appeal " + RandomStringUtils.randomAlphanumeric(10));
            }
            else if (courtCode.endsWith("00")) {
                court.setType(CommonCourtType.DISTRICT);
                court.setName("District " + RandomStringUtils.randomAlphanumeric(10));
            }
            else {
                court.setType(CommonCourtType.REGIONAL);
                court.setName("Regional " + RandomStringUtils.randomAlphanumeric(10));
            }
            commonCourtRepository.save(court);

                        
            generateDivisions(divisionTypes, court);
            
        }
    }

    @Transactional
    public void deleteCourtDivision(String divisionCode) {
        entityManager.createQuery("delete from " + CommonCourtDivision.class.getName() + " d where d.code = :code").setParameter("code", divisionCode).executeUpdate();
    }
    
    @Transactional
    public void deleteCourt(String courtCode) {
        CommonCourt commonCourt = commonCourtRepository.findOneByCode(courtCode);
        commonCourtRepository.delete(commonCourt);
    }
    
    
    //------------------------ PRIVATE --------------------------

    private void generateDivisions(List<CommonCourtDivisionType> divisionTypes,  CommonCourt court) {
        int i = 0;
        for (String divisionCode : DIVISION_CODES) {
            CommonCourtDivision division = new CommonCourtDivision();
            division.setCourt(court);
            division.setCode("000"+divisionCode);
            division.setName(RandomStringUtils.randomAlphanumeric(10));
            division.setType(divisionTypes.get(i));
            i++;
            entityManager.persist(division);
        }
    }


    private List<CommonCourtDivisionType> generateDivisionTypes() {
        List<CommonCourtDivisionType> divisionTypes = Lists.newArrayList();
        for (int i = 0; i<DIVISION_CODES.length; i++) {
            CommonCourtDivisionType divisionType = new CommonCourtDivisionType();
            divisionType.setCode(i+"CODE");
            divisionType.setName("Division name " + i);
            divisionTypes.add(divisionType);
            entityManager.persist(divisionType);
        }
        return divisionTypes;
    }
    
    
   
}
