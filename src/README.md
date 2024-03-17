Authors: Jackson Southern & Andrew Bergey
Jackson implemented backtrack with MRV and FC
Andrew implemented PC2

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