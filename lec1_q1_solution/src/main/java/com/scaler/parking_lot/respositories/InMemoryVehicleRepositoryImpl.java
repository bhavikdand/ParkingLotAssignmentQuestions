package com.scaler.parking_lot.respositories;

import com.scaler.parking_lot.models.Vehicle;
import com.scaler.parking_lot.models.VehicleType;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InMemoryVehicleRepositoryImpl implements VehicleRepository{

    private Map<Long, Vehicle> vehicleMap;
    private static long id = 0;

    public InMemoryVehicleRepositoryImpl(Map<Long, Vehicle> vehicleMap) {
        this.vehicleMap = vehicleMap;
    }

    public InMemoryVehicleRepositoryImpl() {
        this.vehicleMap = new HashMap<>();
    }

    public Optional<Vehicle> getVehicleByRegistrationNumber(String registrationNumber) {
        return this.vehicleMap.values().stream().filter(vehicle -> vehicle.getRegistrationNumber().equals(registrationNumber)).findFirst();
    }

    public Vehicle save(String registrationNumber, VehicleType vehicleType) {
        Vehicle vehicle = new Vehicle(++id, registrationNumber, vehicleType);
        this.vehicleMap.put(vehicle.getId(), vehicle);
        return vehicle;
    }
}
