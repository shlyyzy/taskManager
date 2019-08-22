package model;

import model.exceptions.NullArgumentException;

import java.util.*;

// Represents a Project, a collection of zero or more Tasks
// Class Invariant: no duplicated task; order of tasks is preserved
public class Project extends Todo implements Iterable<Todo> {
    private List<Todo> tasks;

    // MODIFIES: this
    // EFFECTS: constructs a project with the given description
    //     the constructed project shall have no tasks.
    //  throws EmptyStringException if description is null or empty
    public Project(String description) {
        super(description);
        tasks = new ArrayList<>();
    }

    // MODIFIES: this
    // EFFECTS: task is added to this project (if it was not already part of it)
    //   throws NullArgumentException when task is null
    public void add(Todo task) {
        if (!contains(task) && !task.equals(this)) {
            tasks.add(task);
        }
    }

    // MODIFIES: this
    // EFFECTS: removes task from this project
    //   throws NullArgumentException when task is null
    public void remove(Todo task) {
        if (contains(task)) {
            tasks.remove(task);
        }
    }

    // EFFECTS: returns the description of this project
    public String getDescription() {
        return description;
    }

    // EFFECTS: returns an unmodifiable list of tasks in this project.
    @Deprecated
    public List<Task> getTasks() {
        throw new UnsupportedOperationException();
    }

    // EFFECTS: returns an integer between 0 and 100 which represents
    //     the percentage of completion (rounded down to the nearest integer).
    //     the value returned is the average of the percentage of completion of
    //     all the tasks and sub-projects in this project.
    public int getProgress() {
        int taskCompletion = 0;

        if (getNumberOfTasks() == 0) {
            return 0;
        }

        for (Todo todo : tasks) {
            taskCompletion += todo.getProgress();
        }

        return taskCompletion / getNumberOfTasks();
    }

    // EFFECTS: returns the number of tasks (and sub-projects) in this project
    public int getNumberOfTasks() {
        return tasks.size();
    }

    // EFFECTS: returns true if every task (and sub-project) in this project is completed, and false otherwise
    //     If this project has no tasks (or sub-projects), return false.
    public boolean isCompleted() {
        return getNumberOfTasks() != 0 && getProgress() == 100;
    }

    // EFFECTS: returns time to complete all tasks in project
    @Override
    public int getEstimatedTimeToComplete() {
        int totalTime = 0;

        for (Todo todo : tasks) {
            totalTime += todo.getEstimatedTimeToComplete();
        }

        return totalTime;
    }

    // EFFECTS: returns true if this project contains the task
    //   throws NullArgumentException when task is null
    public boolean contains(Todo task) {
        if (task == null) {
            throw new NullArgumentException("Illegal argument: task is null");
        }
        return tasks.contains(task);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Project)) {
            return false;
        }
        Project project = (Project) o;
        return Objects.equals(description, project.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(description);
    }

    @Override
    public Iterator<Todo> iterator() {
        return new TodoIterator();
    }

    // EFFECTS: inner iterator class
    private class TodoIterator implements Iterator<Todo> {
        private int size;
        private int currentIndex;
        private int currentPriority;
        private int numPriority;
        private Boolean moveToNextPriority;
        private Todo someTodo;
        private int sizeEachIteration;

        public TodoIterator() {
            size = tasks.size();
            currentIndex = 0;
            currentPriority = 1;
            numPriority = 0;
            moveToNextPriority = true;
            sizeEachIteration = tasks.size();
        }

        // EFFECTS: if current priority is a real priority, the number of elements gone through has not exceeded
        //          the num of elements in the list, and tasks is not empty, return true otherwise return false.
        @Override
        public boolean hasNext() {
            return currentPriority < 5 && sizeEachIteration > 0 && !tasks.isEmpty();
        }

        // EFFECTS: returns To-dos in correct priority order
        @Override
        public Todo next() throws NoSuchElementException {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            if (moveToNextPriority) {
                getNumPriority();
                while (numPriority == 0) {
                    currentPriority++;
                    getNumPriority();
                }
            }
            sizeEachIteration--;
            return priorityReturn();
        }

        // MODIFIES: this
        // EFFECTS: counts the number of tasks that have the current priority
        private void getNumPriority() {
            while (currentIndex < size) {
                if (equalsCurrentPriority()) {
                    numPriority++;
                }
                currentIndex++;
            }

            currentIndex = 0;
            moveToNextPriority = false;
        }

        // MODIFIES: this
        // EFFECTS: returns project or task with corresponding current priority
        //          taking into account num of to-do w/ priority
        private Todo priorityReturn() {
            while (currentIndex < size) {
                if (numPriority == 1) {
                    if (equalsCurrentPriority()) {
                        numPriority--;
                        someTodo = tasks.get(currentIndex);
                        break;
                    }
                }
                if (equalsCurrentPriority()) {
                    numPriority--;
                    return tasks.get(currentIndex++);
                }
                currentIndex++;
            }
            nextPriorityResetValues();
            return someTodo;
        }

        // MODIFIES: this
        // EFFECTS: helper method resetting values for new priority
        private void nextPriorityResetValues() {
            currentIndex = 0;
            currentPriority++;
            moveToNextPriority = true;
        }

        // EFFECTS: returns true if task of index is of current priority
        private Boolean equalsCurrentPriority() {
            return tasks.get(currentIndex).getPriority().equals(new Priority(currentPriority));
        }
    }
}