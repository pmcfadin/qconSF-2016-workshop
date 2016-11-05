package com.datastax.training.killrvideo.services;

import com.datastax.training.killrvideo.model.Video;
import com.datastax.training.killrvideo.model.Facet;
import com.datastax.training.killrvideo.model.dao.AbstractSearchDAO;
import com.datastax.training.killrvideo.model.dao.cassandra.CassandraVideoSearchDAO;
import com.google.common.collect.Lists;

import javax.ws.rs.*;
import java.io.IOException;

/**
 * Created on 2016-02-12
 *
 * Retrieve results for video search query
 *
 */
@Path("videosearch")
public class SearchService {

    private final AbstractSearchDAO<Video> videoSearchDAO;

    public SearchService() {
        videoSearchDAO = new CassandraVideoSearchDAO();
    }

    class SearchAndFacet {
        private Iterable<Video> searchResults;
        private Iterable<Facet> facets;

        public SearchAndFacet(Iterable<Video> searchResults, Iterable<Facet> facets) {
            this.searchResults = searchResults;
            this.facets = facets;
        }

        public Iterable<Video> getSearchResults() {
            return searchResults;
        }

        public Iterable<Facet> getFacets() {
            return facets;
        }
    }

    // We do it this way, to make it easy to compose the queries
    @GET
    @Path("searchfacetfield/{query}")
    @Produces("application/json")
    public SearchAndFacet searchAndFacetField(@PathParam("query") String queryParam,
            @QueryParam("field") String facetField) throws IOException {

        String query = getQueryString("all", queryParam);

        return new SearchAndFacet(
                Lists.newArrayList(videoSearchDAO.search(query, null, "title ASC", 12)),
                videoSearchDAO.searchFacet(query, null, facetField));
    }

    @GET
    @Path("query/{queryField}/{query}")
    @Produces("application/json")
    public SearchAndFacet searchWithQueryAndFacetField(@PathParam("queryField") String field,
            @PathParam("query") String query, @QueryParam("facetField") String facetField,
            @QueryParam("filter") String filter) {

        String queryString = getQueryString(field, query);
        String filterQuery = ((filter == null) || filter.equals("undefined")) ? null
                : facetField + ":\"" + filter + "\"";

        SearchAndFacet result = new SearchAndFacet(
                Lists.newArrayList(
                        videoSearchDAO.search(queryString, filterQuery, "score DESC", 12)),
                videoSearchDAO.searchFacet(queryString, filterQuery, facetField));

        return result;
    }

    private String getQueryString(String queryField, String query) {

        String queryString = null;

        if (queryField.equalsIgnoreCase("title")) {
            queryString = "title:" + query;
        } else if (queryField.equalsIgnoreCase("description")) {
            queryString = "description:" + query;
        } else if (queryField.equalsIgnoreCase("tags")) {
            queryString = "tags:" + query;
        } else {
            queryString = "title:" + query + " description:" + query + " tags:" + query;
        }

        return queryString;

    }

}
