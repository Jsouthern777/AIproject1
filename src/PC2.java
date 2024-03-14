import java.util.ArrayList;
import java.util.List;

public class PC2 {
    
final static int hoursBetweenShifts = 12;
final static int maxConsecutiveHours = 8;
    
    public static shiftDomains backtrackPC2(final SchedulingProblem  problem, final shiftDomains domains, final int nextHourToAssign){
        
        //if assignment is complete then return the assignments
        if (nextHourToAssign == problem.getNumShifts()){
            return domains;
        }

        shiftDomains domainCopy = new shiftDomains(domains);
        List<Integer> neighbors = new ArrayList<>();


        //Load the neighbors. This assumes time between shifts is greater than the longest allowable shift 

        //Checks if there are at least 'hoursBetweenShifts' shifts before and after the current shift being assigned
        if((nextHourToAssign < (problem.getNumShifts() - 1 - hoursBetweenShifts)) && (nextHourToAssign > (hoursBetweenShifts - 1))){
            for(int i = 1; i < hoursBetweenShifts + 1; i++){
                neighbors.add(nextHourToAssign + i);
                neighbors.add(nextHourToAssign - i);
            }
        }
        else if(nextHourToAssign < (problem.getNumShifts() - 1 - hoursBetweenShifts)){
            for(int i = 1; i < hoursBetweenShifts + 1; i++){
                neighbors.add(nextHourToAssign + i);
            }
        }
        else if(nextHourToAssign > (hoursBetweenShifts - 1)){
            for(int i = 1; i < hoursBetweenShifts + 1; i++){
                neighbors.add(nextHourToAssign - i);
            }
        }


        





    }//end backtrackPC2

}//end PC2
