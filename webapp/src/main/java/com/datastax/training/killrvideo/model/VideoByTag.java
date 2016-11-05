package com.datastax.training.killrvideo.model;

import com.datastax.driver.mapping.annotations.ClusteringColumn;
import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;

import java.util.Objects;
import java.util.Set;
import java.util.UUID;

/**
 * Created on 17/10/2015.
 */

@Table(name = "videos_by_tag", readConsistency = "LOCAL_ONE", writeConsistency = "LOCAL_QUORUM")
public class VideoByTag {

    @PartitionKey
    @Column(name = "tag")
    private String tag;

    @Column(name = "video_id")
    @ClusteringColumn
    private UUID videoId;
    @Column(name = "description")
    private String description;
    @Column(name = "title")
    private String title;
    @Column(name = "type")
    private String type;
    @Column(name = "tags")
    private Set<String> tags;


    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; };

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() { return type; }

    public void setType(String type) { this.type = type; }

    public Set<String> getTags() {
        return tags;
    }

    public void setTags(Set<String> tags) {
        this.tags = tags;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public UUID getVideoId() {
        return videoId;
    }

    public void setVideoId(UUID videoId) {
        this.videoId = videoId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(videoId);
    }

    @Override
    public boolean equals(Object other) {
        boolean eq = false;
        if (other instanceof VideoByTag) {
            VideoByTag otherAccount = (VideoByTag) other;
            eq = (otherAccount.videoId.equals(this.videoId));
        }
        return eq;
    }

}