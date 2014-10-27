package pl.edu.icm.saos.webapp.judgment;

import org.joda.time.LocalDate;

/**
 * @author Łukasz Pawełczak
 *
 */
public final class TestJudgmentCriteriaFormFactory {


	private static LocalDate localDate[] = {new LocalDate(2014, 1, 2), new LocalDate(2015, 3, 22)};
	
	public static JudgmentCriteriaForm createCriteriaForm() {
		
		JudgmentCriteriaForm judgmentCriteriaForm = new JudgmentCriteriaForm();
		
		judgmentCriteriaForm.setAll("I threw a stone at my neighbor.");
		judgmentCriteriaForm.setSignature("Sig. 1.4");
		
		judgmentCriteriaForm.setDateFrom(localDate[0]);
		judgmentCriteriaForm.setDateTo(localDate[1]);
		
		judgmentCriteriaForm.setCommonCourtId("12");
		judgmentCriteriaForm.setCommonCourtName("Sąd Apelacyjny w Białymstoku");
		
		judgmentCriteriaForm.setSupremeChamberId("13");
		judgmentCriteriaForm.setSupremeChamberDivisionId("14");
		
		judgmentCriteriaForm.setJudgeName("Judge Dredd");
		judgmentCriteriaForm.setKeyword("very important keyword");
		judgmentCriteriaForm.setLegalBase("12.55");
		judgmentCriteriaForm.setReferencedRegulation("Art. 4.6");
		
		return judgmentCriteriaForm;
	}

}
