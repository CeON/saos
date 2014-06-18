package pl.edu.icm.saos.common.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import com.google.common.base.Preconditions;

/**
 * 
 * * <br/><br/>
 * Descripition of court division codes: 
 * https://github.com/CeON/saos/tree/master/saos-persistence/src/main/doc/commonCourtCodes.pdf
 * @author Łukasz Dumiszewski
 */

public class CommonCourtDivisionUtils {


    private CommonCourtDivisionUtils() {
        throw new IllegalStateException("not to instantiate");
    }
    
    
    /**
     * Valid code: 3 or 4-digit string starting
     * The first character for the 3-digit string and the 2 first characters for the 4-digit string must be
     * greater than 0 and divisible by 5
     * 
     */
    public static boolean isValidCcDivisionCode(String divisionCode) {
        boolean isNumeric = StringUtils.isNumeric(divisionCode) && (divisionCode.length() == 3 || divisionCode.length() == 4);
        if (!isNumeric) {
            return false;
        }
        int divisionNumber = NumberUtils.toInt(__extractDivisionNumber(divisionCode));
        if (divisionNumber == 0 || divisionNumber % 5 != 0) {
            return false;
        }
        return true;
        
    }
    

    /**
     * Extract division type code
     * @throws IllegalArgumentException if the passed divisionCode is not valid 
     */
    public static String extractDivisionTypeCode(String divisionCode) {
        Preconditions.checkArgument(isValidCcDivisionCode(divisionCode));
        return divisionCode.substring(divisionCode.length()-2, divisionCode.length());
    }
    
    
    /**
     * Extracts division number
     * @throws IllegalArgumentException if the passed divisionCode is not valid 
     */
    public static String extractDivisionNumber(String divisionCode) {
        Preconditions.checkArgument(isValidCcDivisionCode(divisionCode));
        return __extractDivisionNumber(divisionCode);
    }
    
    /**
     * Returns the division real division number (the division number from divisionCode divided by 5) <br/>
     * Uses {@link #extractDivisionNumber(String)}
     */
    public static int extractNormalizedDivisionNumber(String divisionCode) {
        return NumberUtils.toInt(extractDivisionNumber(divisionCode))/5;
    }
    
    
    private static String __extractDivisionNumber(String divisionCode) {
        return divisionCode.substring(0, divisionCode.length()-2);
    }
   
    
    
}
