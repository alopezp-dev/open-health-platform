package com.alopezp.openhealth.patient.domain;

import java.util.List;

public record HumanName(
        String text,
        String family,
        List<String> given
) {

    public HumanName {
        if (text == null || text.isBlank()) {
            throw new IllegalArgumentException("Human name text cannot be empty");
        }

        if (family != null && family.isBlank()) {
            throw new IllegalArgumentException("Family name cannot be blank");
        }

        if (given == null) {
            throw new IllegalArgumentException("Given names cannot be null");
        }

        for (String givenName : given) {
            if (givenName == null || givenName.isBlank()) {
                throw new IllegalArgumentException("Given names cannot contain null or blank values");
            }
        }

        given = List.copyOf(given);
    }
}