package pl.edu.icm.saos.webapp.judgment;

import static org.junit.Assert.assertEquals;

import org.joda.time.LocalDate;
import org.junit.Test;

import pl.edu.icm.saos.search.search.model.CourtType;
import pl.edu.icm.saos.search.search.model.JudgmentCriteria;

/**
 * @author Łukasz Pawełczak
 *
 */
public class JudgmentCriteriaFormConverterTest {

	JudgmentCriteriaFormConverter judgmentCriteriaFormConverter = new JudgmentCriteriaFormConverter();
	
	private LocalDate localDate[] = {new LocalDate(2014, 1, 2), new LocalDate(2015, 3, 22)};
	
	
	//------------------------ LOGIC --------------------------
	
	@Test
	public void convert() {
		JudgmentCriteriaForm judgmentCriteriaForm = createCriteriaForm();
		
		JudgmentCriteria judgmentCriteria = judgmentCriteriaFormConverter.convert(judgmentCriteriaForm);
		
		assertEquals(judgmentCriteriaForm.getAll(), judgmentCriteria.getAll());
		assertEquals(judgmentCriteriaForm.getSignature(), judgmentCriteria.getCaseNumber());

		assertEquals(judgmentCriteriaForm.getDateFrom(), judgmentCriteria.getDateFrom());
		assertEquals(judgmentCriteriaForm.getDateTo(), judgmentCriteria.getDateTo());
		
		assertEquals(judgmentCriteriaForm.getCourtType(), judgmentCriteria.getCourtType());
		
		assertEquals(judgmentCriteriaForm.getCommonCourtId(), judgmentCriteria.getCcCourtId());
		assertEquals(judgmentCriteriaForm.getCommonCourtDivisionId(), judgmentCriteria.getCcCourtDivisionId());
		
		assertEquals(judgmentCriteriaForm.getSupremeChamberId(), judgmentCriteria.getScCourtChamberId());
		assertEquals(judgmentCriteriaForm.getSupremeChamberDivisionId(), judgmentCriteria.getScCourtChamberDivisionId());
		
		assertEquals(judgmentCriteriaForm.getJudgeName(), judgmentCriteria.getJudgeName());
		assertEquals(judgmentCriteriaForm.getKeyword(), judgmentCriteria.getKeyword());
		assertEquals(judgmentCriteriaForm.getLegalBase(), judgmentCriteria.getLegalBase());
		assertEquals(judgmentCriteriaForm.getReferencedRegulation(), judgmentCriteria.getReferencedRegulation());
	}

	
	//------------------------ PRIVATE --------------------------
	
	private JudgmentCriteriaForm createCriteriaForm() {
		
		JudgmentCriteriaForm judgmentCriteriaForm = new JudgmentCriteriaForm();
		
		judgmentCriteriaForm.setAll("I threw a stone at my neighbor.");
		judgmentCriteriaForm.setSignature("Sig. 1.4");
		
		judgmentCriteriaForm.setDateFrom(localDate[0]);
		judgmentCriteriaForm.setDateTo(localDate[1]);
		
		judgmentCriteriaForm.setCourtType(CourtType.COMMON);
		
		judgmentCriteriaForm.setCommonCourtId(12);
		judgmentCriteriaForm.setCommonCourtDivisionId(15);
		
		judgmentCriteriaForm.setSupremeChamberId(13);
		judgmentCriteriaForm.setSupremeChamberDivisionId(14);
		
		judgmentCriteriaForm.setJudgeName("Judge Dredd");
		judgmentCriteriaForm.setKeyword("very important keyword");
		judgmentCriteriaForm.setLegalBase("12.55");
		judgmentCriteriaForm.setReferencedRegulation("Art. 4.6");
		
		return judgmentCriteriaForm;
	}

}
