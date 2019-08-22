package model;
import model.exceptions.EmptyStringException;
import model.exceptions.NullArgumentException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.util.Iterator;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

public class TestProject {
    private Project project;
    private Project p;
    private Project anotherp;
    private Task t;
    private Task a;

    @BeforeEach
    public void initializeProject(){
        project = new Project("project");
        p = new Project("project");
        t = new Task("description");
        a = new Task("hello");
    }

    @Test
    public void testConstructor() {
        assertEquals("project", project.getDescription());
        assertEquals(new Priority(4), project.getPriority());
    }

    @Test
    public void testNonEmptyDescription() {
        try {
            anotherp = new Project("hello!");
            assertEquals("hello!", anotherp.getDescription());
        } catch(EmptyStringException e) {
            fail("Should not have thrown exception");
        }
    }

    @Test
    public void testEmptyDescription() {
        try {
            anotherp = new Project("");
            fail("Should've thrown Exception");
        } catch(EmptyStringException e) {
            // expected
        }
    }

    @Test
    public void testnullDescription() {
        try {
            anotherp = new Project(null);
            fail("Should've thrown Exception");
        } catch (EmptyStringException e) {
            // expected
        }
    }

    @Test
    public void testAdd() {
        try{
            project.add(t);
            assertEquals(1, project.getNumberOfTasks());
        } catch(NullArgumentException e) {
            fail("Shouldn't have thrown that exception");
        }
    }

    @Test
    public void testAddNullandErrorMessage(){
        try{
            project.add(null);
            fail("Should have thrown an exception");
        } catch(NullArgumentException e) {
            assertEquals("Illegal argument: task is null", e.getMessage());
        }
    }

    @Test
    public void testAddSame(){
        try {
            project.add(t);
            project.add(t);
            assertTrue(project.contains(t));
            assertEquals(1, project.getNumberOfTasks());
        } catch(NullArgumentException e){
            fail("Shouldn't have thrown that exception");
        }
    }

    @Test
    public void testRemove(){
        project.add(t);

        try {
            project.remove(t);
            assertFalse(project.contains(t));
            assertEquals(0, project.getNumberOfTasks());
        } catch(NullArgumentException e){
            fail("Shouldn't have thrown that!");
        }
    }

    @Test
    public void testRemoveNull(){
        project.add(t);

        try {
            project.remove(null);
            fail("Should have thrown an exception");
        } catch(NullArgumentException e){
            // expected
        }
    }

    @Test
    public void testRemoveNone(){
        project.add(t);
        try {
            project.remove(a);
            assertFalse(project.contains(a));
        } catch(NullArgumentException e){
            fail("Shouldn't have thrown that!");
        }
    }

    @Test
    public void testgetProgressALL(){
        a.setStatus(Status.DONE);
        t.setStatus(Status.DONE);
        project.add(a);
        project.add(t);

        assertEquals(0, project.getProgress());
    }

    @Test
    public void testgetNumberOfTasksZero(){
        assertEquals(0, project.getNumberOfTasks());
    }

    @Test
    public void testgetNumberOfTasksOne(){
        project.add(t);
        assertEquals(1, project.getNumberOfTasks());
    }

   /* @Test
    public void testisCompleted(){
        assertFalse(project.isCompleted());
    }

    @Test
    public void testisCompletedFalse(){
        a.setStatus(Status.DONE);
        project.add(t);
        project.add(a);
        assertFalse(project.isCompleted());
    }

    @Test
    public void testisCompletedTrueBoth(){
        a.setStatus(Status.DONE);
        t.setStatus(Status.DONE);
        project.add(t);
        project.add(a);
        assertTrue(project.isCompleted());
    }*/

    @Test
    public void testContainsFalse(){
        try {
            assertFalse(project.contains(a));
        } catch(NullArgumentException e){
            fail("Shouldn't have thrown an exception");
        }
    }

    @Test
    public void testContainsTrue(){
        project.add(a);
        try {
            assertTrue(project.contains(a));
        } catch(NullArgumentException e){
            fail("Shouldn't have thrown an exception");
        }
    }

    @Test
    public void testContainsTrueMore(){
        project.add(a);
        project.add(t);
        try {
            assertTrue(project.contains(a));
        } catch(NullArgumentException e){
            fail("Shouldn't have thrown an exception");
        }
    }

    @Test
    public void testContainsNull(){
        project.add(a);
        project.add(t);
        try {
            assertTrue(project.contains(null));
            fail("Should've thrown an exception");
        } catch(NullArgumentException e){
            // expected
        }
    }

    @Test
    public void testSameProject(){
        project.add(p);
        assertEquals(0, project.getNumberOfTasks());
    }

    @Test
    public void testAddProject(){
        Project proj = new Project("a project");

        try {
            project.add(proj);
            project.add(a);
            assertTrue(project.contains(proj));
            assertTrue(project.contains(a));
            assertEquals(2, project.getNumberOfTasks());
            assertTrue(project.contains(new Project("a project")));
        } catch (NullArgumentException e){
            fail("Shouldn't have thrown exception");
        }
    }

    @Test
    public void testAddProjectNull(){
        Project proj = null;

        try {
            project.add(proj);
            fail("Should've thrown exception!");
        } catch (NullArgumentException e){
            assertEquals("Illegal argument: task is null", e.getMessage());
        }
    }

    @Test
    public void testAddProjectToItself(){
        try {
            project.add(project);
            project.add(a);
            assertTrue(project.contains(a));
            assertFalse(project.contains(project));
            assertEquals(1, project.getNumberOfTasks());
        } catch (NullArgumentException e){
            fail("Shouldn't have thrown exception");
        }
    }

    @Test
    public void testGetProgressNone(){
        project.add(a);
        assertEquals(0, project.getProgress());
    }

    @Test
    public void testGetProgressNoTasks(){
        assertEquals(0, project.getProgress());
    }

    @Test
    public void testGetProgressDone(){
        a.setProgress(100);
        project.add(a);
        assertEquals(100, project.getProgress());
    }

    @Test
    public void testGetProgressThreeNone(){
        Task t1 = new Task("some task");
        Task t2 = new Task("another task");
        Task t3 = new Task("third task");

        project.add(t1);
        project.add(t2);
        project.add(t3);

        assertEquals(0, project.getProgress());
    }

    @Test
    public void testGetProgressThreeOneDone(){
        Task t1 = new Task("some task");
        Task t2 = new Task("another task");
        t2.setProgress(100);
        Task t3 = new Task("third task");

        project.add(t1);
        project.add(t2);
        project.add(t3);

        assertEquals(33, project.getProgress());
    }

    @Test
    public void testGetProgressThree(){
        Task t1 = new Task("some task");
        t1.setProgress(100);
        Task t2 = new Task("another task!");
        t2.setProgress(50);
        Task t3 = new Task("third task");
        t3.setProgress(25);

        project.add(t1);
        project.add(t2);
        project.add(t3);

        assertEquals(58, project.getProgress());
    }

    @Test
    public void testGetProgressNestedProject(){
        Task t1 = new Task("some task!");
        t1.setProgress(100);
        Task t2 = new Task("another task!");
        t2.setProgress(50);
        Task t3 = new Task("third task");
        t3.setProgress(25);

        Project anotherproject = new Project("some other one");
        Task t4 = new Task("this is task 4");
        anotherproject.add(t4);

        project.add(t1);
        project.add(t2);
        project.add(t3);

        anotherproject.add(project);

        assertEquals(29, anotherproject.getProgress());
    }

    @Test
    public void testGetEstimatedTime(){
        assertEquals(0, project.getEstimatedTimeToComplete());
    }

    @Test
    public void testGetEstimatedTimeThree(){
        Task t1 = new Task("some task");
        Task t2 = new Task("another task");
        Task t3 = new Task("third task");

        project.add(t1);
        project.add(t2);
        project.add(t3);

        assertEquals(0, project.getEstimatedTimeToComplete());
    }

    @Test
    public void testGetEstimatedTimeMoreThanZero(){
        Task t1 = new Task("some task");
        Task t2 = new Task("another task");
        Task t3 = new Task("third task");

        t1.setEstimatedTimeToComplete(8);
        t2.setEstimatedTimeToComplete(10);
        t3.setEstimatedTimeToComplete(2);

        project.add(t1);
        project.add(t2);
        project.add(t3);

        assertEquals(20, project.getEstimatedTimeToComplete());
    }

    @Test
    public void testGetEstimatedTimeSubProject(){
        Task t1 = new Task("some task");
        Task t2 = new Task("another task");
        Task t3 = new Task("third task");

        t1.setEstimatedTimeToComplete(8);
        t2.setEstimatedTimeToComplete(10);
        t3.setEstimatedTimeToComplete(2);

        Project anotherProject = new Project("another one");
        Task t4 = new Task("this is for p2");
        t4.setEstimatedTimeToComplete(4);
        anotherProject.add(t4);

        project.add(t1);
        project.add(t2);
        project.add(t3);
        anotherProject.add(project);

        assertEquals(24, anotherProject.getEstimatedTimeToComplete());
    }

    @Test
    public void testIsCompletedFalse(){
        assertFalse(project.isCompleted());
    }

    @Test
    public void testIsCompletedTrue(){
        Task t1 = new Task("some task");
        Task t2 = new Task("another task");
        Task t3 = new Task("third task");

        project.add(t1);
        project.add(t2);
        project.add(t3);

        t1.setProgress(100);
        t2.setProgress(100);
        t3.setProgress(100);

        assertTrue(project.isCompleted());
    }

    @Test
    public void testIsCompletedFalseTasks(){
        Task t1 = new Task("some task");

        project.add(t1);
        t1.setProgress(99);

        assertFalse(project.isCompleted());
    }

    @Test
    public void testIsCompletedFalseSubProject(){
        Task t1 = new Task("some task");

        Project anotherProject = new Project("another one!");
        Task t2 = new Task("one for another project");

        project.add(t1);

        t1.setProgress(99);
        t2.setProgress(100);

        anotherProject.add(project);

        assertFalse(project.isCompleted());
    }

    @Test
    public void testIsCompletedTrueSubProject(){
        Task t1 = new Task("some task");

        Project anotherProject = new Project("another one!");
        Task t2 = new Task("one for another project");

        project.add(t1);

        t1.setProgress(100);
        t2.setProgress(100);

        anotherProject.add(t2);
        anotherProject.add(project);

        assertTrue(anotherProject.isCompleted());
    }

    @Test
    public void testGetTasksDeprecated(){
        try {
            project.getTasks();
            fail("Should've thrown exception!");
        } catch (UnsupportedOperationException e) {
            // expected
        }
    }

    @Test
    public void testSetPriority(){
        try {
            project.setPriority(new Priority(1));
            assertEquals(new Priority(1), project.getPriority());
        } catch (NullArgumentException e) {
            fail("Shouldn't have thrown exception!");
        }
    }

    @Test
    public void testSetPriorityNull(){
        Priority pri = null;
        try {
            project.setPriority(pri);
            fail("Should've thrown exception!");
        } catch (NullArgumentException e) {
            assertEquals("Illegal argument: priority is null", e.getMessage());
        }
    }

    @Test
    public void testIteratorNone(){
        assertFalse(project.iterator().hasNext());
    }

    @Test
    public void testIteratorOneImportant(){
        Task aTask = new Task("something ## important");

        project.add(aTask);

        Iterator iterator = project.iterator();

        assertTrue(iterator.hasNext());
        assertTrue(aTask.equals(iterator.next()));

        assertFalse(iterator.hasNext());
    }

    @Test
    public void testIteratorNoImportantOrUrgent(){
        project.add(a);
        project.add(t);

        Iterator iterator = project.iterator();

        assertTrue(iterator.hasNext());
        assertTrue(a.equals(iterator.next()));

        assertTrue(iterator.hasNext());
        assertTrue(t.equals(iterator.next()));

        assertFalse(iterator.hasNext());

    }

    @Test
    public void testIteratorOneImportantOneUrgentMultipleIterators(){
        Task aTask = new Task("important ## important");
        Task uTask = new Task("urgent ## urgent");

        project.add(aTask);
        project.add(uTask);

        Iterator iterator = project.iterator();

        assertTrue(iterator.hasNext());
        assertTrue(aTask.equals(iterator.next()));

        assertTrue(iterator.hasNext());
        assertTrue(uTask.equals(iterator.next()));

        assertFalse(iterator.hasNext());

        Iterator newIterator = project.iterator();

        assertTrue(newIterator.hasNext());
        assertTrue(aTask.equals(newIterator.next()));

        assertTrue(newIterator.hasNext());
        assertTrue(uTask.equals(newIterator.next()));

        assertFalse(newIterator.hasNext());
    }

    @Test
    public void testIteratorMultipleImportantUrgent(){
        Task iTask = new Task("first important one ## important");
        Task iTask2 = new Task("second important one ## important; tomorrow");
        Project iProject = new Project("third important one");
        iProject.setPriority(new Priority(2));
        Task uTask = new Task("first urgent one ## urgent; tomorrow");
        Task uTask2 = new Task("second urgent one ## urgent; tomorrow");

        project.add(iTask);
        project.add(uTask);
        project.add(iProject);
        project.add(uTask2);
        project.add(iTask2);

        Iterator iterator = project.iterator();

        assertTrue(iterator.hasNext());
        assertTrue(iTask.equals(iterator.next()));

        assertTrue(iterator.hasNext());
        assertEquals(iProject, iterator.next());

        assertTrue(iterator.hasNext());
        assertEquals(iTask2, iterator.next());

        assertTrue(iterator.hasNext());
        assertEquals(uTask, iterator.next());

        assertTrue(iterator.hasNext());
        assertEquals(uTask2, iterator.next());

        assertFalse(iterator.hasNext());
    }

    @Test
    public void testIterator(){

        anotherp = new Project("another project");
        anotherp.setPriority(new Priority(1));
        t.setPriority(new Priority(3));
        Task aTask = new Task("something ## important");
        Task veryImportant = new Task("for the tests ## important; urgent");
        Task subTask = new Task("a subtask ## urgent");
        Task newTask = new Task("three in order! ## important; urgent");

        anotherp.add(subTask);
        project.add(anotherp);
        project.add(a);
        project.add(t);
        project.add(aTask);
        project.add(veryImportant);
        project.add(newTask);

        Iterator iterator = project.iterator();

        assertTrue(iterator.hasNext());
        assertTrue(anotherp.equals(iterator.next()));

        assertTrue(iterator.hasNext());
        assertEquals(veryImportant, iterator.next());

        assertTrue(iterator.hasNext());
        assertEquals(newTask, iterator.next());

        assertTrue(iterator.hasNext());
        assertEquals(aTask, iterator.next());

        assertTrue(iterator.hasNext());
        assertEquals(t, iterator.next());

        assertTrue(iterator.hasNext());
        assertEquals(a, iterator.next());

        assertFalse(iterator.hasNext());

        try {
            iterator.next();
            fail("should've thrown exception!");
        } catch (NoSuchElementException e) {
            // expected
        }
    }

    @Test
    public void testHashcodeEquals(){
        Project proj = new Project("project");
        Project notproject = new Project("another project");

        assertEquals(project.hashCode(), proj.hashCode());
        assertFalse(project.hashCode() == notproject.hashCode());
        assertFalse(project.equals(notproject));
    }
}