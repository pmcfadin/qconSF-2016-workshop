package com.datastax.training.killrvideo.model.dao.cassandra;

import com.datastax.training.killrvideo.model.Video;
import com.datastax.training.killrvideo.model.Facet;
import com.datastax.training.killrvideo.model.dao.AbstractSearchDAO;
import com.datastax.training.killrvideo.util.SearchSession;
import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;
import com.datastax.driver.mapping.Mapper;


public class CassandraVideoSearchDAO implements AbstractSearchDAO<Video> {

    private PreparedStatement searchVideos;
    private Mapper<Video> mapper;
    private String query = "SELECT video_id, title, release_year, genres, tags, description, preview_thumbnail " +
                           "FROM videos where solr_query = ?";

    public CassandraVideoSearchDAO() {

        Session session = SearchSession.getSession();
        searchVideos = session.prepare(query + " LIMIT ?");
        mapper = SearchSession.getMappingManager().mapper(Video.class);

    }

    @Override
    public Iterable<Video> search(String solrPredicate, int limit) {
        Session session = SearchSession.getSession();

        BoundStatement bs = searchVideos.bind(solrPredicate, limit);
        ResultSet resultSet = session.execute(bs);

        return mapper.map(resultSet);
    }

    @Override
    public Iterable<Facet> searchFacet(String solrPredicate) {
        Session session = SearchSession.getSession();

        ResultSet resultSet = session.execute(query, solrPredicate);
        return resultSetToFacetList(resultSet);
    }

}
