package pl.edu.icm.saos.batch.jobs.indexer;

import static pl.edu.icm.saos.batch.jobs.indexer.SolrDocumentAssertUtils.assertFieldNotExists;
import static pl.edu.icm.saos.batch.jobs.indexer.SolrDocumentAssertUtils.assertSolrDocumentLongValues;
import static pl.edu.icm.saos.batch.jobs.indexer.SolrDocumentAssertUtils.assertSolrDocumentPostfixedFieldValues;
import static pl.edu.icm.saos.batch.jobs.indexer.SolrDocumentAssertUtils.assertSolrDocumentValues;
import static pl.edu.icm.saos.search.config.model.IndexFieldsConstants.FIELD_VALUE_PREFIX_SEPARATOR;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.solr.common.SolrDocument;

import pl.edu.icm.saos.persistence.model.CommonCourt;
import pl.edu.icm.saos.persistence.model.CommonCourtJudgment;
import pl.edu.icm.saos.persistence.model.ConstitutionalTribunalJudgment;
import pl.edu.icm.saos.persistence.model.ConstitutionalTribunalJudgmentDissentingOpinion;
import pl.edu.icm.saos.persistence.model.Judge;
import pl.edu.icm.saos.persistence.model.Judge.JudgeRole;
import pl.edu.icm.saos.persistence.model.Judgment;
import pl.edu.icm.saos.persistence.model.NationalAppealChamberJudgment;
import pl.edu.icm.saos.persistence.model.SupremeCourtChamber;
import pl.edu.icm.saos.persistence.model.SupremeCourtJudgment;
import pl.edu.icm.saos.search.config.model.JudgmentIndexField;
import pl.edu.icm.saos.search.util.CcCourtAreaFieldValueCreator;

import com.google.common.collect.Lists;

/**
 * @author madryk
 */
class JudgmentIndexAssertUtils {

    //------------------------ CONSTRUCTORS --------------------------
    
    private JudgmentIndexAssertUtils() { }
    
    
    //------------------------ LOGIC --------------------------
    
    public static void assertCcJudgment(SolrDocument doc, CommonCourtJudgment ccJudgment, long referencingCount) {
        
        assertJudgmentCommonFields(doc, ccJudgment, referencingCount);
        assertCcJudgmentSpecificFields(doc, ccJudgment);
    }
    
    public static void assertScJudgment(SolrDocument doc, SupremeCourtJudgment scJudgment, long referencingCount) {
        
        assertJudgmentCommonFields(doc, scJudgment, referencingCount);
        assertScJudgmentSpecificFields(doc, scJudgment);
    }
    
    public static void assertCtJudgment(SolrDocument doc, ConstitutionalTribunalJudgment ctJudgment, long referencingCount) {
        
        assertJudgmentCommonFields(doc, ctJudgment, referencingCount);
        assertCtJudgmentSpecificFields(doc, ctJudgment);
    }
    
    public static void assertNacJudgment(SolrDocument doc, NationalAppealChamberJudgment nacJudgment, long referencingCount) {
        
        assertJudgmentCommonFields(doc, nacJudgment, referencingCount);
    }
    
    
    //------------------------ PRIVATE --------------------------
    
    private static void assertCcJudgmentSpecificFields(SolrDocument doc, CommonCourtJudgment ccJudgment) {
        
        assertKeywords(doc, ccJudgment);
        assertSolrDocumentLongValues(doc, JudgmentIndexField.CC_COURT_ID, ccJudgment.getCourtDivision().getCourt().getId());
        assertSolrDocumentValues(doc, JudgmentIndexField.CC_COURT_TYPE, ccJudgment.getCourtDivision().getCourt().getType().name());
        assertSolrDocumentValues(doc, JudgmentIndexField.CC_COURT_CODE, ccJudgment.getCourtDivision().getCourt().getCode());
        assertSolrDocumentValues(doc, JudgmentIndexField.CC_COURT_NAME, ccJudgment.getCourtDivision().getCourt().getName());
        
        assertSolrDocumentLongValues(doc, JudgmentIndexField.CC_COURT_DIVISION_ID, ccJudgment.getCourtDivision().getId());
        assertSolrDocumentValues(doc, JudgmentIndexField.CC_COURT_DIVISION_CODE, ccJudgment.getCourtDivision().getCode());
        assertSolrDocumentValues(doc, JudgmentIndexField.CC_COURT_DIVISION_NAME, ccJudgment.getCourtDivision().getName());

        
        CommonCourt court = ccJudgment.getCourtDivision().getCourt();
        
        assertSolrDocumentLongValues(doc, JudgmentIndexField.CC_APPEAL_COURT_ID, court.getAppealCourt().getId());
        assertSolrDocumentValues(doc, JudgmentIndexField.CC_APPEAL_AREA, createCcCourtAreaValue(null, court.getAppealCourt()));

        
        if (court.isAppealCourt()) {
            assertSolrDocumentValues(doc, JudgmentIndexField.CC_REGION_AREA, createCcCourtAreaValue(court.getId(), court));
            assertFieldNotExists(doc, JudgmentIndexField.CC_REGIONAL_COURT_ID);
            assertFieldNotExists(doc, JudgmentIndexField.CC_DISTRICT_COURT_ID);
            assertFieldNotExists(doc, JudgmentIndexField.CC_DISTRICT_AREA);
            
        }

        if (court.isRegionalCourt()) { 
            assertSolrDocumentLongValues(doc, JudgmentIndexField.CC_REGIONAL_COURT_ID, court.getId());
            assertSolrDocumentValues(doc, JudgmentIndexField.CC_REGION_AREA, createCcCourtAreaValue(court.getAppealCourt().getId(), court));
            assertSolrDocumentValues(doc, JudgmentIndexField.CC_DISTRICT_AREA, createCcCourtAreaValue(court.getId(), court));
            assertFieldNotExists(doc, JudgmentIndexField.CC_DISTRICT_COURT_ID);
        }
        
        if (court.isDistrictCourt()) {
            assertSolrDocumentLongValues(doc, JudgmentIndexField.CC_REGIONAL_COURT_ID, court.getRegionalCourt().getId());
            assertSolrDocumentValues(doc, JudgmentIndexField.CC_REGION_AREA, createCcCourtAreaValue(court.getAppealCourt().getId(), court.getRegionalCourt()));
            assertSolrDocumentLongValues(doc, JudgmentIndexField.CC_DISTRICT_COURT_ID, court.getId());
            assertSolrDocumentValues(doc, JudgmentIndexField.CC_DISTRICT_AREA, createCcCourtAreaValue(court.getRegionalCourt().getId(), court));
        }
        
        
        
    }
    
    private static void assertScJudgmentSpecificFields(SolrDocument doc, SupremeCourtJudgment scJudgment) {
        
        assertSolrDocumentLongValues(doc, JudgmentIndexField.SC_JUDGMENT_FORM_ID, scJudgment.getScJudgmentForm().getId());
        assertSolrDocumentValues(doc, JudgmentIndexField.SC_JUDGMENT_FORM_NAME, scJudgment.getScJudgmentForm().getName());
	
        assertSolrDocumentValues(doc, JudgmentIndexField.SC_PERSONNEL_TYPE, scJudgment.getPersonnelType().name());
        
        List<String> expectedIndexScChamber = Lists.newArrayList();
        for (SupremeCourtChamber scChamber : scJudgment.getScChambers()) {
            expectedIndexScChamber.add(scChamber.getId() + "|" + scChamber.getName());
        }
        assertSolrDocumentValues(doc, JudgmentIndexField.SC_COURT_CHAMBER, expectedIndexScChamber);
        assertSolrDocumentLongValues(doc, JudgmentIndexField.SC_COURT_CHAMBER_ID, scJudgment.getScChambers().stream().map(x -> x.getId()).collect(Collectors.toList()));
        assertSolrDocumentValues(doc, JudgmentIndexField.SC_COURT_CHAMBER_NAME, scJudgment.getScChambers().stream().map(x -> x.getName()).collect(Collectors.toList()));
        
        assertSolrDocumentLongValues(doc, JudgmentIndexField.SC_COURT_DIVISION_ID, scJudgment.getScChamberDivision().getId());
        assertSolrDocumentValues(doc, JudgmentIndexField.SC_COURT_DIVISION_NAME, scJudgment.getScChamberDivision().getName());
        
        assertSolrDocumentLongValues(doc, JudgmentIndexField.SC_COURT_DIVISIONS_CHAMBER_ID, scJudgment.getScChamberDivision().getScChamber().getId());
        assertSolrDocumentValues(doc, JudgmentIndexField.SC_COURT_DIVISIONS_CHAMBER_NAME, scJudgment.getScChamberDivision().getScChamber().getName());
    }
    
    private static void assertCtJudgmentSpecificFields(SolrDocument doc, ConstitutionalTribunalJudgment ctJudgment) {
        List<String> expectedDissentingOpinions = Lists.newArrayList();
        List<String> expectedDissentingOpinionsAuthors = Lists.newArrayList();
        
        for (ConstitutionalTribunalJudgmentDissentingOpinion dissentingOpinion : ctJudgment.getDissentingOpinions()) {
            expectedDissentingOpinions.add(dissentingOpinion.getTextContent());
            
            for (String dissentingOpinionAuthor : dissentingOpinion.getAuthors()) {
                expectedDissentingOpinions.add(dissentingOpinionAuthor);
                expectedDissentingOpinionsAuthors.add(dissentingOpinionAuthor);
            }
        }

        assertSolrDocumentValues(doc, JudgmentIndexField.CT_DISSENTING_OPINION, expectedDissentingOpinions);
        assertSolrDocumentValues(doc, JudgmentIndexField.CT_DISSENTING_OPINION_AUTHOR, expectedDissentingOpinionsAuthors);
    }
    
    private static void assertJudgmentCommonFields(SolrDocument doc, Judgment judgment, long referencingCount) {
        
        assertSolrDocumentValues(doc, JudgmentIndexField.COURT_TYPE, judgment.getCourtType().name());
        
        assertSolrDocumentValues(doc, JudgmentIndexField.LEGAL_BASE, judgment.getLegalBases());
        assertSolrDocumentValues(doc, JudgmentIndexField.CONTENT, judgment.getRawTextContent());
        assertSolrDocumentValues(doc, JudgmentIndexField.JUDGMENT_TYPE, judgment.getJudgmentType().name());
        assertSolrDocumentValues(doc, JudgmentIndexField.CASE_NUMBER, judgment.getCaseNumbers());
        
        assertSolrDocumentLongValues(doc, JudgmentIndexField.REFERENCING_JUDGMENTS_COUNT, referencingCount);
        
        if (judgment.getMaxMoneyAmount() != null) {
            assertSolrDocumentValues(doc, JudgmentIndexField.MAXIMUM_MONEY_AMOUNT, judgment.getMaxMoneyAmount().getAmount().toString() + ",PLN");
        }
        
        assertJudges(doc, judgment);
        assertReferencedRegulations(doc, judgment);
        assertLawJournalEntries(doc, judgment);

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
    
    private static void assertLawJournalEntries(SolrDocument doc, Judgment judgment) {
        List<Long> lawJournalEntriesIds = judgment.getReferencedRegulations().stream()
                .filter(x -> x.getLawJournalEntry() != null)
                .map(x -> x.getLawJournalEntry().getId())
                .collect(Collectors.toList());
        List<String> lawJournalEntriesCodes = judgment.getReferencedRegulations().stream()
                .filter(x -> x.getLawJournalEntry() != null)
                .map(x -> x.getLawJournalEntry().getEntryCode())
                .collect(Collectors.toList());
        
        assertSolrDocumentLongValues(doc, JudgmentIndexField.LAW_JOURNAL_ENTRY_ID, lawJournalEntriesIds);
        assertSolrDocumentValues(doc, JudgmentIndexField.LAW_JOURNAL_ENTRY_CODE, lawJournalEntriesCodes);
    }
    
    
    private static String createCcCourtAreaValue(Long parentAreaCourtId, CommonCourt court) {
        String parentAreaCourtIdStr = CcCourtAreaFieldValueCreator.NULL_PARENT_COURT_ID;
        if (parentAreaCourtId != null) {
            parentAreaCourtIdStr = "" + parentAreaCourtId;
        }
        return parentAreaCourtIdStr + FIELD_VALUE_PREFIX_SEPARATOR + court.getName() + CcCourtAreaFieldValueCreator.CC_COURT_AREA_VALUE_PART_SEPARATOR + court.getId();
    }
}
