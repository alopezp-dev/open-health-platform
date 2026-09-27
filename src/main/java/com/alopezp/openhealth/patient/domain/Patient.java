package com.alopezp.openhealth.patient.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Patient {

    private final PatientId id;

    private PatientRecordLifecycle lifecycle;
    private PatientIdentityState identityState;

    private final Set<Identifier> identifiers;
    private final List<HumanName> names;

    private BirthInformation birthInformation;

    public Patient(
            PatientId id,
            PatientRecordLifecycle lifecycle,
            PatientIdentityState identityState,
            Set<Identifier> identifiers,
            List<HumanName> names,
            BirthInformation birthInformation
    ) {
        if (id == null) {
            throw new IllegalArgumentException(
                    "Patient ID cannot be null"
            );
        }

        if (lifecycle == null) {
            throw new IllegalArgumentException(
                    "Patient lifecycle cannot be null"
            );
        }

        if (identityState == null) {
            throw new IllegalArgumentException(
                    "Patient identity state cannot be null"
            );
        }

        if (identifiers == null) {
            throw new IllegalArgumentException(
                    "Identifiers cannot be null"
            );
        }

        if (names == null) {
            throw new IllegalArgumentException(
                    "Names cannot be null"
            );
        }

        this.id = id;
        this.lifecycle = lifecycle;
        this.identityState = identityState;
        this.identifiers = new HashSet<>(identifiers);
        this.names = new ArrayList<>(names);
        this.birthInformation = birthInformation;
    }

    public static Patient create(
            PatientIdentityState identityState,
            Set<Identifier> identifiers,
            List<HumanName> names,
            BirthInformation birthInformation
    ) {
        return new Patient(
                PatientId.generate(),
                PatientRecordLifecycle.ACTIVE,
                identityState,
                identifiers,
                names,
                birthInformation
        );
    }
}