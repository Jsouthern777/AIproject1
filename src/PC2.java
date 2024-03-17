import java.util.ArrayList;
import java.util.List;


/*
 * @Author Andrew Bergey
 * 
 * 
 * This class implements a simple backtracking search that uses PC2 as its consistency check. It assigns the shifts in numerical
 * order starting at shift 0. It attempts to assign employees starting from the lowest employee number. 
 * 
 * This code refers to the PC2 pseudocode and examples found on slides 18-20 from https://canvas.auckland.ac.nz/courses/60536/files/7516963/download?download_frd=1#:~:text=The%20PC2%20algorithm%20achieves%20path,values%20that%20violates%20path%20consistency.
 * I used the general structure of this algorithm but modified it to fit this problem. I check all paris of X1 and X2 and removed
 * the nodes that were inconsistent with both assignments and if no consistent assignments are remaining, then it returns null 
 * 
 */

public class PC2 {
       
    //Attempts to solve a shift scheduling CSP. Starts at shift 0 and assigns shifts numerically starting at the lowest 
    //employee number. 
    public static shiftDomains backtrackPC2(final SchedulingProblem  problem, final shiftDomains domains, final int nextHourToAssign, final NodeCounter counter, final int verbosity){
        counter.incrementPC2Count();

        //if assignment is complete then return the assignments
        if (nextHourToAssign == problem.getNumShifts()){
            return domains;
        }
        int currentShift = nextHourToAssign;
        shiftDomains domainCopy = new shiftDomains(domains);
        ArrayList<Integer> currentHourNeighbors = getForwardNeighbors(problem, currentShift);


        //Beginning of mega stacked for loop. Outer loop iterates through the current shift being assigned's domain. 
        //The next inner layer iterates through the neighbors that need to be checked, and the inner-most loop
        //iterates through the domains of that other neighbor to be checked
        
        //Loop through and check all of the children of the current hour being assigned 
            for(int currentShiftDomainIndex = 0; currentShiftDomainIndex < domainCopy.shiftDomains.get(currentShift).size(); currentShiftDomainIndex++){
                
                shiftDomains tempDomains = new shiftDomains(domainCopy);

                //Assign the current shift for checking
                ArrayList<Integer> tempCurrentAssignment = new ArrayList<>();
                tempCurrentAssignment.add(domainCopy.shiftDomains.get(currentShift).get(currentShiftDomainIndex));
                tempDomains.shiftDomains.set(currentShift, tempCurrentAssignment);

                //Loop through the neighbors of the current hour being assigned
                for(int neighborIndex = 0; neighborIndex < currentHourNeighbors.size(); neighborIndex++){

                    //path consistency for this algorithm means checking them against each other and forward checking the neighbors' neighbors with both assignments
                    //only makes a path (triangle) if it is in both neighbors lists. 
                    //Make an is consistent method that takes in two assignment
               
                    //Loop through the other hour being checked's domain
                    for(int otherShiftDomainIndex = 0; otherShiftDomainIndex < tempDomains.shiftDomains.get(currentHourNeighbors.get(neighborIndex)).size(); otherShiftDomainIndex++){

                        //Make the temporary assignments in a new copy of the domain
                        shiftDomains assignmentToCheck = new shiftDomains(tempDomains);

                        //Assign the other shift for checking
                        ArrayList<Integer> variableAssignment = new ArrayList<>();
                        variableAssignment.add(tempDomains.shiftDomains.get(currentHourNeighbors.get(neighborIndex)).get(otherShiftDomainIndex));
                        assignmentToCheck.shiftDomains.set(currentHourNeighbors.get(neighborIndex),variableAssignment);


                        //Check the path consistency of the current 2 variable temporary assignment
                        if(!isConsistent(problem, assignmentToCheck, currentShift, currentHourNeighbors.get(neighborIndex))){
                            
                            //domain is inconsistent so remove it 
                            tempDomains.shiftDomains.get(currentHourNeighbors.get(neighborIndex)).remove(otherShiftDomainIndex);

                            //If any neighbor runs out of possible employees, this assignment
                            if(tempDomains.shiftDomains.get(currentHourNeighbors.get(neighborIndex)).size() <= 0){
                                return null;
                            }
                            otherShiftDomainIndex--; //account for variable removal
                        }
                       
                    }//end otherShiftDomainIndex for

                }//end neighborIndex for

            
            /*
            //If this is the second to last shift, both assignments will work
            if(currentShift == problem.getNumShifts() - 2){
                return assignmentToCheck;
            }
            */


            // If the code gets to this point, the tempory assignment has passed PC2 checks and is ready to be actually assigned

            //Assign the current variable that just passed the PC2 checks
            /*
            shiftDomains recursiveDomains = new shiftDomains(domainCopy);
            ArrayList<Integer> realVariableAssignment = new ArrayList<>();
            realVariableAssignment.add(tempDomains.shiftDomains.get(currentShift).get(currentShiftDomainIndex));
            recursiveDomains.shiftDomains.set(currentShift, realVariableAssignment);
            */

            //Recursive call
            //System.out.println("Making recursive call. CurrShift: " + currentShift + "\n");// + "\nDomain assignments:"+ recursiveDomains);
            shiftDomains result = backtrackPC2(problem, new shiftDomains(tempDomains), currentShift + 1, counter);
            if (result != null){
                return result;
            }

            }//end currentShiftDomainIndex for



            //If the code gets to this point, it checked all of the children and there are no solutions with the current assignment
        return null;

    }//end backtrackPC2


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




//This method checks to see if the potential assignment pair is consistent with everything else
//This assumes that all shifts previous to current shift have been assigned (which is how its been structured thus far)
private static boolean isConsistent(SchedulingProblem problem, shiftDomains domain, int currentShift, int otherShift){
    
        int currentEmployee = domain.shiftDomains.get(currentShift).get(0);
        int otherEmployee = domain.shiftDomains.get(otherShift).get(0);

        //Initialize consistency variables
        int currConsecutiveShiftsCount = 0;
        boolean firstShiftEnded = false;
        boolean firstShiftStarted = false;
        int earliestShiftToCheck;
        int mostRecentOtherShift = Integer.MIN_VALUE;
        boolean sameAssignment = (currentEmployee == otherEmployee);
        boolean otherStarted = false;

        

        //Determine the earliest assignment that is relavent to the current assignment
        if((currentShift - problem.getMinBetweenShifts() - 1) <= 0){  //check the <= here if buggy
            earliestShiftToCheck = 0;
        }
        else{
            earliestShiftToCheck = currentShift - problem.getMinBetweenShifts();
        }

        for(int shiftToCheck = earliestShiftToCheck; shiftToCheck <= currentShift; shiftToCheck++){

            //This is currently breaking because it doesn't get to the appropriate check for minTimeBetween since it takes other employee or just firstshiftstarted elif

            //Need to evaluate the current assignments, can assume everything previous is consistent
            if(!firstShiftEnded && (domain.shiftDomains.get(shiftToCheck).get(0) == currentEmployee)){
                currConsecutiveShiftsCount++;
                if(currConsecutiveShiftsCount > problem.getMaxConsecutiveHours()){
                    return false;
                }
                firstShiftStarted = true;
            }
            //else if means that this is exclusively for otherEmployee != currentEmployee
            else if(!sameAssignment && (domain.shiftDomains.get(shiftToCheck).get(0) == otherEmployee)){
                mostRecentOtherShift = shiftToCheck;
                otherStarted = true;
                if(firstShiftStarted){
                    firstShiftEnded = true;
                }
                }
            //If the first shift has ended already and we have another assignment, that violates the min time between shifts
            else if(firstShiftEnded && (domain.shiftDomains.get(shiftToCheck).get(0) == currentEmployee)){
                return false;
            }
            else if(firstShiftStarted){ //only get here if the shift to check != current employee
                firstShiftEnded = true;
            }
            //No else needed, if this shift is assigned to another employee then its fine

        }//end for loop

        //Do some final checks after the loop
        //Check the case where the two assignments are the same
        if(sameAssignment){
            currConsecutiveShiftsCount++;
            if(currConsecutiveShiftsCount > problem.getMaxConsecutiveHours()){
                return false;
            }
        }

        //Check if the other employee was previously assigned too recently
        if(otherStarted && ((otherShift - mostRecentOtherShift) < problem.getMinBetweenShifts())){
            return false;
        }


    //If it makes it to this point, it has survived the consistency checks
    return true;
}//end isConsistent



}//end PC2
