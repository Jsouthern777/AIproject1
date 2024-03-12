import java.util.ArrayList;

public class Solver {
//same problem will be solved multiple times by both algorithms, so generate availabilities,
// numEmployees, numShifts, and startTime here
    public static boolean runInstance(final int numShifts, final int numEmployees, final int verbosity){
        SchedulingProblem prob = new SchedulingProblem(numShifts, 0, numEmployees);
        ArrayList<ArrayList<Boolean>> employees = prob.getEmployees();
        shiftDomains startDomains = new shiftDomains(0, 10,employees);
        System.out.println(startDomains.toString());
        return false;
    }



    public static void main(String[] args) {
        runInstance(10,20, 1);
    }
}
