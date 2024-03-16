import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
//TODO: implement performance eval and verbosity parameter
public class Solver {
//same problem will be solved multiple times by both algorithms, so generate availabilities,
// numEmployees, numShifts, and startTime here
    public static void runInstance(final int numShifts, final int numEmployees, String availabilitiesFilename) throws IOException{
        
        //Initialize the problem
        SchedulingProblem prob = new SchedulingProblem(numShifts, numEmployees);

        //Initialize the domains from the input file
        shiftDomains startDomains = new shiftDomains(numShifts, numEmployees, availabilitiesFilename);

        System.out.println(startDomains); //for testing


        //Begin instance of Jackson's backtracking MRV search
        ArrayList<Integer> assignedShifts = new ArrayList<>();
        //initialize assignedShifts to allow setting each hour
        for(int i = 0; i <= numShifts; i++){
            assignedShifts.add(i);
        }
        shiftDomains result1 = BacktrackSearch.backtrackMRV(prob,startDomains,assignedShifts);

        //checks the success of the backtracking MRV solver and prints the result
        if(result1 != null){
            System.out.println("Result of backtracking MRV:\n" + result1);
        }
        else{
            System.out.println("No solution found for backtracking MRV");
        }

        
        //Begin instance of Andrew's simple backtracking search using PC2
        int firstHour = 0;
        shiftDomains result2 = PC2.backtrackPC2(prob, startDomains, firstHour);


        //checks the success of the backtracking PC2 solver and prints the result
        if(result2 != null){ 
            System.out.println(result2);
        }
        else{
            System.out.println("No solution found for PC2");
        }



    }




    public static void main(String[] args) throws IOException{
       // runInstance(10,5, "5E 10S.txt");
        runInstance(25, 10, "10E 25S.txt");
    }


}
