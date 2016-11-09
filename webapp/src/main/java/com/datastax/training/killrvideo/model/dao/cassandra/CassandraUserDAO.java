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

    public CassandraUserDAO() {
        super();

        Session session = getCassandraSession();


    }

    @Override
    public boolean addUser(User newUser) throws UserAlreadyExistsException {
        Session session = getCassandraSession();

        SimpleStatement statement = new SimpleStatement(
                "INSERT INTO USER (email, joined, user_id, fname, lname, password, salt) " +
                        "VALUES (:email,:joined,:user_id,:fname,:lname,:password,:salt) IF NOT EXISTS",
                newUser.getEmail(), newUser.getJoined(), newUser.getUserId(), newUser.getFirstName(),
                newUser.getLastName(), newUser.getPassword(), newUser.getSalt());


        ResultSet result = session.execute(statement);

        if (!result.wasApplied()) {
            throw new UserAlreadyExistsException(
                    "Could not save user with the specified id. A duplicate already exists");
        }
        return true;
    }

    @Override
    public User getUser(String email) {
        Session session = getCassandraSession();

        ResultSet resultSet = session.execute("SELECT * FROM user WHERE email = '" + email + "'");

        if (resultSet.isExhausted()) {
            return null;
        }

        Row row = resultSet.one();

        User newUser = new User();
        newUser.setEmail(row.getString("email"));
        newUser.setFirstName(row.getString("fname"));
        newUser.setLastName(row.getString("lname"));
        newUser.setJoined(row.get("joined", Date.class));
        newUser.setPassword(row.getBytes("password"));
        newUser.setSalt(row.getBytes("salt"));
        newUser.setUserId(row.getUUID("user_id"));

        newUser.setPhoneNumbers(row.getMap("phone_numbers", String.class, BigDecimal.class));


        return newUser;
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