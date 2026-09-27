package com.alopezp.openhealth.patient.domain;

public class Identifier {

    private final String system;
    private final String value;

    public Identifier(String system, String value) {
        if (system == null || system.isBlank()) {
            throw new IllegalArgumentException(
                    "Identifier system cannot be empty"
            );
        }

        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(
                    "Identifier value cannot be empty"
            );
        }

        this.system = system;
        this.value = value;
    }

    public String getSystem() {
        return system;
    }

    public String getValue() {
        return value;
    }
}