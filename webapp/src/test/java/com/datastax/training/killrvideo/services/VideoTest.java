package com.datastax.training.killrvideo.services;

import static org.junit.Assert.assertEquals;

import com.datastax.driver.core.utils.UUIDs;
import com.datastax.training.killrvideo.model.Video;
import com.datastax.training.killrvideo.model.VideoAlreadyExistsException;
import com.datastax.training.killrvideo.model.VideoByUser;
import com.datastax.training.killrvideo.model.dao.VideoDAO;
import com.datastax.training.killrvideo.model.dao.cassandra.CassandraVideoDAO;
import com.datastax.training.killrvideo.testutilities.AbstractDSETest;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.*;
import java.nio.ByteBuffer;

public class VideoTest extends AbstractDSETest {

    private static Video video;
    private static VideoDAO videoDAO;

    @BeforeClass
    public static void setupTests() throws VideoAlreadyExistsException {
        HashSet<String> strings = new HashSet<String>();
        strings.add("epic");
        strings.add("awesome");

        // Add a single video
        video = new Video();
        video.setUserId(UUIDs.timeBased());
        video.setVideoId(UUIDs.timeBased());
        video.setTitle("Pirates of the Caribbean");
        video.setAvgRating(4.9f);
        video.setDescription("Epic movie!");
        video.setMpaaRating("PG");
        video.setReleaseDate(new Date(1056780000000L)); // 6/28/2003
        video.setPreviewThumbnail(ByteBuffer.wrap(new byte[] { 0, 1, 2 }));
        video.setType("Movie");
        video.setGenres(strings);
        video.setTags(strings);
        video.setUrl("https://www.youtube.com/watch?v=naQr0uTrH_s");

        videoDAO = new CassandraVideoDAO();
        videoDAO.addVideo(video);
    }

    private void testAreEqual(Video left, Video right)
    {
        assertEquals(left.getUserId(), right.getUserId());
        assertEquals(left.getVideoId(), right.getVideoId());
        assertEquals(left.getTitle(), right.getTitle());
        assertEquals(left.getAvgRating(), right.getAvgRating(), 0.01);
        assertEquals(left.getDescription(), right.getDescription());
        assertEquals(left.getMpaaRating(), right.getMpaaRating());
        assertEquals(left.getReleaseDate(), right.getReleaseDate()); // 6/28/2003
        assertEquals(left.getPreviewThumbnail(), right.getPreviewThumbnail());
        assertEquals(left.getType(), right.getType());
        assertEquals(left.getGenres(), right.getGenres());
        assertEquals(left.getTags(), right.getTags());
        assertEquals(left.getUrl(), right.getUrl());
    }

    private void testAreEqual(VideoByUser left, Video right)
    {
        assertEquals(left.getUserId(), right.getUserId());
        assertEquals(left.getVideoId(), right.getVideoId());
        assertEquals(left.getTitle(), right.getTitle());
        assertEquals(left.getPreviewThumbnail(), right.getPreviewThumbnail());
        assertEquals(left.getType(), right.getType());
        assertEquals(left.getTags(), right.getTags());
    }

    private <T> T single(Iterable<T> iterable)
    {
        T ret = null;
        for(T item : iterable)
            ret = item;
        return ret;
    }

    private Video findVideoWithID(Iterable<Video> videos, UUID videoID)
    {
        for(Video video : videos)
            if(video.getVideoId().equals(videoID))
                return video;
        return null;
    }

    @Test
    public void testOurVideoExists() {
        // getVideo
        Video copy = videoDAO.getVideo(video.getVideoId());
        testAreEqual(video, copy);
    }

    @Test
    public void testOurVideoExistsInAllTables() {
        ///////////////////////////////////////////////////////FINISHFINISHFINISHFINISHFINISHFINISHFINISHFINISH
        Iterable<VideoByUser> videos = videoDAO.getUserVideos(video.getUserId());
        VideoByUser vbu = single(videos);
        testAreEqual(vbu, video);

        // Test getting a video for user that doesn't have videos
        videos = videoDAO.getUserVideos(UUID.randomUUID());
        vbu = single(videos);
        assertEquals(vbu, null);
    }

    @Test
    public void testUpdateDeleteGetLatestVideos() throws VideoAlreadyExistsException {
        // insert video
        // update it (all columns)
        // check all three tables got the updates
        // delete the video

        Video anotherVideo = new Video();
        anotherVideo.setUserId(UUIDs.timeBased());
        anotherVideo.setVideoId(UUIDs.timeBased());
        anotherVideo.setTitle("Pete's Dragon");
        anotherVideo.setAvgRating(4.4f);
        anotherVideo.setDescription("Fire movie!");
        anotherVideo.setMpaaRating("PG");
        anotherVideo.setReleaseDate(new Date(1470981600000L)); // 8/12/2016
        anotherVideo.setPreviewThumbnail(ByteBuffer.wrap(new byte[] { 2, 1, 0 }));
        anotherVideo.setType("Movie");
        HashSet<String> strings = new HashSet<String>();
        strings.add("fire");
        strings.add("breathing");
        anotherVideo.setGenres(strings);
        anotherVideo.setTags(strings);
        anotherVideo.setUrl("https://www.youtube.com/watch?v=JwA7ZdqhPMg");

        videoDAO.addVideo(anotherVideo);

        Video copy = videoDAO.getVideo(anotherVideo.getVideoId());
        testAreEqual(anotherVideo, copy);
        Iterable<VideoByUser> videosByUser =
                videoDAO.getUserVideos(anotherVideo.getUserId());
        testAreEqual(single(videosByUser), copy);

        Iterable<Video> latestVideos = videoDAO.getLatestVideos();
        Video fromLatest = findVideoWithID(latestVideos, copy.getVideoId());

        assertEquals(fromLatest.getVideoId(), copy.getVideoId());
        assertEquals(fromLatest.getTitle(), copy.getTitle());
        assertEquals(fromLatest.getType(), copy.getType());
        assertEquals(fromLatest.getTags(), copy.getTags());
        assertEquals(fromLatest.getPreviewThumbnail(), copy.getPreviewThumbnail());

        // Update video and ensure update reflects in all three tables


        // Ensure video was deleted from all three tables

        copy.setTitle("Pete's Fiery Dragon");

    }

    @Test
    public void testGettingAUsersVideos() {
        // Test one user has videos and another user does not
        // getUserVideos
    }

    @Test
    public void testTaggingVideos() {
        // Be sure to check that tag is in all tables with a video, not just the videos table
        // addTagToVideo
        // removeTagFromVideo

    }

    @Test
    public void testRatingVideos() {
        // Be sure to test that the rating is updated in all tables
        //updateAvgRating
    }
}
