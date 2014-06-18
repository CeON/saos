package pl.edu.icm.saos.persistence.repository;

import pl.edu.icm.saos.common.util.CommonCourtUtils;
import pl.edu.icm.saos.persistence.model.CommonCourt;

/**
 * @author Łukasz Dumiszewski
 */

public interface CommonCourtRepositoryCustom {

    /**
     * Finds court by the given code 
     * @param courtCode 8-digit court code: 15 + {@link CommonCourt#getAppealCourtCode()} + {@link CommonCourt#getRegionalCourtCode()} +{@link CommonCourt#getDistrictCourtCode()}
     * @throws IllegalArgumentException if the passed courtCode is not 8-digit string
     * @see CommonCourtUtils#isValidCommonCourtCode(String)
     */
    CommonCourt getOneByCode(String courtCode) throws IllegalArgumentException;
    
}
