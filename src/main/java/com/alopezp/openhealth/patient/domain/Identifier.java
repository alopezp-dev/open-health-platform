package com.alopezp.openhealth.patient.domain;

public record Identifier (
    String system,
    String value
) {

    public Identifier {
        if(system == null || system.isBlank()) {
            throw new IllegalArgumentException("Identifier system cannot be empty");
        }

        if(value == null || value.isBlank()) {
            throw new IllegalArgumentException("Identifier value cannot be empty");
        }
    }
}