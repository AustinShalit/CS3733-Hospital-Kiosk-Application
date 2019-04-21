package edu.wpi.cs3733.d19.teamO.entity.database;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Set;

import com.google.inject.Inject;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import edu.wpi.cs3733.d19.teamO.entity.Node;
import edu.wpi.cs3733.d19.teamO.entity.SanitationRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(DatabaseExtension.class)
class SanitationRequestDaoDbTest {
    private static final int id1 = 123;
    private static final int id2 = 1234;

    private static final Node testNode1 = new Node(Integer.toString(id1), 0, 0,
            "FL2", "Central", Node.NodeType.CONF, "LONGNAME",
            "SHORTNAME");

    private static final Node testNode2 = new Node(Integer.toString(id2), 1, 1,
            "FL3", "Central", Node.NodeType.DEPT, "LONGNAME",
            "SHORTNAME");

    private static final SanitationRequest testSanitation1 =
            new SanitationRequest(1, LocalDateTime.now(), LocalDateTime.now(), testNode1,
                    "Bob", SanitationRequest.SanitationRequestType.VOMIT,
                    "This is a description", "Dev");

    private static final SanitationRequest testSanitation2 =
            new SanitationRequest(2, LocalDateTime.now(), LocalDateTime.now(), testNode2,
                    "Bill", SanitationRequest.SanitationRequestType.SPILL,
                    "This is a description", "Ken");
    private SanitationRequestDaoDb sanitationdao;
    private NodeDao nodeDao;

    @Inject
    private DatabaseConnectionFactory dcf;

    @BeforeEach
    void setup() throws SQLException {
        nodeDao = new NodeDaoDb(dcf);
        SanitationRequestDaoDb sanitationRequestDaoDb =
                new SanitationRequestDaoDb(dcf);

        nodeDao.insert(testNode1);
        sanitationRequestDaoDb.insert(testSanitation1);


        sanitationdao = new SanitationRequestDaoDb(dcf);
    }

    @Test
    void getTest() {
        sanitationdao.insert(testSanitation1);

        assertTrue(sanitationdao.get(testSanitation1.getId()).isPresent());
    }

    @Test
    void getDifferentObjectTest() {
        sanitationdao.insert(testSanitation1);

        assertNotSame(testSanitation1,
                sanitationdao.get(testSanitation1.getId()).orElseThrow(IllegalStateException::new));
    }

    @Test
    void checkEqual() {
        sanitationdao.insert(testSanitation1);

        assertEquals(testSanitation1, sanitationdao.get(testSanitation1.getId()).get());
    }

    @Test
    void getNotExistingTest() {
        assertFalse(sanitationdao.get(987).isPresent());
    }

    @Test
    void insertTest() {
        nodeDao.insert(testNode2);

        assertTrue(sanitationdao.insert(testSanitation2));
    }

    @Test
    void deleteTest() {
        sanitationdao.insert(testSanitation1);
        assertTrue(sanitationdao.delete(testSanitation1));
    }

    @Test
    void deleteNotExistingTest() {
        assertFalse(sanitationdao.delete(new SanitationRequest(987, null,
                null, testNode1, null, null, null, null)));
    }

    @Test
    void updateTest() {
        sanitationdao.insert(testSanitation1);

        SanitationRequest update = new SanitationRequest(testSanitation1.getId(),
                LocalDateTime.now(), LocalDateTime.now(),
                testNode1, "Fred",
                SanitationRequest.SanitationRequestType.OTHERS,
                "A different description", "Austin");

        assertTrue(sanitationdao.update(update));
        assertNotEquals(testSanitation1, sanitationdao.get(testSanitation1.getId()));
    }

    @Test
    void updateNotExistingTest() {
        Node newNode = new Node("9876", 0, 0, "2", "Fuller",
                Node.NodeType.DEPT, "longname", "shortname");
        assertFalse(sanitationdao.update(new SanitationRequest(987, LocalDateTime.now(),
                LocalDateTime.now(), newNode, "Jane",
                SanitationRequest.SanitationRequestType.BEDDING,
                "This request doesnt exist", "Jill")));
    }

    @Test
    void getAllTest() {
        nodeDao.insert(testNode2);
        sanitationdao.insert(testSanitation2);

        assertEquals(2, sanitationdao.getAll().size());
    }

    @Test
    void getAllEmptyTest() {
        sanitationdao.delete(testSanitation1);

        assertTrue(sanitationdao.getAll().isEmpty());
    }

    @Test
    void autoIncrementTest() {
        for (int i = 0; i < 10; i++) {
            sanitationdao.insert(testSanitation1);
        }

        Set<SanitationRequest> sanitationRequestSet = sanitationdao.getAll();
        for (SanitationRequest sr : sanitationRequestSet) {
            // make sure the id is in the correct range
            assertTrue(sr.getId() < 11 || sr.getId() > 1);
        }
    }
}
