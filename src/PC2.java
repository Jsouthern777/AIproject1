import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;


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
 * The main backtrackPC2 function iterates through the possible domain assignments for the current shift being assigned.
 * For each potential assignment of the current shift, it loads a queue 'pairsToCheck' with all of the possible domains in 
 * each neighbor that follows the current shift. The entries in the queue are checked for path consistency using PC2 and if there
 * is still at least 1 employee remaining in each neighbor's domain then the current shift is clear to be actually assigned.
 * Then a recursive call is made to try the next hour. If all possible domain assignments for the current shift yield no possible
 * solution then it returns null.
 * 
 * This is very slow and generates a lot of nodes. Adding in forward checking would improve the number of backtracks made significantly.
 * If a future shift's domain only has one employee available, it keeps going until it hits that error.
 */

public class PC2 {
       
    //Attempts to solve a shift scheduling CSP. Starts at shift 0 and assigns shifts numerically starting at the lowest 
    //employee number. 
    public static shiftDomains backtrackPC2(final SchedulingProblem  problem, final shiftDomains domains, final int nextHourToAssign, final NodeCounter counter, final int verbosity){
        counter.incrementPC2Count();
        if (verbosity >= 1){
            System.out.println("A recursive call was just made, here are the current domains:\n" + domains);
        }


        //if assignment is complete then return the assignments
        if (nextHourToAssign == problem.getNumShifts()){
            return domains;
        }

        int currentShift = nextHourToAssign;
        shiftDomains domainCopy = new shiftDomains(domains);

        //Because all variables are being assigned in sequential order, anything previous is already assigned
        //so we only need to check the potential current shift assignment's consistency with the neighboring 
        //shifts that come after it. It gets up to "minHoursBetweenShifts" neighbors 
        ArrayList<Integer> currentHourNeighbors = getForwardNeighbors(problem, currentShift);

        //This is the queue that stores the pairs of variables to check
        Queue<Integer> pairsToCheck;


        //Beginning of mega stacked for loop. Outer loop iterates through the current shift being assigned's domain. 
        //The next inner layer iterates through the neighbors that need to be checked, and the inner-most loop
        //iterates through the domains of that other neighbor to be checked

        //This loops through to load the constrained pairs 
                
        //Loop through and check all of the children of the current hour being assigned 
            for(int currentShiftDomainIndex = 0; currentShiftDomainIndex < domainCopy.shiftDomains.get(currentShift).size(); currentShiftDomainIndex++){
                
                shiftDomains tempDomains = new shiftDomains(domainCopy);
                pairsToCheck = new LinkedList<>();

                //Assign the current shift for checking
                ArrayList<Integer> tempCurrentAssignment = new ArrayList<>();
                tempCurrentAssignment.add(domainCopy.shiftDomains.get(currentShift).get(currentShiftDomainIndex));
                tempDomains.shiftDomains.set(currentShift, tempCurrentAssignment);

                //Loop through the neighbors of the current hour being assigned
                for(int neighborIndex = 0; neighborIndex < currentHourNeighbors.size(); neighborIndex++){
               
                    //Loop through the other hour being checked's domain
                    for(int otherShiftDomainIndex = 0; otherShiftDomainIndex < tempDomains.shiftDomains.get(currentHourNeighbors.get(neighborIndex)).size(); otherShiftDomainIndex++){

                        //add the current shift
                        pairsToCheck.add(currentShift);

                        //add the current employee
                        pairsToCheck.add(tempDomains.shiftDomains.get(currentShift).get(0));

                        //add the other shift
                        pairsToCheck.add(currentHourNeighbors.get(neighborIndex));

                        //add the other employee
                        pairsToCheck.add(tempDomains.shiftDomains.get(currentHourNeighbors.get(neighborIndex)).get(otherShiftDomainIndex));

                        //add other shift domain index
                        pairsToCheck.add(otherShiftDomainIndex);

                    }//end otherShiftDomainIndex for

                }//end neighborIndex for


            //The queue is now initially loaded with all of the pairs to check

            int currShift, otherShift, currEmp, otherEmp, otherShiftDomainIndex;

            ArrayList<Integer> removalCorrections = new ArrayList<>();
            for(int i = 0; i <= currentHourNeighbors.size() + 1; i++){
                removalCorrections.add(0);
            }

            while(pairsToCheck.size() > 4){


                currShift = pairsToCheck.remove();
                currEmp = pairsToCheck.remove();
                otherShift = pairsToCheck.remove();
                otherEmp = pairsToCheck.remove();
                otherShiftDomainIndex = pairsToCheck.remove();

            //Make the temporary assignments in a new copy of the domain
            shiftDomains assignmentToCheck = new shiftDomains(tempDomains);

            //Assign the other shift for checking
            ArrayList<Integer> variableAssignment = new ArrayList<>();
            variableAssignment.add(otherEmp);
            assignmentToCheck.shiftDomains.set(otherShift,variableAssignment);


            //Check the path consistency of the current 2 variable temporary assignment
            if(!isConsistent(problem, assignmentToCheck, currShift, otherShift)){

                if(verbosity == 2){
                    System.out.print("Just checked current Shift:" + currShift + " current employee:" + currEmp);
                    System.out.println(" and other Shift:" + otherShift + " other employee:" + otherEmp);
                    System.out.println("It was inconsistent. Now trying the next pair of potential assignments");
                }
                
                //domain is inconsistent so remove it 
                tempDomains.shiftDomains.get(otherShift).remove(otherShiftDomainIndex - removalCorrections.get(otherShift - currShift));
                //if you remove something, that domain shrinks so the saved value in the queue will be wrong
                removalCorrections.set((otherShift - currShift), removalCorrections.get((otherShift - currShift)) +1);

                //If any neighbor runs out of possible employees, this assignment is wrong
                if(tempDomains.shiftDomains.get(otherShift).size() <= 0){
                    if(verbosity == 2){
                        System.out.println("Shift " + otherShift + "'s domain is empty, returning null");
                    }
                    return null;
                }

            }
            else{
                if(verbosity == 2){
                    System.out.print("Just checked current Shift:" + currShift + " current employee:" + currEmp);
                    System.out.println(" and other Shift:" + otherShift + " other employee:" + otherEmp);
                    System.out.println("It was consistent");
                }
            }
        }


            //If the code makes it to here, all of the domains have been reduced so that they are path consistent with the current shift assignment

            //Recursive call
            if(verbosity == 2){
            System.out.println("\nNow assigning employee #" + domainCopy.shiftDomains.get(currentShift).get(currentShiftDomainIndex) + " to shift #" + currentShift + " and making a recursive call");
            }
            shiftDomains result = backtrackPC2(problem, new shiftDomains(tempDomains), currentShift + 1, counter, verbosity);
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