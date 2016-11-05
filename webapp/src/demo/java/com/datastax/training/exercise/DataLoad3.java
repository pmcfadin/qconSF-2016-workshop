package com.datastax.training.exercise;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.Statement;
import com.datastax.driver.core.utils.UUIDs;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.UUID;

public class DataLoad3 {
    public static void main(String[] args) {
        Cluster cluster = Cluster.builder().addContactPoint("localhost").build();

        Session session = cluster.connect("killrvideo");

        PreparedStatement prepared = session.prepare(
                "INSERT INTO searches_by_user_bucketed_300 (user_id, bucket, search_id, search) " +
                        "VALUES (:user_id, :bucket, :search_id, :search)");

        int lineImportCount = 0; // Gives a heartbeat to the console

        try (BufferedReader br = new BufferedReader(new FileReader("./datamodel/csv/searches_by_user_bucketed_300.csv"))) {
            String line;
            br.readLine(); // Drop header
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                Statement bound = prepared.bind()
                        .setUUID("user_id", UUID.fromString(parts[0]))
                        .setInt("bucket", Integer.parseInt(parts[1]))
                        .setUUID("search_id", UUID.fromString(parts[2]))
                        .setString("search", parts[3]);
                session.execute(bound);
                if(lineImportCount++ % 500 == 0)
                    System.out.println(lineImportCount);
            }
        }
        catch (IOException e)
        {
            System.out.println("My life has no meaning.");
            System.out.print(e);
        }
        session.close();
    }
}
