package edu.wpi.cs3733.d19.teamO.entity.database;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

import com.google.inject.Inject;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import edu.wpi.cs3733.d19.teamO.entity.PrescriptionRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(DatabaseExtension.class)
class PrescriptionRequestDaoDbTest {


    private static final PrescriptionRequest PR1 =
            new PrescriptionRequest(LocalDateTime.now(), "Jeff", "AlsoJeff",
                    LocalDate.now(), "Vivance", "50mg",
                    "Take orally.");

    private static final PrescriptionRequest PR2 =
            new PrescriptionRequest(LocalDateTime.now(), "Dan Dimer", "J'efe",
                    LocalDate.now(), "Xanax", "3kg",
                    "Twice a day.");

    private PrescriptionRequestDaoDb prescriptionRequestDaoDb;

    @Inject
    private DatabaseConnectionFactory dcf;

    @BeforeEach
    void setup() throws SQLException {
        prescriptionRequestDaoDb = new PrescriptionRequestDaoDb(dcf);
    }

    @Test
    void getTest() {
        prescriptionRequestDaoDb.insert(PR1);

        assertTrue(prescriptionRequestDaoDb.get(PR1.getId()).isPresent());
    }

    @Test
    void getDifferentObjectTest() {

        prescriptionRequestDaoDb.insert(PR1);

        assertNotSame(PR1, prescriptionRequestDaoDb.get(PR1.getId())
                .orElseThrow(IllegalStateException::new));
    }

    @Test
    void checkEqual() {

        prescriptionRequestDaoDb.insert(PR1);
        assertEquals(PR1.toString(), prescriptionRequestDaoDb.get(PR1.getId()).get().toString());
    }

    @Test
    void getNotExistingTest() {
        assertFalse(prescriptionRequestDaoDb.get(254).isPresent());
    }

    @Test
    void insertTest() {

        assertTrue(prescriptionRequestDaoDb.insert(PR2));
    }

    @Test
    void deleteTest() {
        prescriptionRequestDaoDb.insert(PR1);
        assertTrue(prescriptionRequestDaoDb.delete(PR1));
    }

    @Test
    void deleteNotExistingTest() {
        assertFalse(prescriptionRequestDaoDb.delete(PR2));
    }

    @Test
    void updateTest() {
        prescriptionRequestDaoDb.insert(PR1);

        PrescriptionRequest update =
                new PrescriptionRequest(PR1.getId(), PR1.getTimeRequested(), LocalDateTime.now(),
                        PR1.getWhoRequested(), PR1.getPatientName(), PR1.getPatientDOB(),
                        PR1.getMedicationName(), "300mg", PR1.getDescription());

        assertTrue(prescriptionRequestDaoDb.update(update));
        assertNotEquals(PR1, prescriptionRequestDaoDb.get(PR1.getId()));
    }

    @Test
    void updateNotExistingTest() {
        prescriptionRequestDaoDb.insert(PR1);

        PrescriptionRequest update = PR2;

        assertFalse(prescriptionRequestDaoDb.update(update));
    }

    @Test
    void getAllTest() {
        prescriptionRequestDaoDb.insert(PR1);
        prescriptionRequestDaoDb.insert(PR2);

        assertEquals(2, prescriptionRequestDaoDb.getAll().size());
    }

    @Test
    void getAllEmptyTest() {
        assertTrue(prescriptionRequestDaoDb.getAll().isEmpty());
    }

    @Test
    void autoIncrementTest() {
        prescriptionRequestDaoDb.insert(PR1);

        for (int i = 0; i < 10; i++) {
            prescriptionRequestDaoDb.insert(PR1);
        }

        Set<PrescriptionRequest> patientInfoRequestSet = prescriptionRequestDaoDb.getAll();
        for (PrescriptionRequest sr : patientInfoRequestSet) {
            // make sure the id is in the correct range
            assertTrue(sr.getId() < 11 || sr.getId() > 1);
        }
    }
}
