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


        for(int domainIndex = 0; domainIndex < domainCopy.shiftDomains.get(nextHourToAssign).size(); domainIndex++){


            /*
            * Load the neighbors. This assumes time between shifts is greater than the longest allowable shift 
            */ 
            

            //Checks if there are at least 'hoursBetweenShifts' shifts before and after the current shift being assigned
            if((nextHourToAssign < (problem.getNumShifts() - 1 - hoursBetweenShifts)) && (nextHourToAssign > (hoursBetweenShifts - 1))){
                for(int i = 1; i < hoursBetweenShifts + 1; i++){
                    neighbors.add(nextHourToAssign + i);
                    neighbors.add(nextHourToAssign - i);
                }
            }
            //Checks if the current shift is near the start 
            else if(nextHourToAssign < (problem.getNumShifts() - 1 - hoursBetweenShifts)){
                for(int i = 1; i < hoursBetweenShifts + 1; i++){
                    neighbors.add(nextHourToAssign + i);
                    if((nextHourToAssign - i) >= 0){
                        neighbors.add(nextHourToAssign - i);
                    }
                    else{
                        break; //once an assignment is too close to a boundary it doesn't need to both checking anything further
                    }
                }
            }
            //Checks if the current shift is near the end
            else if(nextHourToAssign > (hoursBetweenShifts - 1)){
                for(int i = 1; i < hoursBetweenShifts + 1; i++){
                    neighbors.add(nextHourToAssign - i);
                    if((nextHourToAssign + i) <= problem.getNumShifts() - 1){
                        neighbors.add(nextHourToAssign + i);
                    }
                    else{
                        break;
                    }
                }
            }
            else{ //The number of shifts is too low so add all neighbors
                for(int i = 0; i < problem.getNumShifts(); i++){
                    if(nextHourToAssign != i){
                        neighbors.add(i);
                    }
                }
            }
            //end of adding neighbors

            
        
            /*
            * Implement PC2
            */
            



            // Assign the current variable that just passed the PC2 checks
            ArrayList<Integer> variableAssignment = new ArrayList<>();
            variableAssignment.add(domainCopy.shiftDomains.get(nextHourToAssign).get(domainIndex));
            domainCopy.shiftDomains.set(nextHourToAssign, variableAssignment);

            //Recursive call
            shiftDomains result = backtrackPC2(problem, new shiftDomains(domainCopy), nextHourToAssign + 1);
            if (result != null){
                return result;
            }

        }//end domain search for loop

        return null;

    }//end backtrackPC2

}//end PC2
