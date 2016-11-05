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

public class DataLoad2 {
    public static void main(String[] args) {
        Cluster cluster = Cluster.builder().addContactPoint("localhost").build();

        Session session = cluster.connect("killrvideo");

        PreparedStatement prepared = session.prepare(
                "INSERT INTO searches_by_user_year_month (user_id, year, month, search_id, search) " +
                        "VALUES (:user_id, :year, :month, :search_id, :search)");

        int lineImportCount = 0; // Gives a heartbeat to the console

        try (BufferedReader br = new BufferedReader(new FileReader("./datamodel/csv/searches_by_user_year_month.csv"))) {
            String line;
            br.readLine(); // Drop header
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                Statement bound = prepared.bind()
                        .setUUID("user_id", UUID.fromString(parts[0]))
                        .setInt("year", Integer.parseInt(parts[1]))
                        .setInt("month", Integer.parseInt(parts[2]))
                        .setUUID("search_id", UUID.fromString(parts[3]))
                        .setString("search", parts[4]);
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
