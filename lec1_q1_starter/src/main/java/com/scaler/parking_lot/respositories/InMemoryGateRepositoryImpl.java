package com.scaler.parking_lot.respositories;

import com.scaler.parking_lot.models.Gate;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InMemoryGateRepositoryImpl implements GateRepository {

    private Map<Long, Gate> gateMap;

    public InMemoryGateRepositoryImpl(Map<Long, Gate> gateMap) {
        this.gateMap = gateMap;
    }

    public InMemoryGateRepositoryImpl() {
        this.gateMap = new HashMap<>();
    }

    public Optional<Gate> findById(long gateId) {
        return Optional.empty();
    }
}
