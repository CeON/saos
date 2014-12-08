package pl.edu.icm.saos.webapp.judgment;

import static org.junit.Assert.assertEquals;

import org.joda.time.LocalDate;
import org.junit.Test;

import pl.edu.icm.saos.persistence.model.CourtType;

import com.google.common.collect.Lists;

import pl.edu.icm.saos.search.search.model.JudgmentCriteria;

/**
 * @author Łukasz Pawełczak
 *
 */
public class JudgmentCriteriaFormConverterTest {

	JudgmentCriteriaFormConverter judgmentCriteriaFormConverter = new JudgmentCriteriaFormConverter();
	
	private LocalDate localDate[] = {new LocalDate(2014, 1, 2), new LocalDate(2015, 3, 22)};
	
	
	//------------------------ TESTS --------------------------
	
	@Test
	public void convert() {
		//given
		JudgmentCriteriaForm judgmentCriteriaForm = createCriteriaForm();
		
		
		//when
		JudgmentCriteria judgmentCriteria = judgmentCriteriaFormConverter.convert(judgmentCriteriaForm);
		
		
		//then
		assertEquals(judgmentCriteriaForm.getAll(), judgmentCriteria.getAll());
		assertEquals(judgmentCriteriaForm.getSignature(), judgmentCriteria.getCaseNumber());

		assertEquals(judgmentCriteriaForm.getDateFrom(), judgmentCriteria.getDateFrom());
		assertEquals(judgmentCriteriaForm.getDateTo(), judgmentCriteria.getDateTo());
		
		assertEquals(judgmentCriteriaForm.getCourtType(), judgmentCriteria.getCourtType());
		
		assertEquals(judgmentCriteriaForm.getCommonCourtId(), judgmentCriteria.getCcCourtId());
		assertEquals(judgmentCriteriaForm.getCommonCourtDivisionId(), judgmentCriteria.getCcCourtDivisionId());
		
		assertEquals(judgmentCriteriaForm.getScJudgmentForm(), judgmentCriteria.getScJudgmentForm());
		assertEquals(judgmentCriteriaForm.getSupremeChamberId(), judgmentCriteria.getScCourtChamberId());
		assertEquals(judgmentCriteriaForm.getSupremeChamberDivisionId(), judgmentCriteria.getScCourtChamberDivisionId());
		
		assertEquals(judgmentCriteriaForm.getJudgeName(), judgmentCriteria.getJudgeName());
		assertEquals(1, judgmentCriteria.getKeywords().size());
		assertEquals(judgmentCriteriaForm.getKeywords(), judgmentCriteria.getKeywords());
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
		
		judgmentCriteriaForm.setScJudgmentForm("wyrok SN");
		judgmentCriteriaForm.setSupremeChamberId(13);
		judgmentCriteriaForm.setSupremeChamberDivisionId(14);
		
		judgmentCriteriaForm.setJudgeName("Judge Dredd");
		judgmentCriteriaForm.setKeywords(Lists.newArrayList("very important keyword"));
		judgmentCriteriaForm.setLegalBase("12.55");
		judgmentCriteriaForm.setReferencedRegulation("Art. 4.6");
		
		return judgmentCriteriaForm;
	}

}
