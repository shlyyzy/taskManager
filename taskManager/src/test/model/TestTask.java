package model;

import model.exceptions.EmptyStringException;
import model.exceptions.InvalidProgressException;
import model.exceptions.NegativeInputException;
import model.exceptions.NullArgumentException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Calendar;
import java.util.Date;

import static model.Task.NO_DUE_DATE;
import static org.junit.jupiter.api.Assertions.*;


public class TestTask {

    private Task t;
    private Task toSetTask;
    private Task t1;
    private Priority p;
    private Priority p1;
    private DueDate duesomeTime;
    private DueDate today;
    private DueDate tomorrow;
    private Date someDate;
    private Calendar cal;

    @BeforeEach
    public void initializeTask() {
        t = new Task("description");
        toSetTask = new Task("Some description ## tag1;today;urgent;in progress;important;tomoRROW");
        p = new Priority(1);
        p1 = new Priority();
        duesomeTime = new DueDate();
        today = new DueDate();
        tomorrow = new DueDate();
        tomorrow.postponeOneDay();

        cal = Calendar.getInstance();
        cal.set(Calendar.MONTH, Calendar.MAY);
        cal.set(Calendar.YEAR, 2019);
        cal.set(Calendar.DAY_OF_MONTH, 15);
        cal.set(Calendar.HOUR, 10);
        cal.set(Calendar.MINUTE, 12);
        cal.set(Calendar.AM_PM, Calendar.AM);
        someDate = cal.getTime();
        duesomeTime.setDueDate(someDate);
    }

    @Test
    public void testConstructor() {
        assertEquals(Status.TODO, t.getStatus());
        assertEquals(NO_DUE_DATE, t.getDueDate());
        assertEquals(0, t.getTags().size());
        assertEquals(0, t.getProgress());
        assertEquals(0, t.getEstimatedTimeToComplete());

        String x = t.getPriority().toString();
        assertEquals("DEFAULT", x);
    }

    @Test
    public void testUnemptyDescription() {
        try {
            t1 = new Task("homework");
            assertEquals("homework", t1.getDescription());
        } catch(EmptyStringException e){
            fail("Shouldn't have had caught an exception!");
        }
    }

    @Test
    public void testEmptyDescription() {
        try {
            t1 = new Task("");
            fail("Should have had caught an exception!");
        } catch(EmptyStringException e){
            // expected
        }
    }

    @Test
    public void testNullDescription() {
        try {
            t1 = new Task(null);
            fail("Should have had caught an exception!");
        } catch(EmptyStringException e){
            // expected
        }
    }


    @Test
    public void testAddTag() {
        try {
            t.addTag("homework");
            assertEquals(1, t.getTags().size());
        } catch(EmptyStringException e){
            fail("Shouldn't have thrown exception");
        }
    }

    @Test
    public void testAddEmptyTag() {
        try {
            t.addTag("");
            fail("Should've thrown an exception");
        } catch(EmptyStringException e){
            // expected
        }
    }

    @Test
    public void testAddNullTag() {
        Tag tag1 = null;
        try {
            t.addTag(tag1);
            fail("Should've thrown an exception");
        } catch(NullArgumentException e){
            assertEquals("Invalid Argument: tag cannot be null", e.getMessage());
        }
    }

    @Test
    public void testContainsNullTagString() {
        String stringtag = null;
        try {
            t.containsTag(stringtag);
            fail("Should've thrown an exception");
        } catch(EmptyStringException e){
            assertEquals("Tag name cannot be empty or null", e.getMessage());
        }
    }

    @Test
    public void testContainsEmptyTagString() {
        try {
            t.containsTag("");
            fail("Should've thrown an exception");
        } catch(EmptyStringException e){
            assertEquals("Tag name cannot be empty or null", e.getMessage());
        }
    }

    @Test
    public void testAddTagWithDuplicate() {
        try {
            t.addTag("homework");
            assertEquals(1, t.getTags().size());
            t.addTag("homework");
            assertEquals(1, t.getTags().size());
        } catch(EmptyStringException e){
            fail("Shouldn't have thrown exception");
        }
    }

    @Test
    public void testAddTagTwo() {
        t.addTag("homework");
        assertEquals(1, t.getTags().size());
        t.addTag("work");
        assertEquals(2, t.getTags().size());
    }


    @Test
    public void testRemoveTag() {
        try {
            t.addTag("homework");
            t.addTag("work");
            t.removeTag("work");

            assertEquals(1, t.getTags().size());
            t.addTag("work");
            assertEquals(2, t.getTags().size());
        } catch(EmptyStringException e){
            fail("Shouldn't have thrown exception");
        }
    }

    @Test
    public void testRemoveEmptyTag(){
        try {
            t.removeTag("");
            fail("Should've thrown an exception");
        } catch(EmptyStringException e){
            // expected
        }
    }

    @Test
    public void testRemoveNullTag(){
        Tag tag1 = null;
        try {
            t.removeTag(tag1);
            fail("Should've thrown an exception");
        } catch(NullArgumentException e){
            assertEquals("Invalid Argument: tag cannot be null", e.getMessage());
        }
    }

    @Test
    public void testRemoveNoTag() {
        try {
            t.addTag("homework");
            t.addTag("work");
            t.removeTag("another");

            assertEquals(2, t.getTags().size());
            t.addTag("work");
            assertEquals(2, t.getTags().size());
        } catch(EmptyStringException e){
            fail("Shouldn't have thrown exception");
        }
    }

    @Test
    public void testSetPriority() {
        try {
            t.setPriority(p);
            assertEquals(p, t.getPriority());

            assertEquals("IMPORTANT & URGENT", t.getPriority().toString());
        } catch(NullArgumentException e) {
            fail("Shouldn't have thrown exception");
        }
    }

    @Test
    public void testSetPriorityNull(){
        try {
            t.setPriority(null);
            fail("Should've thrown exception");
        } catch(NullArgumentException e){
            // expected
        }

    }

    @Test
    public void testSetPrioritySame() {
        try {
            t.setPriority(p1);
            assertEquals(p1, t.getPriority());
            assertEquals("DEFAULT", t.getPriority().toString());
        } catch(NullArgumentException e){
            fail("Shouldn't have thrown exception");
        }
    }

    @Test
    public void testSetStatus() {
        try {
            t.setStatus(Status.DONE);
            assertEquals(Status.DONE, t.getStatus());
        } catch(NullArgumentException e){
            fail("Shouldn't have thrown exception");
        }
    }

    @Test
    public void testSetStatusNull(){
        try {
            t.setStatus(null);
            fail("Should've thrown exception");
        } catch(NullArgumentException e){
            // expected
        }
    }

    @Test
    public void testSetStatusSame() {
        try {
            t.setStatus(Status.TODO);
            assertEquals(Status.TODO, t.getStatus());
        } catch(NullArgumentException e){
            fail("Shouldn't have thrown exception");
        }
    }

    @Test
    public void testSetDescription() {
        try {
            t.setDescription("hello");
            assertEquals("hello", t.getDescription());
        } catch(EmptyStringException e){
            fail("Shouldn't have thrown exception");
        }
    }

    @Test
    public void testsetDescriptionEmpty() {
        try {
            t.setDescription("");
            fail("Should've thrown exception");
        } catch(EmptyStringException e){
            // expected
        }
    }

    @Test
    public void testSetDescriptionSame() {
        try {
            t.setDescription("description");
            assertEquals("description", t.getDescription());
        } catch(EmptyStringException e){
            fail("Shouldn't have thrown exception");
        }
    }

    @Test
    public void testSetProgress(){
        try {
            t.setProgress(50);
            assertEquals(50, t.getProgress());
        } catch (InvalidProgressException e) {
            fail("Should not have thrown exception!");
        }
    }

    @Test
    public void testSetProgressEdge(){
        try {
            t.setProgress(100);
            assertEquals(100, t.getProgress());
        } catch (InvalidProgressException e) {
            fail("Should not have thrown exception!");
        }
    }

    @Test
    public void testSetProgressEdgeZero(){
        try {
            t.setProgress(0);
            assertEquals(0, t.getProgress());
        } catch (InvalidProgressException e) {
            fail("Should not have thrown exception!");
        }
    }

    @Test
    public void testSetProgressThrowException(){
        try {
            t.setProgress(10000);
            fail("Should've thrown InvalidProgressException!");
        } catch (InvalidProgressException e) {
            assertEquals("Invalid Progress: progress must be between 0 and 100!", e.getMessage());
        }
    }

    @Test
    public void testSetProgressThrowExceptionNegative(){
        try {
            t.setProgress(-40);
            fail("Should've thrown exception!");
        } catch (InvalidProgressException e) {
            assertEquals("Invalid Progress: progress must be between 0 and 100!", e.getMessage());
        }
    }

    @Test
    public void testSetEstimatedTimeToComplete(){
        try {
            t.setEstimatedTimeToComplete(2);
            assertEquals(2, t.getEstimatedTimeToComplete());
        } catch (NegativeInputException e){
            fail("Shouldn't have thrown exception!");
        }
    }

    @Test
    public void testSetEstimatedTimeToCompleteEdge(){
        try {
            t.setEstimatedTimeToComplete(0);
            assertEquals(0, t.getEstimatedTimeToComplete());
        } catch (NegativeInputException e){
            fail("Shouldn't have thrown exception!");
        }
    }

    @Test
    public void testSetEstimatedTimeToCompleteThrowException(){
        try {
            t.setEstimatedTimeToComplete(-1);
            fail("Should've thrown exception!");
        } catch (NegativeInputException e){
            assertEquals("Invalid Estimated Time To Complete: must not be negative!", e.getMessage());
        }
    }

    @Test
    public void testSetDueDate() {
        t.setDueDate(duesomeTime);
        String a = t.getDueDate().toString();

        assertEquals("Wed May 15 2019 10:12 AM", a);
    }

    @Test
    public void testtoString() {
        String theString = "{" + "\n" + ("\t" + "Description: description" + "\n"
                + "\t" + "Due date: " + "\n"
                + "\t" + "Status: TODO" + "\n"
                + "\t" + "Priority: DEFAULT" + "\n");

        assertTrue(t.toString().contains(theString));
    }

    @Test
    public void testToStringTags() {
        t.addTag("homework");
        t.addTag("work");
        t.setDueDate(duesomeTime);

        String theString = "{" + "\n" + ("\t" + "Description: description" + "\n"
                + "\t" + "Due date: Wed May 15 2019 10:12 AM" + "\n"
                + "\t" + "Status: TODO" + "\n"
                + "\t" + "Priority: DEFAULT" + "\n");

        assertTrue(t.toString().contains(theString));
    }

    @Test
    public void testSetDescriptionParseUnsetTask(){
        try {
            t.setDescription("Some description ## tag1;tomorrow;urgent;in progress;important;toDAY; tag1");

            assertEquals("Some description ", t.getDescription());
            assertEquals(tomorrow.toString(), t.getDueDate().toString());
            assertTrue(t.getPriority().isImportant());
            assertTrue(t.getPriority().isUrgent());
            assertEquals("IMPORTANT & URGENT", t.getPriority().toString());
            assertEquals(Status.IN_PROGRESS, t.getStatus());
            assertEquals(2, t.getTags().size());
            assertTrue(t.containsTag("toDAY"));
        } catch (EmptyStringException e){
            fail("Shouldn't have thrown empty string exception");
        }
    }

    @Test
    public void testSetDescriptionParseUnsetTaskEmpty(){
        try {
            t.setDescription("");
            fail("should've thrown empty string exception");
        } catch (EmptyStringException e){
            assertEquals("description", t.getDescription());
            assertNull(t.getDueDate());
            assertFalse(t.getPriority().isUrgent());
            assertFalse(t.getPriority().isImportant());
            assertEquals("DEFAULT", t.getPriority().toString());
            assertEquals(Status.TODO, t.getStatus());
            assertEquals(0, t.getTags().size());
        }
    }

    @Test
    public void testSetDescriptionParseUnsetTaskNull(){
        try {
            t.setDescription(null);
            fail("should've thrown empty string exception");
        } catch (EmptyStringException e){
            assertEquals("description", t.getDescription());
            assertNull(t.getDueDate());
            assertFalse(t.getPriority().isUrgent());
            assertFalse(t.getPriority().isImportant());
            assertEquals("DEFAULT", t.getPriority().toString());
            assertEquals(Status.TODO, t.getStatus());
            assertEquals(0, t.getTags().size());
        }
    }

    @Test
    public void testSetDescriptionParseUnsetTaskUrgent(){
        try {
            t.setDescription("Some description ## tag1;tomorrow;urgent;in progress;;toDAY; tag1");

            assertEquals("Some description ", t.getDescription());
            assertEquals(tomorrow.toString(), t.getDueDate().toString());
            assertFalse(t.getPriority().isImportant());
            assertTrue(t.getPriority().isUrgent());
            assertEquals("URGENT", t.getPriority().toString());
            assertEquals(Status.IN_PROGRESS, t.getStatus());
            assertEquals(2, t.getTags().size());
            assertTrue(t.containsTag("toDAY"));
        } catch (EmptyStringException e){
            fail("Shouldn't have thrown empty string exception");
        }
    }

    @Test
    public void testsetDescriptionParseUnsetTaskIMPORTANT(){
        try {
            t.setDescription("Some description ## tag1;tomorrow;important;in progress;;toDAY; tag1");

            assertEquals("Some description ", t.getDescription());
            assertEquals(tomorrow.toString(), t.getDueDate().toString());
            assertFalse(t.getPriority().isUrgent());
            assertTrue(t.getPriority().isImportant());
            assertEquals("IMPORTANT", t.getPriority().toString());
            assertEquals(Status.IN_PROGRESS, t.getStatus());
            assertEquals(2, t.getTags().size());
            assertTrue(t.containsTag("toDAY"));
        } catch (EmptyStringException e){
            fail("Shouldn't have thrown empty string exception");
        }
    }

    @Test
    public void testSetDescriptionParseSetTask(){
        try {
            toSetTask.setDescription("     A description ## hello; tomorrow; today; toDAY ; important   ; done; tag1");

            assertEquals("     A description ", toSetTask.getDescription());
            assertTrue(toSetTask.getPriority().isImportant());
            assertTrue(toSetTask.getPriority().isUrgent());
            assertEquals("IMPORTANT & URGENT", toSetTask.getPriority().toString());
            assertEquals(tomorrow.toString(), toSetTask.getDueDate().toString());
            assertEquals(Status.DONE, toSetTask.getStatus());
            assertEquals(4, toSetTask.getTags().size());
        } catch (EmptyStringException e){
            fail("Shouldn't have thrown empty string exception");
        }
    }

    @Test
    public void testSetDescriptionParseSetTaskNoDelimiter(){
        try {
            toSetTask.setDescription("     A description");

            assertEquals("     A description", toSetTask.getDescription());
            assertTrue(toSetTask.getPriority().isImportant());
            assertTrue(toSetTask.getPriority().isUrgent());
            assertEquals("IMPORTANT & URGENT", toSetTask.getPriority().toString());
            assertEquals(today.toString(), toSetTask.getDueDate().toString());
            assertEquals(Status.IN_PROGRESS, toSetTask.getStatus());
            assertEquals(2, toSetTask.getTags().size());
        } catch (EmptyStringException e){
            fail("Shouldn't have thrown empty string exception");
        }
    }

    @Test
    public void testSetDescriptionParseSetTaskMultiple(){
        try {
            t.setDescription("     A description");
            t.setDescription("Description ## hello; up next; todo; urgent; today");
            t.setDescription("To do ##Tag1; tomorrow; important");

            assertEquals("To do ", t.getDescription());
            assertTrue(t.getPriority().isImportant());
            assertTrue(t.getPriority().isUrgent());
            assertEquals("IMPORTANT & URGENT", t.getPriority().toString());
            assertEquals(tomorrow.toString(), t.getDueDate().toString());
            assertEquals(Status.UP_NEXT, t.getStatus());
            assertEquals(3, t.getTags().size());
        } catch (EmptyStringException e){
            fail("Shouldn't have thrown empty string exception");
        }
    }

    @Test
    public void testTaskEqualsHashcode(){
        Task sameTask = new Task("description");
        Task sameTask1 = new Task("description ## tomorrow; important; done");
        Task sameTask2 = new Task("description ## tomorrow; important; done");
        Task differentTask = new Task("description ## today; urgent; in progress");
        Task differentTask2 = new Task("description ## tomorrow; urgent; done");
        Task differentTask3 = new Task("a description ## tomorrow; important; in progress");
        Task different4 = new Task("description ## today; important; in progress");

        assertFalse(differentTask.equals(t));
        assertTrue(sameTask1.equals(sameTask2));
        assertFalse(sameTask1.equals(differentTask));
        assertFalse(sameTask1.equals(differentTask2));
        assertFalse(sameTask1.equals(differentTask3));
        assertFalse(differentTask.equals(different4));
        assertEquals(sameTask, t);
        assertTrue(t.equals(new Task("description")));
        assertTrue(t.equals(t));
        assertFalse(t.equals("hello"));
        assertEquals(sameTask.hashCode(), t.hashCode());
    }

    @Test
    public void testThrowInvalidProgressExceptionNegativeInputException(){
        try {
            throw new InvalidProgressException();
        } catch (InvalidProgressException e){
            // expected
        }

        try {
            throw new NegativeInputException();
        } catch (NegativeInputException e){
            // expected
        }
    }
}