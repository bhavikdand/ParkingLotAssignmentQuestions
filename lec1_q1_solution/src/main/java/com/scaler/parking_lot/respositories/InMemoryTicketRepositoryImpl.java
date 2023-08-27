package com.scaler.parking_lot.respositories;

import com.scaler.parking_lot.models.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class InMemoryTicketRepositoryImpl implements TicketRepository{
    private Map<Long, Ticket> ticketMap;
    private static int id = 0;

    public InMemoryTicketRepositoryImpl(Map<Long, Ticket> ticketMap) {
        this.ticketMap = ticketMap;
    }

    public InMemoryTicketRepositoryImpl() {
        this.ticketMap = new HashMap<>();
    }

    public Ticket save(Vehicle vehicle, Date entryTime, ParkingSpot parkingSpot, Gate gate, ParkingAttendant parkingAttendant) {
        Ticket ticket = new Ticket(++id, vehicle, entryTime, parkingSpot, gate, parkingAttendant);
        this.ticketMap.put(ticket.getId(), ticket);
        return ticket;
    }

}
