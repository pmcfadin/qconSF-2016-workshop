package com.datastax.training.demo;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.querybuilder.Delete;
import com.datastax.driver.core.querybuilder.Insert;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Select;
import com.datastax.driver.core.utils.UUIDs;
import com.datastax.driver.mapping.annotations.Query;

import java.util.UUID;

import static com.datastax.driver.core.querybuilder.QueryBuilder.*;

public class QueryBuilderDemo {
    public static void main(String[] args) {

        Cluster cluster = Cluster.builder().addContactPoint("localhost").build();
        Session session = cluster.connect("killrvideo");

        // Insert a user
        UUID userID = UUIDs.random();
        Insert insertNewUser =
            QueryBuilder
                .insertInto("killrvideo", "user")
                .value("email", "moo@you.com")
                .value("fname", "Mooer")
                .value("lname", "Youer")
                .value("password", "chicken")
                .value("user_id", userID);

        System.out.println(insertNewUser.getQueryString());
        // Ouput:
        // INSERT INTO killrvideo.user (email,fname,lname,password,user_id) VALUES (?,?,?,?,?)

        session.execute(insertNewUser);

        // Insert two videos for the user
        UUID video1Id = UUIDs.timeBased();
        UUID video2Id = UUIDs.timeBased();

        Insert insertVideo1 =
            QueryBuilder
                .insertInto("killrvideo", "videos_by_tag")
                .value("tag", "adventure")
                .value("video_id", video1Id)
                .value("description", "Awesome pirates!")
                .value("title", "Curse of the Black Pearl");

        Insert insertVideo2 =
                QueryBuilder
                        .insertInto("killrvideo", "videos_by_tag")
                        .value("tag", "adventure")
                        .value("video_id", video2Id)
                        .value("description", "More awesome pirates!")
                        .value("title", "Dead Man's Chest");

        session.execute(insertVideo1);
        session.execute(insertVideo2);

        // Retrieve the latest adventure video
        Select selectLatestVideo =
                QueryBuilder
                        .select()
                        .from("killrvideo", "videos_by_tag")
                        .where(eq("tag", "adventure"))
                        .limit(1);

        ResultSet latestVideo = session.execute(selectLatestVideo);
        System.out.println(latestVideo.one().getString("title"));
        // Ouput:
        // Dead Man's Chest

        // Retrieve the oldest adventure video
        Select selectOldestVideo =
                QueryBuilder
                        .select()
                        .from("killrvideo", "videos_by_tag")
                        .where(eq("tag", "adventure"))
                        .orderBy(asc("video_id"))
                        .limit(1);

        System.out.println(selectOldestVideo.getQueryString());
        // Output:
        // SELECT * FROM killrvideo.videos_by_tag WHERE tag=? ORDER BY video_id ASC LIMIT 1;

        ResultSet oldestVideo = session.execute(selectOldestVideo);
        System.out.println(oldestVideo.one().getString("title"));

        // Output:
        // Curse of the Black Pearl

        // Delete everything we just created
        Delete.Where deleteUser = QueryBuilder.delete()
                .from("killrvideo", "user")
                .where(eq("email", "moo@you.com"));
        Delete.Where deleteVideo1 = QueryBuilder.delete()
                .from("killrvideo", "videos_by_tag")
                .where(eq("video_id", video1Id))
                .and(eq("tag", "adventure"));
        Delete.Where deleteVideo2 = QueryBuilder.delete()
                .from("killrvideo", "videos_by_tag")
                .where(eq("video_id", video2Id))
                .and(eq("tag", "adventure"));

        System.out.println(deleteUser.getQueryString());
        // Output:
        // DELETE FROM killrvideo.user WHERE email=?;

        session.execute(deleteUser);
        session.execute(deleteVideo1);
        session.execute(deleteVideo2);

        session.close();
        cluster.close();
    }
}
