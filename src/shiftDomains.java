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
    public shiftDomains(final int startTime, final int numShifts, ArrayList<ArrayList<Boolean>> employees){
        shiftDomains = new ArrayList<ArrayList<Integer>>();
        for(int hour = startTime; hour < numShifts; hour++){
            //if(employee.get(hour) is true, add to domain
            ArrayList<Integer> availableEmployees = new ArrayList<>();
            for(int employeeIndex = 0; employeeIndex < employees.size(); employeeIndex++){
               if(employees.get(employeeIndex).get(hour)){
                   availableEmployees.add(employeeIndex);
               }

                }
            shiftDomains.add(availableEmployees);
            }
            //TODO: initialize shift domains
            //For each hour,
        }
        //shiftDomains.add(nextVar)

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("Hour\tAvailable Employees\n");
        for (int hour = 0; hour < shiftDomains.size(); hour++) {
            result.append(hour).append("\t\t");
            result.append(shiftDomains.get(hour)).append("\n");
        }
        return result.toString();
    }
    }


