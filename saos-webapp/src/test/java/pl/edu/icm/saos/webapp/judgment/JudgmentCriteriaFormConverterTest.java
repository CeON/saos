package pl.edu.icm.saos.webapp.judgment;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.hamcrest.Matchers;
import org.joda.time.LocalDate;
import org.junit.Test;

import pl.edu.icm.saos.persistence.model.CourtType;
import pl.edu.icm.saos.persistence.model.Judgment.JudgmentType;
import pl.edu.icm.saos.persistence.model.SupremeCourtJudgment.PersonnelType;
import pl.edu.icm.saos.search.search.model.JudgmentCriteria;
import pl.edu.icm.saos.webapp.common.search.CourtCriteria;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

/**
 * @author Łukasz Pawełczak
 *
 */
public class JudgmentCriteriaFormConverterTest {

	JudgmentCriteriaFormConverter judgmentCriteriaFormConverter = new JudgmentCriteriaFormConverter();
	
	private LocalDate localDate[] = {new LocalDate(2014, 1, 2), new LocalDate(2015, 3, 22)};
	
	
	//------------------------ TESTS --------------------------
	
	@Test
	public void convert_ccIncludeDependentCourtJudgments_FALSE() {
		
	    // given
		JudgmentCriteriaForm judgmentCriteriaForm = createCriteriaForm();
		
		
		// execute
		JudgmentCriteria judgmentCriteria = judgmentCriteriaFormConverter.convert(judgmentCriteriaForm);
		
		
		// assert
        assertEquals(judgmentCriteriaForm.getCourtCriteria().getCcCourtId(), judgmentCriteria.getCcCourtId());
        assertNull(judgmentCriteria.getCcDirectOrSuperiorCourtId());

		assertJudgmentCriteria(judgmentCriteriaForm, judgmentCriteria);
	}


	@Test
	public void convert_ccIncludeDependentCourtJudgments_TRUE() {
	    
	    // given
        JudgmentCriteriaForm judgmentCriteriaForm = createCriteriaForm();
        judgmentCriteriaForm.getCourtCriteria().setCcIncludeDependentCourtJudgments(true);
        
        // execute
        JudgmentCriteria judgmentCriteria = judgmentCriteriaFormConverter.convert(judgmentCriteriaForm);
        
        // assert
        assertEquals(judgmentCriteriaForm.getCourtCriteria().getCcCourtId(), judgmentCriteria.getCcDirectOrSuperiorCourtId());
        assertNull(judgmentCriteria.getCcCourtId());

        assertJudgmentCriteria(judgmentCriteriaForm, judgmentCriteria);
        
        
	}
	
	//------------------------ PRIVATE --------------------------
	
	
    private void assertJudgmentCriteria(JudgmentCriteriaForm judgmentCriteriaForm, JudgmentCriteria judgmentCriteria) {
        assertEquals(judgmentCriteriaForm.getAll(), judgmentCriteria.getAll());
        assertEquals(judgmentCriteriaForm.getSignature(), judgmentCriteria.getCaseNumber());

        assertEquals(judgmentCriteriaForm.getDateFrom(), judgmentCriteria.getJudgmentDateFrom());
        assertEquals(judgmentCriteriaForm.getDateTo(), judgmentCriteria.getJudgmentDateTo());
        
        CourtCriteria courtCriteria = judgmentCriteriaForm.getCourtCriteria();
        
        assertEquals(courtCriteria.getCourtType(), judgmentCriteria.getCourtType());
        
        assertEquals(courtCriteria.getCcCourtDivisionId(), judgmentCriteria.getCcCourtDivisionId());

        assertEquals(courtCriteria.getScCourtChamberId(), judgmentCriteria.getScCourtChamberId());
        assertEquals(courtCriteria.getScCourtChamberDivisionId(), judgmentCriteria.getScCourtChamberDivisionId());
        
        assertEquals(judgmentCriteriaForm.getScPersonnelType(), judgmentCriteria.getScPersonnelType());
        assertEquals(judgmentCriteriaForm.getScJudgmentForm(), judgmentCriteria.getScJudgmentForm());
        
        assertEquals(judgmentCriteriaForm.getCtDissentingOpinion(), judgmentCriteria.getCtDissentingOpinion());
        
        assertEquals(judgmentCriteriaForm.getJudgeName(), judgmentCriteria.getJudgeName());
        
        assertEquals(1, judgmentCriteria.getKeywords().size());
        assertEquals(judgmentCriteriaForm.getKeywords(), judgmentCriteria.getKeywords());
        
        assertEquals(2, judgmentCriteria.getJudgmentTypes().size());
        assertThat(judgmentCriteria.getJudgmentTypes(), Matchers.containsInAnyOrder(JudgmentType.SENTENCE, JudgmentType.DECISION));
        
        assertEquals(judgmentCriteriaForm.getLegalBase(), judgmentCriteria.getLegalBase());
        assertEquals(judgmentCriteriaForm.getReferencedRegulation(), judgmentCriteria.getReferencedRegulation());
        assertEquals(judgmentCriteriaForm.getLawJournalEntryId(), judgmentCriteria.getLawJournalEntryId());
        assertEquals(judgmentCriteriaForm.getReferencedCourtCaseId(), judgmentCriteria.getReferencedCourtCaseId());
    }
    
    

	
	private JudgmentCriteriaForm createCriteriaForm() {
		
		JudgmentCriteriaForm judgmentCriteriaForm = new JudgmentCriteriaForm();
		
		judgmentCriteriaForm.setAll("I threw a stone at my neighbor.");
		judgmentCriteriaForm.setSignature("Sig. 1.4");
		
		judgmentCriteriaForm.setDateFrom(localDate[0]);
		judgmentCriteriaForm.setDateTo(localDate[1]);
		
		CourtCriteria courtCriteria = judgmentCriteriaForm.getCourtCriteria();
		courtCriteria.setCourtType(CourtType.COMMON);

		courtCriteria.setCcCourtId(12l);
		courtCriteria.setCcCourtDivisionId(15l);
		
		courtCriteria.setScCourtChamberId(13l);
        courtCriteria.setScCourtChamberDivisionId(14l);
        
		
		judgmentCriteriaForm.setScPersonnelType(PersonnelType.FIVE_PERSON);
		judgmentCriteriaForm.setScJudgmentForm("wyrok SN");
		
		judgmentCriteriaForm.setCtDissentingOpinion("opinion");
		
		judgmentCriteriaForm.setJudgeName("Judge Dredd");
		judgmentCriteriaForm.setKeywords(Lists.newArrayList("very important keyword"));
		judgmentCriteriaForm.setJudgmentTypes(Sets.newHashSet(JudgmentType.SENTENCE, JudgmentType.DECISION));
		judgmentCriteriaForm.setLegalBase("12.55");
		judgmentCriteriaForm.setReferencedRegulation("Art. 4.6");
		judgmentCriteriaForm.setLawJournalEntryId(16l);
		judgmentCriteriaForm.setReferencedCourtCaseId(54l);
		
		return judgmentCriteriaForm;
	}

}
