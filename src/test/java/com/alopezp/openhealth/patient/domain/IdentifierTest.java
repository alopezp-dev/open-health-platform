package com.alopezp.openhealth.patient.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IdentifierTest {

    @Test
    void shouldCreateIdentifier() {
        Identifier identifier = new Identifier("urn:hospital-a:patient", "12345");

        assertEquals("urn:hospital-a:patient", identifier.system());

        assertEquals("12345", identifier.value());
    }

    @Test
    void shouldRejectNullSystem() {
        assertThrows(IllegalArgumentException.class,
                () -> new Identifier(null, "12345")
        );
    }

    @Test
    void shouldRejectBlankSystem() {
        assertThrows(IllegalArgumentException.class,
                () -> new Identifier("   ", "12345")
        );
    }

    @Test
    void shouldRejectNullValue() {
        assertThrows(IllegalArgumentException.class,
                () -> new Identifier("urn:hospital-a:patient", null)
        );
    }

    @Test
    void shouldRejectBlankValue() {
        assertThrows(IllegalArgumentException.class,
                () -> new Identifier("urn:hospital-a:patient", "   ")
        );
    }

    @Test
    void shouldConsiderSameIdentifiersEqual() {
        Identifier first = new Identifier("urn:hospital-a:patient", "12345"
        );

        Identifier second = new Identifier("urn:hospital-a:patient", "12345"
        );

        assertEquals(first, second);
        assertEquals(first.hashCode(), second.hashCode());
    }

    @Test
    void shouldConsiderDifferentSystemsDifferent() {
        Identifier first = new Identifier("urn:hospital-a:patient", "12345");

        Identifier second = new Identifier("urn:hospital-b:patient", "12345");

        assertNotEquals(first, second);
    }

    @Test
    void shouldConsiderDifferentValuesDifferent() {
        Identifier first = new Identifier("urn:hospital-a:patient", "12345");

        Identifier second = new Identifier("urn:hospital-a:patient", "67890");

        assertNotEquals(first, second);
    }
}