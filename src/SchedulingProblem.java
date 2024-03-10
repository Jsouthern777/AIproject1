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

    //list of required shifts/hours
    //shifts.get(i) gives you the employee assigned to that hour (0 = no one is assigned)
    private ArrayList<Integer> shifts;

}
