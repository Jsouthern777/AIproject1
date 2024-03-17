import com.sun.source.doctree.SeeTree;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BacktrackSearch {

    public static shiftDomains backtrackMRV(SchedulingProblem problem, shiftDomains domains) {

        if (isComplete(domains)) {
            return domains;
        }

        int var = selectUnassignedVariable(domains, problem);
        ArrayList<Integer> orderedValues = domains.shiftDomains.get(var);
        for (int value : orderedValues) {
            if (isConsistent(problem, domains, var, value)) {
                shiftDomains newDomains = new shiftDomains(domains);
                newDomains.shiftDomains.get(var).clear(); //clears the hour selected
                newDomains.shiftDomains.get(var).add(value); //adds the value to the previously cleared hour

                //forward checking
                if(forwardCheck(problem, newDomains, var, value)){
                    shiftDomains result = backtrackMRV(problem, newDomains);
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
                if(!checkConstraints(hour, updatedDomains, problem)){
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

//    private static boolean forwardCheck(SchedulingProblem problem, shiftDomains domains, int var, int value) {
//        // Iterate over the unassigned variables affected by the assignment of the current variable
//        for (int neighborVar : getAffectedVariables(problem, var)) {
//            if (!domains.shiftDomains.contains(neighborVar)) {
//                // Remove the assigned value from the domain of the unassigned variable
//                domains.shiftDomains.get(neighborVar).remove(Integer.valueOf(value));
//                if(domains.shiftDomains.get(neighborVar).isEmpty()){
//                    return false;
//                }
//            }
//        }
//        return true;
//    }

    private static List<Integer> getAffectedVariables(SchedulingProblem problem, int var) {
        List<Integer> affectedVariables = new ArrayList<>();

        // Consider shifts occurring immediately before or after the assigned shift
        if (var > 0) {
            affectedVariables.add(var - 1); // Shift before the assigned shift
        }
        if (var < problem.getNumShifts() - 1) {
            affectedVariables.add(var + 1); // Shift after the assigned shift
        }

        // Consider shifts occurring during the break period after the assigned shift
        int breakEnd = var + problem.getMinBetweenShifts(); // Calculate the end of the break period
        for (int i = var + 1; i < breakEnd && i < problem.getNumShifts(); i++) {
            affectedVariables.add(i);
        }

        return affectedVariables;
    }

    public static Boolean checkConstraints(int hour, shiftDomains updatedDomains, SchedulingProblem problem){
        ArrayList<Integer> domain  = updatedDomains.shiftDomains.get(hour);

        for(int employee: domain){
            int consecutiveShifts = 0;
            for(int i = hour; i < updatedDomains.shiftDomains.size(); i++){
                if(updatedDomains.shiftDomains.get(i).contains(employee)){
                    consecutiveShifts++;
                } else{
                    break;
                }
            }
            if(consecutiveShifts >= problem.getMaxConsecutiveHours()){
                return false;
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
        for (int i = 0; i < domains.shiftDomains.size(); i++) {
            ArrayList<Integer> domain = domains.shiftDomains.get(i);
            if (domain.size() > 1 && domain.size() < minRemainingValues) {
                minRemainingValues = domain.size();
                selectedVariable = i;
            }
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
        //constraint 2
//        for(int employee: domain){
//            int lastAssignedIndex = -1;
//            for(int i = var - 1; i >= 0; i--){
//                if(domains.shiftDomains.get(i).contains(employee)){
//                    lastAssignedIndex = i;
//                    break;
//                }
//            }
//            if(lastAssignedIndex != -1 && var - lastAssignedIndex <= problem.getMaxConsecutiveHours()){
//                int breakDuration = var - lastAssignedIndex - 1; // Calculate the break duration
//                if (breakDuration < problem.getMinBetweenShifts()) {
//                    constraintCount++;
//                }
//            }
//        }
        return constraintCount;
    }


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

        // Check constraint: At least 12 hours between consecutive shifts for the same person
//        if (var > 0) {
//            // Find the last time the person was assigned
//            int lastAssignedIndex = -1;
//            for (int i = var - 1; i >= 0; i--) {
//                if (domains.shiftDomains.get(i).contains(value)) {
//                    lastAssignedIndex = i;
//                    break;
//                }
//            }
//
//            // If the person was assigned within the last 8 shifts, check for the break duration
//            if (lastAssignedIndex != -1 && var - lastAssignedIndex <= problem.getMaxConsecutiveHours()) {
//                int breakDuration = var - lastAssignedIndex - 1; // Calculate the break duration
//                if (breakDuration < problem.getMinBetweenShifts()) {
//                    return false; // Break duration is less than the required minimum
//                }
//            }
//        }

        return true;
    }


}
