package com.scaler.parking_lot.controllers.utils;

import com.scaler.parking_lot.models.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TestUtils {

    public static ParkingLot setupParkingLot(int numOfFloors, Map<VehicleType, Integer> numOfSpotsPerVehicleTypePerFloor, int numOfEntryGates, int numOfExitGates, String address){
        int parkingSpotId = 1;
        int parkingFloorId = 1;
        List<ParkingFloor> parkingFloors = new ArrayList<>();
        for(int i=0; i<numOfFloors; i++){
            List<ParkingSpot> spots = new ArrayList<>();
            for(Map.Entry<VehicleType, Integer> entry: numOfSpotsPerVehicleTypePerFloor.entrySet()){
                for(int j=0; j<entry.getValue(); j++){
                    parkingSpotId++;
                    ParkingSpot parkingSpot = new ParkingSpot(parkingSpotId,parkingSpotId,  entry.getKey());
                    spots.add(parkingSpot);
                }
            }
            ParkingFloor parkingFloor = new ParkingFloor(parkingFloorId++, spots, parkingFloorId-1,  FloorStatus.OPERATIONAL);
            parkingFloors.add(parkingFloor);
        }
        List<Gate> gates = new ArrayList<>();
        int parkingAttendantId = 1;
        for(int i=0; i<numOfEntryGates; i++){
            ParkingAttendant parkingAttendant = new ParkingAttendant(parkingAttendantId, String.valueOf(parkingAttendantId), parkingAttendantId +"@gmail.com");
            gates.add(new Gate(parkingAttendantId, String.valueOf(parkingAttendantId), GateType.ENTRY, parkingAttendant));
            parkingAttendantId++;
        }
        for(int i=0; i<numOfExitGates; i++){
            ParkingAttendant parkingAttendant = new ParkingAttendant(parkingAttendantId, String.valueOf(parkingAttendantId), parkingAttendantId+"@gmail.com");
            gates.add(new Gate(parkingAttendantId, String.valueOf(parkingAttendantId), GateType.EXIT, parkingAttendant));
            parkingAttendantId++;
        }
        return new ParkingLot(1, parkingFloors, gates, "Test Parking Lot", address);
    }


}
