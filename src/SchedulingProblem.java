import java.util.ArrayList;
import java.util.List;

/**
 * What we need:
 * Variables: Hours/Shifts
 * Values: People
 * Constraints: No more than 8 consecutive hours, 12 hours between consecutive shifts, random availability
 */
public class SchedulingProblem {
    public ArrayList<ArrayList<Boolean>> getEmployees() {
        return employees;
    }

    //List of employees that will be of random length.
    //employees.get(i) is an unmodifiable list containing that employee's availability
    private ArrayList<ArrayList<Boolean>> employees; //people (values)

    private int numShifts;
    //hour number of the first shift (for employee availability checking)
    private int startTime;

    private int numEmployees;

    public SchedulingProblem(final int numShifts, final int startTime, final int numEmployees){
        this.numShifts = numShifts;
        this.startTime = startTime;
        this.numEmployees = numEmployees;
        employees = new ArrayList<>();
        for(int i = 0; i < numEmployees; i++){
            ArrayList<Boolean> boolList = new ArrayList<>();
            employees.add(boolList);
        }
        System.out.println(employees.size());

        generateAvailability(numShifts,startTime,employees);
        for(int i = 0; i < employees.size(); i++){
            System.out.println("employee: " + i);
            System.out.println(employees.get(i));
        }
        shiftDomains startDomains = new shiftDomains(startTime, numShifts, employees);


    }
//Generates random availability for each employee for the shift duration
    public static void generateAvailability(final int numShifts, final int startTime, ArrayList<ArrayList<Boolean>> employees){
        for(int i = 0; i < employees.size(); i++){

            for(int j = startTime; j <= startTime + numShifts; j++){

                employees.get(i).add(Math.random()<0.5);
            }
        }
    }

    public int getNumShifts(){return numShifts;}

}

