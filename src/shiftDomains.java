import java.util.ArrayList;
import java.util.List;

public class shiftDomains {
    //shiftDomains contains one ArrayList for each variable
    //shiftDomains.get(i) contains the possible values (people) for variable i (hour i)
    public ArrayList<ArrayList<Integer>> shiftDomains;

    public shiftDomains(final shiftDomains otherDomains) {
        shiftDomains = new ArrayList<ArrayList<Integer>>();
        for(int i = 0; i < otherDomains.shiftDomains.size(); i++){
            shiftDomains.add((ArrayList<Integer>) otherDomains.shiftDomains.get(i).clone());
        }
    }

    //somehow check which employees are available for each hour of the shift...
    //would it make sense to have all employees at first and then make that consistent (change availabilities) after?
    public shiftDomains(final int startTime, final int numShifts, ArrayList<List<Integer>> employees){
        shiftDomains = new ArrayList<ArrayList<Integer>>();
        for(int hour = startTime; hour < numShifts; hour++){
            //if(employee.get(hour) is true, add to domain
            ArrayList<Integer> nextHour = new ArrayList<>();
            for(int i = 0; i < employees.size(); i++){
               // if(employees.get(i).get(hour)){

                }
            }
            //TODO: initialize shift domains
            //For each hour,
        }
        //shiftDomains.add(nextVar)

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("Variable\tPossible Values\n");
        for(int varIndex = 0; varIndex < shiftDomains.size(); varIndex++){
            result.append(varIndex).append("\t\t");
            result.append(shiftDomains.get(varIndex));
            result.append("\n");
        }
        return result.toString();
    }
    }


