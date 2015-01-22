package pl.edu.icm.saos.batch.core.indexer;

import static pl.edu.icm.saos.batch.core.indexer.SolrDocumentAssertUtils.assertSolrDocumentIntValues;
import static pl.edu.icm.saos.batch.core.indexer.SolrDocumentAssertUtils.assertSolrDocumentPostfixedFieldValues;
import static pl.edu.icm.saos.batch.core.indexer.SolrDocumentAssertUtils.assertSolrDocumentValues;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.solr.common.SolrDocument;
import org.assertj.core.util.Lists;

import pl.edu.icm.saos.persistence.model.CommonCourtJudgment;
import pl.edu.icm.saos.persistence.model.ConstitutionalTribunalJudgment;
import pl.edu.icm.saos.persistence.model.Judge;
import pl.edu.icm.saos.persistence.model.Judge.JudgeRole;
import pl.edu.icm.saos.persistence.model.Judgment;
import pl.edu.icm.saos.persistence.model.NationalAppealChamberJudgment;
import pl.edu.icm.saos.persistence.model.SupremeCourtChamber;
import pl.edu.icm.saos.persistence.model.SupremeCourtJudgment;
import pl.edu.icm.saos.search.config.model.JudgmentIndexField;

/**
 * @author madryk
 */
class JudgmentIndexAssertUtils {

    //------------------------ CONSTRUCTORS --------------------------
    
    private JudgmentIndexAssertUtils() { }
    
    
    //------------------------ LOGIC --------------------------
    
    public static void assertCcJudgment(SolrDocument doc, CommonCourtJudgment ccJudgment) {
        
        assertJudgmentCommonFields(doc, ccJudgment);
        assertCcJudgmentSpecificFields(doc, ccJudgment);
    }
    
    public static void assertScJudgment(SolrDocument doc, SupremeCourtJudgment scJudgment) {
        
        assertJudgmentCommonFields(doc, scJudgment);
        assertScJudgmentSpecificFields(doc, scJudgment);
    }
    
    public static void assertCtJudgment(SolrDocument doc, ConstitutionalTribunalJudgment ctJudgment) {
        
        assertJudgmentCommonFields(doc, ctJudgment);
    }
    
    public static void assertNacJudgment(SolrDocument doc, NationalAppealChamberJudgment nacJudgment) {
        
        assertJudgmentCommonFields(doc, nacJudgment);
    }
    
    
    //------------------------ PRIVATE --------------------------
    
    private static void assertCcJudgmentSpecificFields(SolrDocument doc, CommonCourtJudgment ccJudgment) {
        
        assertKeywords(doc, ccJudgment);
        assertSolrDocumentIntValues(doc, JudgmentIndexField.CC_COURT_ID, ccJudgment.getCourtDivision().getCourt().getId());
        assertSolrDocumentValues(doc, JudgmentIndexField.CC_COURT_TYPE, ccJudgment.getCourtDivision().getCourt().getType().name());
        assertSolrDocumentValues(doc, JudgmentIndexField.CC_COURT_CODE, ccJudgment.getCourtDivision().getCourt().getCode());
        assertSolrDocumentValues(doc, JudgmentIndexField.CC_COURT_NAME, ccJudgment.getCourtDivision().getCourt().getName());
        
        assertSolrDocumentIntValues(doc, JudgmentIndexField.CC_COURT_DIVISION_ID, ccJudgment.getCourtDivision().getId());
        assertSolrDocumentValues(doc, JudgmentIndexField.CC_COURT_DIVISION_CODE, ccJudgment.getCourtDivision().getCode());
        assertSolrDocumentValues(doc, JudgmentIndexField.CC_COURT_DIVISION_NAME, ccJudgment.getCourtDivision().getName());
    }
    
    private static void assertScJudgmentSpecificFields(SolrDocument doc, SupremeCourtJudgment scJudgment) {
        
        assertSolrDocumentValues(doc, JudgmentIndexField.SC_PERSONNEL_TYPE, scJudgment.getPersonnelType().name());
        
        List<String> expectedIndexScChamber = Lists.newArrayList();
        for (SupremeCourtChamber scChamber : scJudgment.getScChambers()) {
            expectedIndexScChamber.add(scChamber.getId() + "|" + scChamber.getName());
        }
        assertSolrDocumentValues(doc, JudgmentIndexField.SC_COURT_CHAMBER, expectedIndexScChamber);
        assertSolrDocumentIntValues(doc, JudgmentIndexField.SC_COURT_CHAMBER_ID, scJudgment.getScChambers().stream().map(x -> x.getId()).collect(Collectors.toList()));
        assertSolrDocumentValues(doc, JudgmentIndexField.SC_COURT_CHAMBER_NAME, scJudgment.getScChambers().stream().map(x -> x.getName()).collect(Collectors.toList()));
        
        assertSolrDocumentIntValues(doc, JudgmentIndexField.SC_COURT_DIVISION_ID, scJudgment.getScChamberDivision().getId());
        assertSolrDocumentValues(doc, JudgmentIndexField.SC_COURT_DIVISION_NAME, scJudgment.getScChamberDivision().getName());
        
        assertSolrDocumentIntValues(doc, JudgmentIndexField.SC_COURT_DIVISIONS_CHAMBER_ID, scJudgment.getScChamberDivision().getScChamber().getId());
        assertSolrDocumentValues(doc, JudgmentIndexField.SC_COURT_DIVISIONS_CHAMBER_NAME, scJudgment.getScChamberDivision().getScChamber().getName());
    }
    
    private static void assertJudgmentCommonFields(SolrDocument doc, Judgment judgment) {
        
        assertSolrDocumentValues(doc, JudgmentIndexField.COURT_TYPE, judgment.getCourtType().name());
        
        assertSolrDocumentValues(doc, JudgmentIndexField.LEGAL_BASE, judgment.getLegalBases());
        assertSolrDocumentValues(doc, JudgmentIndexField.CONTENT, judgment.getTextContent());
        assertSolrDocumentValues(doc, JudgmentIndexField.JUDGMENT_TYPE, judgment.getJudgmentType().name());
        assertSolrDocumentValues(doc, JudgmentIndexField.CASE_NUMBER, judgment.getCaseNumbers());
        
        assertJudges(doc, judgment);
        assertReferencedRegulations(doc, judgment);

    }
    
    private static void assertKeywords(SolrDocument doc, Judgment judgment) {
        List<String> keywordsStrings = judgment.getKeywords().stream()
                .map(x -> x.getPhrase())
                .collect(Collectors.toList());
        
        assertSolrDocumentValues(doc, JudgmentIndexField.KEYWORD, keywordsStrings);
    }
    
    private static void assertJudges(SolrDocument doc, Judgment judgment) {
        List<String> judgeNames = judgment.getJudges().stream().map(x -> x.getName()).collect(Collectors.toList());
        
        List<String> noRoleJudges = judgment.getJudges(null).stream().map(x -> x.getName()).collect(Collectors.toList());
        List<String> presidingRoleJudges = judgment.getJudges(JudgeRole.PRESIDING_JUDGE).stream().map(x -> x.getName()).collect(Collectors.toList());
        List<String> reportingRoleJudges = judgment.getJudges(JudgeRole.REPORTING_JUDGE).stream().map(x -> x.getName()).collect(Collectors.toList());
        List<String> reasonsForJudgmentRoleJudges = judgment.getJudges(JudgeRole.REASONS_FOR_JUDGMENT_AUTHOR).stream().map(x -> x.getName()).collect(Collectors.toList());
        
        
        
        Collection<Object> jOb = doc.getFieldValues(JudgmentIndexField.JUDGE.getFieldName());
        List<String> expectedIndexJudges = Lists.newArrayList();
        for (Judge j : judgment.getJudges()) {
            for (Object indexJudge : jOb) {
                String indexJudgeString = (String) indexJudge;
                if (indexJudgeString.startsWith(j.getName())) {
                    String expectedIndexJudge = j.getName();
                    if (j.getSpecialRoles().contains(JudgeRole.PRESIDING_JUDGE)) {
                        expectedIndexJudge += "|" + JudgeRole.PRESIDING_JUDGE.name();
                    }
                    if (j.getSpecialRoles().contains(JudgeRole.REPORTING_JUDGE)) {
                        expectedIndexJudge += "|" + JudgeRole.REPORTING_JUDGE.name();
                    }
                    if (j.getSpecialRoles().contains(JudgeRole.REASONS_FOR_JUDGMENT_AUTHOR)) {
                        expectedIndexJudge += "|" + JudgeRole.REASONS_FOR_JUDGMENT_AUTHOR.name();
                    }
                    expectedIndexJudges.add(expectedIndexJudge);
                    break;
                }
            }
        }
        assertSolrDocumentValues(doc, JudgmentIndexField.JUDGE, expectedIndexJudges);
        
        
        assertSolrDocumentValues(doc, JudgmentIndexField.JUDGE_NAME, judgeNames);
        
        assertSolrDocumentPostfixedFieldValues(doc, JudgmentIndexField.JUDGE_WITH_ROLE, "PRESIDING_JUDGE", presidingRoleJudges);
        assertSolrDocumentPostfixedFieldValues(doc, JudgmentIndexField.JUDGE_WITH_ROLE, "REPORTING_JUDGE", reportingRoleJudges);
        assertSolrDocumentPostfixedFieldValues(doc, JudgmentIndexField.JUDGE_WITH_ROLE, "REASONS_FOR_JUDGMENT_AUTHOR", reasonsForJudgmentRoleJudges);
        assertSolrDocumentPostfixedFieldValues(doc, JudgmentIndexField.JUDGE_WITH_ROLE, "NO_ROLE", noRoleJudges);
    }
    
    private static void assertReferencedRegulations(SolrDocument doc, Judgment judgment) {
        List<String> referencedRegulationsStrings = judgment.getReferencedRegulations().stream()
                .map(x -> x.getRawText())
                .collect(Collectors.toList());
        
        assertSolrDocumentValues(doc, JudgmentIndexField.REFERENCED_REGULATION, referencedRegulationsStrings);
    }
}
