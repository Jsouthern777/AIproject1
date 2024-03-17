import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;


//This class was co-written by Andrew and Jackson
public class Solver {
//same problem will be solved multiple times by both algorithms, so generate availabilities,
// numEmployees, numShifts, and startTime here
    public static void runInstance(final int numShifts, final int numEmployees, String availabilitiesFilename, int verbosity) throws IOException{
        
        //Initialize the problem
        SchedulingProblem prob = new SchedulingProblem(numShifts, numEmployees);

        //Initialize the domains from the input file
        shiftDomains startDomains = new shiftDomains(numShifts, numEmployees, availabilitiesFilename);

        //Initialize the NodeCounter for analysis
        NodeCounter counter = new NodeCounter();



        System.out.println("\n\nHere are the domains at the start of the problem " + availabilitiesFilename);
        System.out.println("with maximum consecutive shifts of " + prob.getMaxConsecutiveHours() + " hours and at least " + prob.getMinBetweenShifts() + " hours between shifts");
        System.out.println(startDomains); //for testing

        
        //Begin instance of Jackson's backtracking MRV search
        ArrayList<Integer> assignedShifts = new ArrayList<>();
        //initialize assignedShifts to allow setting each hour
        for(int i = 0; i < numShifts; i++){
            assignedShifts.add(-1);
        }
        long startTime = System.nanoTime();
        shiftDomains result1 = BacktrackSearch.backtrackMRV(prob,startDomains, counter, verbosity);
        long endTime = System.nanoTime();

        long duration1 = (endTime - startTime) / 1000000;
        //checks the success of the backtracking MRV solver and prints the result
        if(result1 != null){
            System.out.println("Result of backtracking MRV:\n" + result1);
            System.out.println("The MRV solution generated " + counter.getMRVCount() + " nodes");
        }
        else{
            System.out.println("No solution found for backtracking MRV");
            System.out.println("The MRV attempt generated " + counter.getMRVCount() + " nodes");
        }

        System.out.println("Attempt for MRV took " + endTime + " milliseconds");

        
        //Begin instance of Andrew's simple backtracking search using PC2
        int firstHour = 0;
        System.out.println("\n\n\nBEGINNING PC2 TEST\n");
        long startTime2 = System.nanoTime();
        shiftDomains result2 = PC2.backtrackPC2(prob, startDomains, firstHour, counter, verbosity);
        long endTime2  = System.nanoTime();
        long duration2 = (endTime2 - startTime2) / 1000000;

        //checks the success of the backtracking PC2 solver and prints the result
        if(result2 != null){ 
           System.out.println("Here are the assignments after the PC2 search:");
           System.out.println(result2);
           System.out.println("The PC2 solution generated " + counter.getPC2Count() + " nodes");

        }
        else{
            System.out.println("No solution found for PC2");
            System.out.println("The PC2 attempt generated " + counter.getPC2Count() + " nodes");

        }

        System.out.println("Attempt for PC2 took " + duration2 + " milliseconds");
    }//end RunInstance



    //Overloaded method allowing to run the instance and change the shift assignment constraints
    public static void runInstance(final int numShifts, final int numEmployees, String availabilitiesFilename, final int minHoursBetweenShifts, final int maxConsecutiveHours, int verbosity) throws IOException{
        
        //Initialize the problem
        SchedulingProblem prob = new SchedulingProblem(numShifts, numEmployees, minHoursBetweenShifts, maxConsecutiveHours);

        //Initialize the domains from the input file
        shiftDomains startDomains = new shiftDomains(numShifts, numEmployees, availabilitiesFilename);

        //Initialize the NodeCounter for analysis
        NodeCounter counter = new NodeCounter();



        System.out.println("\n\nHere are the domains at the beginning of the test " + availabilitiesFilename);
        System.out.println("with maximum consecutive shifts of " + maxConsecutiveHours + " hours and at least " + minHoursBetweenShifts + " hours between shifts");
        System.out.println(startDomains); //for testing

       long startTime = System.nanoTime();
        shiftDomains result1 = BacktrackSearch.backtrackMRV(prob,startDomains, counter, verbosity);
        long endTime = System.nanoTime();
        long duration = (endTime - startTime) / 1000000;
        //checks the success of the backtracking MRV solver and prints the result
        if(result1 != null){
            System.out.println("Result of backtracking MRV:\n" + result1);
            System.out.println("The MRV solution generated " + counter.getMRVCount() + " nodes");
        }
        else{
            System.out.println("No solution found for backtracking MRV");
            System.out.println("The MRV attempt generated " + counter.getMRVCount() + " nodes");
        }

        System.out.println("Attempt for MRV took " + duration + " milliseconds");
        

        
        //Begin instance of Andrew's simple backtracking search using PC2
        int firstHour = 0;
        System.out.println("\n\nBEGINNING PC2 TEST\n\n");
        long startTime2 = System.nanoTime();
        shiftDomains result2 = PC2.backtrackPC2(prob, startDomains, firstHour, counter, verbosity);
        long endTime2 = System.nanoTime();
        long duration2 = (endTime2 - startTime2) / 1000000;

        //checks the success of the backtracking PC2 solver and prints the result
        if(result2 != null){ 
           System.out.println("This is the resulting solution following the PC2 search:");
           System.out.println(result2);
           System.out.println("The PC2 solution generated " + counter.getPC2Count() + " nodes");
        }
        else{
            System.out.println("No solution found for PC2");
            System.out.println("The PC2 attempt generated " + counter.getPC2Count() + " nodes");
        }
        System.out.println("Attempt for PC2 took " + duration2 + " milliseconds");
    }//end RunInstance





    public static void main(String[] args) throws IOException{
        runInstance(10,5, "5E 10S.txt",  0);
        runInstance(25, 10, "10E 25S.txt",0);
        runInstance(25, 10, "10E 25S.txt", 6, 3,0);
        runInstance(25, 10, "10E 25S.txt", 12, 2,0);  //should fail
        runInstance(50, 15, "15E 50S.txt",0);
        //runInstance(100, 30, "30E 100S.txt"); //will run but PC2 is very slow
        //runInstance(200, 30, "30E 200S.txt"); //will run but PC2 is very slow
    }


}
