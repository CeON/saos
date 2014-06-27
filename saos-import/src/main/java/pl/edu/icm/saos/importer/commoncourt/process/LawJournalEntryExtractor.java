package pl.edu.icm.saos.importer.commoncourt.process;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Service;

import com.google.common.base.Preconditions;

/**
 * @author Łukasz Dumiszewski
 */
@Service("lawJournalEntryExtractor")
public class LawJournalEntryExtractor {

    private static final String TITLE_YEAR_SEPARATOR = "###YEAR###";
    
    
    
    /** 
     * Extracts law journal entry data from string of a form like this: <br/>
     * Ustawa z dnia 29 sierpnia 1997 r. o usługach turystycznych (Dz. U. z 1997 r. Nr 133, poz. 884 - art. 11 a; art. 11 a ust. 1; art. 14; art. 14 ust. 6; art. 14 ust. 7)
     * 
     * @returns null if the year or number or entry of the journal cannot be found in the given string
     */
    public LawJournalEntryData extractLawJournalEntry(String lawJournalEntryString) {
        Preconditions.checkNotNull(lawJournalEntryString);
        
        String title = extractTitle(lawJournalEntryString);
        
        if (title == null) {
            return null;
        }
        
        String yearNumberEntryPart = extractYearNumberEntryPart(lawJournalEntryString);
        
        if (StringUtils.isBlank(yearNumberEntryPart)) {
            return null;
        }
        
        
        Integer year = extractYear(yearNumberEntryPart);
        Integer number = extractNumber(yearNumberEntryPart);
        Integer entry = extractEntry(yearNumberEntryPart);
        
        if (year == null || number == null || entry == null) {
            return null;
        }
        
        return new LawJournalEntryData(year, number, entry, title);
        
    }


    
    
    
    //------------------------ PRIVATE --------------------------

    private Integer extractYear(String yearNumberEntryPart) {
        Pattern p = Pattern.compile("^([1|2][0-9]{3}\\s*r)");
        Matcher m = p.matcher(yearNumberEntryPart);
        return findNumber(m);
    }

    private Integer extractNumber(String yearNumberEntryPart) {
        Pattern p = Pattern.compile("[N|n][R|r]\\s*[0-9]+");
        Matcher m = p.matcher(yearNumberEntryPart);
        return findNumber(m);
    }

    private Integer extractEntry(String yearNumberEntryPart) {
        Pattern p = Pattern.compile("[P|p][O|o][Z|z]\\s*\\.*\\s*[0-9]+");
        Matcher m = p.matcher(yearNumberEntryPart);
        return findNumber(m);
    }

    private Integer findNumber(Matcher m) {
        if (m.find()) {
            String number = m.group().replaceAll("\\D", "");
            return NumberUtils.toInt(number);
        }
        return null;
    }
    
    private String extractTitle(String entry) {
        return extractPart(entry, 0);
    }

    private String extractYearNumberEntryPart(String entry) {
        return extractPart(entry, 1);
    }

    private static String extractPart(String entry, int partNo) {
        String[] titleYearParts = normalizeTitleYearSeparator(entry).split(TITLE_YEAR_SEPARATOR);
        if (titleYearParts.length != 2) {
            return null;
        }
        return StringUtils.trim(titleYearParts[partNo]);
    }
    
    
    private static String normalizeTitleYearSeparator(String entry) {
        String changedEntry = entry.replaceAll("\\s*\\(\\s*[D|d][z|Z]\\.\\s*[u|U]\\.\\s*z\\s*", TITLE_YEAR_SEPARATOR);
        return changedEntry;
    }
    
   
    
}
