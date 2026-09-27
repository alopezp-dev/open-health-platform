package com.alopezp.openhealth.patient.domain;

import java.util.List;

public class HumanName {

    private final String text;
    private final String family;
    private final List<String> given;

    public HumanName(
            String text,
            String family,
            List<String> given
    ) {
        if (text == null || text.isBlank()) {
            throw new IllegalArgumentException(
                    "Human name text cannot be empty"
            );
        }

        if (given == null) {
            throw new IllegalArgumentException(
                    "Given names cannot be null"
            );
        }

        this.text = text;
        this.family = family;
        this.given = List.copyOf(given);
    }

    public String getText() {
        return text;
    }

    public String getFamily() {
        return family;
    }

    public List<String> getGiven() {
        return given;
    }
}