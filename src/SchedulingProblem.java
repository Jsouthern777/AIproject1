import java.util.ArrayList;
import java.util.List;

/**
 * What we need:
 * Variables: Hours/Shifts (number is randomized)
 * Values: People (number is randomized)
 * Constraints: No more than 8 consecutive hours, 12 hours between consecutive shifts, random availability
 */
public class SchedulingProblem {
    //List of employees that will be of random length.
    //employees.get(i) is an unmodifiable list containing that employee's availability
    private ArrayList<List<Integer>> employees; //people (values)

    private int numShifts;
    //hour number of the first shift (for employee availability checking)
    private int startTime;

    private int numEmployees;

    public SchedulingProblem(final int numShifts, final int startTime, final int numEmployees, ArrayList<List<Integer>> employees){
        this.numShifts = numShifts;
        this.startTime = startTime;
        this.numEmployees = numEmployees;
        this.employees = employees;
    }
}

