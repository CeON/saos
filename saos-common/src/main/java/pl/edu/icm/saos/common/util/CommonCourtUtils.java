package pl.edu.icm.saos.common.util;

import org.apache.commons.lang3.StringUtils;

import com.google.common.base.Preconditions;

/**
 * 
 * * <br/><br/>
 * Court codes based on: 
 * https://github.com/CeON/saos/tree/master/saos-persistence/src/main/doc/commonCourtCodes.pdf
 
 * 
 * @author Łukasz Dumiszewski
 */

public final class CommonCourtUtils {

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
    
    
}
