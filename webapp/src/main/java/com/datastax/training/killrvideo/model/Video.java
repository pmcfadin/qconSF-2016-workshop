package com.datastax.training.killrvideo.model;

import com.datastax.driver.mapping.annotations.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.nio.ByteBuffer;
import java.util.*;

/**
 * Created on 17/10/2015.
 */

@Table(name = "videos", readConsistency = "LOCAL_ONE", writeConsistency = "LOCAL_QUORUM")
public class Video {

    @PartitionKey
    @Column(name = "video_id")
    private UUID videoId;

    @Column(name = "description")
    private String description;
    @Column(name = "title")
    private String title;
    @Column(name = "type")
    private String type;
    @Column(name = "url")
    private String url;
    @Column(name = "release_date")
    private Date releaseDate;
    @Column(name = "release_year")
    private int releaseYear;
    @Column(name = "avg_rating")
    private float avgRating;
    @Column(name = "mpaa_rating")
    private String mpaaRating;
    @Column(name = "tags")
    private Set<String> tags;
    @Column(name = "preview_thumbnail")
    private ByteBuffer previewThumbnail;
    @Column(name = "genres")
    private Set<String> genres;
    @Column(name = "user_id")
    private UUID userId;

    public String getDescription() {
        return description;
    }

    public boolean hasDescription() { if (description != null) return true; else return false; }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean hasTitle() { if (title != null) return true; else return false; }

    public String getType() { return type; }

    public boolean hasType() { if (type != null) return true; else return false; }

    public void setType(String type) { this.type = type; }

    public String getUrl() {
        return url;
    }

    public boolean hasUrl() { if (url != null) return true; else return false; }

    public void setUrl(String url) {
        this.url = url;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public boolean hasReleaseDate() { if (releaseDate != null) return true; else return false; }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public boolean hasReleaseYear() { if (releaseYear > 0) return true; else return false; }

    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }

    public float getAvgRating() {
        return avgRating;
    }

    public void setAvgRating(float avgRating) {
        this.avgRating = avgRating;
    }

    public String getMpaaRating() {
        return mpaaRating;
    }

    public void setMpaaRating(String mpaaRating) {
        this.mpaaRating = mpaaRating;
    }

    public boolean hasMpaaRating() { if (mpaaRating != null) return true; else return false; }

    public Set<String> getTags() {
        return tags;
    }

    public void setTags(Set<String> tags) {
        this.tags = tags;
    }

    public boolean hasTags() { if (tags != null) return true; else return false; }

    public ByteBuffer getPreviewThumbnail() {
        return previewThumbnail;
    }

    public boolean hasPreviewThumbnail() { if (previewThumbnail != null) return true; else return false; }

    public void setPreviewThumbnail(ByteBuffer previewThumbnail) {
        this.previewThumbnail = previewThumbnail;
    }

    public Set<String> getGenres() {
        return genres;
    }

    public boolean hasGenres() { if (genres != null) return true; else return false; }

    public void setGenres(Set<String> genres) {
        this.genres = genres;
    }

    public UUID getUserId() {
        return userId;
    }

    public boolean hasUserId() { if (userId != null) return true; else return false; }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public UUID getVideoId() {
        return videoId;
    }

    public boolean hasVideoId() { if (videoId != null) return true; else return false; }

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
        if (other instanceof Video) {
            Video otherAccount = (Video) other;
            eq = (otherAccount.videoId.equals(this.videoId));
        }
        return eq;
    }
}