package com.scaler.parking_lot.respositories;

import com.scaler.parking_lot.models.*;

import java.util.Date;

public interface TicketRepository {
    // Do not modify the method signature, feel free to add new methods

    public Ticket save(Vehicle vehicle, Date entryTime, ParkingSpot parkingSpot, Gate gate, ParkingAttendant parkingAttendant);
}
