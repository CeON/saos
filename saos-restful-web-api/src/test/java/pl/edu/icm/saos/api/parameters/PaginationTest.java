package pl.edu.icm.saos.api.parameters;

import org.junit.Test;

import static org.junit.Assert.*;

public class PaginationTest {

    @Test
    public void shouldIncreaseOffset() throws Exception {
        //given
        Pagination pagination = new Pagination(30, 20);

        //when
        Pagination actual = pagination.getNext();

        //then
        assertEquals(actual, new Pagination(30, 50));
    }

    @Test
    public void offsetShouldBeZero(){
        //given
        Pagination pagination = new Pagination(30, 10);

        //when
        Pagination actual = pagination.getPrevious();

        //then
        assertEquals(new Pagination(30, 0), actual);
    }

    @Test
    public void offsetDecreaseOffset(){
        //given
        Pagination pagination = new Pagination(30, 50);

        //when
        Pagination actual = pagination.getPrevious();

        //then
        assertEquals(new Pagination(30, 20), actual);
    }

    @Test
    public void shouldHaveNext(){
        //given
        Pagination pagination = new Pagination(30, 50);
        long allElementsCount = 81;

        //when & then
        assertTrue(pagination.hasNextIn(allElementsCount));
    }

    @Test
    public void shouldNotHaveNext(){
        //given
        Pagination pagination = new Pagination(30, 50);
        long allElementsCount = 80;

        //when & then
        assertFalse(pagination.hasNextIn(allElementsCount));
    }

}