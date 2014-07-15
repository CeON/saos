package pl.edu.icm.saos.importer.commoncourt.court;

import org.apache.commons.lang3.StringUtils;

import pl.edu.icm.saos.persistence.model.CommonCourt.CommonCourtType;

import com.google.common.base.Preconditions;

/**
 * 
 * * <br/><br/>
 * Court codes based on: 
 * https://github.com/CeON/saos/tree/master/saos-persistence/src/main/doc/commonCourtCodes.pdf
 
 * 
 * @author Łukasz Dumiszewski
 */

final class CommonCourtUtils {

    private CommonCourtUtils() {
        throw new IllegalStateException("not to instantiate");
    }

    
    /**
     * Valid code: 8-digit string starting with 15
     */
    public static boolean isValidCommonCourtCode(String courtCode) {
        return StringUtils.isNumeric(courtCode) && courtCode.length() == 8 && courtCode.startsWith("15");
    }
    
    
    /**
     * Extracts appeal court number part from the given courtCode
     * @throws IllegalArgumentException if the passed courtCode is not valid 
     */
    public static String extractAppealCourtNumber(String courtCode) {
        Preconditions.checkArgument(isValidCommonCourtCode(courtCode));
        return courtCode.substring(2, 4);
    }
    
    
    /**
     * Extracts regional court number part from the given courtCode
     * @throws IllegalArgumentException if the passed courtCode is not valid 
     */
    public static String extractRegionalCourtNumber(String courtCode) {
        Preconditions.checkArgument(isValidCommonCourtCode(courtCode));
        return courtCode.substring(4, 6);
    }
    
    
    /**
     * Extracts district court number part from the given courtCode
     * @throws IllegalArgumentException if the passed courtCode is not valid 
     */
    public static String extractDistrictCourtNumber(String courtCode) {
        Preconditions.checkArgument(isValidCommonCourtCode(courtCode));
        return courtCode.substring(6, 8);
    }
    
    
    public static CommonCourtType extractType(String courtCode) {
        String regionalCode = extractRegionalCourtNumber(courtCode);
        String districtCode = extractDistrictCourtNumber(courtCode);
        if (regionalCode.equals("00")) {
            return CommonCourtType.APPEAL;
        }
        if (districtCode.equals("00")) {
            return CommonCourtType.REGIONAL;
        }
        return CommonCourtType.DISTRICT;
    }
    
}
