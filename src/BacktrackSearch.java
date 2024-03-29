import com.sun.source.doctree.SeeTree;

import java.util.ArrayList;

/*  Author: Jackson  Southern
Implements  backtracking search with forward checking and  an MRV heuristic.

 */
public class BacktrackSearch {

    public static shiftDomains backtrackMRV(SchedulingProblem problem, shiftDomains domains, final NodeCounter counter, int verbosity) {

        //If all domains have one value, solution is found
        if (isComplete(domains)) {
            return domains;
        }
        //prints domains for each call if verbosity is 1
        if(verbosity == 1){
            System.out.println("For iteration " + counter.getMRVCount() + ":\n" + domains);
        }
        //uses MRV heuristic to select the next variable/hour
        int var = selectUnassignedVariable(domains, problem);

        //creates a list of values for that variable
        ArrayList<Integer> orderedValues = domains.shiftDomains.get(var);

        //loops through each value in that variable
        for (int value : orderedValues) {
            //consistency check
            if (isConsistent(problem, domains, var, value)) {
                //deep copy of the domains
                shiftDomains newDomains = new shiftDomains(domains);
                newDomains.shiftDomains.get(var).clear(); //clears the hour selected
                newDomains.shiftDomains.get(var).add(value); //adds the value to the previously cleared hour

                //prints every time a variable is assigned if verbosity is 2
                if(verbosity == 2){
                    System.out.println("Assigning variable " + value + " to " + var);
                }

                //forward checking
                if(forwardCheck(problem, newDomains, var, value)){
                    //counter for testing purposes
                    counter.MRVNodeCount();
                    //backtracking
                    shiftDomains result = backtrackMRV(problem, newDomains, counter, verbosity);
                    if (result != null) {
                        return result;
                    }
                }

            }
        }
        return null; // No solution found
    }

    public static Boolean forwardCheck(SchedulingProblem problem, shiftDomains domains, int hourToAssign, int value){
        //copy of domains to not modify original
        shiftDomains updatedDomains = new shiftDomains(domains);
        //Assign the value to the hour in the copy domains
        updatedDomains.shiftDomains.get(hourToAssign).clear();
        updatedDomains.shiftDomains.get(hourToAssign).add(value);

        //Remove the assigned value from other hours' based on constraints
        for(int hour = 0; hour < updatedDomains.shiftDomains.size(); hour++){
            if(hour != hourToAssign){
                ArrayList<Integer> currentDomain = updatedDomains.shiftDomains.get(hour);

                //Check if the assigned value violates any constraints for that hour
                if(!isConsistent(problem, domains, hourToAssign, value)){
                    if(currentDomain.contains(value)){
                        int index = currentDomain.indexOf(value);
                        currentDomain.remove(index);
                    }


                    //Check if removing the value leads to an empty domain
                    if(currentDomain.isEmpty()){
                        return false;
                    }
                }
            }
        }
        return true;
    }


    private static boolean isComplete(shiftDomains domains) {
        // Check if all variables are assigned
        for (ArrayList<Integer> domain : domains.shiftDomains) {
            if (domain.size() != 1) {
                return false;
            }
        }
        return true;
    }

    private static int selectUnassignedVariable(shiftDomains domains, SchedulingProblem problem) {
        // Select the variable with the fewest remaining values
        int minRemainingValues = Integer.MAX_VALUE;
        int selectedVariable = -1;
        int maxConstraints = -1;

        //Checks sizes of all variables, selects one with the least remaining values
        for (int i = 0; i < domains.shiftDomains.size(); i++) {
            ArrayList<Integer> domain = domains.shiftDomains.get(i);
            if (domain.size() > 1 && domain.size() < minRemainingValues) {
                minRemainingValues = domain.size();
                selectedVariable = i;
            }
            //If there's a tie, it selects the hour involved in the most constraints
            else if(domain.size() == minRemainingValues){
                int numConstraints = countConstraints(problem, i, domains);
                if(numConstraints > maxConstraints){
                    selectedVariable = i;
                    maxConstraints = numConstraints;
                }
            }
        }
        return selectedVariable;
    }

    //tie-breaking heuristic for MRV
    private static int countConstraints(SchedulingProblem problem, int var, shiftDomains domains){
        int constraintCount = 0;
        ArrayList<Integer> domain = domains.shiftDomains.get(var);

        //constraint 1
        for(int employee: domain){
            int consecutiveShifts = 0;
            for(int i = var; i < domains.shiftDomains.size(); i++){
                if(domains.shiftDomains.get(i).contains(employee)){
                    consecutiveShifts++;
                } else{
                    break;
                }
            }
            if(consecutiveShifts >= problem.getMaxConsecutiveHours()){
                constraintCount++;
            }
        }
       // constraint 2
        for(int employee: domain){
            int lastAssignedIndex = -1;
            for(int i = var - 1; i >= 0; i--){
                if(domains.shiftDomains.get(i).contains(employee)){
                    lastAssignedIndex = i;
                    break;
                }
            }
            if(lastAssignedIndex != -1 && var - lastAssignedIndex <= problem.getMaxConsecutiveHours()){
                int breakDuration = var - lastAssignedIndex - 1; // Calculate the break duration
                if (breakDuration < problem.getMinBetweenShifts()) {
                    constraintCount++;
                }
            }
        }
        return constraintCount;
    }

//Consistency check
    private static boolean isConsistent(SchedulingProblem problem, shiftDomains domains, int var, int value) {
        // Check if assigning the value to the variable violates any constraints
        ArrayList<Integer> domain = domains.shiftDomains.get(var);
        if (!domain.contains(value)) {
            return false; // Value not in domain
        }

        // Check constraint: No more than 8 consecutive shifts
        int consecutiveShifts = 0;
        for (int i = var; i < domains.shiftDomains.size(); i++) {
            if (domains.shiftDomains.get(i).contains(value)) {
                consecutiveShifts++;
                if (consecutiveShifts > problem.getMaxConsecutiveHours()) {
                    return false;
                }
            } else {
                break;
            }
        }

         //Check constraint: At least 12 hours break after 8 consecutive shifts for the same person
        int  lastAssignedIndex = -1;

        //iterate through each hour's domain starting from last hour
        for(int hour = domains.shiftDomains.size()-1; hour >= 0; hour--){
            ArrayList<Integer> domain2 = domains.shiftDomains.get(hour);

            //Checks if domain of a particular hour solely contains that value
            if(domain2.size() == 1 && domain2.contains(value)){
                lastAssignedIndex = hour;
                break;
            }
        }
        //if no assignment found, constraint is satisfied
        if(lastAssignedIndex == -1){
            return true;
        }

        //Checks if they worked 8 consecutive hours before and including the last assignment
        int consecutiveHoursWorked = 0;
        for(int hour = lastAssignedIndex; hour >= 0; hour--){
            ArrayList<Integer> domain3 = domains.shiftDomains.get(hour);

            //Checks if the assigned value is in the domain
            if(domain3.contains(value)){
                consecutiveHoursWorked++;
                if(consecutiveHoursWorked >= problem.getMaxConsecutiveHours()){
                    break; //worked max consecutive hours
                }
            } else{
                consecutiveHoursWorked = 0;
            }
        }

        //Checks if there exists a minimum 12 hour break after working an 8 hour shift
        if(consecutiveHoursWorked >= problem.getMaxConsecutiveHours()){
            if(lastAssignedIndex - consecutiveHoursWorked >= problem.getMinBetweenShifts()){
                return true;
            } else{
                return false;
            }
        }

        return true;
    }


}
