package model;
import model.exceptions.InvalidPriorityLevelException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

public class TestPriority {
    private Priority p;
    private Priority p1;
    private Priority p3;
    private Priority pp;
    private Priority pexcep;

    @BeforeEach
    public void constructPriority(){
        p = new Priority();
        p1 = new Priority(2);
        p3 = new Priority(3);
        pp = new Priority(1);
    }

    @Test
    public void testConstructor(){
        assertEquals(4, p.getQuadrant());
        assertEquals(2, p1.getQuadrant());
    }

    @Test
    public void testExceptionConstructorandErrorMessage(){
        try {
            pexcep = new Priority(0);
            fail("Should have thrown exception");
        } catch(InvalidPriorityLevelException e){
            assertEquals("Quadrant should be in between 1 and 4", e.getMessage());
        }
    }

    @Test
    public void testNoExceptionConstructor(){
        try {
            pexcep = new Priority(1);
        } catch(InvalidPriorityLevelException e){
            fail("Shouldn't have thrown exception");
        }
    }

    @Test
    public void testExceptionConstructorNeg(){
        try {
            pexcep = new Priority(-1);
            fail("should have thrown exception");
        } catch(InvalidPriorityLevelException e){}
    }

    @Test
    public void testExceptionConstructorLarger(){
        try {
            pexcep = new Priority(7);
            fail("should have thrown exception");
        } catch(InvalidPriorityLevelException e){}
    }

    @Test
    public void testisImportantTrue(){
        assertTrue(p1.isImportant());
    }

    @Test
    public void testisImportantTrueQuadOne(){
        assertTrue(pp.isImportant());
    }

    @Test
    public void testisImportantFalse(){
        assertFalse(p.isImportant());
    }

    @Test
    public void testsetImportantFromUnimportant(){
        p.setImportant(true);
        assertEquals(2, p.getQuadrant());
        assertTrue(p.isImportant());
    }

    @Test
    public void testsetUnImportantFromUnimportant(){
        p.setImportant(false);
        assertEquals(4, p.getQuadrant());
        assertFalse(p.isImportant());
    }

    @Test
    public void testsetUnImportantFromImportant(){
        p1.setImportant(false);
        assertEquals(4, p1.getQuadrant());
        assertFalse(p1.isImportant());
    }

    @Test
    public void testsetImportantFromImportant(){
        p1.setImportant(true);
        assertEquals(2, p1.getQuadrant());
        assertTrue(p1.isImportant());
    }

    @Test
    public void testsetUnImportantFromImportantOne(){
        pp.setImportant(false);
        assertEquals(3, pp.getQuadrant());
        assertFalse(pp.isImportant());
    }

    @Test
    public void testsetImportantFromImportantOne(){
        pp.setImportant(true);
        assertEquals(1, pp.getQuadrant());
        assertTrue(pp.isImportant());
    }

    @Test
    public void testsetImportantFromUnImportantOne(){
        p3.setImportant(true);
        assertEquals(1, p3.getQuadrant());
        assertTrue(p3.isImportant());
    }

    @Test
    public void testsetUnImportantFromUnImportantOne(){
        p3.setImportant(false);
        assertEquals(3, p3.getQuadrant());
        assertFalse(p3.isImportant());
    }


    @Test
    public void testisUrgentTrue(){
        p3.setImportant(true);
        assertTrue(p3.isUrgent());
    }

    @Test
    public void testisUrgentFalse(){
        p.setUrgent(false);
        assertFalse(p.isUrgent());
        assertEquals(4, p.getQuadrant());
    }

    @Test
    public void testisUrgentTrueTwo(){
        p1.setUrgent(true);
        assertEquals(1, p1.getQuadrant());
        assertTrue(p1.isUrgent());
    }

    @Test
    public void testisUrgentFalseTwo(){
        p1.setUrgent(false);
        assertFalse(p1.isUrgent());
        assertEquals(2, p1.getQuadrant());
    }

    @Test
    public void testsetUrgentFromNot(){
        p.setUrgent(true);
        assertEquals(3, p.getQuadrant());
        assertTrue(p.isUrgent());
        assertFalse(p.isImportant());
    }

    @Test
    public void testsetUrgentFromUrgent(){
        p3.setUrgent(true);
        assertTrue(p3.isUrgent());
        assertEquals(3, p3.getQuadrant());
        assertFalse(p3.isImportant());
    }

    @Test
    public void testsetUrgentNot(){
        pp.setUrgent(false);
        assertEquals(2, pp.getQuadrant());
        assertFalse(pp.isUrgent());
        assertTrue(pp.isImportant());
    }

    @Test
    public void testsetUrgentAlready(){
        pp.setUrgent(true);
        assertEquals(1, pp.getQuadrant());
        assertTrue(pp.isUrgent());
        assertTrue(pp.isImportant());
    }

    @Test
    public void testtoStringDEFAULT(){
        assertEquals("DEFAULT", p.toString());
    }

    @Test
    public void testtoStringIMPORTANTURGENT(){
        p3.setImportant(true);
        assertEquals("IMPORTANT & URGENT", p3.toString());
    }

    @Test
    public void testtoStringIMPORTANT(){
        p.setImportant(true);
        assertEquals("IMPORTANT", p.toString());
    }

    @Test
    public void testtoStringURGENT(){
        assertEquals("URGENT", p3.toString());
    }

    @Test
    public void testGetQuadrant(){
        p1.setUrgent(true);
        assertEquals(1, p1.getQuadrant());
    }
}
