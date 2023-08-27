package com.scaler.parking_lot.services;

import com.scaler.parking_lot.exceptions.InvalidParkingLotException;
import com.scaler.parking_lot.exceptions.ParkingSpotNotAvailableException;
import com.scaler.parking_lot.models.*;
import com.scaler.parking_lot.respositories.*;
import com.scaler.parking_lot.exceptions.InvalidGateException;
import com.scaler.parking_lot.strategies.SpotAssignmentStrategy;

import java.util.Date;
import java.util.Optional;

public class TicketServiceImpl implements TicketService{
    private GateRepository gateRepository;

    private VehicleService vehicleService;

    private SpotAssignmentStrategy spotAssignmentStrategy;

    private ParkingLotRepository parkingLotRepository;

    private TicketRepository ticketRepository;

    public TicketServiceImpl(GateRepository gateRepository, VehicleService vehicleService, SpotAssignmentStrategy spotAssignmentStrategy, ParkingLotRepository parkingLotRepository, TicketRepository ticketRepository) {
        this.gateRepository = gateRepository;
        this.vehicleService = vehicleService;
        this.spotAssignmentStrategy = spotAssignmentStrategy;
        this.parkingLotRepository = parkingLotRepository;
        this.ticketRepository = ticketRepository;
    }

    @Override
    public Ticket generateTicket(int gateId, String registrationNumber, String vehicleType) throws InvalidGateException, InvalidParkingLotException, ParkingSpotNotAvailableException {
        return null;
    }
}
