package edu.wpi.cs3733.d19.teamO.entity.database;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Set;

import com.google.inject.Inject;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import edu.wpi.cs3733.d19.teamO.entity.InternalTransportationRequest;
import edu.wpi.cs3733.d19.teamO.entity.Node;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(DatabaseExtension.class)
class InternalTransportationRequestDaoDbTest {
    private static final int id1 = 123;
    private static final int id2 = 1234;

    private static final Node testNode1 = new Node(Integer.toString(id1), 0, 0,
            "FL2", "Central", Node.NodeType.CONF, "LONGNAME",
            "SHORTNAME");

    private static final Node testNode2 = new Node(Integer.toString(id2), 1, 1,
            "FL3", "Central", Node.NodeType.DEPT, "LONGNAME",
            "SHORTNAME");

    private static final InternalTransportationRequest testITRequest1 =
            new InternalTransportationRequest(1, LocalDateTime.now(), LocalDateTime.now(), testNode1,
                    "Bob", InternalTransportationRequest.InternalTransportationRequestType.DELIVERY,
                    "This is a description", "Dev");

    private static final InternalTransportationRequest testITRequest2 =
            new InternalTransportationRequest(2, LocalDateTime.now(), LocalDateTime.now(), testNode2,
                    "Bill", InternalTransportationRequest.InternalTransportationRequestType.STRETCHER,
                    "This is a description", "Ken");
    private InternalTransportationRequestDaoDb itrequestdao;
    private NodeDao nodeDao;

    @Inject
    private DatabaseConnectionFactory dcf;

    @BeforeEach
    void setup() throws SQLException {
        nodeDao = new NodeDaoDb(dcf);
        InternalTransportationRequestDaoDb internalTransportationRequestDaoDb =
                new InternalTransportationRequestDaoDb(dcf);

        nodeDao.insert(testNode1);
        internalTransportationRequestDaoDb.insert(testITRequest1);


        itrequestdao = new InternalTransportationRequestDaoDb(dcf);
    }

    @Test
    void getTest() {
        itrequestdao.insert(testITRequest1);

        assertTrue(itrequestdao.get(testITRequest1.getId()).isPresent());
    }

    @Test
    void getDifferentObjectTest() {
        itrequestdao.insert(testITRequest1);

        assertNotSame(testITRequest1,
                itrequestdao.get(testITRequest1.getId()).orElseThrow(IllegalStateException::new));
    }

    @Test
    void checkEqual() {
        itrequestdao.insert(testITRequest1);

        assertEquals(testITRequest1, itrequestdao.get(testITRequest1.getId()).get());
    }

    @Test
    void getNotExistingTest() {
        assertFalse(itrequestdao.get(987).isPresent());
    }

    @Test
    void insertTest() {
        nodeDao.insert(testNode2);

        assertTrue(itrequestdao.insert(testITRequest2));
    }

    @Test
    void deleteTest() {
        itrequestdao.insert(testITRequest1);
        assertTrue(itrequestdao.delete(testITRequest1));
    }

    @Test
    void deleteNotExistingTest() {
        assertFalse(itrequestdao.delete(new InternalTransportationRequest(987, null,
                null, testNode1, null, null, null, null)));
    }

    @Test
    void updateTest() {
        itrequestdao.insert(testITRequest1);

        InternalTransportationRequest update = new InternalTransportationRequest(testITRequest1.getId(),
                LocalDateTime.now(), LocalDateTime.now(),
                testNode1, "Fred",
                InternalTransportationRequest.InternalTransportationRequestType.OTHERS,
                "A different description", "Austin");

        assertTrue(itrequestdao.update(update));
        assertNotEquals(testITRequest1, itrequestdao.get(testITRequest1.getId()));
    }

    @Test
    void updateNotExistingTest() {
        Node newNode = new Node("9876", 0, 0, "2", "Fuller",
                Node.NodeType.DEPT, "longname", "shortname");
        assertFalse(itrequestdao.update(new InternalTransportationRequest(987, LocalDateTime.now(),
                LocalDateTime.now(), newNode, "Jane",
                InternalTransportationRequest.InternalTransportationRequestType.DELIVERY,
                "This request doesnt exist", "Jill")));
    }

    @Test
    void getAllTest() {
        nodeDao.insert(testNode2);
        itrequestdao.insert(testITRequest2);

        assertEquals(2, itrequestdao.getAll().size());
    }

    @Test
    void getAllEmptyTest() {
        itrequestdao.delete(testITRequest1);

        assertTrue(itrequestdao.getAll().isEmpty());
    }

    @Test
    void autoIncrementTest() {
        for (int i = 0; i < 10; i++) {
            itrequestdao.insert(testITRequest1);
        }

        Set<InternalTransportationRequest> internalTransportationRequestSet = itrequestdao.getAll();
        for (InternalTransportationRequest sr : internalTransportationRequestSet) {
            // make sure the id is in the correct range
            assertTrue(sr.getId() < 11 || sr.getId() > 1);
        }
    }
}
