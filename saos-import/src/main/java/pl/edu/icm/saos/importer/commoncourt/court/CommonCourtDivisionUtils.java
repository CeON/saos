package pl.edu.icm.saos.importer.commoncourt.court;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

/**
 * 
 * * <br/><br/>
 * Descripition of court division codes: 
 * https://github.com/CeON/saos/tree/master/saos-persistence/src/main/doc/commonCourtCodes.pdf
 * @author Łukasz Dumiszewski
 */

class CommonCourtDivisionUtils {

   
    private CommonCourtDivisionUtils() {
        throw new IllegalStateException("not to instantiate");
    }
    
    
    /**
     * Valid code: 3 to 7-digit string
     * The the 4th and 3th character counting from the end, must make a number greater than 0 and divisible by 5
     * 
     */
    public static boolean isStrictlyValidCcDivisionCode(String divisionCode) {
        boolean isNumeric = StringUtils.isNumeric(divisionCode) && (divisionCode.length() >= 3 || divisionCode.length() <= 7);
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
     * Valid code: 3 to 7-digit string
     */
    public static boolean isTolerantlyValidCcDivisionCode(String divisionCode) {
        boolean isNumeric = StringUtils.isNumeric(divisionCode) && (divisionCode.length() >= 3 || divisionCode.length() <= 7);
        if (!isNumeric) {
            return false;
        }
        return true;
        
    }
    
    /**
     * Returns non local type code or null if code cannot be extracted <br/>
     * pl. kod jednostki zamiejscowej 
     */
    public static String tryExtractNonLocalType(String divisionCode) {
        if (divisionCode.length()<=4) {
            return null;
        }
        return divisionCode.substring(divisionCode.length()-5, divisionCode.length()-4);
        
    }
    
    
    public static String tryExtractNonLocalCode(String divisionCode) {
        if (divisionCode.length()<=5) {
            return null;
        }
        return divisionCode.substring(0, divisionCode.length()-5);
    }

    /**
     * Extract division type code or null if the passed divisionCode is not valid 
     */
    public static String tryExtractDivisionTypeCode(String divisionCode) {
        if (!isTolerantlyValidCcDivisionCode(divisionCode)) {
            return null;
        }
        return divisionCode.substring(divisionCode.length()-2, divisionCode.length());
    }
    
    
    /**
     * Extracts division number or null if the passed divisionCode is not valid 
     */
    public static String tryExtractDivisionNumber(String divisionCode) {
        if (!isTolerantlyValidCcDivisionCode(divisionCode)) {
            return null;
        }
        return __extractDivisionNumber(divisionCode);
    }
    
    private static String __extractDivisionNumber(String divisionCode) {
        String normalizedDivisionCode = StringUtils.leftPad(divisionCode, 4, '0');
        return normalizedDivisionCode.substring(normalizedDivisionCode.length()-4, normalizedDivisionCode.length()-2);
    }
   
    
    
}
