package com.datastax.training.killrvideo.model.dao;

import com.datastax.training.killrvideo.model.*;

import java.util.Collection;
import java.util.UUID;

/**
 * Created on 17/10/2015.
 */
public interface VideoDAO {

    boolean addVideo(Video newVideo) throws VideoAlreadyExistsException;

    UUID deleteVideo(UUID videoId);

    Video getVideo(UUID videoId);

    Iterable<Video> getLatestVideos();

    Iterable<VideoByUser> getUserVideos(UUID userId);

    Iterable<VideoByTag> getVideosByTag(String tag);

    Collection<VideoByTag> getVideosByTags(Collection<String> tags);

    boolean addTagToVideo(UUID videoID, String tag);

    boolean removeTagFromVideo(UUID videoId, String tag);

    boolean updateAvgRating(UUID videoID, double avgRating);

}
