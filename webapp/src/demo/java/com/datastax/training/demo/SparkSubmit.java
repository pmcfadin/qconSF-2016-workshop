package com.datastax.training.demo;

import java.io.Serializable;
import org.apache.spark.SparkConf;

import org.apache.spark.api.java.JavaSparkContext;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.datastax.spark.connector.cql.CassandraConnector;

public class SparkSubmit implements Serializable {
	private static final long serialVersionUID = 1L;
	private transient SparkConf conf;

	private SparkSubmit(SparkConf conf) {
		this.conf = conf;
	}

	private void run() {
		JavaSparkContext sc = new JavaSparkContext(conf);
		doThings(sc);
		sc.stop();
	}

	private void doThings(JavaSparkContext sc) {
		CassandraConnector connector = CassandraConnector.apply(sc.getConf());
		Session session = connector.openSession();
		ResultSet results = session.execute("SELECT * FROM demo.users");

		for (Row row : results) {
			System.out.println("User is: " + row.getString(0));
		}

	}

	public static void main(String[] args) {
		// NOTE:  The location of this jar has to be where your app compiles and places it
		String[] jars = { "../../target/scratch-0.0.1-SNAPSHOT.jar" };
		SparkConf conf = new SparkConf(true);
		conf.setAppName("Java Submit to Spark from IDE");
		conf.setMaster("spark://127.0.0.1:7077");
		conf.set("spark.cassandra.connection.host", "127.0.0.1");
		conf.set("spark.cores.max", "1");
		conf.setJars(jars);

		SparkSubmit app = new SparkSubmit(conf);
		app.run();
	}
}
