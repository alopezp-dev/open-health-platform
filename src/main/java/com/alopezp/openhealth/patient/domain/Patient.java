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
            BirthInformation birthInformation)
    {
        if (id == null) {
            throw new IllegalArgumentException("Patient ID cannot be null");
        }

        if (lifecycle == null) {
            throw new IllegalArgumentException("Patient lifecycle cannot be null");
        }

        if (identityState == null) {
            throw new IllegalArgumentException("Patient identity state cannot be null");
        }

        if (identifiers == null) {
            throw new IllegalArgumentException("Identifiers cannot be null");
        }

        if (names == null) {
            throw new IllegalArgumentException("Names cannot be null");
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
            BirthInformation birthInformation)
    {
        return new Patient(
                PatientId.generate(),
                PatientRecordLifecycle.ACTIVE,
                identityState,
                identifiers,
                names,
                birthInformation
        );
    }

    public PatientId getId() {
        return id;
    }

    public PatientRecordLifecycle getLifecycle() {
        return lifecycle;
    }

    public PatientIdentityState getIdentityState() {
        return identityState;
    }

    public Set<Identifier> getIdentifiers() {
        return Set.copyOf(identifiers);
    }

    public List<HumanName> getNames() {
        return List.copyOf(names);
    }

    public BirthInformation getBirthInformation() {
        return birthInformation;
    }

    public void addIdentifier(Identifier identifier) {
        if (identifier == null) {
            throw new IllegalArgumentException("Identifier cannot be null");
        }

        identifiers.add(identifier);
    }

    public void addName(HumanName name) {
        if (name == null) {
            throw new IllegalArgumentException("Human name cannot be null");
        }

        names.add(name);
    }

    public void updateBirthInformation(BirthInformation birthInformation) {
        this.birthInformation = birthInformation;
    }

    public void declareIdentity() {
        if (identityState != PatientIdentityState.PROVISIONAL) {
            throw new IllegalStateException("Only a provisional identity can be declared");
        }

        identityState = PatientIdentityState.DECLARED;
    }

    public void verifyIdentity() {
        if (identityState == PatientIdentityState.VERIFIED) return;

        if (identityState != PatientIdentityState.PROVISIONAL
                && identityState != PatientIdentityState.DECLARED) {
            throw new IllegalStateException("Patient identity cannot be verified from the current state");
        }

        identityState = PatientIdentityState.VERIFIED;
    }

    public void activate() {
        if (lifecycle == PatientRecordLifecycle.ACTIVE) return;

        if (lifecycle != PatientRecordLifecycle.INACTIVE) {
            throw new IllegalStateException("Only an inactive patient record can be activated");
        }

        lifecycle = PatientRecordLifecycle.ACTIVE;
    }

    public void deactivate() {
        if (lifecycle == PatientRecordLifecycle.INACTIVE) return;

        if (lifecycle != PatientRecordLifecycle.ACTIVE) {
            throw new IllegalStateException("Only an active patient record can be deactivated");
        }

        lifecycle = PatientRecordLifecycle.INACTIVE;
    }

    public void markEnteredInError() {
        if (lifecycle == PatientRecordLifecycle.ENTERED_IN_ERROR) return;

        if (lifecycle != PatientRecordLifecycle.ACTIVE
                && lifecycle != PatientRecordLifecycle.INACTIVE) {
            throw new IllegalStateException("Patient record cannot be marked as entered in error");
        }

        lifecycle = PatientRecordLifecycle.ENTERED_IN_ERROR;
    }
}