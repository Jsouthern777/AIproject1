import java.util.ArrayList;
//TODO: implement performance eval and verbosity parameter
public class Solver {
//same problem will be solved multiple times by both algorithms, so generate availabilities,
// numEmployees, numShifts, and startTime here
    public static boolean runInstance(final int numShifts, final int numEmployees, final int verbosity){
        SchedulingProblem prob = new SchedulingProblem(numShifts, 0, numEmployees);
        ArrayList<ArrayList<Boolean>> employees = prob.getEmployees(); //you need to access the list of employees + availabilities from prob
        shiftDomains startDomains = new shiftDomains(0, 10,employees);
        System.out.println(startDomains.toString()); //for testing
        ArrayList<Integer> assignedShifts = new ArrayList<>();
        shiftDomains result1 = BacktrackSearch.backtrackMRV(prob,startDomains,assignedShifts);
        //Andrew, call your search function here on the same startDomain
        if(result1 != null){ //result2 (Andrew's search) will need to be checked too
            System.out.println(result1);
            return true;
        }
        else{
            System.out.println("No solution found for result 1");
            return false;
        }
    }



    public static void main(String[] args) {
        runInstance(10,20, 1);
    }
}
