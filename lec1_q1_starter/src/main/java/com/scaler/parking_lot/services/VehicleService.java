package com.scaler.parking_lot.services;

import com.scaler.parking_lot.models.Vehicle;
import com.scaler.parking_lot.models.VehicleType;

import java.util.Optional;

public interface VehicleService {
    // Do not modify the method signatures, feel free to add new methods

    public Optional<Vehicle> getVehicleByRegistrationNumber(String registrationNumber);

    public Vehicle save(String registrationNumber, VehicleType vehicleType);

    public Vehicle getOrInsertIfNotExists(String registrationNumber, VehicleType vehicleType);
}
