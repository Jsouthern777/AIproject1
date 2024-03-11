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

    public shiftDomains(final int numShifts, ArrayList<List<Integer>> employees){
        shiftDomains = new ArrayList<ArrayList<Integer>>();
        for(int varIndex = 0; varIndex < numShifts; varIndex++){
            ArrayList<Integer> nextShift = new ArrayList<>();
            //TODO: initialize shift domains

        }
    }
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
