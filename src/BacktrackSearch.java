import java.util.ArrayList;

//This class will do the backtracking with FC and MRV search
public class BacktrackSearch {

        public static shiftDomains backtrackMRV(final SchedulingProblem  problem, final shiftDomains domains, final int nextHourToAssign){
            boolean solution = true;

            for(ArrayList<Integer> domain: domains.shiftDomains){
                if(domain.size() != 1){
                    solution = false;
                    break;
                }
            }
            if(solution) {
                return domains;
            }

            //TODO: Implement search







            return null;
        }

        public static Boolean isConsistent(){
            //TODO: Run tests to see if a particular value assignment is consistent
            return false;
        }
}
