package com.alopezp.openhealth.patient.domain;

import java.util.UUID;

public class PatientId {

    private final UUID value;

    public PatientId(UUID value) {
        if (value == null) {
            throw new IllegalArgumentException("Patient ID cannot be null");
        }

        this.value = value;
    }

    public static PatientId generate() {
        return new PatientId(UUID.randomUUID());
    }

    public UUID getValue() {
        return value;
    }
}