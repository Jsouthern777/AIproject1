import java.util.ArrayList;
import java.util.List;

public class PC2 {
       
    //Attempts to solve a shift scheduling CSP. Starts at shift 0 and assigns shifts numerically starting at the lowest 
    //employee number. 
    public static shiftDomains backtrackPC2(final SchedulingProblem  problem, final shiftDomains domains, final int nextHourToAssign){
        
        //if assignment is complete then return the assignments
        if (nextHourToAssign == problem.getNumShifts()){
            return domains;
        }
        int hoursBetweenShifts = problem.getMinBetweenShifts();
        int maxConsecutiveHours = problem.getMaxConsecutiveHours();

        shiftDomains domainCopy = new shiftDomains(domains);
        ArrayList<Integer> currentHourNeighbors = getForwardNeighbors(problem, nextHourToAssign);


        //Beginning of mega stacked for loop. Outer loop iterates through the current shift being assigned's domain. 
        //The next inner layer iterates through the neighbors that need to be checked, and the inner-most loop
        //iterates through the domains of that other neighbor to be checked

        //Initialize variables needed in the mega loop
        int consecutiveShiftsCount = 1;
        int hoursBetweenShiftsCount = 0;
        boolean firstShiftEnded = false;
        boolean inconsistentAssignment = false;

            //Loop through and check all of the children of the current hour being assigned 
            for(int currentShiftDomainIndex = 0; currentShiftDomainIndex < domainCopy.shiftDomains.get(nextHourToAssign).size(); currentShiftDomainIndex++){
                
                //Reset this between checking each child
                inconsistentAssignment = false;

                //Loop through the neighbors of the current hour being assigned
                for(int neighborIndex = 0; neighborIndex < currentHourNeighbors.size(); neighborIndex++){

                    //path consistency for this algorithm means checking them against each other and forward checking the neighbors' neighbors with both assignments
                    //only makes a path (triangle) if it is in both neighbors lists. 
                    //Make an is consistent method that takes in two assignments
                    ArrayList<Integer> otherHourNeighbors = getForwardNeighbors(problem, currentHourNeighbors.get(neighborIndex));

                
                    //Loop through the other hour being checked's domain
                    for(int otherShiftDomainIndex = 0; otherShiftDomainIndex < domainCopy.shiftDomains.get(currentHourNeighbors.get(neighborIndex)).size(); otherShiftDomainIndex++){

                        //Check for number of consecutive shifts
                        //If the original shift is still going and current value being checked if the same as the next shift increment the consecutive counter
                        if(!firstShiftEnded && (domainCopy.shiftDomains.get(nextHourToAssign).get(currentShiftDomainIndex) == domainCopy.shiftDomains.get(currentHourNeighbors.get((neighborIndex))).get(otherShiftDomainIndex))){
                            consecutiveShiftsCount++;
                        }

                        //Check if the consecutive shift constraint has been violated, remove this option
                        if(consecutiveShiftsCount >= maxConsecutiveHours){
                            inconsistentAssignment = true;
                            break;
                        }

                        //This runs the first time that a following shift is assigned to a different employee
                        else if(!firstShiftEnded){
                            firstShiftEnded = true;
                        }

                        //Check for hours between shifts
                        //If there is still a gap between shifts then increment that counter 
                        if(firstShiftEnded && (domainCopy.shiftDomains.get(nextHourToAssign).get(currentShiftDomainIndex) != domainCopy.shiftDomains.get(currentHourNeighbors.get((neighborIndex))).get(otherShiftDomainIndex))){
                            hoursBetweenShiftsCount++;
                        }
                        //Check if the gap between shifts constraint has been violated
                        else if(firstShiftEnded && (hoursBetweenShiftsCount < hoursBetweenShifts)){
                            inconsistentAssignment = true;
                            break;
                        }

                    }//end otherShiftDomainIndex for

                    //If this assignment has already failed, break out of the nested loop
                    if(inconsistentAssignment){
                        break;
                    }

                }//end neighborIndex for


                // If the code gets to this point, the tempory assignment has passed PC2 checks and is ready to be actually assigned
                
                //Assign the current variable that just passed the PC2 checks
                ArrayList<Integer> variableAssignment = new ArrayList<>();
                variableAssignment.add(domainCopy.shiftDomains.get(nextHourToAssign).get(currentShiftDomainIndex));
                domainCopy.shiftDomains.set(nextHourToAssign, variableAssignment);

                //Recursive call
                shiftDomains result = backtrackPC2(problem, new shiftDomains(domainCopy), nextHourToAssign + 1);
                if (result != null){
                    return result;
                }

            }//end currentShiftDomainIndex for


            /* 
            //Load the neighbors. This assumes time between shifts is greater than the longest allowable shift 
            
            

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
            */
            
            

            /*
            //Assign the current variable that just passed the PC2 checks
            ArrayList<Integer> variableAssignment = new ArrayList<>();
            variableAssignment.add(domainCopy.shiftDomains.get(nextHourToAssign).get(domainIndex));
            domainCopy.shiftDomains.set(nextHourToAssign, variableAssignment);

            //Recursive call
            shiftDomains result = backtrackPC2(problem, new shiftDomains(domainCopy), nextHourToAssign + 1);
            if (result != null){
                return result;
            }
            */

        //If the code gets to this point, it checked all of the children and there are no solutions with the current assignment
        return null;

    }//end backtrackPC2


    //Returns a list of neighboring shifts. This assumes time between shifts is greater than the longest allowable shift 
    private static ArrayList<Integer> getNeighbors(SchedulingProblem problem, int shift){

        //Import the problem constraints
        int hoursBetweenShifts = problem.getMinBetweenShifts();

        ArrayList<Integer> neighbors = new ArrayList<>();
           
        //Checks if there are at least 'hoursBetweenShifts' shifts before and after the current shift being assigned
        if((shift < (problem.getNumShifts() - 1 - hoursBetweenShifts)) && (shift > (hoursBetweenShifts - 1))){
            for(int i = 1; i < hoursBetweenShifts + 1; i++){
                neighbors.add(shift + i);
                neighbors.add(shift - i);
            }
        }
        //Checks if the current shift is near the start 
        else if(shift < (problem.getNumShifts() - 1 - hoursBetweenShifts)){
            for(int i = 1; i < hoursBetweenShifts + 1; i++){
                neighbors.add(shift + i);
                if((shift - i) >= 0){
                    neighbors.add(shift - i);
                }
                else{
                    break; //once an assignment is too close to a boundary it doesn't need to both checking anything further
                }
            }
        }
        //Checks if the current shift is near the end
        else if(shift > (hoursBetweenShifts - 1)){
            for(int i = 1; i < hoursBetweenShifts + 1; i++){
                neighbors.add(shift - i);
                if((shift + i) <= problem.getNumShifts() - 1){
                    neighbors.add(shift + i);
                }
                else{
                    break;
                }
            }
        }
        else{ //The number of shifts is too low so add all neighbors
            for(int i = 0; i < problem.getNumShifts(); i++){
                if(shift != i){
                    neighbors.add(i);
                }
            }
        }

        return neighbors;
    }//end getNeighbors


   //Returns a list of neighboring shifts. This assumes time between shifts is greater than the longest allowable shift 
   private static ArrayList<Integer> getForwardNeighbors(SchedulingProblem problem, int shift){

    //Import the problem constraints
    int hoursBetweenShifts = problem.getMinBetweenShifts();

    ArrayList<Integer> neighbors = new ArrayList<>();
       
    //Checks if there are at least 'hoursBetweenShifts' shifts after the current shift being assigned
    if(shift < (problem.getNumShifts() - 1 - hoursBetweenShifts)){
        for(int i = 1; i < hoursBetweenShifts + 1; i++){
            neighbors.add(shift + i);
        }
    }
    //Adds remaining chronological shifts if the current shift is near the end
    else {
        for(int i = 1; i < (problem.getNumShifts() - shift); i++){
            neighbors.add(shift + i);
        }
    }

    return neighbors;
}//end getForwardNeighbors



//This method checks to see if the 
private static boolean isConsistent(SchedulingProblem problem, shiftDomains domain, int currentShift, int otherShift, int currentEmployee, int otherEmployee){
    
    //need to check chronologically earlier ones for consistency because later assignments aren't determined yet


    //If it makes it to this point, it has survived the consistency checks
    return true;
}



}//end PC2
