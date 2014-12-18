package pl.edu.icm.saos.persistence.model;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import java.util.List;

/**
 * @author madryk
 */
@Entity
public class ConstitutionalTribunalJudgment extends Judgment {

    private List<ConstitutionalTribunalJudgmentDissentingOpinion> dissentingOpinions = Lists.newArrayList();

    
    //------------------------ GETTERS --------------------------
    
    @Transient
    @Override
    public CourtType getCourtType() {
        return CourtType.CONSTITUTIONAL_TRIBUNAL;
    }

    @OneToMany(mappedBy="judgment", orphanRemoval=true, cascade=CascadeType.ALL)
    private List<ConstitutionalTribunalJudgmentDissentingOpinion> getDissentingOpinions_() {
        return dissentingOpinions;
    }
    
    @Transient
    public List<ConstitutionalTribunalJudgmentDissentingOpinion> getDissentingOpinions() {
        return ImmutableList.copyOf(getDissentingOpinions_());
    }
    
    
    //------------------------ LOGIC --------------------------
    
    public void addDissentingOpinion(ConstitutionalTribunalJudgmentDissentingOpinion dissentingOpinion) {
        Preconditions.checkArgument(!containsDissentingOpinion(dissentingOpinion));
        dissentingOpinion.setJudgment(this);
        this.dissentingOpinions.add(dissentingOpinion);
    }

    public boolean containsDissentingOpinion(ConstitutionalTribunalJudgmentDissentingOpinion dissentingOpinion) {
        return this.dissentingOpinions.contains(dissentingOpinion);
    }
    
    public void removeDissentingOpinion(ConstitutionalTribunalJudgmentDissentingOpinion dissentingOpinion) {
        this.dissentingOpinions.remove(dissentingOpinion);
    }

    
    //------------------------ SETTERS --------------------------
    
    @SuppressWarnings("unused") // for hibernate
    private void setDissentingOpinions_(
            List<ConstitutionalTribunalJudgmentDissentingOpinion> dissentingOpinions) {
        this.dissentingOpinions = dissentingOpinions;
    }

    
    
}
