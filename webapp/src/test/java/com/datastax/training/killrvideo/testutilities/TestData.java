package com.datastax.training.killrvideo.testutilities;

import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.datastax.training.killrvideo.model.Address;
import com.datastax.training.killrvideo.model.RegisteringUser;
import com.datastax.training.killrvideo.model.User;
import com.datastax.driver.core.utils.UUIDs;

/**
 * DataStax Academy Sample Application
 * <p>
 * Copyright 2015 DataStax
 */
public class TestData {

    // Helper to avoid try/catch blocks all over the place.
    private static Date parseDate(SimpleDateFormat fmt, String date_string) {
        Date ret_date = new Date();
        try {
            ret_date = fmt.parse(date_string);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return ret_date;
    }

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    private static final SimpleDateFormat DATETIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    public static final UUID ACCTID1 = UUID.fromString("e568970a-8891-11e5-af63-feff819cdc9f");

    public static final String TEST_DATE_STR1;
    public static final String TEST_DATE_STR2;

    public static Date TEST_DATE1 = null;
    public static Date TEST_DATE2 = null;
    public static Date TEST_TIME_1 = null;
    public static Date TEST_TIME_2 = null;
    public static Date TEST_TIME_3 = null;
    public static Date TEST_TIME_4 = null;
    public static Date TEST_TIME_5 = null;

    public static final User TEST_USER1 = new User();
    public static final User TEST_USER2 = new User();

    public static final RegisteringUser REGISTERING_USER1 = new RegisteringUser();

    public static final Address TEST_ADDRESS1 = new Address();
    public static final Address TEST_ADDRESS2 = new Address();
    public static final Address TEST_ADDRESS3 = new Address();

    public static final UUID TEST_ACCTID1 = UUID.fromString("e568970a-8891-11e5-af63-feff819cdc9f");
    public static final String TEST_ACCT1_NAME = "Margin Account";


    static {
        TEST_DATE_STR1 = "2014-03-03";
        TEST_DATE_STR2 = "2014-03-04";

        TEST_DATE1 = parseDate(DATE_FORMAT, TEST_DATE_STR1);
        TEST_DATE2 = parseDate(DATE_FORMAT, TEST_DATE_STR2);
        TEST_TIME_1 = parseDate(DATETIME_FORMAT, TEST_DATE_STR1 + " 10:00:01");
        TEST_TIME_2 = parseDate(DATETIME_FORMAT, TEST_DATE_STR1 + " 10:00:02");
        TEST_TIME_3 = parseDate(DATETIME_FORMAT, TEST_DATE_STR2 + " 10:00:03");
        TEST_TIME_4 = parseDate(DATETIME_FORMAT, TEST_DATE_STR2 + " 10:00:04");
        TEST_TIME_5 = parseDate(DATETIME_FORMAT, TEST_DATE_STR2 + " 10:00:05");

        TEST_ADDRESS1.setStreet("123 Main Street");
        TEST_ADDRESS1.setCity("Springfield");
        TEST_ADDRESS1.setCountry("USA");
        TEST_ADDRESS1.setPostalCode("12345");

        TEST_ADDRESS2.setStreet("123 Main Street");
        TEST_ADDRESS2.setCity("New Orléans");
        TEST_ADDRESS2.setCountry("USA");
        TEST_ADDRESS2.setPostalCode("12345");

        TEST_ADDRESS3.setStreet("123 Main Street");
        TEST_ADDRESS3.setCity("Smalltown");
        TEST_ADDRESS3.setCountry("USA");

        TEST_USER1.setEmail("joeschmo@blah.com");
        TEST_USER1.setFirstName("Joseph");
        TEST_USER1.setLastName("Schmo");
        TEST_USER1.setJoined(new Date());

        TEST_USER1.setPassword(ByteBuffer.wrap("fake".getBytes()));
        TEST_USER1.setSalt(ByteBuffer.wrap("bake".getBytes()));
        TEST_USER1.setUserId(UUID.randomUUID());
        Map<String, BigDecimal> user1phones = new HashMap<>();
        user1phones.put("Home", new BigDecimal(123456789));
        user1phones.put("Mobile", new BigDecimal(2125551212));
        TEST_USER1.setPhoneNumbers(user1phones);

        TEST_USER2.setEmail("duplicate@blah.com");
        TEST_USER2.setFirstName("Doo");
        TEST_USER2.setLastName("Plicate");
        TEST_USER2.setJoined(new Date());
        TEST_USER2.setPassword(ByteBuffer.wrap("fake".getBytes()));
        TEST_USER2.setSalt(ByteBuffer.wrap("bake".getBytes()));
        TEST_USER2.setUserId(UUID.randomUUID());
        Map<String, BigDecimal> user2phones = new HashMap<>();
        user2phones.put("Home", new BigDecimal(987654321));
        user2phones.put("Mobile", new BigDecimal(4158675309L));
        TEST_USER2.setPhoneNumbers(user2phones);


        REGISTERING_USER1.setEmail("joe.schmo@gmail.moc");
        REGISTERING_USER1.setFirstName("Joe");
        REGISTERING_USER1.setLastName("Schmo");
        REGISTERING_USER1.setClearTextPassword("letmein");

    }
}
