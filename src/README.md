Authors: Jackson Southern & Andrew Bergey
Jackson implemented backtrack with MRV and FC
Andrew implemented PC2 as the consistency check in a simple backtracking search

Problem: Given a number of shifts and set of employees with
differing availabilities, assign all shifts such that:
1) No more than "maxConsecutiveHours" shifts consecutively (8 by default)
2) After "maxConsecutiveHours" shifts, an employee must not be scheduled for
3) "minHoursBetweenShifts" shifts (12 by default).

Instructions for use and testing:
The solver class contains two runInstances methods, 
one loads the default values of "maxConsecutiveHours" and 
minHoursBetweenShifts, and the other runInstance can be used 
to manually enter "maxConsecutiveShifts" and "minHoursBetweenShifts".

The first three times "runInstance" is called in the main method
should be used for testing. The last parameter is verbosity which
can be changed.
0 = default (just the starting and final domains)
1 = print the domains each time a recursive call is made
2 = Prints every time the program attemps to assign a variable.

There is also a testBuilder.java class which generates text files
containing randomly generated employee availabilities. The output
filename describes the number of employees and number of shifts 
included in the test case. The left column is the employee number
and the right column corresponds to a shift that that employee is 
available to work. A few test cases are included in the zip file, 
but more can be easily generated and tested using testBuilder.java.

Notes on interpretation:
When a set of domains prints out, the left column labeled 'Hour'
corresponds to the shift being assigned, and the 'Available Employees'
column is a list of any employess that are available to work that shift.
As the code progresses domains will shrink to zero. When the code finishes
the domains will contain only one employee and that is the employee 
assigned to work the corresponding shift

Test case for validation : 
A few test cases are in main in the Solver class which launches an instance of 
the same problem using each search algorithm. A good small case for testing is
runInstance(10,5, "5E 10S.txt",  0);
This case has 10 shifts to fill, 5 employees with varying abilities defined
in the text file, and the last parameter is the verbosity. This call to runInstance
uses the default maximum shift of 8 hours long and a minimum of 12 hours between
shifts. If you wish to change these default parameters please use the function call
runInstance(10,2, "5E 10S.txt", minHoursBetweenShifts, maxConsecutiveShifts, verbosity);

There is also a test case included in the solver class that is not solvable if you would
like to validate that:
runInstance(25, 10, "10E 25S.txt", 12, 2,0);  //should fail

