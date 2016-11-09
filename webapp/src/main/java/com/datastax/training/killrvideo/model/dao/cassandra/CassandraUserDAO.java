package com.datastax.training.killrvideo.model.dao.cassandra;

import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.datastax.driver.core.*;
import com.datastax.training.killrvideo.model.Address;
import com.datastax.training.killrvideo.model.User;
import com.datastax.training.killrvideo.model.UserAlreadyExistsException;
import com.datastax.training.killrvideo.model.dao.UserDAO;

public class CassandraUserDAO extends CassandraDAO implements UserDAO {

    private PreparedStatement addAddressToUserStatement;

    public CassandraUserDAO() {
        super();
    }

    @Override
    public boolean addUser(User newUser) throws UserAlreadyExistsException {
        Session session = getCassandraSession();

        return true;
    }

    @Override
    public User getUser(String email) {
        Session session = getCassandraSession();

        return null;
    }

    @Override
    public boolean addAddressToUser(String email, String addressName, Address newAddress) {
        Session session = getCassandraSession();


        return true;
    }

    public UDTValue addressToUDT(Address address) {
        if (address == null) {
            return null;
        }


        return null;
    }

    public Address UDTToAddress(UDTValue udtValue) {

        return null;
    }

    @Override
    public boolean updateUser(User updatedUser) {
        throw new UnsupportedOperationException("Not done yet!");
    }

}