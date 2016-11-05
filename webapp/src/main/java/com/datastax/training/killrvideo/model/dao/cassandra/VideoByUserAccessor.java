package com.datastax.training.killrvideo.model.dao.cassandra;

import com.datastax.training.killrvideo.model.VideoByUser;
import com.datastax.driver.mapping.Result;
import com.datastax.driver.mapping.annotations.Accessor;
import com.datastax.driver.mapping.annotations.Param;
import com.datastax.driver.mapping.annotations.Query;

import java.util.UUID;

@Accessor
public interface VideoByUserAccessor {
    @Query("SELECT * FROM videos_by_user WHERE user_id = :userId LIMIT :rowLimit")
    Result<VideoByUser> getVideosForUser(@Param("userId") UUID userId, @Param("rowLimit") int rowLimit);
}