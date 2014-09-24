package pl.edu.icm.saos.webapp.judgment;

import java.util.Date;

import org.joda.time.LocalDate;

/**
 * @author Łukasz Pawełczak
 *
 */
public final class TestJudgmentCriteriaFormFactory {

	
	@SuppressWarnings("deprecation")
	private static Date date = new Date(114, 0, 2);
	private static LocalDate localDate = new LocalDate(2014, 1, 2);
	
	public static JudgmentCriteriaForm createCriteriaForm() {
		
		JudgmentCriteriaForm judgmentCriteriaForm = new JudgmentCriteriaForm();
		
		judgmentCriteriaForm.setAll("I threw a stone at my neighbor.");
		judgmentCriteriaForm.setSignature("Sig. 1.4");
		
		judgmentCriteriaForm.setDateFrom(localDate);
		judgmentCriteriaForm.setDateTo(localDate);
		
		judgmentCriteriaForm.setCourtId("12");
		judgmentCriteriaForm.setCourtName("Sąd Apelacyjny w Białymstoku");
		
		judgmentCriteriaForm.setJudgeName("Judge Dredd");
		judgmentCriteriaForm.setKeyword("very important keyword");
		judgmentCriteriaForm.setLegalBase("12.55");
		judgmentCriteriaForm.setReferencedRegulation("Art. 4.6");
		
		return judgmentCriteriaForm;
	}
	
	public static Date createDate() {
		return date;
	}
	
}
