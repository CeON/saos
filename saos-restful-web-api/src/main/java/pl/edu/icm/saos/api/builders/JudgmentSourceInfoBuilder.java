package pl.edu.icm.saos.api.builders;

import org.joda.time.DateTime;
import pl.edu.icm.saos.persistence.model.JudgmentSourceInfo;
import pl.edu.icm.saos.persistence.model.SourceCode;

/**
 * @author pavtel
 */
public class JudgmentSourceInfoBuilder extends JudgmentSourceInfo {

    JudgmentSourceInfoBuilder(SourceCode sourceCode) {
        setSourceCode(sourceCode);
    }

    public JudgmentSourceInfoBuilder sourceJudgmentUrl(String sourceJudgmentUrl){
        setSourceJudgmentUrl(sourceJudgmentUrl);
        return this;
    }

    public JudgmentSourceInfoBuilder sourceJudgmentId(String sourceJudgmentId){
        setSourceJudgmentId(sourceJudgmentId);
        return this;
    }

    public JudgmentSourceInfoBuilder publisher(String publisher){
        setPublisher(publisher);
        return this;
    }

    public JudgmentSourceInfoBuilder reviser(String reviser){
        setReviser(reviser);
        return this;
    }

    public JudgmentSourceInfoBuilder publicationDate(DateTime publicationDate){
        setPublicationDate(publicationDate);
        return this;
    }
}
