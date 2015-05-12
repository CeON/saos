package pl.edu.icm.saos.webapp.analysis.request.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.persistence.model.CommonCourt;
import pl.edu.icm.saos.persistence.model.CommonCourt.CommonCourtType;
import pl.edu.icm.saos.persistence.model.CourtType;
import pl.edu.icm.saos.persistence.repository.CommonCourtRepository;
import pl.edu.icm.saos.search.analysis.request.XField;
import pl.edu.icm.saos.search.analysis.request.XSettings;
import pl.edu.icm.saos.webapp.analysis.request.JudgmentGlobalFilter;
import pl.edu.icm.saos.webapp.analysis.result.ChartCode;
import pl.edu.icm.saos.webapp.common.search.CourtCriteria;

import com.google.common.base.Preconditions;

/**
 * An {@link XSettingsGenerator} implementation that creates {@link XSettings} for
 * {@link ChartCode#CC_COURT_CHART} charts
 * 
 * @author Łukasz Dumiszewski
 * 
 */@Service("ccCourtChartXSettingsGenerator")
public class CcCourtChartXSettingsGenerator implements XSettingsGenerator {

    
    private CommonCourtRepository commonCourtRepository;
    
    
    
    
    
    //------------------------ LOGIC --------------------------
    
    
    @Override
    public XSettings generateXSettings(JudgmentGlobalFilter judgmentGlobalFilter) {
        
        Preconditions.checkArgument(canGenerateXSettings(judgmentGlobalFilter));
        
        XSettings xsettings = new XSettings();

        
        CourtCriteria courtCriteria = judgmentGlobalFilter.getCourtCriteria();
        
        if (courtCriteria.getCcCourtId() == null) {
        
            xsettings.setField(XField.CC_APPEAL);
            
            return xsettings;
        
        }
        
        
        CommonCourt commonCourt = commonCourtRepository.findOne(courtCriteria.getCcCourtId());
        
        
        if (CommonCourtType.APPEAL == commonCourt.getType()) {
        
            xsettings.setField(XField.CC_REGION);
            xsettings.setFieldValuePrefix(""+commonCourt.getId());
            
            return xsettings;
        }
        
            
        if (CommonCourtType.REGIONAL == commonCourt.getType()) {
        
            xsettings.setField(XField.CC_DISTRICT);
            xsettings.setFieldValuePrefix(""+commonCourt.getId());
            
            return xsettings;
        
        }
        
        
        throw new IllegalStateException("may not generate xsettings");
    
    }
    

    @Override
    public boolean canGenerateXSettings(JudgmentGlobalFilter judgmentGlobalFilter) {
    
        Preconditions.checkNotNull(judgmentGlobalFilter);
        
        CourtCriteria courtCriteria = judgmentGlobalFilter.getCourtCriteria();
        
        if (CourtType.COMMON != courtCriteria.getCourtType()) {
            
            return false;
        
        }
        
        
        if (courtCriteria.getCcCourtId() == null) {
            
            return true;
            
        };
        
        
        if (!courtCriteria.isCcIncludeDependentCourtJudgments()) {
            
            return false;
            
        }
            
            
        
        CommonCourt commonCourt = commonCourtRepository.findOne(courtCriteria.getCcCourtId());
        
        if (CommonCourtType.DISTRICT == commonCourt.getType()) {
        
            return false;
        
        }
        
        return true;
        
    
    }
    
    

    @Override
    public boolean handles(ChartCode chartCode) {
        
        Preconditions.checkNotNull(chartCode);
        
        return chartCode == ChartCode.CC_COURT_CHART;
    }

    
    
    //------------------------ SETTERS --------------------------

    @Autowired
    public void setCommonCourtRepository(CommonCourtRepository commonCourtRepository) {
        this.commonCourtRepository = commonCourtRepository;
    }
    
   
   

}
