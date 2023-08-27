package com.scaler.parking_lot.controllers;

import com.scaler.parking_lot.controllers.utils.TestUtils;
import com.scaler.parking_lot.dtos.GetParkingLotCapacityRequestDto;
import com.scaler.parking_lot.dtos.GetParkingLotCapacityResponseDto;
import com.scaler.parking_lot.dtos.ResponseStatus;
import com.scaler.parking_lot.models.*;
import com.scaler.parking_lot.respositories.InMemoryParkingLotRepositoryImpl;
import com.scaler.parking_lot.respositories.ParkingLotRepository;
import com.scaler.parking_lot.services.*;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class ParkingLotControllerTest {

    ParkingLotRepository parkingLotRepository;
    ParkingLotController parkingLotController;
    ParkingLotService parkingLotService;

    @Test
    public void testGetParkingLotCapacityWithNegativeParkingLotId(){
        setupTest(2, Map.of(VehicleType.CAR, 2, VehicleType.TRUCK, 2), 1, 1, "Bangalore");
        GetParkingLotCapacityRequestDto getParkingLotCapacityRequestDto = new GetParkingLotCapacityRequestDto();
        getParkingLotCapacityRequestDto.setParkingLotId(-1);
        getParkingLotCapacityRequestDto.setParkingFloorIds(null);
        getParkingLotCapacityRequestDto.setVehicleTypes(null);
        GetParkingLotCapacityResponseDto parkingLotCapacity = parkingLotController.getParkingLotCapacity(getParkingLotCapacityRequestDto);
        assertEquals(parkingLotCapacity.getResponse().getResponseStatus(), ResponseStatus.FAILURE);
    }

    @Test
    public void testGetParkingLotCapacityWithInvalidParkingLotId(){
        setupTest(2, Map.of(VehicleType.CAR, 2, VehicleType.TRUCK, 2), 1, 1, "Bangalore");
        GetParkingLotCapacityRequestDto getParkingLotCapacityRequestDto = new GetParkingLotCapacityRequestDto();
        getParkingLotCapacityRequestDto.setParkingLotId(2);
        getParkingLotCapacityRequestDto.setParkingFloorIds(null);
        getParkingLotCapacityRequestDto.setVehicleTypes(null);
        GetParkingLotCapacityResponseDto parkingLotCapacity = parkingLotController.getParkingLotCapacity(getParkingLotCapacityRequestDto);
        assertEquals(parkingLotCapacity.getResponse().getResponseStatus(), ResponseStatus.FAILURE);
    }

    @Test
    public void testGetParkingLotCapacityWithNoVehiclesParked(){
        setupTest(2, Map.of(VehicleType.CAR, 2, VehicleType.TRUCK, 2), 1, 1, "Bangalore");
        GetParkingLotCapacityRequestDto getParkingLotCapacityRequestDto = new GetParkingLotCapacityRequestDto();
        getParkingLotCapacityRequestDto.setParkingLotId(1);
        getParkingLotCapacityRequestDto.setParkingFloorIds(null);
        getParkingLotCapacityRequestDto.setVehicleTypes(null);
        GetParkingLotCapacityResponseDto parkingLotCapacity = parkingLotController.getParkingLotCapacity(getParkingLotCapacityRequestDto);
        assertEquals(parkingLotCapacity.getResponse().getResponseStatus(), ResponseStatus.SUCCESS);
        assertEquals(parkingLotCapacity.getCapacityMap().size(), 2);

        Optional<ParkingLot> parkingLotOptional = parkingLotRepository.getParkingLotById(1);
        ParkingLot parkingLot = parkingLotOptional.get();
        ParkingFloor parkingFloor = parkingLot.getParkingFloors().get(0);
        assertNotNull(parkingLotCapacity.getCapacityMap().get(parkingFloor));
        assertEquals(parkingLotCapacity.getCapacityMap().get(parkingFloor).get(VehicleType.CAR.name()), 2);
        assertEquals(parkingLotCapacity.getCapacityMap().get(parkingFloor).get(VehicleType.TRUCK.name()), 2);

        parkingFloor = parkingLot.getParkingFloors().get(1);
        assertNotNull(parkingLotCapacity.getCapacityMap().get(parkingFloor));
        assertEquals(parkingLotCapacity.getCapacityMap().get(parkingFloor).get(VehicleType.CAR.name()), 2);
        assertEquals(parkingLotCapacity.getCapacityMap().get(parkingFloor).get(VehicleType.TRUCK.name()), 2);
    }

    @Test
    public void testGetParkingLotCapacityWithFewSpotsOccupied(){
        setupTest(2, Map.of(VehicleType.CAR, 2, VehicleType.TRUCK, 2), 1, 1, "Bangalore");
        Optional<ParkingLot> parkingLotByGateId = parkingLotRepository.getParkingLotByGateId(1);
        ParkingLot parkingLot = parkingLotByGateId.get();
        // Set car on floor 1, spot 1
        ParkingFloor parkingFloor = parkingLot.getParkingFloors().get(0);
        parkingFloor.getSpots().stream().filter(spot -> spot.getSupportedVehicleType().equals(VehicleType.CAR)).findFirst().get().setStatus(ParkingSpotStatus.OCCUPIED);
        // Set truck on floor 2, spot 1
        parkingFloor = parkingLot.getParkingFloors().get(1);
        parkingFloor.getSpots().stream().filter(spot -> spot.getSupportedVehicleType().equals(VehicleType.TRUCK)).findFirst().get().setStatus(ParkingSpotStatus.OCCUPIED);


        GetParkingLotCapacityRequestDto getParkingLotCapacityRequestDto = new GetParkingLotCapacityRequestDto();
        getParkingLotCapacityRequestDto.setParkingLotId(1);
        getParkingLotCapacityRequestDto.setParkingFloorIds(null);
        getParkingLotCapacityRequestDto.setVehicleTypes(null);
        GetParkingLotCapacityResponseDto parkingLotCapacity = parkingLotController.getParkingLotCapacity(getParkingLotCapacityRequestDto);
        assertEquals(parkingLotCapacity.getResponse().getResponseStatus(), ResponseStatus.SUCCESS);
        assertEquals(parkingLotCapacity.getCapacityMap().size(), 2);
        assertEquals(parkingLotCapacity.getCapacityMap().get(parkingLot.getParkingFloors().get(0)).get(VehicleType.CAR.name()), 1);
        assertEquals(parkingLotCapacity.getCapacityMap().get(parkingLot.getParkingFloors().get(0)).get(VehicleType.TRUCK.name()), 2);

        assertEquals(parkingLotCapacity.getCapacityMap().get(parkingLot.getParkingFloors().get(1)).get(VehicleType.CAR.name()), 2);
        assertEquals(parkingLotCapacity.getCapacityMap().get(parkingLot.getParkingFloors().get(1)).get(VehicleType.TRUCK.name()), 1);
    }

    @Test
    public void testGetParkingLotCapacityWithFewSpotsOccupiedAndJust2ndFloor(){
        setupTest(2, Map.of(VehicleType.CAR, 2, VehicleType.TRUCK, 2), 1, 1, "Bangalore");
        Optional<ParkingLot> parkingLotByGateId = parkingLotRepository.getParkingLotByGateId(1);
        ParkingLot parkingLot = parkingLotByGateId.get();
        // Set car on floor 1, spot 1
        ParkingFloor parkingFloor = parkingLot.getParkingFloors().get(0);
        parkingFloor.getSpots().stream().filter(spot -> spot.getSupportedVehicleType().equals(VehicleType.CAR)).findFirst().get().setStatus(ParkingSpotStatus.OCCUPIED);
        // Set truck on floor 2, spot 1
        parkingFloor = parkingLot.getParkingFloors().get(1);
        parkingFloor.getSpots().stream().filter(spot -> spot.getSupportedVehicleType().equals(VehicleType.TRUCK)).findFirst().get().setStatus(ParkingSpotStatus.OCCUPIED);


        GetParkingLotCapacityRequestDto getParkingLotCapacityRequestDto = new GetParkingLotCapacityRequestDto();
        getParkingLotCapacityRequestDto.setParkingLotId(1);
        getParkingLotCapacityRequestDto.setParkingFloorIds(Arrays.asList(2L));
        getParkingLotCapacityRequestDto.setVehicleTypes(null);
        GetParkingLotCapacityResponseDto parkingLotCapacity = parkingLotController.getParkingLotCapacity(getParkingLotCapacityRequestDto);
        assertEquals(parkingLotCapacity.getResponse().getResponseStatus(), ResponseStatus.SUCCESS);
        assertEquals(parkingLotCapacity.getCapacityMap().size(), 1);

        assertNotNull(parkingLotCapacity.getCapacityMap().get(parkingLot.getParkingFloors().get(1)));
        assertEquals(parkingLotCapacity.getCapacityMap().get(parkingLot.getParkingFloors().get(1)).get(VehicleType.CAR.name()), 2);
        assertEquals(parkingLotCapacity.getCapacityMap().get(parkingLot.getParkingFloors().get(1)).get(VehicleType.TRUCK.name()), 1);
    }

    @Test
    public void testGetParkingLotCapacityWithFewSpotsOccupiedAndVehicleTypeCar(){
        setupTest(2, Map.of(VehicleType.CAR, 2, VehicleType.TRUCK, 2), 1, 1, "Bangalore");
        Optional<ParkingLot> parkingLotByGateId = parkingLotRepository.getParkingLotByGateId(1);
        ParkingLot parkingLot = parkingLotByGateId.get();
        // Set car on floor 1, spot 1
        ParkingFloor parkingFloor = parkingLot.getParkingFloors().get(0);
        parkingFloor.getSpots().stream().filter(spot -> spot.getSupportedVehicleType().equals(VehicleType.CAR)).findFirst().get().setStatus(ParkingSpotStatus.OCCUPIED);
        // Set truck on floor 2, spot 1
        parkingFloor = parkingLot.getParkingFloors().get(1);
        parkingFloor.getSpots().stream().filter(spot -> spot.getSupportedVehicleType().equals(VehicleType.TRUCK)).findFirst().get().setStatus(ParkingSpotStatus.OCCUPIED);


        GetParkingLotCapacityRequestDto getParkingLotCapacityRequestDto = new GetParkingLotCapacityRequestDto();
        getParkingLotCapacityRequestDto.setParkingLotId(1);
        getParkingLotCapacityRequestDto.setParkingFloorIds(null);
        getParkingLotCapacityRequestDto.setVehicleTypes(Arrays.asList(VehicleType.CAR.name()));
        GetParkingLotCapacityResponseDto parkingLotCapacity = parkingLotController.getParkingLotCapacity(getParkingLotCapacityRequestDto);
        assertEquals(parkingLotCapacity.getResponse().getResponseStatus(), ResponseStatus.SUCCESS);
        assertEquals(parkingLotCapacity.getCapacityMap().size(), 2);

        assertNotNull(parkingLotCapacity.getCapacityMap().get(parkingLot.getParkingFloors().get(0)));
        assertEquals(parkingLotCapacity.getCapacityMap().get(parkingLot.getParkingFloors().get(0)).get(VehicleType.CAR.name()), 1);
        assertNull(parkingLotCapacity.getCapacityMap().get(parkingLot.getParkingFloors().get(0)).get(VehicleType.TRUCK.name()));



        assertNotNull(parkingLotCapacity.getCapacityMap().get(parkingLot.getParkingFloors().get(1)));
        assertEquals(parkingLotCapacity.getCapacityMap().get(parkingLot.getParkingFloors().get(1)).get(VehicleType.CAR.name()), 2);
        assertNull(parkingLotCapacity.getCapacityMap().get(parkingLot.getParkingFloors().get(1)).get(VehicleType.TRUCK.name()));
    }


    @Test
    public void testGetParkingLotCapacityWithFewSpotsOccupiedAndVehicleTypeCarAnd2ndFloor(){
        setupTest(2, Map.of(VehicleType.CAR, 2, VehicleType.TRUCK, 2), 1, 1, "Bangalore");
        Optional<ParkingLot> parkingLotByGateId = parkingLotRepository.getParkingLotByGateId(1);
        ParkingLot parkingLot = parkingLotByGateId.get();
        // Set car on floor 1, spot 1
        ParkingFloor parkingFloor = parkingLot.getParkingFloors().get(0);
        parkingFloor.getSpots().stream().filter(spot -> spot.getSupportedVehicleType().equals(VehicleType.CAR)).findFirst().get().setStatus(ParkingSpotStatus.OCCUPIED);
        // Set truck on floor 2, spot 1
        parkingFloor = parkingLot.getParkingFloors().get(1);
        parkingFloor.getSpots().stream().filter(spot -> spot.getSupportedVehicleType().equals(VehicleType.TRUCK)).findFirst().get().setStatus(ParkingSpotStatus.OCCUPIED);


        GetParkingLotCapacityRequestDto getParkingLotCapacityRequestDto = new GetParkingLotCapacityRequestDto();
        getParkingLotCapacityRequestDto.setParkingLotId(1);
        getParkingLotCapacityRequestDto.setParkingFloorIds(List.of(2L));
        getParkingLotCapacityRequestDto.setVehicleTypes(List.of(VehicleType.CAR.name()));
        GetParkingLotCapacityResponseDto parkingLotCapacity = parkingLotController.getParkingLotCapacity(getParkingLotCapacityRequestDto);
        assertEquals(parkingLotCapacity.getResponse().getResponseStatus(), ResponseStatus.SUCCESS);
        assertEquals(parkingLotCapacity.getCapacityMap().size(), 1);

        assertNotNull(parkingLotCapacity.getCapacityMap().get(parkingLot.getParkingFloors().get(1)));
        assertEquals(parkingLotCapacity.getCapacityMap().get(parkingLot.getParkingFloors().get(1)).get(VehicleType.CAR.name()), 2);
        assertNull(parkingLotCapacity.getCapacityMap().get(parkingLot.getParkingFloors().get(1)).get(VehicleType.TRUCK.name()));
    }



    public void setupTest(int numOfFloors, Map<VehicleType, Integer> numOfSpotsPerVehicleTypePerFloor, int numOfEntryGates, int numOfExitGates, String address) {
        ParkingLot parkingLot = TestUtils.setupParkingLot(numOfFloors, numOfSpotsPerVehicleTypePerFloor, numOfEntryGates, numOfExitGates, address);
        Map<Long, ParkingLot> parkingLotMap = Map.of(parkingLot.getId(), parkingLot);
        parkingLotRepository = new InMemoryParkingLotRepositoryImpl(parkingLotMap);
        parkingLotService = new ParkingLotServiceImpl(parkingLotRepository);
        parkingLotController = new ParkingLotController(parkingLotService);
    }
}
