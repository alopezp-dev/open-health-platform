package com.alopezp.openhealth.patient.domain;

import java.util.UUID;

public record PatientId(UUID value) {

    public PatientId {
        if(value == null) throw new IllegalArgumentException("PatientId can not be null");
    }

    public static PatientId generate() {
        return new PatientId(UUID.randomUUID());
    }
}