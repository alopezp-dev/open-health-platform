package com.alopezp.openhealth.patient.domain;

import java.time.LocalDate;
import java.time.Year;
import java.time.YearMonth;

public class BirthInformation {

    private final Integer year;
    private final Integer month;
    private final Integer day;
    private final BirthDatePrecision precision;
    private final boolean estimated;
    private final BirthInformationStatus status;

    private BirthInformation(Integer year,
                             Integer month,
                             Integer day,
                             BirthDatePrecision precision,
                             boolean estimated,
                             BirthInformationStatus status)
    {
        if (status == null) {
            throw new IllegalArgumentException("Birth information status cannot be null");
        }

        if (status == BirthInformationStatus.UNKNOWN) {
            if (year != null || month != null || day != null || precision != null || estimated) {
                throw new IllegalArgumentException("Unknown birth information cannot contain date values");
            }
        }

        if (status == BirthInformationStatus.KNOWN) {
            if (precision == null) {
                throw new IllegalArgumentException("Known birth information requires a precision");
            }

            switch (precision) {
                case YEAR:
                    if (year == null || month != null || day != null) {
                        throw new IllegalArgumentException("YEAR precision requires only year");
                    }

                    if (year < Year.MIN_VALUE || year > Year.MAX_VALUE) {
                        throw new IllegalArgumentException("Invalid year");
                    }

                case YEAR_MONTH:
                    if (year == null || month == null || day != null) {
                        throw new IllegalArgumentException("YEAR_MONTH precision requires year and month only");
                    }

                    if (year < Year.MIN_VALUE || year > Year.MAX_VALUE) {
                        throw new IllegalArgumentException("Invalid year");
                    }

                    if (month < 1 || month > 12) {
                        throw new IllegalArgumentException("Invalid month");
                    }

                case FULL_DATE:
                    if (year == null || month == null || day == null) {
                        throw new IllegalArgumentException("FULL_DATE precision requires year, month and day");
                    }

                    if (year < Year.MIN_VALUE || year > Year.MAX_VALUE) {
                        throw new IllegalArgumentException("Invalid year");
                    }

                    if (month < 1 || month > 12) {
                        throw new IllegalArgumentException("Invalid month");
                    }

                    YearMonth yearMonth = YearMonth.of(year, month);

                    if (!yearMonth.isValidDay(day)) {
                        throw new IllegalArgumentException("Invalid day for year and month");
                    }

            }
        }

        this.year = year;
        this.month = month;
        this.day = day;
        this.precision = precision;
        this.estimated = estimated;
        this.status = status;
    }

    public static BirthInformation exact(
            int year,
            int month,
            int day)
    {
        return new BirthInformation(
                year,
                month,
                day,
                BirthDatePrecision.FULL_DATE,
                false,
                BirthInformationStatus.KNOWN
        );
    }

    public static BirthInformation yearMonth(
            int year,
            int month)
    {
        return new BirthInformation(
                year,
                month,
                null,
                BirthDatePrecision.YEAR_MONTH,
                false,
                BirthInformationStatus.KNOWN
        );
    }

    public static BirthInformation year(
            int year)
    {
        return new BirthInformation(
                year,
                null,
                null,
                BirthDatePrecision.YEAR,
                false,
                BirthInformationStatus.KNOWN
        );
    }

    public static BirthInformation estimatedYear(
            int year
    ) {
        return new BirthInformation(
                year,
                null,
                null,
                BirthDatePrecision.YEAR,
                true,
                BirthInformationStatus.KNOWN
        );
    }

    public static BirthInformation unknown() {
        return new BirthInformation(
                null,
                null,
                null,
                null,
                false,
                BirthInformationStatus.UNKNOWN
        );
    }

    public Integer getYear() {
        return year;
    }

    public Integer getMonth() {
        return month;
    }

    public Integer getDay() {
        return day;
    }

    public BirthDatePrecision getPrecision() {
        return precision;
    }

    public boolean isEstimated() {
        return estimated;
    }

    public BirthInformationStatus getStatus() {
        return status;
    }
}