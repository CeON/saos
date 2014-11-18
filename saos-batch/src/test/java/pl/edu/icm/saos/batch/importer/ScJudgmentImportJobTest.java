package pl.edu.icm.saos.batch.importer;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.hamcrest.Matchers;
import org.joda.time.LocalDate;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ClassPathResource;

import pl.edu.icm.saos.batch.BatchTestSupport;
import pl.edu.icm.saos.batch.JobForcingExecutor;
import pl.edu.icm.saos.common.testcommon.category.SlowTest;
import pl.edu.icm.saos.importer.common.ImportDateTimeFormatter;
import pl.edu.icm.saos.importer.notapi.common.ImportFileUtils;
import pl.edu.icm.saos.persistence.correction.JudgmentCorrectionRepository;
import pl.edu.icm.saos.persistence.correction.model.CorrectedProperty;
import pl.edu.icm.saos.persistence.correction.model.JudgmentCorrection;
import pl.edu.icm.saos.persistence.model.Judge;
import pl.edu.icm.saos.persistence.model.Judge.JudgeRole;
import pl.edu.icm.saos.persistence.model.Judgment;
import pl.edu.icm.saos.persistence.model.SourceCode;
import pl.edu.icm.saos.persistence.model.SupremeCourtChamber;
import pl.edu.icm.saos.persistence.model.SupremeCourtChamberDivision;
import pl.edu.icm.saos.persistence.model.SupremeCourtJudgment;
import pl.edu.icm.saos.persistence.model.SupremeCourtJudgment.PersonnelType;
import pl.edu.icm.saos.persistence.model.SupremeCourtJudgmentForm;
import pl.edu.icm.saos.persistence.model.importer.notapi.RawSourceScJudgment;
import pl.edu.icm.saos.persistence.repository.JudgmentRepository;
import pl.edu.icm.saos.persistence.repository.RawSourceScJudgmentRepository;
import pl.edu.icm.saos.persistence.repository.ScChamberDivisionRepository;
import pl.edu.icm.saos.persistence.repository.ScChamberRepository;
import pl.edu.icm.saos.persistence.repository.ScJudgmentFormRepository;
import pl.edu.icm.saos.persistence.repository.ScJudgmentRepository;

/**
 * @author Łukasz Dumiszewski
 */
@Category(SlowTest.class)
public class ScJudgmentImportJobTest extends BatchTestSupport {

    
    
    @Autowired
    @Qualifier("scjImportFileUtils")
    private ImportFileUtils importFileUtils;
    
    @Autowired
    private Job scJudgmentImportJob;
    
    @Autowired
    private JobForcingExecutor jobExecutor;
    
    @Autowired
    private RawSourceScJudgmentRepository rJudgmentRepository;
    
    @Autowired
    private ScJudgmentRepository scJudgmentRepository;
    
    @Autowired
    private ScChamberRepository scChamberRepository;
    
    @Autowired
    private ScChamberDivisionRepository scChamberDivisionRepository;
    
    @Autowired
    private ScJudgmentFormRepository scjFormRepository;
    
    @Autowired
    private JudgmentRepository judgmentRepository;
    
    @Autowired
    @Qualifier("scjImportDateTimeFormatter")
    private ImportDateTimeFormatter importDateTimeFormatter;
    
    @Autowired
    private JudgmentCorrectionRepository judgmentCorrectionRepository;
    
    /*
     * 
     * Info:
     * raw judgments with corrections:
     * rJudgmentId Change
     * ----------------------
     * ded0b5bb7135cf1e196f80175ce07584 Izba Administracyjna, Pracy i Ubezpieczeń Społecznych -> Izba Pracy, Ubezpieczeń Społecznych i Spraw Publicznych 
     * ded0b5bb7135cf1e196f80175ce07584 orzeczenie sn -> SENTENCE (judgmentType)
     * ded0b5bb7135cf1e196f80175ce07584 orzeczenie sn -> wyrok sn (judgment form name)
     * 5e17ce355710a893e2812807a63d247c Izba Administracyjna, Pracy i Ubezpieczeń Społecznych -> Izba Pracy, Ubezpieczeń Społecznych i Spraw Publicznych
     * b082922617256d5b4092cf23864c8894 Izba Administracyjna, Pracy i Ubezpieczeń Społecznych -> Izba Pracy, Ubezpieczeń Społecznych i Spraw Publicznych
     */
    
    //------------------------ LOGIC --------------------------
    
    @Test
    public void scJudgmentImportProcessJob_IMPORT_NEW() throws Exception {
        
        //-------------- given --------------
                
        resetImportDir("import/supremeCourt/judgments/version1");
        
        
        //-------------- execute --------------
        
        JobExecution jobExecution = jobExecutor.forceStartNewJob(scJudgmentImportJob);
        
        
        //-------------- assert --------------
        
        assertEquals(5, rJudgmentRepository.count());
        assertEquals(0, rJudgmentRepository.findAllNotProcessedIds().size());
        
        JobExecutionAssertUtils.assertJobExecution(jobExecution, 0, 5);
        
        assertEquals(5, scJudgmentRepository.count());
        
        assertSourceJudgmentIds("ded0b5bb7135cf1e196f80175ce07584", "5e17ce355710a893e2812807a63d247c", "9b1052b42fde3fe481769042fae34b69", "24ffe0d974d5823db702e6436dbb9f0f", "b082922617256d5b4092cf23864c8894");
        
        assertChambers("Izba Pracy, Ubezpieczeń Społecznych i Spraw Publicznych", "Izba Cywilna");

        assertChamberDivisions("Izba Pracy, Ubezpieczeń Społecznych i Spraw Publicznych", "Wydział II");
        assertChamberDivisions("Izba Cywilna", "Wydział II", "Wydział III");
        
        assertScJudgmentForms("uchwała SN", "wyrok SN");
        
        assertJudgment_24ffe0d974d5823db702e6436dbb9f0f();
        
        
        
        // assert corrections
        
        assertEquals(5, judgmentCorrectionRepository.count());
        
        List<JudgmentCorrection> judgmentCorrections = judgmentCorrectionRepository.findAll();
        JudgmentCorrectionAssertUtils.assertJudgmentCorrections(judgmentCorrections, CorrectedProperty.SC_CHAMBER_NAME, 3);
        JudgmentCorrectionAssertUtils.assertJudgmentCorrections(judgmentCorrections, CorrectedProperty.JUDGMENT_TYPE, 1);
        JudgmentCorrectionAssertUtils.assertJudgmentCorrections(judgmentCorrections, CorrectedProperty.SC_JUDGMENT_FORM_NAME, 1);
        
        assertCorrections_ded0b5bb7135cf1e196f80175ce07584();
    }


    
   


    @Test
    public void scJudgmentImportProcessJob_IMPORT_UPDATE() throws Exception {
        
        //-------------- given --------------
        
        resetImportDir("import/supremeCourt/judgments/version1");
        JobExecution jobExecution = jobExecutor.forceStartNewJob(scJudgmentImportJob);
        int scJudgmentb082Id = scJudgmentRepository.findOneBySourceCodeAndSourceJudgmentId(SourceCode.SUPREME_COURT, "b082922617256d5b4092cf23864c8894").getId();
                
        resetImportDir("import/supremeCourt/judgments/version2");
        
        
        //--------------- execute --------------
        
        jobExecution = jobExecutor.forceStartNewJob(scJudgmentImportJob);
        
        
        //--------------- assert --------------
        
        assertEquals(5, rJudgmentRepository.count());
        assertEquals(0, rJudgmentRepository.findAllNotProcessedIds().size());
        
        JobExecutionAssertUtils.assertJobExecution(jobExecution, 0, 5);
        
        assertEquals(5, scJudgmentRepository.count());
        
        assertSourceJudgmentIds("ded0b5bb7135cf1e196f80175ce07584", "5e17ce355710a893e2812807a63d247c", "9b1052b42fde3fe481769042fae34b69", "b082922617256d5b4092cf23864c8894", "9e2119f54a24521c52d20c6cbe580180");
        
        assertChambers("Izba Pracy, Ubezpieczeń Społecznych i Spraw Publicznych", "Izba Cywilna");

        assertChamberDivisions("Izba Pracy, Ubezpieczeń Społecznych i Spraw Publicznych", "Wydział I", "Wydział II", "Wydział III");
        assertChamberDivisions("Izba Cywilna", "Wydział III");
        
        assertScJudgmentForms("uchwała SN", "wyrok SN");
        
        
        // should not exist anymore
        assertNull(scJudgmentRepository.findOneBySourceCodeAndSourceJudgmentId(SourceCode.SUPREME_COURT, "24ffe0d974d5823db702e6436dbb9f0f"));
        
        
        // changed one
        
        assertJudgment_b082922617256d5b4092cf23864c8894();
        // id shouldn't have changed if it's been update not delete/insert
        assertEquals(scJudgmentb082Id, scJudgmentRepository.findOneBySourceCodeAndSourceJudgmentId(SourceCode.SUPREME_COURT, "b082922617256d5b4092cf23864c8894").getId());
        
        
        
        // assert corrections
        
        assertEquals(4, judgmentCorrectionRepository.count());
        
        List<JudgmentCorrection> judgmentCorrections = judgmentCorrectionRepository.findAll();
        JudgmentCorrectionAssertUtils.assertJudgmentCorrections(judgmentCorrections, CorrectedProperty.SC_CHAMBER_NAME, 4);
        
        assertCorrections_ded0b5bb7135cf1e196f80175ce07584_afterUpdate();
    }

    
    
    

    
    
    //------------------------ PRIVATE --------------------------

    
    
    private void assertJudgment_b082922617256d5b4092cf23864c8894() {
        
        SupremeCourtJudgment scJudgment = scJudgmentRepository.findOneBySourceCodeAndSourceJudgmentId(SourceCode.SUPREME_COURT, "b082922617256d5b4092cf23864c8894");
        scJudgment = judgmentRepository.findOneAndInitialize(scJudgment.getId());
        
        assertNotNull(scJudgment.getScChamberDivision());
        assertEquals("Wydział I", scJudgment.getScChamberDivision().getName());
        
        assertEquals(1, scJudgment.getScChambers().size());
        assertEquals("Izba Pracy, Ubezpieczeń Społecznych i Spraw Publicznych", scJudgment.getScChambers().get(0).getName());
        
        assertEquals(scJudgment.getScChambers().get(0), scJudgment.getScChamberDivision().getScChamber());
        
        assertEquals(importDateTimeFormatter.parse("2014-02-11 13:04"), scJudgment.getSourceInfo().getPublicationDate());
        assertEquals("http://www.sn.pl/orzecznictwo/SitePages/Baza%20orzecze%C5%84.aspx?ItemID=&ListName=b082922617256d5b4092cf23864c8894", scJudgment.getSourceInfo().getSourceJudgmentUrl());
        
        assertEquals("II UZP 26/94", scJudgment.getCaseNumbers().get(0));
        assertEquals(new LocalDate("1994-09-28"), scJudgment.getJudgmentDate());
        assertEquals(PersonnelType.SEVEN_PERSON, scJudgment.getPersonnelType());
        assertEquals("uchwała SN", scJudgment.getScJudgmentForm().getName());
        
        assertEquals(1, scJudgment.getJudges().size());
        assertJudge(scJudgment, "Adam Józefowski", null, JudgeRole.PRESIDING_JUDGE, JudgeRole.REPORTING_JUDGE);
        
        assertEquals( "Uchwała z dnia 28 września 1994 r.\nII UZP 26/94\nPrzewodniczący SSN: Stefania Szymańska, Sędziowie SN: Józef Iwulski, Adam\nJózefowicz (sprawozdawca),\nSąd Najwyższy, przy udziale prokuratora Stefana Trautsolta, w sprawie z\nwniosku Jana K. przeciwko Zakładowi Ubezpieczeń Społecznych Oddział w W.\no zwrot nienależnie pobranego świadczenia, po rozpoznaniu na posiedzeniu jawnym\ndnia 28 września 1994 r. zagadnienia prawnego przekazanego przez Sąd Apelacyjny\nwe Wrocławiu postanowieniem z dnia 22 lutego 1994 r. [...] do rozstrzygnięcia w trybie\nart. 391 k.p.c.\n\"Czy pojęcie \"dochód\" powodujące zawieszenie lub zmniejszenie świadczenia, o\nktórym mowa w art. 24 ustawy z dnia 17.10.1991 r. o rewaloryzacji emerytur i rent, o\nzasadach ustalania emerytur i rent oraz o zmianie niektórych ustaw (Dz. U. Nr 104, poz.\n450 ze zm.) i w § 1 rozporządzenia Ministra Pracy i Polityki Socjalnej z dnia 22 lipca\n1992 r. w sprawie szczegółowych zasad zawieszania lub zmniejszania emerytur i rent\n(Dz. U. Nr 58, poz. 290) obejmuje dochód po odliczeniu kosztów jego uzyskania, czy\nteż łącznie z tymi kosztami ?\"\np o d j ą ł następującą uchwałę:\nPojęcie \"dochód\", zawarte w art. 24 ustawy z dnia 17 października 1991 r. o\nrewaloryzacji emerytur i rent, o zasadach ustalania emerytur i rent oraz o zmianie\nniektórych ustaw (Dz. U. Nr 104, poz. 450 ze zm.) oraz w § 1 ust. 1 pkt 3 i ust. 3\nrozporządzenia Ministra Pracy i Polityki Socjalnej z dnia 22 lipca 1992 r. w\nsprawie szczegółowych zasad zawieszania lub zmniejszania emerytury i renty\n(Dz. U. Nr 58, poz. 290) oznacza kwotę otrzymanego wynagrodzenia przez\nzleceniobiorcę po odliczeniu kosztów uzyskania wynagrodzenia według analo-\ngicznych zasad przewidzianych w przepisach o podatku dochodowym od osób\nfizycznych.\nU z a s a d n i e n i e\nPrzedstawione zagadnienie prawne powstało w następującym stanie faktycznym\ni prawnym sprawy.\nDecyzją z dnia 28 lipca 1993 r. [...] Zakład Ubezpieczeń Społecznych, Oddział w\nW. zobowiązał Jana K. do zwrotu kwoty 4.844.400 zł w związku z osiągnięciem przez\nniego w 1992 r. dodatkowego dochodu w wysokości 29.156.900 zł., w kwocie\nprzekraczającej kwotę graniczną, ustaloną na 1992 r.\nW odwołaniu od decyzji wnioskodawca Jan K. wniósł o zmianę zaskarżonej\ndecyzji przez przyjęcie, że kwota dochodu o jaką przekroczył niższą kwotę graniczną\nwynosi 23.325.500 zł, a nie 29.156.900 zł po odliczeniu kosztów uzyskania przychodu.\nSąd Wojewódzki-Sąd Pracy i Ubezpieczeń Społecznych we Wrocławiu wyrokiem\nz dnia 8 listopada 1993 r. oddalił odwołanie wnioskodawcy po ustaleniu, że otrzymał on\nz tytułu zatrudnienia w połowie czasu pracy na podstawie umowy zlecenia za 1992 r.\nprowizję w wysokości 29.156.900 zł, pomniejszoną o koszty uzyskania przychodu w\nkwocie 5.831.400 zł, czyli dochód w wysokości 23.325.500 zł Sąd Wojewódzki uznał, że\nodwołanie jest bezzasadne, gdyż zaskarżona decyzja wydana została zgodnie z art. 24\nustawy z dnia 17 października 1991 r. o rewaloryzacji emerytur i rent, o zasadach\nustalania emerytur i rent oraz o zmianie niektórych ustaw (Dz. U. Nr 104, poz. 450 ze\nzm.) oraz § 5 ust. 2, § 6 ust. 1 i § 8 ust. 1 i 2 rozporządzenia Ministra Pracy i Polityki\nSocjalnej z dnia 22 lipca 1992 r. w sprawie szczegółowych zasad zawieszania lub\nzmniejszania emerytury i renty (Dz. U. Nr 58, poz. 290). Zdaniem Sądu Wojewódzkiego\nnie jest możliwe odliczanie od faktycznie osiągniętego dochodu kosztów uzyskania\nprzychodu, gdyż ustawodawca nie określa dochodu netto i nie ma podstaw do\nodliczenia kosztów uzyskania przychodu.\nSąd Apelacyjny we Wrocławiu rozpoznając rewizję Jana K. od wyroku Sądu\nWojewódzkiego we Wrocławiu postanowieniem z dnia 22 kwietnia 1994 r. [...] przedsta-\nwił Sądowi Najwyższemu w trybie art. 391 k.p.c. do rozstrzygnięcia budzące poważne\nwątpliwości zagadnienie prawne, przytoczone w sentencji uchwały.\nW uzasadnieniu postanowienia Sąd Apelacyjny podał, że istotna kwestia wy-\nnikająca z przedstawionego zagadnienia prawnego sprowadza się do dokonania wykła-\ndni pojęcia dochodu powodującego w świetle wymienionych wyżej przepisów\nzawieszanie lub zmniejszanie emerytur i rent, a w szczególności czy pojęcie to obej-\nmuje dochód po odliczeniu kosztów jego uzyskania, czy też łącznie z tymi kosztami.\nZdaniem Sądu Apelacyjnego przepisy posługujące się pojęciami występującymi w\nróżnych dziedzinach regulacji winny tworzyć zborny system prawny. Tymczasem\npowołane wyżej przepisy wprowadzają definicję dochodu dla potrzeb zawieszania lub\nzmniejszania świadczeń emerytalnych i rentowych bez żadnego ustosunkowania się do\ntakiego samego pojęcia, zawartego w przepisach podatkowych, zwłaszcza w ustawie z\ndnia 26 lipca 1991 r. o podatku dochodowym od osób fizycznych (Dz. U. Nr 80, poz.\n350 ze zm.), odmiennie określającego to pojęcie. Zdaniem Sądu Apelacyjnego nie\nmożna dla potrzeb prawa z zakresu ubezpieczeń społecznych recypować sformułowań i\npojęć z ustawy podatkowej. W związku z tym wykształciła się praktyka nieodliczania\nkosztów przychodu przy stosowaniu przepisów o zawieszaniu lub zmniejszaniu\nświadczeń z ubezpieczenia społecznego, przyjmująca, że za uzyskany dochód uważa\nsię wynagrodzenie brutto bez odliczeń. Podobną praktykę stosuje ZUS w odniesieniu\ndo innych dochodów, uzyskanych poza zatrudnieniem.\nSąd Najwyższy zważył, co następuje:\nPodstawowym zagadnieniem dla prawidłowego rozstrzygnięcia sprawy jest\nustalenie pojęcia dochodu, o którym mowa w art. 24 ustawy z dnia 17 października\n1991 r. o rewaloryzacji emerytur i rent, o zasadach ustalania emerytur i rent oraz o\nzmianie niektórych ustaw (Dz. U. Nr 104, poz. 450 ze zm.) oraz w § 1 ust. 1 pkt 3 i ust.\n3 rozporządzenia Ministra Pracy i Polityki Socjalnej z dnia 22 lipca 1992 r. w sprawie\nszczegółowych zasad zawieszania lub zmniejszania emerytury i renty (Dz. U. Nr 58,\npoz. 290). Problem tkwi w tym, że powyższa ustawa nie określa tego pojęcia a\npowołane rozporządzenie wykonawcze wydane na podstawie upoważnienia\nustawowego, zawartego w art. 25 ust. 4 tej ustawy uściśla to pojęcie stanowiąc, że \"za\ndochód osiągnięty uważa się faktyczny dochód uzyskany przez emeryta lub rencistę w\ndanym roku kalendarzowym\". Chodzi tu o dochody z tytuły pracy wykonywanej na\npodstawie umowy zlecenia lub umowy agencyjnej, jeżeli praca ta jest wykonywana\nprzez okres co najmniej 30 dni. Uściślenie to ma istotne znaczenie przy wykładni\npojęcia dochodu, pomimo iż prawodawca nie uregulował sposobu ustalenia \"dochodu\nfaktycznego\", ani nie odesłał do innego działu prawa, który reguluje tę kwestię, to jest\ndo ustawy z dnia 26 lipca 1991 r. o podatku dochodowym od osób fizycznych (Dz. U. Nr\n80, poz. 350 ze zm.). Powstała więc luka prawna, która winna być wypełniona w drodze\nwykładni prawa, przede wszystkim z uwzględnieniem powyższych przepisów z zakresu\nubezpieczeń społecznych. Jeżeli okaże się, że przepisy te są niewystarczające do\nwyjaśnienia pojęcia dochodu w przepisach wyżej wymienionej ustawy o rewaloryzacji\nemerytur i rent, to powstaje kwestia, czy jest możliwe posłużenie się regulacją zawartą\nw systemie prawa podatkowego, który określa pojęcie dochodu. Trzeba bowiem mieć\nna względzie tę okoliczność, że system prawa podatkowego zasadniczo różni się celem\nnormatywnym od stosunków prawnych ubezpieczenia społecznego. Oba te systemy\nstanowią bowiem odrębne działy prawa. Wzgląd ten przemawia zasadniczo przeciwko\nposzukiwaniu rozwiązań w przepisach innego systemu prawa i recypowania - jak to\nsłusznie zaznacza Sąd Apelacyjny - wprost definicji prawnych prawa podatkowego do\nsystemu prawa ubezpieczeń społecznych. Może wchodzić w rachubę tylko odpowiednie\ni uzupełniające stosowanie rozwiązań prawnych, przyjętych w innym dziale prawa w\ntakim zakresie w jakim nie narusza norm regulujących funkcjonowanie ubezpieczeń\nspołecznych, a zwłaszcza uprawnień ubezpieczonego do świadczeń w wyjątkowej\nsytuacji, gdy ubezpieczony pozostaje nadal częściowo aktywny zawodowo po\nprzekroczeniu granicy wieku emerytalnego. Dlatego rozstrzygające znaczenie musi\nmieć w tych warunkach wykładnia pojęcia \"faktycznego dochodu\", uzyskanego przez\nemeryta lub rencistę w danym roku kalendarzowym, który według przepisu § 1 ust. 3\ncytowanego rozporządzenia jest dochodem w rozumieniu powołanej wyżej ustawy o\nrewaloryzacji emerytur i rent. Chodzi tu o dochody z tytułu pracy, wykonywanej na\npodstawie umowy zlecenia lub umowy agencyjnej. W obu typach umów\ncywilnoprawnych, przewiduje się, że przyjmujący zlecenie zleceniobiorca lub agent\nobowiązany jest ponosić wydatki w celu należytego wykonania zlecenia (art. 742, 750 i\n762 k.c.), z których nie wszystkie podlegają zwrotowi, lecz tylko uzasadnione w razie\nbraku odmiennej umowy. Okoliczność ta wskazuje na potrzebę zdefiniowania pojęcia\nfaktycznego dochodu zgodnie z powszechnym rozumieniem tego pojęcia. Z definicji tej\nwynika, że faktyczny dochód stanowi różnicę między uzyskanym wynagrodzeniem, a\nkosztami jego uzyskania. Bez uwzględnienia kosztów nie można w ogóle mówić o\nfaktycznym dochodzie z wyjątkiem sytuacji, gdy z uzyskaniem wynagrodzenia\n(przychodu) nie łączą się żadne koszty. Możliwy jest zatem przypadek, że całe\nwynagrodzenie jako przychód staje się jednocześnie dochodem. Skoro prawodawca\nstanowi, że dochodem w rozumieniu ustawy o waloryzacji emerytur i rent jest faktyczny\ndochód, to oznacza, że od każdego osiągniętego przez emeryta lub rencistę\nwynagrodzenia w określonym okresie trzeba odliczyć koszty jego uzyskania. Koszty\nuzyskania dochodu mogą być w każdym indywidualnym przypadku różne i mogą\nstanowić - jak w niniejszej sprawie - koszty przejazdu środkami lokomocji do miejsca\nświadczenia usług, w którym emeryt osiąga dochód, koszty materiałów biurowych,\nkoszty remontu, malowania pomieszczeń kolektury \"Toto\", drobne naprawy (np.\noszklenie stłuczonej szyby) obciążające agenta (zgodnie z umową), opłaty za energię\nelektryczną i cieplną itd. Jak oświadczył skarżący jego koszty uzyskania wyniosły w\nciągu objętego sporem roku 1992 około 20% uzyskanego wynagrodzenia. Na dowód\nskarżący okazał Sądowi Najwyższemu książeczkę opłat za energię, uregulowanych w\n1992 r. z tytułu prowadzonej w wyodrębnionym lokalu kolektury \"Toto\". Jeżeli\nustawodawca wyraźnie uregulował, że pojęcie \"dochód\" obejmuje, \"faktyczny dochód\",\nto Zakład Ubezpieczeń Społecznych obowiązany byłby w każdym indywidualnym\nwypadku ustalać faktyczne koszty uzyskania dochodu, kontrolować i weryfikować\nzasadność wydatków, niezbędnych do osiągnięcia dochodu na podstawie dokumentów\ni innych środków dowodowych, np. koszty przejazdu publicznymi lub prywatnymi\nśrodkami lokomocji oraz celowość użycia określonego środka lokomocji przez emeryta\nlub inwalidę do miejsca świadczenia usług. Mogłoby to spowodować trudności w\nudowodnieniu rodzaju i wysokości poniesionych wydatków, stanowiących koszty\nuzyskania dochodu. Takie rozumienie pojęcia dochodu w sprawach z zakresu\nubezpieczeń społecznych mogłoby wywołać liczne spory i konflikty, których\nuczestnikami byłyby osoby starsze a niekiedy niepełnosprawne. Nie można przeto\nzakładać, że ustawodawca zmierzał do takiego sposobu unormowania omawianej\nkwestii, która wymagałaby od organów rentowych badania każdorazowo zasadności\nodliczania kosztów uzyskania dochodu. Należy przeto rozważyć, czy w sytuacji, gdy\ndokładne wyliczenie kosztów uzyskania dochodu jest niemożliwe lub bardzo utrudnione,\nnie byłoby właściwe przyjęcie ryczałtowego określenia tych kosztów tak, jak to czyni\nustawodawca w podobnej sytuacji w prawie podatkowym. Unormowania takiego jednak\nbrak w przepisach z zakresu ubezpieczeń społecznych. W związku z tym nie ma\nprzeszkód do uzupełnienia luki w tych przepisach, skoro określenie zawarte w § 1 ust. 3\ncytowanego rozporządzenia, że \"za dochód osiągnięty uważa się faktyczny dochód\nuzyskany prze emeryta lub rencistę w danym roku kalendarzowym\" jest sformuło-\nwaniem nieprecyzyjnym i niewyczerpującą regulacją prawną. Nie zawiera bowiem\ndyspozycji dotyczącej sposobu ustalania faktycznego dochodu. Nie wiadomo przeto w\njaki sposób należy obliczać faktyczny dochód - osiągnięty przez emeryta lub rencistę.\nLuka prawna w tym zakresie powinna być wypełniona dla potrzeb praktyki i\norzecznictwa. Do tego w istocie zmierza przedstawione zagadnienie prawne. Zdaniem\nSądu Najwyższego w demokratycznym państwie prawnym jedną z podstawowych\nzasad prawa ubezpieczeń społecznych powinno być dokładne i nie budzące\nwątpliwości określenie przez ustawodawcę podstaw redukcji świadczeń emerytalnych i\nrentowych osobom korzystającym ze świadczeń z ubezpieczenia społecznego, którzy\nosobistym staraniem dążą do poprawy swej sytuacji materialno-bytowej.\nBezpieczeństwo prawne i ekonomiczne uprawnień emerytów i rencistów wymaga\nwyraźnej i przejrzystej regulacji normatywnej, ułatwiającej tym osobom rozeznanie swej\nsytuacji prawnej i konsekwencji wynikających z podjęcia w zakresie ograniczonym\nzatrudnienia. Wobec braku takiej regulacji prawnej, lukę w unormowaniu w zakresie\nustalania kosztów uzyskania dochodu - zdaniem Sądu Najwyższego - należy wypełnić\nprzez zastosowanie w drodze analogii \"iuris\", zasad przewidzianych w przepisach o\npodatku dochodowym od osób fizycznych. Trzeba mieć przy tym na uwadze, że pojęcie\n\"faktyczny dochód\" w rozumieniu przepisów z zakresu ubezpieczeń społecznych odpo-\nwiada pojęciu \"dochodu\" w wymienionej ustawie o podatku dochodowym od osób\nfizycznych. W związku z tym przepis art. 22 w/w ustawy o podatku dochodowym od\nosób fizycznych nadaje się do odpowiedniego zastosowania w drodze analogii do\nryczałtowego określenia kosztów uzyskania dochodu w sprawach z zakresu\nubezpieczeń społecznych, ze względu na rodzajowo podobne pojęcie kosztów\nuzyskania przychodu w prawie podatkowym i uzyskania dochodu w prawie ubezpieczeń\nspołecznych. Sąd Najwyższy uznał, że w celu ujednolicenia praktyki orzeczniczej\nmożliwe jest przyjęcie analogicznych rozwiązań dla podobnych regulacji dotyczących\nkosztów uzyskania przychodu w stosunkach ubezpieczeń społecznych i w prawie\npodatkowym.\nMając powyższe rozważania na uwadze Sąd Najwyższy doszedł do przekonania,\nże na przedstawione do rozstrzygnięcia zagadnienie prawne należy udzielić\nodpowiedzi, jak w sentencji uchwały.\n========================================\n", 
                scJudgment.getTextContent());
        
        assertScSpecificEmpty(scJudgment);
    }
    
    
    
    private void assertJudgment_24ffe0d974d5823db702e6436dbb9f0f() {
        
        SupremeCourtJudgment scJudgment = scJudgmentRepository.findOneBySourceCodeAndSourceJudgmentId(SourceCode.SUPREME_COURT, "24ffe0d974d5823db702e6436dbb9f0f");
        scJudgment = judgmentRepository.findOneAndInitialize(scJudgment.getId());
        
        assertNotNull(scJudgment.getScChamberDivision());
        assertEquals("Wydział III", scJudgment.getScChamberDivision().getName());
        
        assertEquals(1, scJudgment.getScChambers().size());
        assertEquals("Izba Cywilna", scJudgment.getScChambers().get(0).getName());
        
        assertEquals(scJudgment.getScChambers().get(0), scJudgment.getScChamberDivision().getScChamber());
        
        assertEquals(importDateTimeFormatter.parse("2014-02-11 10:24"), scJudgment.getSourceInfo().getPublicationDate());
        assertEquals("http://www.sn.pl/orzecznictwo/SitePages/Baza%20orzecze%C5%84.aspx?ItemID=&ListName=24ffe0d974d5823db702e6436dbb9f0f", scJudgment.getSourceInfo().getSourceJudgmentUrl());
        
        assertEquals("III CZP 39/02", scJudgment.getCaseNumbers().get(0));
        assertEquals(new LocalDate("2002-07-05"), scJudgment.getJudgmentDate());
        assertEquals(PersonnelType.THREE_PERSON, scJudgment.getPersonnelType());
        assertEquals("uchwała SN", scJudgment.getScJudgmentForm().getName());
        
        assertEquals(3, scJudgment.getJudges().size());
        assertJudge(scJudgment, "Mirosław Bączyk", "SSN");
        assertJudge(scJudgment, "Gerard Bieniek", "SSN");
        assertJudge(scJudgment, "Irena Gromska-Szuster", "SSN", JudgeRole.PRESIDING_JUDGE, JudgeRole.REASONS_FOR_JUDGMENT_AUTHOR, JudgeRole.REPORTING_JUDGE);

        assertEquals("Wyrok z dnia 28 września 1994 r.\nII URN 30/94\nPracownik dffdc", scJudgment.getTextContent());
        
        assertScSpecificEmpty(scJudgment);
    }



    private void assertScSpecificEmpty(SupremeCourtJudgment scJudgment) {
        assertEquals(0, scJudgment.getCourtReporters().size());
        assertEquals(0, scJudgment.getLegalBases().size());
        assertEquals(0, scJudgment.getReferencedRegulations().size());
        assertNull(scJudgment.getSummary());
        assertNull(scJudgment.getSourceInfo().getPublisher());
        assertNull(scJudgment.getSourceInfo().getReviser());
    }
    
    
    private void assertJudge(Judgment judgment, String name, String expectedFunction, JudgeRole... expectedRoles) {
        Judge judge = judgment.getJudge(name);
        assertNotNull(judge);
        assertThat(judge.getSpecialRoles(), Matchers.containsInAnyOrder(expectedRoles));
        assertEquals(expectedFunction, judge.getFunction());
    }
    
    private void assertScJudgmentForms(String... scjFormNames) {
        List<String> names = scjFormRepository.findAll().stream().map(f->f.getName()).collect(Collectors.toList());
        assertThat(names, containsInAnyOrder(scjFormNames));
    }

    private void assertChambers(String... chamberNames) {
        List<String> names = scChamberRepository.findAll().stream().map(c->c.getName()).collect(Collectors.toList());
        assertThat(names, containsInAnyOrder(chamberNames));
    }

    
    private void assertSourceJudgmentIds(String... sourceJudgmentIds) {
        List<String> ids = scJudgmentRepository.findAll().stream().map(j->j.getSourceInfo().getSourceJudgmentId()).collect(Collectors.toList());
        assertThat(ids, containsInAnyOrder(sourceJudgmentIds));
    }
    
    
    private void assertCorrections_ded0b5bb7135cf1e196f80175ce07584() {
        
        SupremeCourtJudgment judgment = getInitializedJudgment("ded0b5bb7135cf1e196f80175ce07584");
         
        List<JudgmentCorrection> judgmentCorrections = judgmentCorrectionRepository.findAllByJudgmentId(judgment.getId());
        
        
        assertEquals(3, judgmentCorrections.size());
        
        assertTrue(judgmentCorrections.contains(new JudgmentCorrection(judgment, null, null, CorrectedProperty.JUDGMENT_TYPE, "orzeczenie SN", "SENTENCE")));
        
        assertTrue(judgmentCorrections.contains(new JudgmentCorrection(judgment, SupremeCourtChamber.class, judgment.getScChambers().get(0).getId(), CorrectedProperty.SC_CHAMBER_NAME, "Izba Administracyjna, Pracy i Ubezpieczeń Społecznych", "Izba Pracy, Ubezpieczeń Społecznych i Spraw Publicznych")));
        
        assertTrue(judgmentCorrections.contains(new JudgmentCorrection(judgment, SupremeCourtJudgmentForm.class, judgment.getScJudgmentForm().getId(), CorrectedProperty.SC_JUDGMENT_FORM_NAME, "orzeczenie SN", "wyrok SN")));
        
    }
    
    
    private void assertCorrections_ded0b5bb7135cf1e196f80175ce07584_afterUpdate() {
        
        SupremeCourtJudgment judgment = getInitializedJudgment("ded0b5bb7135cf1e196f80175ce07584");
         
        List<JudgmentCorrection> judgmentCorrections = judgmentCorrectionRepository.findAllByJudgmentId(judgment.getId());
        
        
        assertEquals(1, judgmentCorrections.size());
        
        assertTrue(judgmentCorrections.contains(new JudgmentCorrection(judgment, SupremeCourtChamber.class, judgment.getScChambers().get(0).getId(), CorrectedProperty.SC_CHAMBER_NAME, "Izba Administracyjna, Pracy i Ubezpieczeń Społecznych", "Izba Pracy, Ubezpieczeń Społecznych i Spraw Publicznych")));
        
    }



    private SupremeCourtJudgment getInitializedJudgment(String rSourceJudgmentId) {
        
        RawSourceScJudgment rJudgment = rJudgmentRepository.findOneBySourceId(rSourceJudgmentId);
        Judgment j = judgmentRepository.findOneBySourceCodeAndSourceJudgmentId(SourceCode.SUPREME_COURT, rJudgment.getSourceId());
        SupremeCourtJudgment judgment = judgmentRepository.findOneAndInitialize(j.getId());
        return judgment;
        
    }


    private void assertChamberDivisions(String chamberName, String... divisionNames) {
        SupremeCourtChamber scChamber = scChamberRepository.findOneByName(chamberName);
        List<SupremeCourtChamberDivision> scChamberDivisions = scChamberDivisionRepository.findAllByScChamberId(scChamber.getId());
        List<String> scChamber1DivisionNames = scChamberDivisions.stream().map(d->d.getName()).collect(Collectors.toList());
        assertThat(scChamber1DivisionNames, containsInAnyOrder(divisionNames));
    }

    
    
    
    private void resetImportDir(String importDirClasspath) {
        try {
            File importDir = new ClassPathResource(importDirClasspath).getFile();
            importFileUtils.setImportDir(importDir.getAbsolutePath());
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
