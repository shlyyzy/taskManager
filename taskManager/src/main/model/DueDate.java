package model;

import model.exceptions.InvalidTimeException;
import model.exceptions.NullArgumentException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;

// Note: Any library in JDK 8 may be used to implement this class, however,
//     you must not use any third-party library in your implementation
// Hint: Explore https://docs.oracle.com/javase/8/docs/api/java/util/Calendar.html

// Represents the due date of a Task
public class DueDate {

    private Calendar cal;
    private Calendar rightNow;


    // MODIFIES: this
    // EFFECTS: creates a DueDate with deadline at end of day today (i.e., today at 11:59 PM)
    public DueDate() {
        cal = Calendar.getInstance();
        rightNow = Calendar.getInstance();

        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

    }

    // MODIFIES: this
    // EFFECTS: creates a DueDate with deadline of the given non-null date
    //          throws null argument exception when date is null
    public DueDate(Date date) throws NullArgumentException {
        if (date == null) {
            throw new NullArgumentException();
        } else {
            cal = Calendar.getInstance();
            rightNow = Calendar.getInstance();

            setDueDate(date);
        }
    }

    // MODIFIES: this
    // EFFECTS: changes the due date to the given non-null date
    //          throws exception when date is null
    public void setDueDate(Date date) throws NullArgumentException {
        if (date == null) {
            throw new NullArgumentException("Null DueDate");
        } else {
            cal.setTime(date);
        }
    }

    // MODIFIES: this
    // EFFECTS: changes the due time to hh:mm (non-negative&reasonable times) leaving the month, day and year the same
    //          throws exception if hh or mm are not within proper bounds of hours and min
    public void setDueTime(int hh, int mm) throws InvalidTimeException {
        if (!((0 <= hh && hh <= 23) && (0 <= mm && mm <= 59))) {
            throw new InvalidTimeException("Time should be within 0-23 hours and 0-59 minutes");
        } else {
            cal.set(Calendar.HOUR_OF_DAY, hh);
            cal.set(Calendar.MINUTE, mm);
        }

    }

    // MODIFIES: this
    // EFFECTS: postpones the due date by one day (leaving the time the same as
    //     in the original due date) based on the rules of the Gregorian calendar.
    public void postponeOneDay() {
        cal.add(Calendar.DAY_OF_MONTH, 1);
    }

    // MODIFIES: this
    // EFFECTS: postpones the due date by 7 days
    //     (leaving the time the same as in the original due date)
    //     based on the rules of the Gregorian calendar.
    public void postponeOneWeek() {
        cal.add(Calendar.DAY_OF_MONTH, 7);
    }

    // EFFECTS: returns the due date
    public Date getDate() {
        return cal.getTime();
    }

    // EFFECTS: returns true if due date (and due time) is passed
    public boolean isOverdue() {
        long now = rightNow.getTimeInMillis();
        long dueTime = cal.getTimeInMillis();

        return now > dueTime;
    }

    // EFFECTS: returns true if due date is at any time today, and false otherwise
    public boolean isDueToday() {
        boolean value = rightNow.get(Calendar.MONTH) == cal.get(Calendar.MONTH);
        boolean value1 = (rightNow.get(Calendar.DAY_OF_MONTH) == cal.get(Calendar.DAY_OF_MONTH));
        boolean value2 = rightNow.get(Calendar.YEAR) == cal.get(Calendar.YEAR);

        return (value && value1 && value2);
    }

    // EFFECTS: returns true if due date is at any time tomorrow, and false otherwise
    public boolean isDueTomorrow() {

        boolean a = rightNow.get(Calendar.MONTH) == cal.get(Calendar.MONTH);
        boolean b = cal.get(Calendar.DAY_OF_MONTH) == rightNow.get(Calendar.DAY_OF_MONTH) + 1;
        boolean c = rightNow.get(Calendar.YEAR) == cal.get(Calendar.YEAR);

        return (a && b && c);
    }

    // EFFECTS: returns true if due date is within the next seven days, irrespective of time of the day,
    // and false otherwise. Example, assume it is 8:00 AM on a Monday
    // then any task with due date between 00:00 AM today (Monday) and 11:59PM the following Sunday is due within a week
    public boolean isDueWithinAWeek() {
        int daysBetween = cal.get(Calendar.DAY_OF_MONTH) - rightNow.get(Calendar.DAY_OF_MONTH);
        boolean a = rightNow.get(Calendar.MONTH) == cal.get(Calendar.MONTH);
        boolean b = rightNow.get(Calendar.YEAR) == cal.get(Calendar.YEAR);
        return ((0 <= daysBetween && daysBetween < 7) && a && b);
    }

    // EFFECTS: returns a string representation of due date in the following format
    //     day-of-week month day year hour:minute
    //  example: Sun Jan 25 2019 10:30 AM
    @Override
    public String toString() {
        String amOrPm;
        if (cal.get(Calendar.AM_PM) == Calendar.AM) {
            amOrPm = "AM";
        } else {
            amOrPm = "PM";
        }

        DateFormat df = new SimpleDateFormat("EEE MMM dd yyyy hh:mm");
        return df.format(getDate()) + " " + amOrPm;

    }

    // EFFECTS: returns calendar month
    public int getMonth() {
        return cal.get(Calendar.MONTH);
    }

    // EFFECTS: returns calendar day
    public int getDay() {
        return cal.get(Calendar.DAY_OF_MONTH);
    }

    // EFFECTS: returns calendar hour
    public int getHour() {
        return cal.get(Calendar.HOUR);
    }

    // EFFECTS: returns calendar minute
    public int getMinute() {
        return cal.get(Calendar.MINUTE);
    }


}