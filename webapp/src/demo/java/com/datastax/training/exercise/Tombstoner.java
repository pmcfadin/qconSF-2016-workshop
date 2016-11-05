package com.datastax.training.exercise;

import com.datastax.driver.core.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.UUID;

public class Tombstoner {
    public static void main(String[] args) {
        Cluster cluster = Cluster.builder().addContactPoint("localhost").build();

        Session session = cluster.connect("killrvideo");

        PreparedStatement prepared = session.prepare(
                "INSERT INTO searches_by_user (user_id, search_id, search) " +
                        "VALUES (:user_id, :search_id, :search) USING TTL :ttl");

        int lineImportCount = 0; // Gives a heartbeat to the console

        try (BufferedReader br = new BufferedReader(new FileReader("./datamodel/csv/searches_by_user_big.csv"))) {
            String line;
            br.readLine(); // Drop header
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                Statement bound = prepared.bind()
                        .setUUID("user_id", UUID.fromString(parts[0]))
                        .setUUID("search_id", UUID.fromString(parts[1]))
                        .setString("search", parts[2])
                        .setInt("ttl", 60);
                session.execute(bound);
                if(++lineImportCount % 500 == 0)
                    System.out.println(lineImportCount);
            }
        }
        catch (IOException e)
        {
            System.out.println("My life has no meaning.");
            System.out.print(e);
        }
        finally {
            session.close();
        }
    }

}
