package pl.edu.icm.saos.api.support;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.icm.saos.persistence.builder.CourtCaseBuilder;
import pl.edu.icm.saos.persistence.model.*;
import pl.edu.icm.saos.persistence.repository.*;

import java.util.Arrays;

import static pl.edu.icm.saos.api.services.FieldsDefinition.JC;
import static pl.edu.icm.saos.persistence.builder.BuildersFactory.*;

/**
 * @author pavtel
 */
@Service
public class TestPersistenceObjectsFactory {

    @Autowired
    private CommonCourtRepository courtRepository;

    @Autowired
    private CcDivisionRepository ccDivisionRepository;

    @Autowired
    private CcDivisionTypeRepository ccDivisionTypeRepository;

    @Autowired
    private CcJudgmentRepository ccJudgmentRepository;

    @Autowired
    private LawJournalEntryRepository lawJournalEntryRepository;




    public TestPersistenceObjectsContext createPersistenceObjectsContext(){
        TestPersistenceObjectsContext context = new TestPersistenceObjectsContext();
        createCommonCourt(context);
        createCommonJudgment(context);
        createSupremeCourtJudgment(context);
        return context;
    }




    public CommonCourt createCommonCourt(TestPersistenceObjectsContext context){

        CommonCourt commonCourt =
                commonCourt()
                        .name(JC.COURT_NAME)
                        .code(JC.COURT_CODE)
                        .type(JC.COURT_TYPE)
                        .build()
                ;



        commonCourt = courtRepository.save(commonCourt);

        CommonCourt parent = commonCourt()
                .name(JC.COURT_PARENT_NAME)
                .type(JC.COURT_PARENT_TYPE)
                .code(JC.COURT_PARENT_CODE)
                .build();


        courtRepository.save(parent);

        commonCourt.setParentCourt(parent);
        commonCourt = courtRepository.save(commonCourt);


        context.setCommonCourt(commonCourt);

        context.setFirstDivision(createCommonDivision(commonCourt));
        context.setSecondDivision(createSecondCommonDivision(commonCourt));

        return commonCourt;
    }


    public CommonCourtDivision createCommonDivision(CommonCourt court){
        CommonCourtDivision commonCourtDivision =
                commonCourtDivision()
                        .code(JC.DIVISION_CODE)
                        .name(JC.DIVISION_NAME)
                        .type(createCommonCourtDivisionType(JC.DIVISION_TYPE_NAME, JC.DIVISION_TYPE_CODE))
                        .court(court)
                        .build();

        return ccDivisionRepository.save(commonCourtDivision);
    }

    private CommonCourtDivisionType createCommonCourtDivisionType(String typeName, String typeCode){
        CommonCourtDivisionType  commonCourtDivisionType = commonCourtDivisionType(typeName, typeCode);
        ccDivisionTypeRepository.save(commonCourtDivisionType);
        return commonCourtDivisionType;
    }

    private  CommonCourtDivision createSecondCommonDivision(CommonCourt court){
        CommonCourtDivision secondDivision =
                commonCourtDivision()
                        .code(JC.SECOND_DIVISION_CODE)
                        .name(JC.SECOND_DIVISION_NAME)
                        .type(createCommonCourtDivisionType(JC.SECOND_DIVISION_TYPE_NAME, JC.SECOND_DIVISION_TYPE_CODE))
                        .court(court)
                        .build()
                ;

        return ccDivisionRepository.save(secondDivision);
    }

    public  CommonCourtJudgment createCommonJudgment(TestPersistenceObjectsContext context){
        CommonCourtJudgment judgment = commonCourtJudgment()
                .addCourtCase(CourtCaseBuilder.create(JC.CASE_NUMBER).build())
                .decision(JC.DECISION)
                .legalBases(Arrays.asList(JC.FIRST_LEGAL_BASE, JC.SECOND_LEGAL_BASE))
                .courtReporters(Arrays.asList(JC.FIRST_COURT_REPORTER, JC.SECOND_COURT_REPORTER))
                .summary(JC.SUMMARY)
                .judgmentType(Judgment.JudgmentType.SENTENCE)
                .textContent(JC.TEXT_CONTENT)
                .judgmentDate(new LocalDate(JC.DATE_YEAR, JC.DATE_MONTH, JC.DATE_DAY))
                .keywords(Arrays.asList())
                .division(context.getFirstDivision())
                .build();




        //*** references ***
        JudgmentReferencedRegulation judgmentReferencedRegulation = judgmentReferencedRegulation()
                .rawText(JC.FIRST_REFERENCED_REGULATION_TEXT)
                .lawJournalEntry(createFirstLawJournalEntry()
                ).build();

        JudgmentReferencedRegulation secondJudgmentReferencedRegulation = judgmentReferencedRegulation()
                .rawText(JC.SECOND_REFERENCED_REGULATION_TEXT)
                .lawJournalEntry(createSecondLawJournalEntry()
                ).build();

        JudgmentReferencedRegulation thirdJudgmentReferencedRegulation = judgmentReferencedRegulation()
                .rawText(JC.THIRD_REFERENCED_REGULATION_TEXT)
                .lawJournalEntry(createThirdLawJournalEntry()
                ).build();

        judgment.addReferencedRegulation(judgmentReferencedRegulation);
        judgment.addReferencedRegulation(secondJudgmentReferencedRegulation);
        judgment.addReferencedRegulation(thirdJudgmentReferencedRegulation);
        //*** end references ***





        judgment.addJudge(
                judge(JC.PRESIDING_JUDGE_NAME)
                        .judgesRoles(Judge.JudgeRole.PRESIDING_JUDGE)
                        .build()
        );

        judgment.addJudge(
                judge(JC.SECOND_JUDGE_NAME)
                        .build()
        );

        judgment.addJudge(
                judge(JC.THIRD_JUDGE_NAME)
                        .build()
        );


        JudgmentSourceInfo judgmentSourceInfo = judgmentSourceInfo(SourceCode.COMMON_COURT)
                .sourceJudgmentId(JC.SOURCE_JUDGMENT_ID)
                .sourceJudgmentUrl(JC.SOURCE_JUDGMENT_URL)
                .publisher(JC.SOURCE_PUBLISHER)
                .reviser(JC.SOURCE_REVISER)
                .publicationDate(new DateTime(JC.SOURCE_PUBLICATION_DATE_IN_MILLISECONDS))
                .build()
                ;

        judgment.setSourceInfo(judgmentSourceInfo);


        judgment.addKeyword(keyword(JC.FIRST_KEYWORD));
        judgment.addKeyword(keyword(JC.SECOND_KEYWORD));


        judgment = ccJudgmentRepository.save(judgment);
        context.setCommonCourtJudgment(judgment);

        return judgment;
    }

    private LawJournalEntry createFirstLawJournalEntry(){
        LawJournalEntry lawJournalEntry = lawJournalEntry()
                .title(JC.FIRST_REFERENCED_REGULATION_TITLE)
                .year(JC.FIRST_REFERENCED_REGULATION_YEAR)
                .journalNo(JC.FIRST_REFERENCED_REGULATION_JOURNAL_NO)
                .entry(JC.FIRST_REFERENCED_REGULATION_ENTRY)
                .build();

        lawJournalEntry = lawJournalEntryRepository.save(lawJournalEntry);
        return lawJournalEntry;
    }

    private LawJournalEntry createSecondLawJournalEntry(){
        LawJournalEntry lawJournalEntry = lawJournalEntry()
                .title(JC.SECOND_REFERENCED_REGULATION_TITLE)
                .year(JC.SECOND_REFERENCED_REGULATION_YEAR)
                .journalNo(JC.SECOND_REFERENCED_REGULATION_JOURNAL_NO)
                .entry(JC.SECOND_REFERENCED_REGULATION_ENTRY)
                .build();

        lawJournalEntry = lawJournalEntryRepository.save(lawJournalEntry);
        return lawJournalEntry;
    }

    private LawJournalEntry createThirdLawJournalEntry(){
        LawJournalEntry lawJournalEntry = lawJournalEntry()
                .title(JC.THIRD_REFERENCED_REGULATION_TITLE)
                .year(JC.THIRD_REFERENCED_REGULATION_YEAR)
                .journalNo(JC.THIRD_REFERENCED_REGULATION_JOURNAL_NO)
                .entry(JC.THIRD_REFERENCED_REGULATION_ENTRY)
                .build();

        lawJournalEntry = lawJournalEntryRepository.save(lawJournalEntry);
        return lawJournalEntry;
    }

    private void createSupremeCourtJudgment(TestPersistenceObjectsContext context) {
        SupremeCourtJudgment judgment = new SupremeCourtJudgment();
        judgment.setDecision(JC.DECISION);
        judgment.setJudgmentType(Judgment.JudgmentType.DECISION);
        judgment.setJudgmentDate(new LocalDate(JC.DATE_YEAR, JC.DATE_MONTH, JC.DATE_DAY));

    }

}
