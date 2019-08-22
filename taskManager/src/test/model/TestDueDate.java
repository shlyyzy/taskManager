package model;

import model.exceptions.InvalidTimeException;
import model.exceptions.NullArgumentException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class TestDueDate {

    private DueDate d;
    private DueDate nullDate;
    private DueDate specified;
    private DueDate endOfMonth;
    private Date aDate;
    private Date endOfMonthDate;
    private Calendar anotherCal;
    private Calendar newCal;
    private Calendar endCal;


    @BeforeEach
    public void initializeDueDate() {
        d = new DueDate();
        newCal = Calendar.getInstance();
        endCal = Calendar.getInstance();

        newCal.set(Calendar.YEAR, 2019);
        newCal.set(Calendar.MONTH, Calendar.FEBRUARY);
        newCal.set(Calendar.DAY_OF_MONTH, 8);
        newCal.set(Calendar.HOUR, 11);
        newCal.set(Calendar.MINUTE, 30);
        newCal.set(Calendar.SECOND, 0);
        newCal.set(Calendar.AM_PM, Calendar.AM);
        aDate = newCal.getTime(); // gets time from calendar and sets to date
        specified = new DueDate(aDate);

        endCal.set(Calendar.YEAR, 2019);
        endCal.set(Calendar.MONTH, Calendar.JULY);
        endCal.set(Calendar.DAY_OF_MONTH, 31);
        endCal.set(Calendar.HOUR, 11);
        endCal.set(Calendar.MINUTE, 30);
        endCal.set(Calendar.SECOND, 0);
        endCal.set(Calendar.MILLISECOND, 0);
        endOfMonthDate = endCal.getTime();
        endOfMonth = new DueDate(endOfMonthDate);

    }

    @Test
    public void testConstructorToday() {
        assertEquals(11, d.getHour());
        assertEquals(59, d.getMinute());
        assertTrue(d.isDueToday());
    }

    @Test
    public void testConstructorLater() {
        assertEquals(1, specified.getMonth());
        assertEquals(8, specified.getDay());
        assertEquals(11, specified.getHour());
        assertEquals(30, specified.getMinute());

    }

    @Test
    public void testDueDateNull(){
        try {
            nullDate = new DueDate(null);
            fail("Should've thrown null exception");
        } catch(NullArgumentException e){
        }
    }

    @Test
    public void testDueDateNotNull(){
        try {
            nullDate = new DueDate();
        } catch(NullArgumentException e){
            fail("Should not have thrown null exception");
        }
    }

    @Test
    public void testsetDueDateRightNow() {
        anotherCal = Calendar.getInstance();
        Date newDate = anotherCal.getTime();

        try {
            specified.setDueDate(newDate);
            int a = anotherCal.get(Calendar.HOUR);
            int b = anotherCal.get(Calendar.MINUTE);

            assertEquals(a, specified.getHour());
            assertEquals(b, specified.getMinute());

            assertTrue(specified.isDueToday());
        } catch(NullArgumentException e) {
            fail("Should not have thrown null exception");
        }

    }

    @Test
    public void testsetDueDateNull() {
        try {
            specified.setDueDate(null);
            fail("Should have thrown exception");
        } catch(NullArgumentException e) {
            assertEquals("Null DueDate", e.getMessage());
        }

    }

    @Test
    public void testSetDueTimeDifferent() {
        try {
            specified.setDueTime(11, 30);

            assertEquals(11, specified.getHour());
            assertEquals(30, specified.getMinute());
            assertEquals(1, specified.getMonth());
            assertEquals(8, specified.getDay());
        } catch(InvalidTimeException e){
            fail("Shouldn't have thrown exception");
        }
    }

    @Test
    public void testSetDueTimeSame() {
        try {
            d.setDueTime(11, 59);
            assertTrue(d.isDueToday());
            assertEquals(11, d.getHour());
            assertEquals(59, d.getMinute());
        } catch(InvalidTimeException e){
            fail("Shouldn't have thrown exception");
        }
    }

    @Test
    public void testSetDueTimeHourNegandErrorMessage() {
        try {
            d.setDueTime(-1, 59);
            fail("Should have thrown exception");
        } catch(InvalidTimeException e){
            assertEquals("Time should be within 0-23 hours and 0-59 minutes", e.getMessage());
            assertTrue(d.isDueToday());
            assertEquals(11, d.getHour());
            assertEquals(59, d.getMinute());
            assertEquals("Time should be within 0-23 hours and 0-59 minutes", e.getMessage());
        }
    }

    @Test
    public void testSetDueTimeMinuteNeg() {
        try {
            d.setDueTime(3, -30);
            fail("Should have thrown exception");
        } catch(InvalidTimeException e){
            assertTrue(d.isDueToday());
            assertEquals(11, d.getHour());
            assertEquals(59, d.getMinute());
        }

    }

    @Test
    public void testSetDueTimeWrong() {
        try {
            d.setDueTime(99, 59);
            fail("should have thrown exception");
        } catch(InvalidTimeException e){
            assertTrue(d.isDueToday());
            assertEquals(11, d.getHour());
            assertEquals(59, d.getMinute());
        }
    }

    @Test
    public void testSetDueTimeWrongTwo() {
        try {
            d.setDueTime(11, 90);
            assertTrue(d.isDueToday());
            assertEquals(11, d.getHour());
            assertEquals(59, d.getMinute());
        } catch(InvalidTimeException e){
            assertTrue(d.isDueToday());
            assertEquals(11, d.getHour());
            assertEquals(59, d.getMinute());
        }
    }

    @Test
    public void testisDueTodayTrue(){
        assertTrue(d.isDueToday());
    }

    @Test
    public void testisDueTodayFalseMonth(){
        anotherCal = Calendar.getInstance();
        int nextmonth = anotherCal.get(Calendar.MONTH) + 4;
        anotherCal.set(Calendar.MONTH, nextmonth);
        Date newDate = anotherCal.getTime();

        d.setDueDate(newDate);
        assertFalse(d.isDueToday());
    }

    @Test
    public void testisDueTodayTrueMonth(){
        anotherCal = Calendar.getInstance();
        int samemonth = anotherCal.get(Calendar.MONTH);
        anotherCal.set(Calendar.MONTH, samemonth);
        Date newDate = anotherCal.getTime();

        d.setDueDate(newDate);
        assertTrue(d.isDueToday());
    }

    @Test
    public void testisDueTodayFalseMonthYear(){
        anotherCal = Calendar.getInstance();
        int nextmonth = anotherCal.get(Calendar.MONTH) - 1;
        anotherCal.set(Calendar.MONTH, nextmonth);
        int nextyear = anotherCal.get(Calendar.YEAR) + 1;
        anotherCal.set(Calendar.YEAR, nextyear);
        Date newDate = anotherCal.getTime();

        d.setDueDate(newDate);
        assertFalse(d.isDueToday());
    }

    @Test
    public void testisDueTodayFalseYear(){
        anotherCal = Calendar.getInstance();
        int nextyear = anotherCal.get(Calendar.YEAR) + 1;
        anotherCal.set(Calendar.YEAR, nextyear);
        Date newDate = anotherCal.getTime();

        d.setDueDate(newDate);
        assertFalse(d.isDueToday());
    }

    @Test
    public void testSetDueDateLarger() {
        try {
            d.setDueTime(40, 1203);
        } catch(InvalidTimeException e){
            assertEquals(11, d.getHour());
            assertEquals(59, d.getMinute());
            assertTrue(d.isDueToday());
        }
    }

    @Test
    public void testpostponeOneDay() {
        specified.postponeOneDay();

        assertEquals(9, specified.getDay());
        assertEquals(11, specified.getHour());
        assertEquals(30, specified.getMinute());
        assertEquals(1, specified.getMonth());
    }

    @Test
    public void testpostponeOneDayEndOfMonth() {
        endOfMonth.postponeOneDay();

        assertEquals(1, endOfMonth.getDay());
        assertEquals(11, endOfMonth.getHour());
        assertEquals(30, endOfMonth.getMinute());
        assertEquals(Calendar.AUGUST, endOfMonth.getMonth());
    }

    @Test
    public void testpostpone21Days() {

        for (int x = 0; x < 21; x++){
            specified.postponeOneDay();
        }

        assertEquals(1, specified.getDay());
        assertEquals(11, specified.getHour());
        assertEquals(30, specified.getMinute());
        assertEquals(2, specified.getMonth());
    }

    @Test
    public void testpostponeOneWeek(){
        specified.postponeOneWeek();

        assertEquals(15, specified.getDay());
        assertEquals(11, specified.getHour());
        assertEquals(30, specified.getMinute());
        assertEquals(1, specified.getMonth());
    }

    @Test
    public void testpostponeOneWeekEndOfMonth(){
        endOfMonth.postponeOneWeek();
        // assertEquals(1565202600000L, endOfMonth.getDate().getTime());

        assertEquals(7, endOfMonth.getDay());
        assertEquals(11, endOfMonth.getHour());
        assertEquals(30, endOfMonth.getMinute());
        assertEquals(Calendar.AUGUST, endOfMonth.getMonth());
    }

    @Test
    public void testpostponeOneWeekEndOfYear(){
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_MONTH, 31);
        c.set(Calendar.MONTH, Calendar.DECEMBER);
        c.set(Calendar.YEAR, 2019);
        c.set(Calendar.HOUR_OF_DAY, 10);
        c.set(Calendar.MINUTE, 30);
        endOfMonth = new DueDate(c.getTime());

        endOfMonth.postponeOneWeek();
    }

    @Test
    public void testpostponeThreeWeeks(){
        for (int x = 0; x < 3; x++){
            specified.postponeOneWeek();
        }

        assertEquals(1, specified.getDay());
        assertEquals(11, specified.getHour());
        assertEquals(30, specified.getMinute());
        assertEquals(2, specified.getMonth());
    }

    @Test
    public void testisOverdueNot(){
        anotherCal = Calendar.getInstance();
        anotherCal.set(2022, Calendar.JANUARY, 24);
        anotherCal.set(Calendar.HOUR, 12);
        anotherCal.set(Calendar.MINUTE, 12);
        Date newD = anotherCal.getTime();

        d.setDueDate(newD);
        assertFalse(d.isOverdue());
    }

    @Test
    public void testisOverdue(){
        anotherCal = Calendar.getInstance();
        anotherCal.set(2018, Calendar.JANUARY, 24);
        anotherCal.set(Calendar.HOUR, 12);
        anotherCal.set(Calendar.MINUTE, 12);
        Date newD = anotherCal.getTime();

        d.setDueDate(newD);
        assertTrue(d.isOverdue());
    }

    @Test
    public void testisOverdueTodayFalse(){
        assertFalse(d.isOverdue());
    }

    @Test
    public void testisDueTomorrowFalse(){
        assertFalse(d.isDueTomorrow());
    }

    @Test
    public void testisDueTomorrowTrue(){
        anotherCal = Calendar.getInstance();
        int nextDay = anotherCal.get(Calendar.DAY_OF_MONTH) + 1;
        anotherCal.set(Calendar.DAY_OF_MONTH, nextDay);
        Date newDate = anotherCal.getTime();

        d.setDueDate(newDate);
        assertTrue(d.isDueTomorrow());
    }

    @Test
    public void testisDueTomorrowFalseYear(){
        anotherCal = Calendar.getInstance();
        int nextDay = anotherCal.get(Calendar.DAY_OF_MONTH) + 1;
        anotherCal.set(Calendar.DAY_OF_MONTH, nextDay);
        anotherCal.set(Calendar.YEAR, 2011);
        Date newDate = anotherCal.getTime();

        d.setDueDate(newDate);
        assertFalse(d.isDueTomorrow());
    }

    @Test
    public void testisDueTomorrowFalseMonth(){
        anotherCal = Calendar.getInstance();
        int nextDay = anotherCal.get(Calendar.DAY_OF_MONTH) + 1;
        anotherCal.set(Calendar.DAY_OF_MONTH, nextDay);
        anotherCal.set(Calendar.MONTH, d.getMonth() - 1);
        Date newDate = anotherCal.getTime();

        d.setDueDate(newDate);
        assertFalse(d.isDueTomorrow());
    }

    @Test
    public void testIsDueWithinAWeekFalse(){
        anotherCal = Calendar.getInstance();
        int nextDay = anotherCal.get(Calendar.DAY_OF_MONTH) + 7;
        anotherCal.set(Calendar.DAY_OF_MONTH, nextDay);
        Date newDate = anotherCal.getTime();

        d.setDueDate(newDate);
        assertFalse(d.isDueWithinAWeek());
    }

    @Test
    public void testIsDueWithinAWeekTrueToday(){
        assertTrue(d.isDueWithinAWeek());
    }

    @Test
    public void testIsDueWithinAWeekTrue(){
        anotherCal = Calendar.getInstance();
        int nextDay = anotherCal.get(Calendar.DAY_OF_MONTH) + 6;
        anotherCal.set(Calendar.DAY_OF_MONTH, nextDay);
        Date newDate = anotherCal.getTime();

        d.setDueDate(newDate);
        assertTrue(d.isDueWithinAWeek());
    }

    @Test
    public void testIsDueWithinAWeekTrueHour(){
        anotherCal = Calendar.getInstance();
        anotherCal.set(Calendar.HOUR, 12);
        anotherCal.set(Calendar.MINUTE, 0);
        anotherCal.set(Calendar.AM_PM, Calendar.AM);
        Date newDate = anotherCal.getTime();

        d.setDueDate(newDate);
        assertTrue(d.isDueWithinAWeek());
    }

    @Test
    public void testIsDueWithinAWeekFalseNineDays(){
        anotherCal = Calendar.getInstance();
        anotherCal.set(Calendar.DAY_OF_MONTH, anotherCal.get(Calendar.DAY_OF_MONTH) + 9);
        Date newDate = anotherCal.getTime();

        d.setDueDate(newDate);
        assertFalse(d.isDueWithinAWeek());
    }

    @Test
    public void testIsDueWithinAWeekFalseNegative(){
        anotherCal = Calendar.getInstance();
        int day = anotherCal.get(Calendar.DAY_OF_MONTH);
        anotherCal.set(Calendar.DAY_OF_MONTH, day - (day+2));
        Date newDate = anotherCal.getTime();

        d.setDueDate(newDate);
        assertFalse(d.isDueWithinAWeek());
    }

    @Test
    public void testIsDueWithinAWeekFalseMonth(){
        anotherCal = Calendar.getInstance();
        int nextDay = anotherCal.get(Calendar.DAY_OF_MONTH) + 6;
        anotherCal.set(Calendar.DAY_OF_MONTH, nextDay);
        anotherCal.set(Calendar.MONTH, d.getMonth() - 1);
        Date newDate = anotherCal.getTime();

        d.setDueDate(newDate);
        assertFalse(d.isDueWithinAWeek());
    }

    @Test
    public void testIsDueWithinAWeekFalseYear(){
        anotherCal = Calendar.getInstance();
        int nextDay = anotherCal.get(Calendar.DAY_OF_MONTH) + 6;
        anotherCal.set(Calendar.DAY_OF_MONTH, nextDay);
        anotherCal.set(Calendar.YEAR, anotherCal.get(Calendar.YEAR) - 1);
        Date newDate = anotherCal.getTime();

        d.setDueDate(newDate);
        assertFalse(d.isDueWithinAWeek());
    }


    @Test
    public void testtoString(){
        anotherCal = Calendar.getInstance();
        anotherCal.set(Calendar.DAY_OF_MONTH, 10);
        anotherCal.set(Calendar.MONTH, Calendar.FEBRUARY);
        anotherCal.set(Calendar.HOUR, 11);
        anotherCal.set(Calendar.MINUTE, 30);
        anotherCal.set(Calendar.SECOND, 0);
        anotherCal.set(Calendar.AM_PM, Calendar.AM);

        String theDate = "Sun Feb 10 2019 11:30 AM";

        Date newDate = anotherCal.getTime();
        specified.setDueDate(newDate);

        assertEquals(theDate, specified.toString());
    }

    @Test
    public void testtoStringPM(){
        anotherCal = Calendar.getInstance();
        anotherCal.set(Calendar.DAY_OF_MONTH, 9);
        anotherCal.set(Calendar.MONTH, Calendar.FEBRUARY);
        anotherCal.set(Calendar.HOUR, 11);
        anotherCal.set(Calendar.MINUTE, 30);
        anotherCal.set(Calendar.SECOND, 0);
        anotherCal.set(Calendar.AM_PM, Calendar.PM);

        String theDate = "Sat Feb 09 2019 11:30 PM";

        Date newDate = anotherCal.getTime();
        specified.setDueDate(newDate);

        assertEquals(theDate, specified.toString());
    }


    @Test
    public void testtoStringZero(){
        anotherCal = Calendar.getInstance();
        anotherCal.set(Calendar.DAY_OF_MONTH, 9);
        anotherCal.set(Calendar.MONTH, Calendar.FEBRUARY);
        anotherCal.set(Calendar.HOUR, 3);
        anotherCal.set(Calendar.MINUTE, 30);
        anotherCal.set(Calendar.SECOND, 0);
        anotherCal.set(Calendar.AM_PM, Calendar.PM);

        String theDate = "Sat Feb 09 2019 03:30 PM";

        Date newDate = anotherCal.getTime();
        specified.setDueDate(newDate);

        assertEquals(theDate, specified.toString());
    }


}
