import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

//Only pseudocode used was that from our own textbook
//This class will do the backtracking with FC and MRV search
public class BacktrackSearch {
        public static shiftDomains backtrackMRV(final SchedulingProblem  problem, final shiftDomains domains, final ArrayList<Integer> assignedShifts){
            boolean solution = true;

            //Check if  size of all domains is one
            for(ArrayList<Integer> domain: domains.shiftDomains){
                if(domain.size() != 1){
                    solution = false;
                    break;
                }
            }
            //A solution has thus been theoretically found
            if(solution) {
                return domains;
            }

            int hourToAssign = assignNextHour(domains, assignedShifts);
           // System.out.println("hour to assign: " + hourToAssign);
            for(int value: domains.shiftDomains.get(hourToAssign)){
                System.out.println("Hour to Assign: " + hourToAssign + " value: " + value);
                assignedShifts.set(hourToAssign, value);
               // System.out.println("assignedShifts size: " + assignedShifts.size());
                if(isConsistent(domains, assignedShifts)){
                  //  System.out.println("consistency check");
                    //System.out.println("consistency found");
                    shiftDomains newDomains = new shiftDomains(domains);
                    newDomains.shiftDomains.get(hourToAssign).clear();
                    newDomains.shiftDomains.get(hourToAssign).add(value);
                    //System.out.println("Line 33" + newDomains.shiftDomains.get(hourToAssign));
                    //Do forward checking here?
                    if(forwardCheck(hourToAssign, value, newDomains)){
                        shiftDomains result = backtrackMRV(problem, newDomains, assignedShifts);
                        if(result != null){
                            return result;
                        }
                    }
                    //newDomains.shiftDomains.set(hourToAssign, new ArrayList<>(domains.shiftDomains.get((hourToAssign))));
                }
               // System.out.println("assignedShift size -1: " + (assignedShifts.size()-1));
               // System.out.println("removing shift: " + assignedShifts.get(assignedShifts.size()-1));

                //domain restoration
                assignedShifts.remove(hourToAssign);
            }
            //backtracking
            return null;
        }

        //Constraints:
        // 1) No more than 8 hours consecutively for one person
        // 2) After working for at max 8 shifts consecutively,
        // a person MUST have at least 12 hours/shifts NOT WORKING before they can be scheduled again.
        public static Boolean isConsistent(shiftDomains domains, ArrayList<Integer> assignedShifts){
            //Constraint 1: One person can't work more than 8 hours/shifts consecutively
            for(int i = 0; i < assignedShifts.size(); i++){
                int person = assignedShifts.get(i);
                int consecutiveHours = 1;
                for(int j = i + 1; j < assignedShifts.size(); j++){
                    if(assignedShifts.get(j) == person){
                       // System.out.println("assignedShifts.get(j) = " + assignedShifts.get(j) + " which == " + person);
                        consecutiveHours++;
                       // System.out.println("consecutive Hours: " + consecutiveHours);
                        if (consecutiveHours > 8){
                            return false;
                        }
                    } else{
                        break;
                    }
                }
            }
            //constraint 2
            for (int i = 0; i < assignedShifts.size() - 8; i++) {
                int person = assignedShifts.get(i);
                int consecutiveHours = 0;
                for (int j = i; j < i + 8; j++) {
                    if (assignedShifts.get(j) == person) {
                        consecutiveHours++;
                    }
                }
                if (consecutiveHours == 8) {
                    int breakStartHour = i + 8;
                    int breakEndHour = breakStartHour + 12;
                    for (int k = breakStartHour; k < breakEndHour; k++) {
                        if (assignedShifts.get(k) == person) {
                            return false; // Less than 12 hours break after 8 consecutive shifts
                        }
                    }
                }
            }
            return true;
        }
//want to make sure that assigning a particular hour doesn't leave another hour with nothing in its domain
        public static Boolean forwardCheck(final int  hourToAssign, final int value, final shiftDomains domains){
            // Create a copy of the current domains to work with
            shiftDomains updatedDomains = new shiftDomains(domains);
            System.out.println("Forward check call for hour: " + hourToAssign + ", value: " + value);

            // Assign the value to the hour in the updated domains
            updatedDomains.shiftDomains.get(hourToAssign).clear();
            updatedDomains.shiftDomains.get(hourToAssign).add(value);

            // Remove the assigned value from other hours' domains based on constraints
            for (int h = 0; h < updatedDomains.shiftDomains.size(); h++) {
                if (h != hourToAssign) {
                    ArrayList<Integer> currentDomain = updatedDomains.shiftDomains.get(h);
                    // Check if the assigned value violates any constraints for this hour
                    // For example, if the assigned value is not consistent with the constraints of the problem
                    // you would remove it from the domain of this hour
                    if (!checkConstraints(h, updatedDomains)) {
                        currentDomain.remove(Integer.valueOf(value));
                        // Check if removing the value leads to an empty domain
                        if (currentDomain.isEmpty()) {
                            return false; // Empty domain found, inconsistency detected
                        }
                    }
                }
            }
            return true; // No empty domains found, consistent assignment
        }

        private static Boolean checkConstraints(int hour, shiftDomains updatedDomains){
// Get the domain of the updated hour
            List<Integer> currentHourDomain = updatedDomains.shiftDomains.get(hour);
            int consecutiveShifts = 0;
            int lastShiftHour = -1;

            for (int i = 0; i < currentHourDomain.size(); i++) {
                int currentShiftHour = currentHourDomain.get(i);

                // Check for consecutive shifts
                if (currentShiftHour == lastShiftHour + 1) {
                    consecutiveShifts++;
                } else {
                    consecutiveShifts = 1; // Reset consecutive shifts count
                }

                // Check if consecutive shifts exceed the maximum allowed
                if (consecutiveShifts > 8) {
                    return false; // Exceeded maximum consecutive hours constraint
                }

                // Check for minimum break between shifts
                if (i > 0) {
                    int breakDuration = currentShiftHour - lastShiftHour;
                    if (breakDuration < 12) {
                        return false; // Break duration constraint violated
                    }
                }

                lastShiftHour = currentShiftHour;
            }
            return true; // All constraints satisfied
        }


        public static int assignNextHour(final shiftDomains domains, final ArrayList<Integer> assignedShifts){
            //need to have statement such that if an hour already has one element in its domain, do NOT check
            //TODO: use MRV to get the next variable
            int minValue = Integer.MAX_VALUE;
            int minHourVal = Integer.MAX_VALUE;


            // Loop through each hour (variable) in the domain
            for(int hour = 0; hour < domains.shiftDomains.size(); hour++){
                //System.out.println("For loop hour: " + hour);
                // Get the domain of values for the current hour
                ArrayList<Integer> currentHourDomain = domains.shiftDomains.get(hour);
                //System.out.println("Domain for hour iteration " + hour);
                //System.out.println(currentHourDomain.toString());

                // Calculate the number of remaining values for the current hour
                int remainingValues = currentHourDomain.size();

                if(remainingValues == 1){
                    continue;
                }

                //  System.out.println("Remaining values for this hour: " + remainingValues);

                // If the current hour has more remaining values than the previous maximum, update the maximum
                if(remainingValues < minValue){
                    // System.out.println("At hour " + hour + ", remaining values " + remainingValues + " is less than maxValue " + minValue);
                    minValue = remainingValues;
                    minHourVal = hour; // Update the hour of the variable with the most remaining values
                }
               //  If the current hour has the same number of remaining values as the previous maximum, apply most-constraints tie-breaker

                else if (remainingValues == minValue) {
                  //  System.out.println("Before else if remvalues == minvalues: " + minValue);
                    // Check which hour is involved in the most constraints
                    // System.out.println("At hour " + hour + ", remaining values " + remainingValues + " is equal to maxValue " + minValue);
                    ArrayList<Integer> hourWithMostConstraints = mostConstraints(domains.shiftDomains.get(minHourVal), currentHourDomain, assignedShifts);
                    //System.out.println("Hour with most constraints is " + hour + " " + hourWithMostConstraints);
                    // Update the maximum hour based on the tie-breaking logic

                    if (hourWithMostConstraints == currentHourDomain) {
                        // System.out.println("maxHourVal " + minHourVal + "is the hour: " + hour);
                        minHourVal = hour;
                       // System.out.println("Thus, minHourVal is " + hour);
                    }
                    else{
                       // System.out.println("min hour val remains " + minHourVal);
                    }
                }
            }
            return minHourVal;
        }
    public static ArrayList<Integer> mostConstraints(final ArrayList<Integer> hour1, final ArrayList<Integer> hour2, final ArrayList<Integer> assignedShifts){
            //TODO: See which of the two variables is involved in the most constraints
        int constraintsInDomain1 = countConstraints(hour1);
        int constraintsInDomain2 = countConstraints(hour2);

        // Compare the number of constraints in each domain
        if (constraintsInDomain1 >= constraintsInDomain2) {
            return hour1;
        } else {
            return hour2;
        }
    }

    // Helper method to count the number of constraints for a set of domain values
    private static int countConstraints(ArrayList<Integer> domain) {
        int consecutiveHours = 0;
        boolean reachedEightConsecutiveHours = false;
        int hoursWithoutBreak = 0;
        int constraints = 0;

        for (int i = 0; i < domain.size(); i++) {
            int currentShift = domain.get(i);

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



}
