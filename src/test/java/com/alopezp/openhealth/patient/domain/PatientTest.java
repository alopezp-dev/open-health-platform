package com.alopezp.openhealth.patient.domain;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class PatientTest {

    @Test
    void shouldCreatePatient() {

        Patient patient = Patient.create(PatientIdentityState.PROVISIONAL, Set.of(), List.of(), null);

        assertNotNull(patient.getId());

        assertEquals(PatientRecordLifecycle.ACTIVE, patient.getLifecycle());
        assertEquals(PatientIdentityState.PROVISIONAL, patient.getIdentityState());

        assertTrue(patient.getIdentifiers().isEmpty());
        assertTrue(patient.getNames().isEmpty());
        assertNull(patient.getBirthInformation());
    }

    @Test
    void shouldRejectNullId() {
        assertThrows(IllegalArgumentException.class,
                () -> new Patient(
                        null,
                        PatientRecordLifecycle.ACTIVE,
                        PatientIdentityState.PROVISIONAL,
                        Set.of(),
                        List.of(),
                        null
                )
        );
    }

    @Test
    void shouldRejectNullLifecycle() {
        assertThrows(IllegalArgumentException.class,
                () -> new Patient(
                        PatientId.generate(),
                        null,
                        PatientIdentityState.PROVISIONAL,
                        Set.of(),
                        List.of(),
                        null
                )
        );
    }

    @Test
    void shouldRejectNullIdentityState() {
        assertThrows(IllegalArgumentException.class,
                () -> new Patient(
                        PatientId.generate(),
                        PatientRecordLifecycle.ACTIVE,
                        null,
                        Set.of(),
                        List.of(),
                        null
                )
        );
    }

    @Test
    void shouldRejectNullIdentifiersCollection() {
        assertThrows(IllegalArgumentException.class,
                () -> new Patient(
                        PatientId.generate(),
                        PatientRecordLifecycle.ACTIVE,
                        PatientIdentityState.PROVISIONAL,
                        null,
                        List.of(),
                        null
                )
        );
    }

    @Test
    void shouldRejectNullNamesCollection() {
        assertThrows(IllegalArgumentException.class,
                () -> new Patient(
                        PatientId.generate(),
                        PatientRecordLifecycle.ACTIVE,
                        PatientIdentityState.PROVISIONAL,
                        Set.of(),
                        null,
                        null
                )
        );
    }

    @Test
    void shouldDefensivelyCopyCollectionsOnConstruction() {
        Set<Identifier> identifiers = new HashSet<>();
        List<HumanName> names = new ArrayList<>();

        Patient patient = Patient.create(PatientIdentityState.PROVISIONAL, identifiers, names, null);

        identifiers.add(
                new Identifier("urn:hospital-a:patient", "12345")
        );

        names.add(
                new HumanName("María López", "López", List.of("María"))
        );

        assertTrue(patient.getIdentifiers().isEmpty());
        assertTrue(patient.getNames().isEmpty());
    }

    @Test
    void shouldNotAllowExternalModificationOfIdentifiers() {
        Patient patient = Patient.create(PatientIdentityState.PROVISIONAL, Set.of(), List.of(), null);

        assertThrows(UnsupportedOperationException.class,
                () -> patient.getIdentifiers().add(
                        new Identifier("urn:hospital-a:patient", "12345")
                )
        );
    }

    @Test
    void shouldAddIdentifier() {
        Patient patient = createBasicPatient();

        Identifier identifier = new Identifier(
                "urn:hospital-a:patient",
                "12345"
        );

        patient.addIdentifier(identifier);

        assertTrue(
                patient.getIdentifiers().contains(identifier)
        );
    }

    @Test
    void shouldRejectNullIdentifier() {
        Patient patient = createBasicPatient();

        assertThrows(IllegalArgumentException.class,
                () -> patient.addIdentifier(null)
        );
    }

    @Test
    void shouldNotDuplicateSameIdentifier() {
        Patient patient = createBasicPatient();

        Identifier first = new Identifier("urn:hospital-a:patient", "12345");

        Identifier second = new Identifier("urn:hospital-a:patient", "12345");

        patient.addIdentifier(first);
        patient.addIdentifier(second);

        assertEquals(1, patient.getIdentifiers().size());
    }

    @Test
    void shouldAddName() {
        Patient patient = createBasicPatient();

        HumanName name = new HumanName("María del Carmen López", "López", List.of("María", "del Carmen"));

        patient.addName(name);

        assertTrue(patient.getNames().contains(name));
    }

    @Test
    void shouldRejectNullName() {
        Patient patient = createBasicPatient();

        assertThrows(IllegalArgumentException.class,
                () -> patient.addName(null)
        );
    }

    @Test
    void shouldUpdateBirthInformation() {
        Patient patient = createBasicPatient();

        BirthInformation birthInformation =
                BirthInformation.exact(1998, 4, 23);

        patient.updateBirthInformation(birthInformation);

        assertSame(birthInformation, patient.getBirthInformation());
    }

    @Test
    void shouldDeclareProvisionalIdentity() {
        Patient patient = createBasicPatient();

        patient.declareIdentity();

        assertEquals(PatientIdentityState.DECLARED, patient.getIdentityState());
    }

    @Test
    void shouldNotDeclareAlreadyDeclaredIdentity() {
        Patient patient = new Patient(
                PatientId.generate(),
                PatientRecordLifecycle.ACTIVE,
                PatientIdentityState.DECLARED,
                Set.of(),
                List.of(),
                null
        );

        assertThrows(IllegalStateException.class, patient::declareIdentity);
    }

    @Test
    void shouldVerifyProvisionalIdentity() {
        Patient patient = createBasicPatient();

        patient.verifyIdentity();

        assertEquals(PatientIdentityState.VERIFIED, patient.getIdentityState());
    }

    @Test
    void shouldVerifyDeclaredIdentity() {
        Patient patient = createBasicPatient();

        patient.declareIdentity();
        patient.verifyIdentity();

        assertEquals(PatientIdentityState.VERIFIED, patient.getIdentityState());
    }

    @Test
    void shouldDeactivateActivePatient() {
        Patient patient = createBasicPatient();

        patient.deactivate();

        assertEquals(PatientRecordLifecycle.INACTIVE, patient.getLifecycle());
    }

    @Test
    void shouldActivateInactivePatient() {
        Patient patient = new Patient(
                PatientId.generate(),
                PatientRecordLifecycle.INACTIVE,
                PatientIdentityState.PROVISIONAL,
                Set.of(),
                List.of(),
                null
        );

        patient.activate();

        assertEquals(PatientRecordLifecycle.ACTIVE, patient.getLifecycle());
    }

    @Test
    void shouldMarkActivePatientAsEnteredInError() {
        Patient patient = createBasicPatient();

        patient.markEnteredInError();

        assertEquals(PatientRecordLifecycle.ENTERED_IN_ERROR, patient.getLifecycle());
    }

    @Test
    void shouldNotActivatePatientEnteredInError() {
        Patient patient = new Patient(
                PatientId.generate(),
                PatientRecordLifecycle.ENTERED_IN_ERROR,
                PatientIdentityState.PROVISIONAL,
                Set.of(),
                List.of(),
                null
        );

        assertThrows(
                IllegalStateException.class,
                patient::activate
        );
    }

    private Patient createBasicPatient() {
        return Patient.create(
                PatientIdentityState.PROVISIONAL,
                Set.of(),
                List.of(),
                null
        );
    }
}