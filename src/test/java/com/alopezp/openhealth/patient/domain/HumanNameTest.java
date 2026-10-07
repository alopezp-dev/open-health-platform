package com.alopezp.openhealth.patient.domain;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HumanNameTest {

    @Test
    void shouldCreateHumanName() {
        HumanName name = new HumanName("María del Carmen Pérez García", "Pérez García",
                List.of("María", "del Carmen")
        );

        assertEquals("María del Carmen Pérez García", name.text()
        );

        assertEquals("Pérez García", name.family());

        assertEquals(List.of("María", "del Carmen"), name.given());
    }

    @Test
    void shouldAllowNullFamily() {
        HumanName name = new HumanName("Madonna", null, List.of("Madonna"));

        assertNull(name.family());
    }

    @Test
    void shouldAllowEmptyGivenNames() {
        HumanName name = new HumanName("李小龙", null, List.of());

        assertTrue(name.given().isEmpty());
    }

    @Test
    void shouldRejectNullText() {
        assertThrows(IllegalArgumentException.class,
                () -> new HumanName(null, "Pérez", List.of("María"))
        );
    }

    @Test
    void shouldRejectBlankText() {
        assertThrows(IllegalArgumentException.class,
                () -> new HumanName("   ", "Pérez", List.of("María"))
        );
    }

    @Test
    void shouldRejectBlankFamily() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new HumanName("María", "   ", List.of("María"))
        );
    }

    @Test
    void shouldRejectNullGivenNames() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new HumanName("María Pérez", "Pérez", null)
        );
    }

    @Test
    void shouldRejectNullGivenName() {
        List<String> given = new ArrayList<>();
        given.add("María");
        given.add(null);

        assertThrows(IllegalArgumentException.class,
                () -> new HumanName("María Pérez", "Pérez", given)
        );
    }

    @Test
    void shouldRejectBlankGivenName() {
        assertThrows(IllegalArgumentException.class,
                () -> new HumanName("María Pérez", "Pérez", List.of("María", "   "))
        );
    }

    @Test
    void shouldDefensivelyCopyGivenNames() {
        List<String> given = new ArrayList<>();
        given.add("María");

        HumanName name = new HumanName("María Pérez", "Pérez", given);

        given.add("Carmen");

        assertEquals(List.of("María"), name.given());
    }

    @Test
    void shouldReturnImmutableGivenNames() {
        HumanName name = new HumanName("María Pérez", "Pérez", List.of("María"));

        assertThrows(UnsupportedOperationException.class, () -> name.given().add("Carmen"));
    }
}