package com.datastax.training.demo;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.SimpleStatement;
import com.datastax.driver.core.Statement;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;

public class AccessingCassandra {

	public static void main(String[] args) {
		// NOTE: Change contact point if you are using a cloud node, etc
		Cluster cluster = Cluster.builder().addContactPoint("localhost").build();

		// NOTE: do run this demo you need a keyspace called 'demo' created
		// create KEYSPACE IF NOT EXISTS demo WITH replication = {'class': 'SimpleStrategy',
		// 'replication_factor': 1 };
		Session session = cluster.connect("demo");

		String createTable = "CREATE TABLE IF NOT EXISTS person (\n" +
				"    first_name text,\n" +
				"    last_name text,\n" +
				"    PRIMARY KEY (first_name, last_name)\n" +
				")";

		session.execute(createTable);

		// Statement using just raw CQL
		String insert = "INSERT INTO person (first_name, last_name) VALUES ('Bob', 'Smyte')";
		session.execute(insert);

		// Statement with a SimpleStatment, essentially the same as above
		String insertOne = "INSERT INTO person (first_name, last_name) VALUES ('Bob', 'Smyth')";
		SimpleStatement stmtOne = new SimpleStatement(insertOne);
		session.execute(stmtOne);

		// Using a Prepared statement so you bind with a bound statement for
		// greater efficiency
		// if reusing the statement over and over, and just changing params
		String insertTwo = "INSERT INTO person (first_name, last_name) VALUES (:first, :last)";
		PreparedStatement prep = session.prepare(insertTwo);
		Statement stmtTwo = prep.bind().setString("first", "Bob").setString("last", "Smeth");
		session.execute(stmtTwo);

		// QueryBuilder
		Statement stmtThree = QueryBuilder.insertInto("person").value("first_name", "Bob").value("last_name", "Smite");
		session.execute(stmtThree);

		// Mapper
		Person bob = new Person();
		bob.setFirstName("Bob");
		bob.setLastName("Smith");
		MappingManager manager = new MappingManager(session);
		Mapper<Person> mapper = manager.mapper(Person.class);
		mapper.save(bob);

		session.close();

		System.out.println("Done!  Check your table ");

		System.exit(0);
	}

}
