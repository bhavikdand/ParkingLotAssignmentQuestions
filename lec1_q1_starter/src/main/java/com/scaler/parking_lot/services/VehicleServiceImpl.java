package com.scaler.parking_lot.services;

import com.scaler.parking_lot.models.Vehicle;
import com.scaler.parking_lot.models.VehicleType;
import com.scaler.parking_lot.respositories.InMemoryVehicleRepositoryImpl;
import com.scaler.parking_lot.respositories.VehicleRepository;

import java.util.Optional;

public class VehicleServiceImpl implements VehicleService{

    private VehicleRepository vehicleRepository;

    public VehicleServiceImpl(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    public Optional<Vehicle> getVehicleByRegistrationNumber(String registrationNumber) {
        return Optional.empty();
    }

    public Vehicle save(String registrationNumber, VehicleType vehicleType) {
        return null;
    }

    public Vehicle getOrInsertIfNotExists(String registrationNumber, VehicleType vehicleType) {
        return null;
    }


}
