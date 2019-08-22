package model;

import model.exceptions.EmptyStringException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class TestTag {
    private Tag t;
    private Tag t1;
    private Tag t2;

    @BeforeEach
    public void initializeTag(){
        t = new Tag("homework");
        t1 = new Tag("social");
    }

    @Test
    public void testTagConstructor(){
        assertEquals("homework", t.getName());
        assertEquals("social", t1.getName());
    }

    @Test
    public void testNonEmptyName() {
        try {
            t2 = new Tag("home");
            assertEquals("home", t2.getName());
        } catch (EmptyStringException e) {
            fail("Threw exception when you weren't supposed to!");
        }
    }

    @Test
    public void testNullNameandErrorMessage() {
        try {
            t2 = new Tag(null);
            fail("Should've thrown exception!");
        } catch (EmptyStringException e) {
            assertEquals("Name can't be empty!", e.getMessage());
        }
    }

    @Test
    public void testEmptyName() {
        try {
            t2 = new Tag("");
            fail("Should've thrown exception!");
        } catch (EmptyStringException e) {
        }
    }

    @Test
    public void testtoString(){
        assertEquals("#homework", t.toString());
        assertEquals("#social", t1.toString());
    }
}
