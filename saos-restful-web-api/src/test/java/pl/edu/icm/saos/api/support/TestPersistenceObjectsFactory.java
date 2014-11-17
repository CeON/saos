package pl.edu.icm.saos.api.support;

import static pl.edu.icm.saos.persistence.builder.BuildersFactory.commonCourt;
import static pl.edu.icm.saos.persistence.builder.BuildersFactory.commonCourtDivision;
import static pl.edu.icm.saos.persistence.builder.BuildersFactory.commonCourtDivisionType;
import static pl.edu.icm.saos.persistence.builder.BuildersFactory.commonCourtJudgment;
import static pl.edu.icm.saos.persistence.builder.BuildersFactory.judge;
import static pl.edu.icm.saos.persistence.builder.BuildersFactory.judgmentReferencedRegulation;
import static pl.edu.icm.saos.persistence.builder.BuildersFactory.judgmentSourceInfo;
import static pl.edu.icm.saos.persistence.builder.BuildersFactory.keyword;
import static pl.edu.icm.saos.persistence.builder.BuildersFactory.lawJournalEntry;

import java.util.Arrays;

import org.apache.commons.lang3.RandomStringUtils;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.api.services.FieldsDefinition.JC;
import pl.edu.icm.saos.persistence.builder.CourtCaseBuilder;
import pl.edu.icm.saos.persistence.model.CommonCourt;
import pl.edu.icm.saos.persistence.model.CommonCourtDivision;
import pl.edu.icm.saos.persistence.model.CommonCourtDivisionType;
import pl.edu.icm.saos.persistence.model.CommonCourtJudgment;
import pl.edu.icm.saos.persistence.model.Judge;
import pl.edu.icm.saos.persistence.model.Judgment;
import pl.edu.icm.saos.persistence.model.JudgmentReferencedRegulation;
import pl.edu.icm.saos.persistence.model.JudgmentSourceInfo;
import pl.edu.icm.saos.persistence.model.LawJournalEntry;
import pl.edu.icm.saos.persistence.model.SourceCode;
import pl.edu.icm.saos.persistence.model.SupremeCourtChamber;
import pl.edu.icm.saos.persistence.model.SupremeCourtChamberDivision;
import pl.edu.icm.saos.persistence.model.SupremeCourtJudgment;
import pl.edu.icm.saos.persistence.model.SupremeCourtJudgmentForm;
import pl.edu.icm.saos.persistence.repository.CcDivisionRepository;
import pl.edu.icm.saos.persistence.repository.CcDivisionTypeRepository;
import pl.edu.icm.saos.persistence.repository.CcJudgmentRepository;
import pl.edu.icm.saos.persistence.repository.CommonCourtRepository;
import pl.edu.icm.saos.persistence.repository.LawJournalEntryRepository;
import pl.edu.icm.saos.persistence.repository.ScChamberDivisionRepository;
import pl.edu.icm.saos.persistence.repository.ScChamberRepository;
import pl.edu.icm.saos.persistence.repository.ScJudgmentFormRepository;
import pl.edu.icm.saos.persistence.repository.ScJudgmentRepository;

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

    @Autowired
    private ScChamberRepository scChamberRepository;

    @Autowired
    private ScChamberDivisionRepository scChamberDivisionRepository;

    @Autowired
    private ScJudgmentFormRepository scJudgmentFormRepository;

    @Autowired
    private ScJudgmentRepository scJudgmentRepository;




    public TestPersistenceObjectsContext createPersistenceObjectsContext(){
        TestPersistenceObjectsContext context = new TestPersistenceObjectsContext();
        createCommonCourt(context);
        createCommonJudgment(context);

        createSupremeCourtChamber(context);
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
        SupremeCourtJudgment scJudgment = new SupremeCourtJudgment();
        scJudgment.addCourtCase(CourtCaseBuilder.create(JC.CASE_NUMBER).build());
        scJudgment.setPersonnelType(SupremeCourtJudgment.PersonnelType.FIVE_PERSON);
        scJudgment.setScChamberDivision(context.getScDivision());
        scJudgment.addScChamber(context.getScChamber());
        scJudgment.getSourceInfo().setSourceCode(SourceCode.SUPREME_COURT);
        scJudgment.getSourceInfo().setSourceJudgmentId(RandomStringUtils.random(50));

        SupremeCourtJudgmentForm scJudgmentForm = createSupremeCourtJudgmentForm(context);

        scJudgment.setScJudgmentForm(scJudgmentForm);
        scJudgmentRepository.save(scJudgment);
        context.setScJudgment(scJudgment);
    }

    private void createSupremeCourtChamber(TestPersistenceObjectsContext context){
        SupremeCourtChamber scChamber = new SupremeCourtChamber();
        scChamber.setName(JC.SC_CHAMBER_NAME);
        scChamberRepository.save(scChamber);

        context.setScChamber(scChamber);
        SupremeCourtChamberDivision scDivision = createSupremeCourtDivision(context);
        scChamber.addDivision(scDivision);

        scChamberRepository.save(scChamber);
        scChamberDivisionRepository.save(scDivision);

        context.setScChamber(scChamber);
    }

    private SupremeCourtChamberDivision createSupremeCourtDivision(TestPersistenceObjectsContext context){
        SupremeCourtChamberDivision scDivision = new SupremeCourtChamberDivision();
        scDivision.setName(JC.SC_CHAMBER_DIVISION_NAME);
        scDivision.setFullName(JC.SC_CHAMBER_DIVISION_FULL_NAME);
        scDivision.setScChamber(context.getScChamber());

        scChamberDivisionRepository.save(scDivision);
        context.setScDivision(scDivision);
        return scDivision;
    }

    private SupremeCourtJudgmentForm createSupremeCourtJudgmentForm(TestPersistenceObjectsContext context){
        SupremeCourtJudgmentForm scJudgmentForm = new SupremeCourtJudgmentForm();
        scJudgmentForm.setName(JC.SC_JUDGMENT_FORM_NAME);
        scJudgmentFormRepository.save(scJudgmentForm);

        context.setScJudgmentForm(scJudgmentForm);
        return scJudgmentForm;
    }

}
