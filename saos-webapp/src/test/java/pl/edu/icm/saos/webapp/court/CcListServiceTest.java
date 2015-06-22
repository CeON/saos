package pl.edu.icm.saos.webapp.court;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import pl.edu.icm.saos.persistence.model.CommonCourt;
import pl.edu.icm.saos.persistence.model.CommonCourtDivision;
import pl.edu.icm.saos.persistence.repository.CcDivisionRepository;
import pl.edu.icm.saos.persistence.repository.CommonCourtRepository;

/**
 * @author Łukasz Pawełczak
 *
 */
@RunWith(MockitoJUnitRunner.class)  
public class CcListServiceTest {

    @InjectMocks
    private CcListService ccListService = new CcListService();

    @Mock
    private SimpleEntityConverter simpleEntityConverter;

    @Mock
    private SimpleCommonCourtConverter simpleCommonCourtConverter;

    @Mock
    private CommonCourtRepository commonCourtRepository;

    @Mock
    private CcDivisionRepository ccDivisionRepository;


    //------------------------ TESTS --------------------------

    @Test
    public void findCommonCourts_correct_order() {
        //given
        CommonCourt commonCourtOne = new CommonCourt();
        commonCourtOne.setCode("0510");
        commonCourtOne.setName("Sąd w Aninie");
        CommonCourt commonCourtTwo = new CommonCourt();
        commonCourtTwo.setCode("0520");
        commonCourtTwo.setName("Sąd w Łodzi");
        CommonCourt commonCourtThree = new CommonCourt();
        commonCourtThree.setCode("0530");
        commonCourtThree.setName("Sąd w Ryczywołach");

        SimpleCommonCourt simpleCommonCourtOne = new SimpleCommonCourt();
        simpleCommonCourtOne.setName("Sąd w Aninie");
        SimpleCommonCourt simpleCommonCourtTwo = new SimpleCommonCourt();
        simpleCommonCourtTwo.setName("Sąd w Łodzi");
        SimpleCommonCourt simpleCommonCourtThree = new SimpleCommonCourt();
        simpleCommonCourtThree.setName("Sąd w Ryczywołach");

        List<CommonCourt> courtsWrongOrder = Arrays.asList(commonCourtThree, commonCourtOne, commonCourtTwo);
        List<CommonCourt> courtsRightOrder = Arrays.asList(commonCourtOne, commonCourtTwo, commonCourtThree);

        when(commonCourtRepository.findAll()).thenReturn(courtsWrongOrder);
        when(simpleCommonCourtConverter.convertCommonCourts(courtsRightOrder))
                .thenReturn(Arrays.asList(simpleCommonCourtOne, simpleCommonCourtTwo, simpleCommonCourtThree));


        //when
        List<SimpleCommonCourt> courts = ccListService.findCommonCourts();


        //then
        assertEquals(3, courts.size());
        assertEquals(courts.get(0).getName(), commonCourtOne.getName());
        assertEquals(courts.get(1).getName(), commonCourtTwo.getName());
        assertEquals(courts.get(2).getName(), commonCourtThree.getName());
    }

    @Test
    public void findCcDivisions_correct_order() {
        //given
        CommonCourtDivision ccDivisionOne = new CommonCourtDivision();
        ccDivisionOne.setCode("0001");
        ccDivisionOne.setName("I Wydział Cywilny");
        CommonCourtDivision ccDivisionTwo = new CommonCourtDivision();
        ccDivisionTwo.setCode("0002");
        ccDivisionTwo.setName("IV Wydział Karny");
        CommonCourtDivision ccDivisionThree = new CommonCourtDivision();
        ccDivisionThree.setCode("0003");
        ccDivisionThree.setName("IX Wydział Penitencjarny i Nadzoru nad Wykonywaniem Orzeczeń Karnych");

        SimpleEntity simpleCcDivisionOne = new SimpleEntity();
        simpleCcDivisionOne.setName("I Wydział Cywilny");
        SimpleEntity simpleCcDivisionTwo = new SimpleEntity();
        simpleCcDivisionTwo.setName("IV Wydział Karny");
        SimpleEntity simpleCcDivisionThree = new SimpleEntity();
        simpleCcDivisionThree.setName("IX Wydział Penitencjarny i Nadzoru nad Wykonywaniem Orzeczeń Karnych");
        

        List<CommonCourtDivision> divisionsWrongOrder = Arrays.asList(ccDivisionThree, ccDivisionOne, ccDivisionTwo);
        List<CommonCourtDivision> divisionsRightOrder = Arrays.asList(ccDivisionOne, ccDivisionTwo, ccDivisionThree);

        when(ccDivisionRepository.findAllByCourtId(1)).thenReturn(divisionsWrongOrder);
        when(simpleEntityConverter.convertCcDivisions(divisionsRightOrder))
                .thenReturn(Arrays.asList(simpleCcDivisionOne, simpleCcDivisionTwo, simpleCcDivisionThree));


        //when
        List<SimpleEntity> divisions = ccListService.findCcDivisions(1);


        //then
        assertEquals(3, divisions.size());
        assertEquals(divisions.get(0).getName(), ccDivisionOne.getName());
        assertEquals(divisions.get(1).getName(), ccDivisionTwo.getName());
        assertEquals(divisions.get(2).getName(), ccDivisionThree.getName());
    }

    @Test
    public void findCcDivisions_correct_order_names_without_roman_numbers() {
        //given
        CommonCourtDivision ccDivisionOne = new CommonCourtDivision();
        ccDivisionOne.setName("Wydział Cywilny");
        CommonCourtDivision ccDivisionTwo = new CommonCourtDivision();
        ccDivisionTwo.setName("Wydział Karny");
        CommonCourtDivision ccDivisionThree = new CommonCourtDivision();
        ccDivisionThree.setName("IX Wydział Penitencjarny i Nadzoru nad Wykonywaniem Orzeczeń Karnych");

        SimpleEntity simpleCcDivisionOne = new SimpleEntity();
        simpleCcDivisionOne.setName("Wydział Cywilny");
        SimpleEntity simpleCcDivisionTwo = new SimpleEntity();
        simpleCcDivisionTwo.setName("Wydział Karny");
        SimpleEntity simpleCcDivisionThree = new SimpleEntity();
        simpleCcDivisionThree.setName("IX Wydział Penitencjarny i Nadzoru nad Wykonywaniem Orzeczeń Karnych");
        
        List<CommonCourtDivision> divisionsWrongOrder = Arrays.asList(ccDivisionThree, ccDivisionTwo, ccDivisionOne);
        List<CommonCourtDivision> divisionsRightOrder = Arrays.asList(ccDivisionOne, ccDivisionTwo, ccDivisionThree);

        when(ccDivisionRepository.findAllByCourtId(1)).thenReturn(divisionsWrongOrder);
        when(simpleEntityConverter.convertCcDivisions(divisionsRightOrder))
                .thenReturn(Arrays.asList(simpleCcDivisionOne, simpleCcDivisionTwo, simpleCcDivisionThree));


        //when
        List<SimpleEntity> divisions = ccListService.findCcDivisions(1);


        //then
        assertEquals(3, divisions.size());
        assertEquals(divisions.get(0).getName(), ccDivisionOne.getName());
        assertEquals(divisions.get(1).getName(), ccDivisionTwo.getName());
        assertEquals(divisions.get(2).getName(), ccDivisionThree.getName());
    }


}
