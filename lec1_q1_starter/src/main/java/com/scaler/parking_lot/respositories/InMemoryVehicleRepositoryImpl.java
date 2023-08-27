package com.scaler.parking_lot.respositories;

import com.scaler.parking_lot.models.Vehicle;
import com.scaler.parking_lot.models.VehicleType;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InMemoryVehicleRepositoryImpl implements VehicleRepository{

    private Map<Long, Vehicle> vehicleMap;

    public InMemoryVehicleRepositoryImpl(Map<Long, Vehicle> vehicleMap) {
        this.vehicleMap = vehicleMap;
    }

    public InMemoryVehicleRepositoryImpl() {
        this.vehicleMap = new HashMap<>();
    }

    public Optional<Vehicle> getVehicleByRegistrationNumber(String registrationNumber) {
        return Optional.empty();
    }

    public Vehicle save(String registrationNumber, VehicleType vehicleType) {
        return null;
    }
}
