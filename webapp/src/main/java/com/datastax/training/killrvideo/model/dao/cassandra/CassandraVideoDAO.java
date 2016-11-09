package com.datastax.training.killrvideo.model.dao.cassandra;

import com.datastax.driver.core.*;
import com.datastax.driver.core.querybuilder.Insert;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.utils.UUIDs;

import com.datastax.training.killrvideo.model.Video;
import com.datastax.training.killrvideo.model.VideoAlreadyExistsException;
import com.datastax.training.killrvideo.model.VideoByTag;
import com.datastax.training.killrvideo.model.VideoByUser;
import com.datastax.training.killrvideo.model.dao.VideoDAO;

import java.util.*;

/**
 * Created on 18/10/2015.
 */
public class CassandraVideoDAO extends AbstractMapperDAO<Video> implements VideoDAO {

    private final PreparedStatement insertStatement;
    private final PreparedStatement deleteStatement;
    private final PreparedStatement deleteVideosByTagStatement;
    private final PreparedStatement selectByTag;
    private final PreparedStatement updateAvgRatingStatement;
    private final PreparedStatement addTagStatement;
    private final PreparedStatement removeTagStatement;
    private final PreparedStatement updateVideoStatement;

    private VideoByUserAccessor accessor;

    // Used to set the bucket value for the latest_videos table
    private int currentDate;

    public CassandraVideoDAO() {
        super();
        Session session = getCassandraSession();

        insertStatement = session.prepare(
                "INSERT INTO VIDEOS (video_id, user_id, title, type, " +
                    "release_date, description, mpaa_rating, genres, tags, " +
                    "preview_thumbnail, url, avg_rating) VALUES (" +
                    ":video_id, :user_id, :title, :type, :release_date, " +
                    ":description, :mpaa_rating, :genres, :tags, " +
                    ":preview_thumbnail, :url, :avg_rating)"
        );

        deleteStatement = session.prepare(
                "BEGIN BATCH " +
                "DELETE FROM videos WHERE video_id = :videoId; " +
                "DELETE FROM latest_videos WHERE video_bucket = :bucket AND video_id = :videoId; " +
                "DELETE FROM videos_by_user WHERE user_id = :userId AND video_id = :videoId; " +
                "APPLY BATCH;"
        );

        deleteVideosByTagStatement = session.prepare("DELETE FROM videos_by_tag where tag = :tagss AND video_id = :videoId");

        selectByTag = session.prepare("SELECT * FROM videos_by_tag WHERE tag = ? LIMIT ?");

        updateAvgRatingStatement = session.prepare("UPDATE videos SET avg_rating = :avgRating WHERE video_id = :videoId");
        addTagStatement = session.prepare("UPDATE videos SET tags = tags + :tag WHERE video_id = :videoId");
        removeTagStatement = session.prepare("UPDATE videos SET tags = tags - :tag WHERE video_id = :videoId");
        updateVideoStatement = session.prepare("UPDATE videos SET description = :description, title = :title, "
                + "type = :type, url = :url, release_date = :releaseDate, mpaa_rating = :mpaaRating, "
                + "avg_rating = :avgRating , tags = :tags, genres = :genres, preview_thumbnail = :previewThumbnail, "
                + "user_id = :userId WHERE video_id = :videoId");

        accessor = mapper.getManager().createAccessor(VideoByUserAccessor.class);

        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        currentDate = cal.get(Calendar.YEAR)*10000 + (cal.get(Calendar.MONTH)+1)*100 + cal.get(Calendar.DAY_OF_MONTH);
    }

    @Override
    public boolean addVideo(Video newVideo) throws VideoAlreadyExistsException {
        Session session = getCassandraSession();

        BoundStatement insertToVideos = insertStatement.bind();

        if (newVideo.hasVideoId()) {
            insertToVideos.setUUID("video_id", newVideo.getVideoId());
        }

        if (newVideo.hasUserId()) {
            insertToVideos.setUUID("user_id", newVideo.getUserId());
        }

        if (newVideo.hasTitle()) {
            insertToVideos.setString("title", newVideo.getTitle());
        }

        if (newVideo.hasType()) {
            insertToVideos.setString("type", newVideo.getType());
        }

        if (newVideo.hasReleaseDate()) {
            insertToVideos.setTimestamp("release_date", newVideo.getReleaseDate());
        }

        if (newVideo.hasReleaseYear()) {
            insertToVideos.setInt("release_year", newVideo.getReleaseYear());
        }

        if (newVideo.hasDescription()) {
            insertToVideos.setString("description", newVideo.getDescription());
        }

        if (newVideo.hasMpaaRating()) {
            insertToVideos.setString("mpaa_rating", newVideo.getMpaaRating());
        }

        if (newVideo.hasGenres()) {
            insertToVideos.setSet("genres", newVideo.getGenres());
        }

        if (newVideo.hasTags()) {
            insertToVideos.setSet("tags", newVideo.getTags());
        }

        if (newVideo.hasPreviewThumbnail()) {
            insertToVideos.setBytes("preview_thumbnail", newVideo.getPreviewThumbnail());
        }

        if (newVideo.hasUrl()) {
            insertToVideos.setString("url", newVideo.getUrl());
        }

        insertToVideos.setFloat("avg_rating", newVideo.getAvgRating());

        // Execute the statement
        ResultSet result = session.execute(insertToVideos);

        return true;
    }

    // Builds an insert for each tag and adds it to the batch
    public void addVideosByTag(Video newVideo, BatchStatement videoBatch) {
        for (String tag : newVideo.getTags()) {
            Insert insertToVideosByTag;
            insertToVideosByTag = QueryBuilder.insertInto("videos_by_tag");
            if (newVideo.hasVideoId()) {
                insertToVideosByTag.value("video_id", newVideo.getVideoId());
            }
            if (newVideo.hasTitle()) {
                insertToVideosByTag.value("title", newVideo.getTitle());
            }
            if (newVideo.hasType()) {
                insertToVideosByTag.value("type", newVideo.getType());
            }
            if (newVideo.hasDescription()) {
                insertToVideosByTag.value("description", newVideo.getDescription());
            }
            if (newVideo.hasTags()) {
                insertToVideosByTag.value("tags", newVideo.getTags());
            }
            insertToVideosByTag.value("tag", tag);
            videoBatch.add(insertToVideosByTag);
        }
    }

    @Override
    public UUID deleteVideo(UUID videoId) {
        Session session = getCassandraSession();

        Video deleteVideo = getVideo(videoId);

        // Bucket value extracted from videoId, used for the latest_videos table
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date(UUIDs.unixTimestamp(deleteVideo.getVideoId())));
        int bucket = cal.get(Calendar.YEAR)*10000 + (cal.get(Calendar.MONTH)+1)*100 + cal.get(Calendar.DAY_OF_MONTH);

        BoundStatement boundStatement = deleteStatement.bind();
        boundStatement.setUUID("videoId", videoId);
        boundStatement.setUUID("userId", deleteVideo.getUserId());
        boundStatement.setInt("bucket", bucket);

        session.execute(boundStatement);

        // Remove entries in videos_by_tag
        for (String tag : deleteVideo.getTags()) {
            session.execute(deleteVideosByTagStatement.bind(tag, videoId));
        }

        return videoId;
    }

    @Override
    public Video getVideo(UUID video_id) {
        return null;
    }

    @Override
    public Iterable<Video> getLatestVideos() {
        ArrayList<Video> latestVideos = new ArrayList<Video>();

        return latestVideos;
    }

    @Override
    public Iterable<VideoByUser> getUserVideos(UUID userId) {
        int rowLimit = 12;
        return accessor.getVideosForUser(userId, rowLimit);
    }

    @Override
    public Iterable<VideoByTag> getVideosByTag(String tag) {
        Session session = getCassandraSession();
        ArrayList<VideoByTag> videos = new ArrayList<VideoByTag>();
        int rowLimit = 12;

        ResultSet rs = session.execute(selectByTag.bind(tag, rowLimit));
        for (Row row : rs) {
            VideoByTag video = new VideoByTag();

            video.setTag(row.getString("tag"));
            video.setVideoId(row.getUUID("video_id"));
            video.setTitle(row.getString("title"));
            video.setType(row.getString("type"));
            video.setTags(row.getSet("tags", String.class));
            video.setDescription(row.getString("description"));
            videos.add(video);
        }
        return videos;
    }

    @Override
    public Collection<VideoByTag> getVideosByTags(Collection<String> tags) {
        List<VideoByTag> videos = new ArrayList<>();
        List<ResultSetFuture> futureVideos = new ArrayList<>();
        int rowLimit = 12;

        // Send out all of the queries in parallel
        for (String tag : tags) {
            futureVideos.add(getCassandraSession().executeAsync(selectByTag.bind(tag, rowLimit)));
        }

        // Wait for all of the results to come back
        for (ResultSetFuture futureVideo : futureVideos) {
            try {
                ResultSet rs = futureVideo.getUninterruptibly();
                for (Row row : rs) {
                    VideoByTag video = new VideoByTag();

                    video.setTag(row.getString("tag"));
                    video.setVideoId(row.getUUID("video_id"));
                    video.setTitle(row.getString("title"));
                    video.setType(row.getString("type"));
                    video.setTags(row.getSet("tags", String.class));
                    video.setDescription(row.getString("description"));

                    if (!videos.contains(video))
                        videos.add(video);
                }
            } catch (Exception e) {
                throw new RuntimeException("Some sort of error occured");
            }
        }

        return videos;
    }

    @Override
    public boolean addTagToVideo(UUID videoId, String tag) {
        Session session = getCassandraSession();

        BoundStatement boundStatement = addTagStatement.bind().setString("tag", tag).setUUID("video_id", videoId);

        session.execute(boundStatement);
        return true;
    }

    @Override
    public boolean removeTagFromVideo(UUID videoId, String tag) {
        Session session = getCassandraSession();

        BoundStatement boundStatement = removeTagStatement.bind().setString("tag", tag).setUUID("video_id", videoId);

        session.execute(boundStatement);
        return true;
    }

    @Override
    public boolean updateAvgRating(UUID videoId, double avgRating) {
        Session session = getCassandraSession();

        BoundStatement boundStatement = updateAvgRatingStatement.bind().setDouble("avgRating", avgRating)
                .setUUID("videoId", videoId);

        session.execute(boundStatement);
        return true;
    }

}
