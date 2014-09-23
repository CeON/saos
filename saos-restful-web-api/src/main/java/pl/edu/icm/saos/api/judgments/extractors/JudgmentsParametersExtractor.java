package pl.edu.icm.saos.api.judgments.extractors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.edu.icm.saos.api.exceptions.WrongRequestParameterException;
import pl.edu.icm.saos.api.judgments.parameters.JudgmentsParameters;
import pl.edu.icm.saos.api.parameters.ParametersExtractor;
import static pl.edu.icm.saos.api.ApiConstants.*;

/**
 * @author pavtel
 * Extract and validate request parameters.
 */
@Component
public class JudgmentsParametersExtractor {

    //***** fields **********

    @Autowired
    private ParametersExtractor parametersExtractor;

    //***** END fields ********


    //******* business methods ************

    /**
     * Set default values ( if necessary), validate and extract request parameters
     * @param inParameters parameters to process.
     * @return JudgmentsParameters which contains all valid parameters values
     * @throws WrongRequestParameterException
     */
    public JudgmentsParameters extractFrom(InputParametersBuilder inParameters) throws WrongRequestParameterException {
        JudgmentsParameters outParameters = new JudgmentsParameters();

        outParameters.setPagination(
                parametersExtractor.extractAndValidatePagination(inParameters.limit, inParameters.offset)
        );

        if(StringUtils.isNotBlank(inParameters.all)){
            outParameters.setAll(inParameters.all);
        }

        if(StringUtils.isNotBlank(inParameters.courtName)){
            outParameters.setCourtName(inParameters.courtName);
        }

        if(StringUtils.isNotBlank(inParameters.judgeName)){
            outParameters.setJudgeName(inParameters.judgeName);
        }

        if(StringUtils.isNotBlank(inParameters.keyword)){
            outParameters.setKeyword(inParameters.keyword);
        }

        if(StringUtils.isNotBlank(inParameters.legalBase)){
            outParameters.setLegalBase(inParameters.legalBase);
        }

        if(StringUtils.isNotBlank(inParameters.referencedRegulation)){
            outParameters.setReferencedRegulation(inParameters.referencedRegulation);
        }

        outParameters.setJudgmentDateFrom(
                parametersExtractor.extractLocalDate(inParameters.judgmentDateFrom, JUDGMENT_DATE_FROM)
        );

        outParameters.setJudgmentDateTo(
                parametersExtractor.extractLocalDate(inParameters.judgmentDateTo, JUDGMENT_DATE_TO)
        );

        return outParameters;
    }


    //******* END business methods **************


    //******** utils *************

    public static InputParametersBuilder inputParameters(){
        return new InputParametersBuilder();
    }

    public static class InputParametersBuilder {
        private InputParametersBuilder() {
        }

        private int limit;
        private int offset;
        private String all;
        private String courtName;
        private String legalBase;
        private String referencedRegulation;
        private String judgeName;
        private String keyword;
        private String judgmentDateFrom;
        private String judgmentDateTo;

        public InputParametersBuilder limit(int limit){
            this.limit = limit;
            return this;
        }

        public InputParametersBuilder offset(int offset){
            this.offset = offset;
            return this;
        }

        public InputParametersBuilder all(String all){
            this.all = all;
            return this;
        }

        public InputParametersBuilder courtName(String courtName){
            this.courtName = courtName;
            return this;
        }

        public InputParametersBuilder legalBase(String legalBase){
            this.legalBase = legalBase;
            return this;
        }

        public InputParametersBuilder referencedRegulation(String referencedRegulation){
            this.referencedRegulation = referencedRegulation;
            return this;
        }

        public InputParametersBuilder judgeName(String judgeName){
            this.judgeName = judgeName;
            return this;
        }

        public InputParametersBuilder keyword(String keyword){
            this.keyword = keyword;
            return this;
        }

        public InputParametersBuilder judgmentDateFrom(String judgmentDateFrom){
            this.judgmentDateFrom = judgmentDateFrom;
            return this;
        }

        public InputParametersBuilder judgmentDateTo(String judgmentDateTo){
            this.judgmentDateTo = judgmentDateTo;
            return this;
        }

    }

    //******* END utils **********

    //*** setters ***
    public void setParametersExtractor(ParametersExtractor parametersExtractor) {
        this.parametersExtractor = parametersExtractor;
    }
}
