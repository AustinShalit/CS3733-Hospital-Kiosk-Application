package edu.wpi.cs3733.d19.teamO.entity.database;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Set;

import com.google.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;

import edu.wpi.cs3733.d19.teamO.entity.Node;
import edu.wpi.cs3733.d19.teamO.entity.ReligiousServiceRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(DatabaseExtension.class)
class ReligiousServiceRequestDaoDbTest {
	private static final int id1 = 123;
	private static final int id2 = 1234;

	private static final Node testNode1 = new Node(Integer.toString(id1), 0, 0,
			"FL2", "Central", Node.NodeType.CONF, "LONGNAME",
			"SHORTNAME");

	private static final Node testNode2 = new Node(Integer.toString(id2), 1, 1,
			"FL3", "Central", Node.NodeType.DEPT, "LONGNAME",
			"SHORTNAME");

	private static final ReligiousServiceRequest testRSRequest1 =
			new ReligiousServiceRequest(1, LocalDateTime.now(), LocalDateTime.now(), testNode1,
					"Bob", ReligiousServiceRequest.ReligiousServiceRequestType.LAST_RITES,
					"This is a description", "Dev");

	private static final ReligiousServiceRequest testRSRequest2 =
			new ReligiousServiceRequest(2, LocalDateTime.now(), LocalDateTime.now(), testNode2,
					"Bill", ReligiousServiceRequest.ReligiousServiceRequestType.BLESSING,
					"This is a description", "Ken");
	private ReligiousServiceRequestDaoDb rservicedao;
	private NodeDao nodeDao;

	@Inject
	private DatabaseConnectionFactory dcf;

	@BeforeEach
	void setup(TestInfo testInfo) throws SQLException {
		nodeDao = new NodeDaoDb(dcf);
		ReligiousServiceRequestDaoDb religiousServiceRequestDaoDb =
				new ReligiousServiceRequestDaoDb(dcf);

		nodeDao.insert(testNode1);
		religiousServiceRequestDaoDb.insert(testRSRequest1);


		rservicedao = new ReligiousServiceRequestDaoDb(dcf);
	}

	@Test
	void getTest() {
		rservicedao.insert(testRSRequest1);

		assertTrue(rservicedao.get(testRSRequest1.getId()).isPresent());
	}

	@Test
	void getDifferentObjectTest() {
		rservicedao.insert(testRSRequest1);

		assertNotSame(testRSRequest1,
				rservicedao.get(testRSRequest1.getId()).orElseThrow(IllegalStateException::new));
	}

	@Test
	void checkEqual() {
		rservicedao.insert(testRSRequest1);

		assertEquals(testRSRequest1, rservicedao.get(testRSRequest1.getId()).get());
	}

	@Test
	void getNotExistingTest() {
		assertFalse(rservicedao.get(987).isPresent());
	}

	@Test
	void insertTest() {
		nodeDao.insert(testNode2);

		assertTrue(rservicedao.insert(testRSRequest2));
	}

	@Test
	void deleteTest() {
		rservicedao.insert(testRSRequest1);
		assertTrue(rservicedao.delete(testRSRequest1));
	}

	@Test
	void deleteNotExistingTest() {
		assertFalse(rservicedao.delete(new ReligiousServiceRequest(987, null,
				null, testNode1, null, null, null, null)));
	}

	@Test
	void updateTest() {
		rservicedao.insert(testRSRequest1);

		ReligiousServiceRequest update = new ReligiousServiceRequest(testRSRequest1.getId(),
				LocalDateTime.now(), LocalDateTime.now(),
				testNode1, "Fred",
				ReligiousServiceRequest.ReligiousServiceRequestType.OTHERS,
				"A different description", "Austin");

		assertTrue(rservicedao.update(update));
		assertNotEquals(testRSRequest1, rservicedao.get(testRSRequest1.getId()));
	}

	@Test
	void updateNotExistingTest() {
		Node newNode = new Node("9876", 0, 0, "2", "Fuller",
				Node.NodeType.DEPT, "longname", "shortname");
		assertFalse(rservicedao.update(new ReligiousServiceRequest(987, LocalDateTime.now(),
				LocalDateTime.now(), newNode, "Jane",
				ReligiousServiceRequest.ReligiousServiceRequestType.BLESSING,
				"This request doesnt exist", "Jill")));
	}

	@Test
	void getAllTest() {
		nodeDao.insert(testNode2);
		rservicedao.insert(testRSRequest2);

		assertEquals(2, rservicedao.getAll().size());
	}

	@Test
	void getAllEmptyTest() {
		rservicedao.delete(testRSRequest1);

		assertTrue(rservicedao.getAll().isEmpty());
	}

	@Test
	void autoIncrementTest() {
		for (int i = 0; i < 10; i++) {
			rservicedao.insert(testRSRequest1);
		}

		Set<ReligiousServiceRequest> religiousServiceRequestSet = rservicedao.getAll();
		for (ReligiousServiceRequest sr : religiousServiceRequestSet) {
			// make sure the id is in the correct range
			assertTrue(sr.getId() < 11 || sr.getId() > 1);
		}
	}
}
