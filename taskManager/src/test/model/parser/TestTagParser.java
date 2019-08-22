package parser;

import model.DueDate;
import model.Status;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import parsers.TagParser;
import parsers.exceptions.ParsingException;

import static org.junit.jupiter.api.Assertions.*;

public class TestTagParser {

    private Task t;
    private Task t1;
    private Task withDate;
    private Task aTask;
    private Task notUrgentTask;
    private Task notImportantTask;
    private TagParser parsing;
    private DueDate testTomorrow;
    private DueDate date;

    @BeforeEach
    public void initializeConstructor(){
        t = new Task("This is a description");
        t1 = new Task("This is a descrip ## up next");
        date = new DueDate();
        withDate = new Task("Hello !##today");
        aTask = new Task("Do laundry! ## Tag1; tomorrow; to do; urgent; important; Tag1; tAg1; tag1; in progress; today");
        notUrgentTask = new Task("Buy stickies! ##Tag1; tomorrow; to do; important; today; tag 1");
        notImportantTask = new Task("Buy stickies! ##Tag1; tomorrow; to do; urgent; today; tag 1");
        testTomorrow = new DueDate();
        testTomorrow.postponeOneDay();
        parsing = new TagParser();
    }

    @Test
    public void testExceptionsConstructor() {
        try {
            throw new ParsingException();
        } catch (ParsingException e){}
    }

    @Test
    public void testParseNoDelimiterTask(){
        String output = "{" + "\n" + ("\t" + "Description: This is a description" + "\n"
                + "\t" + "Due date: " + "\n"
                + "\t" + "Status: " + Status.TODO + "\n"
                + "\t" + "Priority: DEFAULT" + "\n"
                + "\t" + "Tags: " + "\n" + "}");

        assertEquals(output, t.toString());
    }

    @Test
    public void testParseOnlyDate(){
        String output = "{" + "\n" + ("\t" + "Description: Hello !" + "\n"
                + "\t" + "Due date: " + date.toString() + "\n"
                + "\t" + "Status: TODO" + "\n"
                + "\t" + "Priority: DEFAULT" + "\n"
                + "\t" + "Tags: ") + "\n" + "}";

        assertEquals(output, withDate.toString());
    }

    @Test
    public void testParseFull(){
        String output = "{" + "\n" + ("\t" + "Description: Do laundry! " + "\n"
                + "\t" + "Due date: " + testTomorrow.toString() + "\n"
                + "\t" + "Status: TODO" + "\n"
                + "\t" + "Priority: IMPORTANT & URGENT" + "\n"
                + "\t" + "Tags: #Tag1, #in progress, #today") + "\n" + "}";

        assertEquals(output, aTask.toString());
    }

    @Test
    public void testSetDescription() {
        t.setDescription("Hello! ## ");
        assertEquals("Hello! ## ", t.getDescription());
    }

    @Test
    public void testParseSpaceNoTags() {
        try {
            parsing.parse("Hello! ## ", t);
        } catch (ParsingException e){
            assertEquals("Hello! ## ", t.getDescription());
            assertEquals("Hello! ## ", parsing.getDescription());
        }
    }

    @Test
    public void testParseOneTagNoSpace(){
        try {
            parsing.parse("Description ##hello", t);
            assertEquals(1, t.getTags().size());
            assertTrue(t.containsTag("hello"));
            assertEquals("Description ", t.getDescription());
        } catch (ParsingException e){
            fail("Should not have thrown parsing exception");
        }
    }

    @Test
    public void testParseNoDelimiterAlreadySet(){
        try{
            parsing.parse("Task description with tags but not delimiter. tag1; tag2", aTask);
            fail("Should've thrown parsing exception");
        } catch(ParsingException e) {
            assertEquals("Task description with tags but not delimiter. tag1; tag2", parsing.getDescription());
            assertEquals("Task description with tags but not delimiter. tag1; tag2", aTask.getDescription());
            assertEquals(Status.TODO, aTask.getStatus());
            assertEquals("IMPORTANT & URGENT", aTask.getPriority().toString());
            assertEquals(3, aTask.getTags().size());
            assertEquals("Doesn't contain tag delimiter", e.getMessage());
        }
    }

    @Test
    public void testParseNoDelimiter(){
        try{
            parsing.parse("Task description with tags but not delimiter. tag1; tag2", t);
            fail("Should've thrown parsing exception");
        } catch(ParsingException e) {
            assertEquals("Task description with tags but not delimiter. tag1; tag2", parsing.getDescription());
            assertEquals("Task description with tags but not delimiter. tag1; tag2", t.getDescription());
            assertEquals(Status.TODO, t.getStatus());
            assertEquals("DEFAULT", t.getPriority().toString());
            assertEquals(0, t.getTags().size());
            assertEquals("Doesn't contain tag delimiter", e.getMessage());
        }
    }

    @Test
    public void testParseDelimiterNoTags(){
        try{
            parsing.parse("This is a description ## ", t);

            assertEquals("This is a description ", parsing.getDescription());
            assertEquals("This is a description ", t.getDescription());
        } catch(ParsingException e) {
            fail("Should not have thrown parsing exception");
        }
    }

    @Test
    public void testParseDelimiterWithOneTagDataDefaultTask(){
        try{
            parsing.parse("Do laundry!## importanT     ", t);
            assertEquals("Do laundry!", parsing.getDescription());
            assertEquals("Do laundry!", t.getDescription());
            assertEquals("IMPORTANT", t.getPriority().toString());
        } catch(ParsingException e) {
            fail("Should not have thrown parsing exception");
        }
    }

    @Test
    public void testParseDelimiterWithOneTagTodoDefaultTask(){
        try{
            parsing.parse("Do laundry!## up next     ", t);
            parsing.parse("Do laundry!## in progress     ", t1);
            parsing.parse("Do laundry!## to do     ", t1);
            parsing.parse("Do laundry!## done     ", t);
            assertEquals("Do laundry!", parsing.getDescription());
            assertEquals("Do laundry!", t.getDescription());
            assertEquals("DEFAULT", t1.getPriority().toString());
            assertEquals(Status.DONE, t.getStatus());
        } catch(ParsingException e) {
            fail("Should not have thrown parsing exception");
        }
    }

    @Test
    public void testParseDelimiterWithOneTagDefaultTask(){
        try{
            parsing.parse("Do laundry!## hello     ", t);
            assertEquals("Do laundry!", parsing.getDescription());
            assertEquals("Do laundry!", t.getDescription());
            assertEquals("DEFAULT", t.getPriority().toString());
            assertTrue(t.containsTag("hello"));
        } catch(ParsingException e) {
            fail("Should not have thrown parsing exception");
        }
    }

    @Test
    public void testParseDelimiterWithMoreTagsDefaultTask(){
        try{
            parsing.parse("Do laundry!## important; in progress; home; to do", t);
            assertEquals("Do laundry!", parsing.getDescription());
            assertEquals("Do laundry!", t.getDescription());
            assertEquals(2, t.getPriority().getQuadrant());
            assertEquals(Status.IN_PROGRESS, t.getStatus());
            assertEquals(2, t.getTags().size());
        } catch(ParsingException e) {
            fail("Should not have thrown parsing exception");
        }
    }

    @Test
    public void testParseDelimiterWithDuplicateTagsNonDefaultTask() {
        try{
            parsing.parse("Do homework!## done; doNE; in Progress; home; urgent; urgent; to do; tag1; Tag 1", aTask);
            assertEquals("Do homework!", parsing.getDescription());
            assertEquals("Do homework!", aTask.getDescription());
            assertEquals(1, aTask.getPriority().getQuadrant());
            assertEquals(Status.DONE, aTask.getStatus());
            assertEquals(6, aTask.getTags().size());
        } catch(ParsingException e) {
            fail("Should not have thrown parsing exception");
        }
    }

    @Test
    public void testParseDelimiterWithSpacingandDuplicateTagsNonDefaultTask() {
        try{
            parsing.parse("     Do homework!    ## in progress; home;important; important; to do;       Tag 1", aTask);
            assertEquals("     Do homework!    ", parsing.getDescription());
            assertEquals("     Do homework!    ", aTask.getDescription());
            assertEquals(1, aTask.getPriority().getQuadrant());
            assertEquals(Status.IN_PROGRESS, aTask.getStatus());
            assertTrue(aTask.containsTag("Tag 1"));
            assertEquals(6, aTask.getTags().size());
        } catch(ParsingException e) {
            fail("Should not have thrown parsing exception");
        }
    }

    @Test
    public void testParseTwice() {
        try{
            parsing.parse("A description ## hello; today", t);
            parsing.parse("     Do homework!    ## in progress; in prOgreSS; home; urgent, to do, tag1, Tag 1", aTask);
            assertEquals("     Do homework!    ", parsing.getDescription());
            assertEquals("     Do homework!    ", aTask.getDescription());
            assertEquals(1, aTask.getPriority().getQuadrant());
            assertEquals(Status.IN_PROGRESS, aTask.getStatus());
            assertEquals(5, aTask.getTags().size());
        } catch(ParsingException e) {
            fail("Should not have thrown parsing exception");
        }
    }

    @Test
    public void testParseOrderOfTags(){
        try{
            parsing.parse("Some description ## tAg1; tag1; TAG1; tag1", t);
            assertEquals(1, t.getTags().size());
            assertTrue(t.containsTag("tAg1"));
        } catch(ParsingException e){
            fail("Should not have thrown parsing exception");
        }
    }

    @Test
    public void testParseLeadingWhiteSpaceTags(){
        try{
            parsing.parse("Something to test## tag1     ; tag 1;     tag1;    ;   tag 2", t);
            assertEquals(3, t.getTags().size());
            assertTrue(t.containsTag("tag1"));
            assertTrue(t.containsTag("tag 1"));
            assertTrue(t.containsTag("tag 2"));
        } catch (ParsingException e) {
            fail("Should not have thrown parsing exception");
        }
    }

    @Test
    public void testParseNormal() {
        try{
            parsing.parse("Some description ## tag1;today;urGent;in progress;important", t);
            assertEquals("IMPORTANT & URGENT", t.getPriority().toString());
            assertEquals(date.toString(), t.getDueDate().toString());
            assertEquals(Status.IN_PROGRESS, t.getStatus());
            assertEquals(1, t.getTags().size());
            assertTrue(t.containsTag("tag1"));
        } catch (ParsingException e) {
            fail("Should not have thrown parsing exception");
        }
    }

    @Test
    public void testParseMultipleDuplicates(){
        try{
            parsing.parse("Some description ## tag1;tomorrow;up next;toDay;in progress", t);
            assertEquals("Some description ", parsing.getDescription());
            assertEquals("Some description ", t.getDescription());
            assertEquals(testTomorrow.toString(), t.getDueDate().toString());
            assertEquals(Status.UP_NEXT, t.getStatus());
            assertEquals(3, t.getTags().size());
            assertTrue(t.containsTag("tag1"));
            assertTrue(t.containsTag("today"));
            assertTrue(t.containsTag("in progress"));
        } catch (ParsingException e){
            fail("Should not have thrown parsing exception");
        }

    }

    @Test
    public void testParseWithDate(){
        try{
            parsing.parse("Something to test## tag1     ; tag 1;     tag1;    ;   tag 2", withDate);
            assertEquals("Something to test", withDate.getDescription());
            assertEquals(date.toString(), withDate.getDueDate().toString());
            assertEquals(3, withDate.getTags().size());
            assertTrue(withDate.containsTag("tag1"));
            assertTrue(withDate.containsTag("tag 1"));
            assertTrue(withDate.containsTag("tag 2"));
        } catch (ParsingException e) {
            fail("Should not have thrown parsing exception");
        }
    }

    @Test
    public void testParseWithTagsAlready(){
        try{
            parsing.parse("Something to test## tag 2; some tags; cool ; urgent", notUrgentTask);
            assertEquals("Something to test", notUrgentTask.getDescription());
            assertEquals(testTomorrow.toString(), notUrgentTask.getDueDate().toString());
            assertEquals(1, notUrgentTask.getPriority().getQuadrant());
            assertEquals(6, notUrgentTask.getTags().size());
            assertTrue(notUrgentTask.containsTag("tag 2"));
            assertTrue(notUrgentTask.containsTag("today"));
            assertTrue(notUrgentTask.containsTag("cool"));
        } catch (ParsingException e) {
            fail("Should not have thrown parsing exception");
        }
    }

    @Test
    public void testParseWithTagsAlreadyNotImportant(){
        try{
            parsing.parse("Something to test## tag 2; some tags; cool ;;", notImportantTask);
            assertEquals("Something to test", notImportantTask.getDescription());
            assertEquals(testTomorrow.toString(), notImportantTask.getDueDate().toString());
            assertEquals(3, notImportantTask.getPriority().getQuadrant());
            assertEquals(6, notImportantTask.getTags().size());
            assertTrue(notImportantTask.containsTag("tag 2"));
            assertTrue(notImportantTask.containsTag("today"));
            assertTrue(notImportantTask.containsTag("cool"));
        } catch (ParsingException e) {
            fail("Should not have thrown parsing exception");
        }
    }

    @Test
    public void testParseTodo(){
        try {
            parsing.parse("Test ## todo; toDO; to do; to_do; to Do; TO DO; to DO", t);
            assertEquals(2, t.getTags().size());
            assertTrue(t.containsTag("todo"));
            assertTrue(t.containsTag("to_do"));
            assertEquals(Status.TODO, t.getStatus());
        } catch (ParsingException e){
            fail("Should not have thrown parsing exception");
        }
    }
}
