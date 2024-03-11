import java.util.ArrayList;
import java.util.List;

/**
 * What we need:
 * Variables: Hours/Shifts
 * Values: People
 * Constraints: No more than 8 consecutive hours, 12 hours between consecutive shifts, random availability
 */
public class SchedulingProblem {
    //List of employees that will be of random length.
    //employees.get(i) is an unmodifiable list containing that employee's availability
    private ArrayList<List<Boolean>> employees; //people (values)

    private int numShifts;
    //hour number of the first shift (for employee availability checking)
    private int startTime;

    private int numEmployees;

    public SchedulingProblem(final int numShifts, final int startTime, final int numEmployees, ArrayList<List<Boolean>> employees){
        this.numShifts = numShifts;
        this.startTime = startTime;
        this.numEmployees = numEmployees;
        this.employees = employees;

        generateAvailability(numShifts,startTime,employees);

    }
//Generates random availability for each employee for the shift duration
    public static void generateAvailability(final int numShifts, final int startTime, ArrayList<List<Boolean>> employees){
        for(int i = 0; i < employees.size()-1; i++){
            for(int j = startTime; j <= numShifts; j++){
                employees.get(i).add(Math.random()<0.5);
            }
        }
    }
}

