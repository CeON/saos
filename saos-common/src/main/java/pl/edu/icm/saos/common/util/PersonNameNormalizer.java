package pl.edu.icm.saos.common.util;

import org.apache.commons.lang3.text.WordUtils;

import com.google.common.base.Preconditions;
/**
 * @author Łukasz Dumiszewski
 */

public final class PersonNameNormalizer {
    
    
    
    //------------------------ CONSTRUCTORS --------------------------
    
    private PersonNameNormalizer() {
        throw new IllegalStateException("may not be instantiated");
    }
    
    
    
    //------------------------ LOGIC --------------------------
    
    /**
     * Normalizes the given firstLastName, i.e. removes white-spaces, capitalize first letters (using Locale.ROOT) and removes
     * non-alphabetic characters <br/><br/>
     * 
     * <pre>
     * normalize("%, Jan \t kowalski") -> "Jan Kowalski"
     * </pre>  
     */
    public static String normalize(String firstLastName) {
        
        Preconditions.checkNotNull(firstLastName);
        
        
        firstLastName = unify(firstLastName);
        
        firstLastName = removeNonAlphabetic(firstLastName);
        
        firstLastName = StringTools.squashAndTrim(firstLastName); // after removing nonAlphabetic characters there can be new long spaces
        

        return capitalizeFirstLetters(firstLastName);
        
    }


    /**
     * Returns the given first last name unified. Performs the given operations:
     * <ul>
     * <li>{@link StringTools#toRootLowerCase(String)}</li>
     * <li>{@link StringTools#squashAndTrimm(String)}</li>
     * <li>{@link #replaceLongDashWithShort(String)}</li>
     * </ul>
    */
    public static String unify(String firstLastName) {
        
        firstLastName = StringTools.toRootLowerCase(firstLastName);
        
        firstLastName = StringTools.squashAndTrim(firstLastName);
        
        firstLastName = replaceLongDashWithShort(firstLastName);
        
        return firstLastName;
        
    }
    
    
    //------------------------ PRIVATE --------------------------
    
    private static String removeNonAlphabetic(String value) {
        return value.replaceAll("[^\\p{L} \\-\\.]+", "");
    }
    
    private static String capitalizeFirstLetters(String value) {
        value = WordUtils.capitalize(value);
        value = WordUtils.capitalize(value, '-');
        value.replace(" Von ", " von ");
        return value;
    }
    
    private static String replaceLongDashWithShort(String value) {
        return value.replaceAll("–", "-").replaceAll("\\s*\\-\\s*", "-");
    }
    
    
}
