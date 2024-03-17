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
one is standard, and the other can be used to change the minimum hours
between 8 consecutive shifts, and the other is the allowed
maximum consecutive shifts.

The first three times "runInstance" is called in the main method
should be used for testing. The last parameter is verbosity which
can be changed.
0 = default (just the starting and final domains)
1 = print the domains each time a recursive call is made
2 = Prints every time the program attemps to assign a variable.