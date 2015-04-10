package pl.edu.icm.saos.webapp.analysis.request.converter;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.webapp.analysis.result.ChartCode;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

/**
 * A manager of {@link XSettingsGenerator}s
 * @author Łukasz Dumiszewski
 */
@Service("xsettingsGeneratorManager")
public class XSettingsGeneratorManager {

    private List<XSettingsGenerator> xsettingsGenerators = Lists.newArrayList();
    
    
    
    //------------------------ LOGIC --------------------------
    
    
    public XSettingsGenerator getXSettingsGenerator(ChartCode chartCode) {
        
        Preconditions.checkNotNull(chartCode);
        
        for (XSettingsGenerator xsettingsGenerator : xsettingsGenerators) {
            if (xsettingsGenerator.handles(chartCode)) {
                return xsettingsGenerator;
            }
        }
        
        throw new IllegalArgumentException("no " + XSettingsGenerator.class.getName() + " handling " + chartCode.name() + " found");
    }

    
    //------------------------ SETTERS --------------------------
    
    @Autowired
    public void setXsettingsGenerators(List<XSettingsGenerator> xsettingsGenerators) {
        this.xsettingsGenerators = xsettingsGenerators;
    }
    
}
