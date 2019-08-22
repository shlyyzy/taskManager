package model;

import model.exceptions.InvalidPriorityLevelException;

// To model priority of a task according to the Eisenhower Matrix
//     https://en.wikipedia.org/wiki/Time_management#The_Eisenhower_Method
public class Priority {

    private int quadrant;

    // MODIFIES: this
    // EFFECTS: construct a default priority (i.e., not important nor urgent)
    public Priority() {
        this.quadrant = 4;
    }

    // MODIFIES: this
    // EFFECTS: constructs a Priority according to the value of "quadrant" [1, 4]
    //          throws invalid priority level exception if quadrant is not in between 1 and 4, inclusive
    //     the parameter "quadrant" refers to the quadrants of the Eisenhower Matrix
    public Priority(int quadrant) throws InvalidPriorityLevelException {
        if (!(1 <= quadrant && quadrant <= 4)) {
            throw new InvalidPriorityLevelException("Quadrant should be in between 1 and 4");
        } else {
            this.quadrant = quadrant;
        }
    }

    // EFFECTS: returns the importance of Priority (i.e., whether it is "important" or not)
    public boolean isImportant() {
        return (quadrant == 1 || quadrant == 2);
    }

    // MODIFIES: this
    // EFFECTS: updates the importance of Priority
    public void setImportant(boolean important) {
        if (important && !(quadrant == 1 || quadrant == 2)) {
            quadrant -= 2;
        } else if (!important && !(quadrant == 3 || quadrant == 4)) {
            quadrant += 2;
        }
    }

    // EFFECTS: returns the urgency of Priority
    //     (i.e., whether it is "urgent" or not)
    public boolean isUrgent() {
        return (quadrant == 1 || quadrant == 3);
    }

    // MODIFIES: this
    // EFFECTS: updates the urgency of Priority
    public void setUrgent(boolean urgent) {
        if (urgent && !(quadrant == 1 || quadrant == 3)) {
            quadrant -= 1;
        } else if (!urgent && !(quadrant == 2 || quadrant == 4)) {
            quadrant += 1;
        }
    }

    // EFFECTS: returns one of the four string representation of Priority
    //    "IMPORTANT & URGENT",  "IMPORTANT", "URGENT", "DEFAULT"
    @Override
    public String toString() {

        if (quadrant == 1) {
            return "IMPORTANT & URGENT";
        } else if (quadrant == 2) {
            return "IMPORTANT";
        } else if (quadrant == 3) {
            return "URGENT";
        } else {
            return "DEFAULT";
        }
    }

    public int getQuadrant() {
        return quadrant;
    }
}

