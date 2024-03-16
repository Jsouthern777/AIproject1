import java.util.ArrayList;
import java.util.List;

/**
 * What we need:
 * Variables: Hours/Shifts
 * Values: People
 * Constraints: No more than 8 consecutive hours, 12 hours between consecutive shifts, random availability
 */
public class SchedulingProblem {

    private int numShifts;
    private int numEmployees;
    private ArrayList<List<Integer>> shiftAssignments;
    private int hoursBetweenShifts;
    private int maxConsecutiveHours;


    //Create a new scheduling problem
    public SchedulingProblem(final int numShifts, final int numEmployees, final int hoursBetweenShifts, final int maxConsecutiveHours){
        this.numShifts = numShifts;
        this.numEmployees = numEmployees;
        this.hoursBetweenShifts = hoursBetweenShifts;
        this.maxConsecutiveHours = maxConsecutiveHours;
    }

    //Constructor for simple problem with default parameters
    public SchedulingProblem(final int numShifts, final int numEmployees){
        this.numShifts = numShifts;
        this.numEmployees = numEmployees;
        maxConsecutiveHours = 8;
        hoursBetweenShifts = 12;
    }

    public int getNumShifts(){return numShifts;}

}//end SchedulingProblem

