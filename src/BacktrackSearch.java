import com.sun.source.doctree.SeeTree;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class BacktrackSearch {

    public static shiftDomains backtrackMRV(SchedulingProblem problem, shiftDomains domains) {

        if (isComplete(domains)) {
            return domains;
        }

        int var = selectUnassignedVariable(domains);
        ArrayList<Integer> orderedValues = orderDomainValues(domains, var);
        for (int value : orderedValues) {
            if (isConsistent(problem, domains, var, value)) {
                shiftDomains newDomains = new shiftDomains(domains);
                newDomains.shiftDomains.get(var).clear(); //clears the hour selected
                newDomains.shiftDomains.get(var).add(value); //adds the value to the previously cleared hour
                    shiftDomains result = backtrackMRV(problem, newDomains);
                    if (result != null) {
                        return result;
                    }

            }
        }

        return null; // No solution found
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

    private static int selectUnassignedVariable(shiftDomains domains) {
        // Select the variable with the fewest remaining values
        int minRemainingValues = Integer.MAX_VALUE;
        int selectedVariable = -1;
        for (int i = 0; i < domains.shiftDomains.size(); i++) {
            ArrayList<Integer> domain = domains.shiftDomains.get(i);
            if (domain.size() > 1 && domain.size() < minRemainingValues) {
                minRemainingValues = domain.size();
                selectedVariable = i;
            }
            else if(domain.size() > 1 && domain.size() == minRemainingValues){
                selectedVariable = mostConstraints(domains, selectedVariable, i);
            }
        }
        return selectedVariable;
    }
    public static int mostConstraints(shiftDomains domains, int hour1, int hour2){
        //TODO: See which of the two variables is involved in the most constraints
        int constraintsInDomain1 = countConstraints(domains, hour1);
        int constraintsInDomain2 = countConstraints(domains, hour2);

        // Compare the number of constraints in each domain
        if (constraintsInDomain1 >= constraintsInDomain2) {
            return hour1;
        } else {
            return hour2;
        }
    }

    // Helper method to count the number of constraints for a set of domain values
    private static int countConstraints(shiftDomains domains, int hour) {
        ArrayList<Integer> domain = domains.shiftDomains.get(hour);
        int consecutiveHours = 0;
        boolean reachedEightConsecutiveHours = false;
        int hoursWithoutBreak = 0;
        int constraints = 0;

        for (int i = 0; i < domains.shiftDomains.size(); i++) {
            int currentShift = domains.shiftDomains.get(i);

            // Increment consecutive hours counter
            consecutiveHours++;

            // If the person worked for 8 consecutive hours
            if (consecutiveHours == 8) {
                reachedEightConsecutiveHours = true;
            }

            // Check if a break of 12 hours is required
            if (reachedEightConsecutiveHours) {
                // If this is the first hour after 8 consecutive hours, start counting
                if (hoursWithoutBreak == 0) {
                    // Start counting hours without a break
                    for (int j = i + 1; j < domain.size(); j++) {
                        if (domain.get(j) == currentShift) {
                            break; // Break found, stop counting
                        }
                        hoursWithoutBreak++;
                    }
                }

                // If the hours without a break reach 12 or more, the constraint is satisfied
                if (hoursWithoutBreak < 12) {
                    constraints++; // Constraint violated
                }
            }
        }
        return constraints;
    }

    private static ArrayList<Integer> orderDomainValues(shiftDomains domains, int var) {
        // Order values according to some heuristic (e.g., least-constraining value)
        return domains.shiftDomains.get(var);
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
        if (var > 0) {
            // Find the last time the person was assigned
            int lastAssignedIndex = -1;
            for (int i = var - 1; i >= 0; i--) {
                if (domains.shiftDomains.get(i).contains(value)) {
                    lastAssignedIndex = i;
                    break;
                }
            }

            // If the person was assigned within the last 8 shifts, check for the break duration
            if (lastAssignedIndex != -1 && var - lastAssignedIndex <= problem.getMaxConsecutiveHours()) {
                int breakDuration = var - lastAssignedIndex - 1; // Calculate the break duration
                if (breakDuration < problem.getMinBetweenShifts()) {
                    return false; // Break duration is less than the required minimum
                }
            }
        }

        return true;
    }


}
