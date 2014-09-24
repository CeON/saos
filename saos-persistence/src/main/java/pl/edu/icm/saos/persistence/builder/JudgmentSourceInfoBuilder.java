package pl.edu.icm.saos.persistence.builder;

import org.joda.time.DateTime;
import pl.edu.icm.saos.persistence.model.JudgmentSourceInfo;
import pl.edu.icm.saos.persistence.model.SourceCode;

/**
 * @author pavtel
 * Simplified {@link pl.edu.icm.saos.persistence.model.JudgmentSourceInfo JudgmentSourceInfo} creation.
 * Do not use it in conjugation with persistence's repositories.
 */
public class JudgmentSourceInfoBuilder{

    private JudgmentSourceInfo element;

    JudgmentSourceInfoBuilder(SourceCode sourceCode) {
        element = new JudgmentSourceInfo();
        element.setSourceCode(sourceCode);
    }

    public JudgmentSourceInfoBuilder sourceJudgmentUrl(String sourceJudgmentUrl){
        element.setSourceJudgmentUrl(sourceJudgmentUrl);
        return this;
    }

    public JudgmentSourceInfoBuilder sourceJudgmentId(String sourceJudgmentId){
        element.setSourceJudgmentId(sourceJudgmentId);
        return this;
    }

    public JudgmentSourceInfoBuilder publisher(String publisher){
        element.setPublisher(publisher);
        return this;
    }

    public JudgmentSourceInfoBuilder reviser(String reviser){
        element.setReviser(reviser);
        return this;
    }

    public JudgmentSourceInfoBuilder publicationDate(DateTime publicationDate){
        element.setPublicationDate(publicationDate);
        return this;
    }

    public JudgmentSourceInfo build(){
        return element;
    }
}
