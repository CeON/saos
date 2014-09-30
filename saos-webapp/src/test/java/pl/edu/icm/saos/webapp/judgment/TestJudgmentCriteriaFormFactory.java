package pl.edu.icm.saos.webapp.judgment;

import java.util.Date;

import org.joda.time.LocalDate;

/**
 * @author Łukasz Pawełczak
 *
 */
public final class TestJudgmentCriteriaFormFactory {

	
	@SuppressWarnings("deprecation")
	private static Date date[] = {new Date(114, 0, 2), new Date(115, 2, 22)};
	private static LocalDate localDate[] = {new LocalDate(2014, 1, 2), new LocalDate(2015, 3, 22)};
	
	public static JudgmentCriteriaForm createCriteriaForm() {
		
		JudgmentCriteriaForm judgmentCriteriaForm = new JudgmentCriteriaForm();
		
		judgmentCriteriaForm.setAll("I threw a stone at my neighbor.");
		judgmentCriteriaForm.setSignature("Sig. 1.4");
		
		judgmentCriteriaForm.setDateFrom(localDate[0]);
		judgmentCriteriaForm.setDateTo(localDate[1]);
		
		judgmentCriteriaForm.setCourtId("12");
		judgmentCriteriaForm.setCourtName("Sąd Apelacyjny w Białymstoku");
		
		judgmentCriteriaForm.setJudgeName("Judge Dredd");
		judgmentCriteriaForm.setKeyword("very important keyword");
		judgmentCriteriaForm.setLegalBase("12.55");
		judgmentCriteriaForm.setReferencedRegulation("Art. 4.6");
		
		return judgmentCriteriaForm;
	}
	
	public static Date[] createDate() {
		return date;
	}
	
}
