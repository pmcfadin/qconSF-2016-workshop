package com.datastax.training.killrvideo.services;

import com.datastax.driver.core.utils.UUIDs;
import com.datastax.training.killrvideo.model.*;
import com.datastax.training.killrvideo.model.dao.VideoDAO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * Created on 05/10/2015.
 */
@Path("videos")
@Singleton
public class VideoService {

    private VideoDAO videoDAO;
    private final ObjectMapper jsonMapper;

    @Inject
    public VideoService(VideoDAO dao) {

        this.videoDAO = dao;
        jsonMapper = new ObjectMapper();
    }

    @PUT
    @Path("add/{userid}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Secured
    public Response addVideo(@Context UriInfo uriInfo, @PathParam("userid") UUID userId, Video video) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        Response response;
        video.setUserId(userId);
        video.setVideoId(UUIDs.timeBased());
        UriBuilder builder = uriInfo.getAbsolutePathBuilder();
        try {
            videoDAO.addVideo(video);
            return Response.created(builder.path(userId.toString()).path(video.getVideoId().toString()).build()).build();
        } catch (VideoAlreadyExistsException uae) {
            response = Response.status(Response.Status.CONFLICT).build();
        }
        return response;
    }

    /**
     * Retrieve the video by video ID.
     */
    @GET
    @Path("delete/{videoId}")
    @Produces(MediaType.APPLICATION_JSON)
    public UUID deleteVideo(@PathParam("videoId") UUID videoId) { return videoDAO.deleteVideo(videoId);}

    /**
     * Retrieve the video by video ID.
     */
    @GET
    @Path("{videoId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Video get(@PathParam("videoId") UUID videoId) { return videoDAO.getVideo(videoId);}

    /**
     * Retrieve the most recently uploaded videos.
     */
    @GET
    @Path("recent")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRecentVideos() throws IOException {

        Iterable<Video> videos = videoDAO.getLatestVideos();

        List<Video> allVideos = Lists.newArrayList(videos);

        return Response.ok(jsonMapper.writeValueAsString(allVideos)).build();
    }

    /**
     * Retrieve all videos uploaded with a certain user ID.
     */
    @GET
    @Path("user/{userid}")
    @Produces(MediaType.APPLICATION_JSON)
    @Secured
    public Response getUserVideos(@PathParam("userid") UUID userId) throws IOException {

        Iterable<VideoByUser> videos = videoDAO.getUserVideos(userId);

        List<VideoByUser> allVideos = Lists.newArrayList(videos);

        return Response.ok(jsonMapper.writeValueAsString(allVideos)).build();
    }

    /**
     * Retrieve all videos with a certain tag.
     */
    @GET
    @Path("tag/{tag}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getVideosByTag(@PathParam("tag") String tag) throws IOException {

        Iterable<VideoByTag> videos = videoDAO.getVideosByTag(tag);

        List<VideoByTag> allVideos = Lists.newArrayList(videos);

        return Response.ok(jsonMapper.writeValueAsString(allVideos)).build();
    }

    @PUT
    @Path("tags")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getVideosByTags(@Context UriInfo uriInfo, Set<String> tags) throws IOException {

        UriBuilder builder = uriInfo.getAbsolutePathBuilder();
        Iterable<VideoByTag> videos = videoDAO.getVideosByTags(tags);

        return Response.ok(jsonMapper.writeValueAsString(videos)).build();
        //return Response.ok(builder.path(jsonMapper.writeValueAsString(videos)).build()).build();
    }
}
