package pl.edu.icm.saos.importer.common.converter;

import java.util.regex.Pattern;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.common.util.PersonNameNormalizer;
import pl.edu.icm.saos.common.util.StringTools;

/**
 * Service that normalizes (corrects) imported judge names.
 * 
 * @author Łukasz Dumiszewski
 */
@Service("judgeNameNormalizer")
public class JudgeNameNormalizer {

     private final String[] DEFAULT_PARTS_TO_REMOVE = new String[] {
            "sędzia",
            "sędziowie",
            "sądu",
            "rejonowego",
            "okręgowego",
            "st. sekr.",
            "sekr",
            "sądowy",
            "prof", "prof.",
            "sa", "sa.", 
            "do",
            "spr", "spr.", 
            "wso",
            "w",
            "gdańsku",
            "słupsku",
            "toruniu",
            "sw",
            "so",
            "sprawozdawca",
            "p.o.",
            "sso",
            "s.s.r", "s.s.r.",
            "spraw", "spraw.",
            "delegowana",
            "delegowany",
            "sn",
            "swsg",
            "gen", "gen.",
            "płk",
            "ppłk",
            "sr",
            "del", "del."
            
     };
    
     private String[] partsToRemove = DEFAULT_PARTS_TO_REMOVE;
     
     
     private String[] DEFAULT_REGEXES_TO_REMOVE = new String[] {
             "\\-(spr)\\s*$",
             "\\-(sprawozdawca)\\s*$",
             "^\\s*w\\s+w\\s+",
             "^\\s*(sa)\\s*\\-",
      };

     
     private String[] regexesToRemove = DEFAULT_REGEXES_TO_REMOVE;
    
    
     private String[] allRegexesToRemove = null;
    
    
    
    //------------------------ LOGIC --------------------------
    
    /**
     * Normalizes the given judgeName, i.e. removes whitespaces, capitalize first letters, and/or removes
     * parts of the name that seem to be a mistake (see: {@link #setRegexesToRemove(String[])} and {@link #setPartsToRemove(String[])})<br/>
     * <br/>
     * Uses {@link PersonNameNormalizer#normalize(String)} internally
     * <br/>
     * <pre>
     * normalize("Sędzia Jan Kowalski") -> "Jan Kowalski"
     * normalize("gen. Jan Kowalski ()") -> "Jan Kowalski"
     * normalize("SA- Jan Kowalski-Bzyk ()") -> "Jan Kowalski-Bzyk"
     * Assumption: sędzia, gen and sa are defined in {@link #setPartsToRemove(String[])}
     * </pre>  
     */
    public String normalize(String judgeName) {
        
        if (StringUtils.isBlank(judgeName)) {
            return "";
        }
        
        judgeName = PersonNameNormalizer.normalize(judgeName);
        
        judgeName = StringTools.toRootLowerCase(judgeName);
        
        
        judgeName = removeDefinedParts(judgeName);
        
        judgeName = removeTrailingDashes(judgeName);
        
        judgeName = removeTrailingDots(judgeName);
        
        
        return PersonNameNormalizer.normalize(judgeName);
        
    }

    
    
    @PostConstruct
    public void postConstruct() {
        
        generatePartToRemoveRegexes();
    
    }


   
    
    //------------------------ PRIVATE --------------------------
    
    
    private void generatePartToRemoveRegexes() {
        
        allRegexesToRemove = new String[partsToRemove.length + regexesToRemove.length];
        
        int j=0;
        for (j=0; j < regexesToRemove.length; j++) {
            allRegexesToRemove[j] = regexesToRemove[j];
        }
        
        for (int i=0; i < partsToRemove.length; i++) {
            
            String partToRemove = Pattern.quote(partsToRemove[i]);
            
            String beginningPart = "^\\s*("+partToRemove+")"+"\\s+";
            String middlePart = "\\s+("+partToRemove+")\\s+";
            String endPart = "\\s+("+partToRemove+")\\s*$";
            
            allRegexesToRemove[j+i] = beginningPart+"|"+middlePart+"|"+endPart;
            
        }
        
        
    }
    
    
    private String removeDefinedParts(String judgeName) {
        
        if (allRegexesToRemove == null || allRegexesToRemove.length==0) {
            throw new IllegalStateException("no parsts or regexes to remove defined");
        }
        
        
        for (String partToRemoveRegex : allRegexesToRemove) {
        
            judgeName = judgeName.replaceAll(partToRemoveRegex, " ");
        
        }
        
        return judgeName;
        
    }



    private String removeTrailingDashes(String judgeName) {
        return judgeName.replaceAll("^(\\s*\\-)|(\\-\\s*)$", "");
    }

    private String removeTrailingDots(String judgeName) {
        return judgeName.replaceAll("^(\\s*\\.)|(\\.\\s*)$", "");
    }
    
   
    //------------------------ SETTERS --------------------------
    
    /**
     * Parts of a name that should be removed from judge names.<br/>
     * Appropriate regexes will be generated from these parts internally by adding white-spaces and/or beginning and end 
     * characters and these regexes will be used later in the removal process:<br/>
     * regexToRemove =  "^\\s("+partToRemove+")"+"\\s+|\\s+("+partToRemove+")\\s+|\\s+("+partToRemove)+"\\s*$;
     * <br/><br/>
     * 
     * The case of characters doesn't matter. The non-alphabetic characters mustn't be included (they are removed
     * from the name of a judge before removing the given parts from it). You should however include dots [.] and/or
     * dashes [-] if appropriate. <br/><br/>
     * 
   
     * Don't set this field if you want to use default values (see: {@link #DEFAULT_PARTS_TO_REMOVE} <br/>
     * 
     */
    public void setPartsToRemove(String[] partsToRemove) {
        this.partsToRemove = partsToRemove;
    }


    /**
     * Sets the strict regexes that should be removed from judge names.<br/>
     * You don't have to set this field if you want to use default values (see: {@link #DEFAULT_REGEXES_TO_REMOVE). <br/>
     */
    public void setRegexesToRemove(String[] regexesToRemove) {
        this.regexesToRemove = regexesToRemove;
    }
       
    
}
