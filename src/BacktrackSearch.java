import java.lang.reflect.Array;
import java.util.ArrayList;
//Only pseudocode used was that from our own textbook
//This class will do the backtracking with FC and MRV search
public class BacktrackSearch {
    ArrayList<Integer> assignedShifts = new ArrayList<>();

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

            int hourToAssign = assignNextHour(problem, domains);
            for(int value: domains.shiftDomains.get(hourToAssign)){
                assignedShifts.set(hourToAssign, value);
                if(isConsistent(domains, assignedShifts)){
                    shiftDomains newDomains = new shiftDomains(domains);
                    newDomains.shiftDomains.get(hourToAssign).clear();
                    newDomains.shiftDomains.get(hourToAssign).add(value);
                    shiftDomains result = backtrackMRV(problem, newDomains, assignedShifts);
                    if(result != null){
                        return result;
                    }
                }
                assignedShifts.remove(assignedShifts.size() - 1);
            }
            return null;
        }

        //Constraints:
        // 1) No more than 8 hours consecutively for one person
        // 2) After working for at max 8 shifts consecutively,
        // a person MUST have at least 12 hours/shifts NOT WORKING before they can be scheduled again.
        public static Boolean isConsistent(shiftDomains domains, ArrayList<Integer> assignedShifts){
            //Constraint 1: One person can't work more than 8 hours/shifts consecutively
            for(int person: assignedShifts){
                int consecutiveShifts = 0;
                for(int i = 1; i < assignedShifts.size(); i++){
                    if(assignedShifts.get(i) == person && assignedShifts.get(i) == assignedShifts.get(i-1)){
                        consecutiveShifts++;
                        if(consecutiveShifts > 8){
                            return false;
                        }
                    }
                    else{
                        consecutiveShifts = 1;
                    }

                }
            }
        //Constraint 2: Minimum 12-hour break between consecutive shifts for each person
            for(int i = 1; i < assignedShifts.size(); i++){
                int currentHour = assignedShifts.get(i);
                int previousHour = assignedShifts.get(i-1);
                if(currentHour - previousHour <= 12 && i >= 8){
                    return false;
                }
            }
            return true;
        }

        public static Boolean forwardCheck(final SchedulingProblem prob, final shiftDomains domains){
            return false;
        }

        //basically the MRV portion!
        public static int assignNextHour(final SchedulingProblem prob, final shiftDomains domains){
            //TODO: use MRV to get the next variable
            int maxValue = -1;
            int maxHourVal =  -1;


            // Loop through each hour (variable) in the domain
            for(int hour = 0; hour < domains.shiftDomains.size(); hour++){
                // Get the domain of values for the current hour
                ArrayList<Integer> currentHourDomain = domains.shiftDomains.get(hour);

                // Calculate the number of remaining values for the current hour
                int remainingValues = currentHourDomain.size();

                // If the current hour has more remaining values than the previous maximum, update the maximum
                if(remainingValues > maxValue){
                    maxValue = remainingValues;
                    maxHourVal = hour; // Update the hour of the variable with the most remaining values
                }
                // If the current hour has the same number of remaining values as the previous maximum, apply tie-breaking logic
                else if (remainingValues == maxValue) {
                    // Check which hour is involved in the most constraints
                    ArrayList<Integer> hourWithMostConstraints = mostConstraints(domains.shiftDomains.get(maxHourVal), currentHourDomain);
                    // Update the maximum hour based on the tie-breaking logic
                    if (hourWithMostConstraints == currentHourDomain) {
                        maxHourVal = hour;
                    }
                }
            }
            return maxHourVal;
        }
    public static ArrayList<Integer> mostConstraints(final ArrayList<Integer> hour1, final ArrayList<Integer> hour2){
            //TODO: See which of the two variables is involved in the most constraints
            return hour1;
    }
}
