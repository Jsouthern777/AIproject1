import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class shiftDomains {
    //shiftDomains contains one ArrayList for each variable
    //shiftDomains.get(i) contains the possible values (people) for variable i (hour i)
    public ArrayList<ArrayList<Integer>> shiftDomains;

    //deep copy constructor
    public shiftDomains(final shiftDomains otherDomains) {
        shiftDomains = new ArrayList<ArrayList<Integer>>();
        for(int i = 0; i < otherDomains.shiftDomains.size(); i++){
            shiftDomains.add((ArrayList<Integer>) otherDomains.shiftDomains.get(i).clone());
        }
    }

    //somehow check which employees are available for each hour of the shift...
    //would it make sense to have all employees at first and then make that consistent (change availabilities) after?
    public shiftDomains(final int numShifts, final int numEmployees, final String availabilitesFilepath) throws IOException{
        shiftDomains = new ArrayList<ArrayList<Integer>>();
        
		for (int varIndex=0; varIndex<numShifts; varIndex++) {
			// add a mutable list for now; then convert to unmodifiable after they are built
			shiftDomains.add(new ArrayList<Integer>());
		}

        String filepathString = Files.readString(Path.of(availabilitesFilepath));
		Scanner scn = new Scanner(filepathString);
		while (scn.hasNextInt()) {
			int var1 = scn.nextInt(); //employee number
			int var2 = scn.nextInt(); //shift number

			if (var1 < 0 || var1 >= numEmployees) {
				throw new IllegalArgumentException(
						"Invalid shift domain input file: Employee " + var1 + " is out of bounds [0," + (numEmployees-1) + "]");
			}
			if (var2 < 0 || var2 >= numShifts) {
				throw new IllegalArgumentException(
						"Invalid shift domain input file: Shift " + var2 + " is out of bounds [0," + (numShifts-1) + "]");
			}

            //Add the employee number to the shifts that they are available for
			shiftDomains.get(var2).add(var1);
		}//end while

		if (scn.hasNext()) {
			throw new IllegalArgumentException("Invalid shift domain input file: non-numeric value: " + scn.next());
		}
		scn.close();

        /* REMOVED SAFETY FACTOR
		// Make the connections lists unmodifiable
		// This allows us to safely return iterators to those collections, since we know the
		// iterator cannot be used to change our private data!
		for (int varIndex=0; varIndex<numShifts; varIndex++) {
			shiftDomains.set(varIndex, Collections.unmodifiableList(shiftDomains.get(varIndex)));
		}
        */


    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("Hour\tAvailable Employees\n");
        for (int hour = 0; hour < shiftDomains.size(); hour++) {
            result.append(hour).append("\t\t");
            result.append(shiftDomains.get(hour)).append("\n");
        }
        return result.toString();
    }

    }//end class


