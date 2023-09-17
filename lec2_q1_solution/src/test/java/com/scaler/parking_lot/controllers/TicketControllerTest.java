package com.scaler.parking_lot.controllers;

import com.scaler.parking_lot.dtos.GenerateTicketRequestDto;
import com.scaler.parking_lot.dtos.GenerateTicketResponseDto;
import com.scaler.parking_lot.dtos.ResponseStatus;
import com.scaler.parking_lot.models.*;
import com.scaler.parking_lot.respositories.*;
import com.scaler.parking_lot.services.TicketService;
import com.scaler.parking_lot.services.TicketServiceImpl;
import com.scaler.parking_lot.services.VehicleService;
import com.scaler.parking_lot.services.VehicleServiceImpl;
import com.scaler.parking_lot.strategies.EqualDistributionAssignmentStrategy;
import com.scaler.parking_lot.strategies.SpotAssignmentStrategy;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class TicketControllerTest {

    GateRepository gateRepository;
    VehicleRepository vehicleRepository;
    SpotAssignmentStrategy spotAssignmentStrategy;
    ParkingLotRepository parkingLotRepository;
    TicketRepository ticketRepository;
    VehicleService vehicleService;
    TicketService ticketService;
    TicketController ticketController;

    @Test
    public void testIssueTicketWith1AvailableParkingSpot() {
        setupTest(1, Map.of(VehicleType.CAR, 1), 1, 1, "Bangalore");
        GenerateTicketResponseDto responseDto = ticketController.generateTicket(new GenerateTicketRequestDto(1, "KA-01-HH-1234", "CAR"));
        assertEquals(responseDto.getResponse().getResponseStatus(), ResponseStatus.SUCCESS);
        assertNotNull(responseDto.getTicket());
    }

    @Test
    public void testIssueTicketWith2AvailableParkingSpots() {
        setupTest(1, Map.of(VehicleType.CAR, 2), 1, 1, "Bangalore");
        GenerateTicketResponseDto responseDto = ticketController.generateTicket(new GenerateTicketRequestDto(1, "KA-01-HH-1234", "CAR"));
        assertEquals(responseDto.getResponse().getResponseStatus(), ResponseStatus.SUCCESS);
        assertNotNull(responseDto.getTicket());

        responseDto = ticketController.generateTicket(new GenerateTicketRequestDto(1, "KA-01-HH-1235", "CAR"));
        assertEquals(responseDto.getResponse().getResponseStatus(), ResponseStatus.SUCCESS);
        assertNotNull(responseDto.getTicket());
    }

    @Test
    public void testIssueTicketWithNoAvailableSpots(){
        setupTest(1, Map.of(VehicleType.CAR, 1), 1, 1, "Bangalore");
        GenerateTicketResponseDto responseDto = ticketController.generateTicket(new GenerateTicketRequestDto(1, "KA-01-HH-1234", "CAR"));
        assertEquals(responseDto.getResponse().getResponseStatus(), ResponseStatus.SUCCESS);
        assertNotNull(responseDto.getTicket());

        responseDto = ticketController.generateTicket(new GenerateTicketRequestDto(1, "KA-01-HH-1235", "CAR"));
        assertEquals(responseDto.getResponse().getResponseStatus(), ResponseStatus.FAILURE);
        assertNull(responseDto.getTicket());
    }

    @Test
    public void testIssueTicketWithFromExitGate(){
        setupTest(1, Map.of(VehicleType.CAR, 1), 1, 1, "Bangalore");
        GenerateTicketResponseDto responseDto = ticketController.generateTicket(new GenerateTicketRequestDto(2, "KA-01-HH-1234", "CAR"));
        assertEquals(responseDto.getResponse().getResponseStatus(), ResponseStatus.FAILURE);
        assertNull(responseDto.getTicket());
    }

    @Test
    public void testIssueTicketFromNonExistingGate(){
        setupTest(1, Map.of(VehicleType.CAR, 1), 1, 1, "Bangalore");
        GenerateTicketResponseDto responseDto = ticketController.generateTicket(new GenerateTicketRequestDto(3, "KA-01-HH-1234", "CAR"));
        assertEquals(responseDto.getResponse().getResponseStatus(), ResponseStatus.FAILURE);
        assertNull(responseDto.getTicket());
    }

    @Test
    public void testIssueTicketWith3Floors2CarsOn1stAnd2ndFloorAnd1On3rdFloor(){
        setupTest(3, Map.of(VehicleType.CAR, 2), 1, 1, "Bangalore");
        GenerateTicketResponseDto responseDto = ticketController.generateTicket(new GenerateTicketRequestDto(1, "KA-01-HH-1234", "CAR"));
        assertEquals(responseDto.getResponse().getResponseStatus(), ResponseStatus.SUCCESS);
        assertNotNull(responseDto.getTicket()); // Lead to 1st floor

        responseDto = ticketController.generateTicket(new GenerateTicketRequestDto(1, "KA-01-HH-1235", "CAR"));
        assertEquals(responseDto.getResponse().getResponseStatus(), ResponseStatus.SUCCESS);
        assertNotNull(responseDto.getTicket()); //Lead to 2nd floor

        responseDto = ticketController.generateTicket(new GenerateTicketRequestDto(1, "KA-01-HH-1236", "CAR"));
        assertEquals(responseDto.getResponse().getResponseStatus(), ResponseStatus.SUCCESS);
        assertNotNull(responseDto.getTicket()); //Lead to 3rd floor

        responseDto = ticketController.generateTicket(new GenerateTicketRequestDto(1, "KA-01-HH-1237", "CAR"));
        assertEquals(responseDto.getResponse().getResponseStatus(), ResponseStatus.SUCCESS);
        assertNotNull(responseDto.getTicket()); //Lead to 1st floor

        responseDto = ticketController.generateTicket(new GenerateTicketRequestDto(1, "KA-01-HH-1238", "CAR"));
        assertEquals(responseDto.getResponse().getResponseStatus(), ResponseStatus.SUCCESS);
        assertNotNull(responseDto.getTicket()); //Lead to 2nd floor

        Optional<ParkingLot> parkingLotOptional = parkingLotRepository.getParkingLotById(1);
        ParkingLot parkingLot = parkingLotOptional.get();
        ParkingFloor parkingFloor = parkingLot.getParkingFloors().stream().filter(floor -> floor.getId() == 3).findFirst().get();
        int count = 0;
        for (ParkingSpot spot : parkingFloor.getSpots()) {
            if(spot.getStatus().equals(ParkingSpotStatus.AVAILABLE) && spot.getSupportedVehicleType().equals(VehicleType.CAR)){
                count++;
                break;
            }
        }
        assertEquals(count, 1);

        responseDto = ticketController.generateTicket(new GenerateTicketRequestDto(1, "KA-01-HH-1239", "CAR"));
        assertEquals(responseDto.getResponse().getResponseStatus(), ResponseStatus.SUCCESS);
        assertNotNull(responseDto.getTicket()); //Lead to 3rd floor


        parkingLotOptional = parkingLotRepository.getParkingLotById(1);
        parkingLot = parkingLotOptional.get();
        parkingFloor = parkingLot.getParkingFloors().stream().filter(floor -> floor.getId() == 3).findFirst().get();
        count = 0;
        for (ParkingSpot spot : parkingFloor.getSpots()) {
            if(spot.getStatus().equals(ParkingSpotStatus.AVAILABLE) && spot.getSupportedVehicleType().equals(VehicleType.CAR)){
                count++;
                break;
            }
        }
        assertEquals(count, 0);
    }

    @Test
    public void testIssueWithWith1FloorUnderMaintenance(){
        setupTest(3, Map.of(VehicleType.CAR, 1), 1, 1, "Bangalore");
        Optional<ParkingLot> parkingLotOptional = parkingLotRepository.getParkingLotById(1);
        ParkingLot parkingLot = parkingLotOptional.get();
        ParkingFloor parkingFloor = parkingLot.getParkingFloors().stream().filter(floor -> floor.getId() == 2).findFirst().get();
        parkingFloor.setStatus(FloorStatus.UNDER_MAINTENANCE);
        GenerateTicketResponseDto responseDto = ticketController.generateTicket(new GenerateTicketRequestDto(1, "KA-01-HH-1234", "CAR"));
        assertEquals(responseDto.getResponse().getResponseStatus(), ResponseStatus.SUCCESS);
        assertNotNull(responseDto.getTicket()); // Lead to 1st floor

        responseDto = ticketController.generateTicket(new GenerateTicketRequestDto(1, "KA-01-HH-1235", "CAR"));
        assertEquals(responseDto.getResponse().getResponseStatus(), ResponseStatus.SUCCESS);
        assertNotNull(responseDto.getTicket()); //Lead to 3rd floor

        responseDto = ticketController.generateTicket(new GenerateTicketRequestDto(1, "KA-01-HH-1235", "CAR"));
        assertEquals(responseDto.getResponse().getResponseStatus(), ResponseStatus.FAILURE);
        assertNull(responseDto.getTicket());
    }



    public void setupTest(int numOfFloors, Map<VehicleType, Integer> numOfSpotsPerVehicleTypePerFloor, int numOfEntryGates, int numOfExitGates, String address) {
        ParkingLot parkingLot = setupParkingLot(numOfFloors, numOfSpotsPerVehicleTypePerFloor, numOfEntryGates, numOfExitGates, address);
        Map<Long, ParkingLot> parkingLotMap = Map.of(parkingLot.getId(), parkingLot);
        Map<Long,Gate> gateMap = parkingLot.getGates().stream().collect(Collectors.toMap(Gate::getId, Function.identity()));
        gateRepository = new InMemoryGateRepositoryImpl(gateMap);
        vehicleRepository = new InMemoryVehicleRepositoryImpl();
        spotAssignmentStrategy = new EqualDistributionAssignmentStrategy();
        parkingLotRepository = new InMemoryParkingLotRepositoryImpl(parkingLotMap);
        ticketRepository = new InMemoryTicketRepositoryImpl();
        vehicleService = new VehicleServiceImpl(vehicleRepository);
        ticketService = new TicketServiceImpl(gateRepository, vehicleService, spotAssignmentStrategy, parkingLotRepository, ticketRepository);
        ticketController = new TicketController(ticketService);
    }

    public ParkingLot setupParkingLot(int numOfFloors, Map<VehicleType, Integer> numOfSpotsPerVehicleTypePerFloor, int numOfEntryGates, int numOfExitGates, String address){
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
