package pl.edu.icm.saos.importer.commoncourt.court;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.importer.commoncourt.court.XmlCommonCourt.Department;
import pl.edu.icm.saos.persistence.model.CommonCourt;
import pl.edu.icm.saos.persistence.model.CommonCourtDivision;
import pl.edu.icm.saos.persistence.repository.CcDivisionTypeRepository;

import com.google.common.base.Preconditions;

/**
 * @author Łukasz Dumiszewski
 */
@Service
class SourceCommonCourtConverter {

    
    private CcDivisionTypeRepository ccDivisionTypeRepository;
    
    
    /** 
     * 
     * @throws IllegalArgumentException if {@link XmlCommonCourt#getId()} is not valid, see: {@link CommonCourtUtils#isValidCommonCourtCode(String)}
     * @throws NullPointerException if xmlCourt == null
     */
    public CommonCourt convert(XmlCommonCourt xmlCourt) {
        Preconditions.checkNotNull(xmlCourt);
        Preconditions.checkArgument(CommonCourtUtils.isValidCommonCourtCode(xmlCourt.getId()));
        
        CommonCourt court = new CommonCourt();
        court.setCode(xmlCourt.getId());
        court.setName(xmlCourt.getName());
        court.setType(CommonCourtUtils.extractType(court.getCode()));
        
        convertDivisions(xmlCourt, court);
        
        return court;
        
    }

    
    //------------------------ PRIVATE --------------------------
    
    private void convertDivisions(XmlCommonCourt xmlCourt, CommonCourt court) {
        for (Department dept : xmlCourt.getDepartments()) {
            CommonCourtDivision division = new CommonCourtDivision();
            division = new CommonCourtDivision();
            division.setCode(dept.getId());
            division.setName(dept.getName());
            String divisionTypeCode = CommonCourtDivisionUtils.tryExtractDivisionTypeCode(division.getCode());
            division.setType(ccDivisionTypeRepository.findByCode(divisionTypeCode));
            court.addDivision(division);
        }
    }

    
    //------------------------ SETTERS --------------------------
    
    @Autowired
    public void setCcDivisionTypeRepository(CcDivisionTypeRepository ccDivisionTypeRepository) {
        this.ccDivisionTypeRepository = ccDivisionTypeRepository;
    }
    
}
