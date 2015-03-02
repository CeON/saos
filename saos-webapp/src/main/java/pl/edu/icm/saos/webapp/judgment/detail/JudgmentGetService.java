package pl.edu.icm.saos.webapp.judgment.detail;

import org.hibernate.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.enrichment.apply.JudgmentEnrichmentService;
import pl.edu.icm.saos.persistence.correction.JudgmentCorrectionRepository;
import pl.edu.icm.saos.persistence.model.Judgment;


/**
 * Service for getting Judgment 
 * 
 * @author Łukasz Pawełczak
 *
 */
@Service
public class JudgmentGetService {

	
	private JudgmentEnrichmentService judgmentEnrichmentService;
	
	
	//------------------------ LOGIC --------------------------
	
	/**
	 * Get Judgment {@link pl.edu.icm.saos.persistence.model.Judgment} by judgmentId.
	 * 
	 * @param judgmentId
	 * @throws ObjectNotFoundException when there is no Judgment with judgmentId
	 * @return Judgment  
	 */
	public Judgment getJudgment(long judgmentId) throws ObjectNotFoundException{
		
		Judgment judgment = judgmentEnrichmentService.findOneAndEnrich(judgmentId);
		
		if (judgment == null) {
			throw new ObjectNotFoundException(String.valueOf(judgmentId), Judgment.class.getName());
		}
		
		return judgment;
	}
	
	//------------------------ SETTERS --------------------------
	
	@Autowired
	public void setJudgmentEnrichmentService(JudgmentEnrichmentService judgmentEnrichmentService) {
		this.judgmentEnrichmentService = judgmentEnrichmentService;
	}
}

