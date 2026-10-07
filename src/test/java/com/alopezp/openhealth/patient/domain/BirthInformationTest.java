package com.alopezp.openhealth.patient.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BirthInformationTest {

    @Test
    void shouldCreateExactBirthDate() {
        BirthInformation birth =
                BirthInformation.exact(1998, 4, 23);

        assertEquals(1998, birth.getYear());
        assertEquals(4, birth.getMonth());
        assertEquals(23, birth.getDay());

        assertEquals(BirthDatePrecision.FULL_DATE, birth.getPrecision());

        assertEquals(BirthInformationStatus.KNOWN, birth.getStatus());

        assertFalse(birth.isEstimated());
    }

    @Test
    void shouldCreateYearMonthBirthDate() {
        BirthInformation birth = BirthInformation.yearMonth(1998, 4);

        assertEquals(1998, birth.getYear());
        assertEquals(4, birth.getMonth());
        assertNull(birth.getDay());

        assertEquals(BirthDatePrecision.YEAR_MONTH, birth.getPrecision());

        assertEquals(BirthInformationStatus.KNOWN, birth.getStatus());

        assertFalse(birth.isEstimated());
    }

    @Test
    void shouldCreateYearBirthDate() {
        BirthInformation birth = BirthInformation.year(1998);

        assertEquals(1998, birth.getYear());
        assertNull(birth.getMonth());
        assertNull(birth.getDay());

        assertEquals(BirthDatePrecision.YEAR, birth.getPrecision());

        assertFalse(birth.isEstimated());
    }

    @Test
    void shouldCreateEstimatedYear() {
        BirthInformation birth = BirthInformation.estimatedYear(1980);

        assertEquals(1980, birth.getYear());
        assertNull(birth.getMonth());
        assertNull(birth.getDay());

        assertEquals(BirthDatePrecision.YEAR, birth.getPrecision());

        assertEquals(BirthInformationStatus.KNOWN, birth.getStatus());

        assertTrue(birth.isEstimated());
    }

    @Test
    void shouldCreateUnknownBirthInformation() {
        BirthInformation birth = BirthInformation.unknown();

        assertNull(birth.getYear());
        assertNull(birth.getMonth());
        assertNull(birth.getDay());
        assertNull(birth.getPrecision());

        assertEquals(BirthInformationStatus.UNKNOWN, birth.getStatus());

        assertFalse(birth.isEstimated());
    }

    @Test
    void shouldAcceptLeapYearDate() {
        BirthInformation birth = BirthInformation.exact(2024, 2, 29);

        assertEquals(29, birth.getDay());
    }

    @Test
    void shouldRejectInvalidLeapYearDate() {
        assertThrows(IllegalArgumentException.class,
                () -> BirthInformation.exact(2025, 2, 29)
        );
    }

    @Test
    void shouldRejectInvalidDayForMonth() {
        assertThrows(IllegalArgumentException.class,
                () -> BirthInformation.exact(2025, 4, 31)
        );
    }

    @Test
    void shouldRejectMonthZero() {
        assertThrows(IllegalArgumentException.class,
                () -> BirthInformation.yearMonth(2025, 0)
        );
    }

    @Test
    void shouldRejectMonthGreaterThanTwelve() {
        assertThrows(IllegalArgumentException.class,
                () -> BirthInformation.yearMonth(2025, 13));
    }

    @Test
    void shouldRejectInvalidExactDateMonth() {
        assertThrows(IllegalArgumentException.class,
                () -> BirthInformation.exact(2025, 13, 1)
        );
    }
}