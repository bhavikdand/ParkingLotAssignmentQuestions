## Assign vehicles evenly across floors

Currently, we are using a simple assign nearest available parking spot while 
issuing a ticket. This is not optimal as it can lead to uneven distribution of
vehicles across floors. For example, if there are 100 parking spots on 1st floor, 
and 1000 vehicles arrive at the same time, all of them will be assigned to 1st floor.
This will lead to congestion on 1st floor, while other floors will be empty.
We need to improve the algorithm to assign vehicles evenly across floors.

Expected working of the system:
1. When a vehicle arrives, the system should assign it to the floor with least number
 of available spots for that vehicle type.
2. If there are multiple floors with same number of available spots, the system should
 assign the vehicle to the floor with the lowest floor number.
3. If there are no available spots on any floor, the system should not issue a ticket.

Implement the EqualDistributionAssignmentStrategy class to achieve this. 
Also implement ParkingLotService and required repository classes to achieve the goal.
