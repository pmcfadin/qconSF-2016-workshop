package com.datastax.training.killrvideo.dao.cassandra;

import static junit.framework.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Map;

import com.datastax.training.killrvideo.model.Address;
import com.datastax.training.killrvideo.model.UserAlreadyExistsException;
import com.datastax.training.killrvideo.testutilities.TestData;
import com.datastax.training.killrvideo.util.CassandraSession;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.datastax.training.killrvideo.model.User;
import com.datastax.training.killrvideo.model.dao.cassandra.CassandraUserDAO;
import com.datastax.training.killrvideo.testutilities.AbstractDSETest;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.UDTValue;

import junit.framework.Assert;

/**
 * Created on 19/10/2015.
 */
@RunWith(JUnit4.class)
public class UserDAOAddressTest extends AbstractDSETest {

    @Before
    public void cleanupTransactionTables() {
        Session session = CassandraSession.getSession();
        session.execute("TRUNCATE user");
    }

    @Test
    public void testAddressToUDT() {

        CassandraUserDAO cu = new CassandraUserDAO();

        UDTValue addressUDT1 = cu.addressToUDT(TestData.TEST_ADDRESS1);
        assertEquals("123 Main Street", addressUDT1.getString("street"));

        UDTValue addressUDT2 = cu.addressToUDT(TestData.TEST_ADDRESS2);
        assertEquals("New Orléans", addressUDT2.getString("city"));

        UDTValue addressUDT3 = cu.addressToUDT(TestData.TEST_ADDRESS3);
        assertNull(addressUDT3.getString("postalcode"));
    }

    @Test
    public void testUDTToAddress() {

        CassandraUserDAO cu = new CassandraUserDAO();

        UDTValue addressUDT1 = cu.addressToUDT(TestData.TEST_ADDRESS1);
        Address newAddress = cu.UDTToAddress(addressUDT1);

        org.junit.Assert.assertEquals(TestData.TEST_ADDRESS1.getStreet(), newAddress.getStreet());
        org.junit.Assert.assertEquals(TestData.TEST_ADDRESS1.getCity(), newAddress.getCity());
        org.junit.Assert.assertEquals(TestData.TEST_ADDRESS1.getPostalCode(), newAddress.getPostalCode());

    }

    @Test
    public void testAddUserAndRetrieveSameUserWithAddress() throws UserAlreadyExistsException {
        CassandraUserDAO userDAO = new CassandraUserDAO();

        userDAO.addUser(TestData.TEST_USER1);
        userDAO.addAddressToUser(TestData.TEST_USER1.getEmail(), "Home", TestData.TEST_ADDRESS1);

        User savedUser = userDAO.getUser(TestData.TEST_USER1.getEmail());

        assertNotNull(savedUser);
        assertNotNull(savedUser.getUserId());

        Address homeAddress = savedUser.getAddresses().get("Home");
        Assert.assertEquals("Springfield", homeAddress.getCity());

    }

    @Test
    public void testAddAddressToUserAndRetrieve() throws UserAlreadyExistsException {
        CassandraUserDAO userDAO = new CassandraUserDAO();

        userDAO.addUser(TestData.TEST_USER1);
        userDAO.addAddressToUser(TestData.TEST_USER1.getEmail(), "Home", TestData.TEST_ADDRESS1);
        userDAO.addAddressToUser(TestData.TEST_USER1.getEmail(), "Billing", TestData.TEST_ADDRESS3);

        User savedUser = userDAO.getUser(TestData.TEST_USER1.getEmail());

        assertNotNull(savedUser);
        assertNotNull(savedUser.getUserId());

        Map<String, Address> addresses = savedUser.getAddresses();
        assertEquals(2, addresses.size());
        Address homeAddress = addresses.get("Billing");
        Assert.assertEquals("Smalltown", homeAddress.getCity());

    }

}
