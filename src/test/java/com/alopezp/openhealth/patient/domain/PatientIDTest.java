package com.alopezp.openhealth.patient.domain;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class PatientIdTest {

    @Test
    void shouldCreatePatientId() {
        UUID uuid = UUID.randomUUID();

        PatientId patientId = new PatientId(uuid);

        assertEquals(uuid, patientId.value());
    }

    @Test
    void shouldRejectNullValue() {
        assertThrows(IllegalArgumentException.class,
                () -> new PatientId(null)
        );
    }

    @Test
    void shouldGeneratePatientId() {
        PatientId patientId = PatientId.generate();

        assertNotNull(patientId);
        assertNotNull(patientId.value());
    }

    @Test
    void shouldGenerateDifferentPatientIds() {
        PatientId first = PatientId.generate();
        PatientId second = PatientId.generate();

        assertNotEquals(first, second);
    }

    @Test
    void shouldConsiderSameUuidEqual() {
        UUID uuid = UUID.randomUUID();

        PatientId first = new PatientId(uuid);
        PatientId second = new PatientId(uuid);

        assertEquals(first, second);
        assertEquals(first.hashCode(), second.hashCode());
    }
}