package com.scaler.parking_lot.dtos;

public class GenerateTicketRequestDto {
    private int gateId;
    private String registrationNumber;
    private String vehicleType;

    public GenerateTicketRequestDto(int gateId, String registrationNumber, String vehicleType) {
        this.gateId = gateId;
        this.registrationNumber = registrationNumber;
        this.vehicleType = vehicleType;
    }

    public int getGateId() {
        return gateId;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public String getVehicleType() {
        return vehicleType;
    }
}
