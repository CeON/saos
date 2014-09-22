package pl.edu.icm.saos.webapp.judgment;

import java.util.Date;

import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.search.model.JudgmentCriteria;

/**
 * @author Łukasz Pawełczak
 *
 */
@Service
public class JudgmentCriteriaFormConverter {

	public JudgmentCriteria convert(JudgmentCriteriaForm element) {
		JudgmentCriteria judgmentCriteria = new JudgmentCriteria();
		
		judgmentCriteria.setAll(element.getAll());
		judgmentCriteria.setSignature(element.getSignature());
		judgmentCriteria.setCourtId(element.getCourtId());
		judgmentCriteria.setCourtName(element.getCourtName());
		judgmentCriteria.setJudgeName(element.getJudgeName());
		judgmentCriteria.setKeyword(element.getKeyword());
		judgmentCriteria.setLegalBase(element.getLegalBase());
		judgmentCriteria.setReferencedRegulation(element.getReferencedRegulation());

		LocalDate dateFrom = element.getDateFrom();
		LocalDate dateTo = element.getDateTo();
		
		
		if (dateFrom != null) {
			judgmentCriteria.setDateFrom(convertDate(dateFrom));
		}
		
		if (dateTo != null) {
			judgmentCriteria.setDateTo(convertDate(dateTo));
		}
		
		return judgmentCriteria;
	}
	
	private Date convertDate(LocalDate localDate) {
		//localDate.toDateTimeAtCurrentTime(zone)
		return localDate.toDateTimeAtStartOfDay(DateTimeZone.getDefault()).toDate();
	}
	
	
}
