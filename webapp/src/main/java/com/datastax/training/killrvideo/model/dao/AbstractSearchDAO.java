package com.datastax.training.killrvideo.model.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.datastax.training.killrvideo.model.Facet;
import org.json.JSONStringer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.datastax.driver.core.ColumnDefinitions;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

/**
 * Created on 14/10/2015.
 */

public interface AbstractSearchDAO<T> {

    // Any Search predicate to return a result set of T

    Iterable<T> search(String solrPredicate, int limit);

    // Any Search predicate with a single facet field or a facet queries

    Iterable<Facet> searchFacet(String solrPredicate);

    // Convenience method to search with a column:predicate

    default Iterable<T> search(String column, String predicate, int limit) {
        String solrPredicate = column + ":" + predicate;
        return search(solrPredicate, limit);
    }

    // Method to pass search predicate, filter, sort, and limit

    default Iterable<T> search(String predicate, String filterPredicate, String sort, int limit) {
        String solrPredicate = buildQueryString(predicate, filterPredicate, sort, null, null);
        return search(solrPredicate, limit);
    }

    // Method to facet based predicate, filter predicate, and a single facet
    // field

    default Iterable<Facet> searchFacet(String predicate, String filterPredicate, String facetField) {
        String solrPredicate = buildQueryString(predicate, filterPredicate, null, facetField, null);
        return searchFacet(solrPredicate);
    }

    // Method to facet based predicate, filter predicate, and a single facet
    // field

    default <U extends Object> Iterable<Facet> searchFacetQuery(String predicate, String filterPredicate,
            String facetField, List<U> ranges) {

        List<String> queries = new ArrayList<String>();
        String lastEndPoint = "*";
        for (Object endPoint : ranges) {
            queries.add(mkRangeQuery(facetField, lastEndPoint, endPoint.toString()));
            lastEndPoint = endPoint.toString();
        }
        queries.add(mkRangeQuery(facetField, lastEndPoint, "*"));

        String solrPredicate = buildQueryString(predicate, filterPredicate, null, null, queries);
        return searchFacet(solrPredicate);

    }

    // Method to perform a facet query on ranges expressed as a list

    default String mkRangeQuery(String facetField, String lastEndPoint, String endPoint) {
        return facetField + ":[" + lastEndPoint + " TO " + endPoint + "]";
    }

    // Method to build a json-style query string for Solr

    default String buildQueryString(String predicate, String filterPredicate, String sort, String facetField,
            List<String> facetQueries) {

        // Build something like this

        // {"q":"some query","fq":"some query","sort":"sort exp",
        // "facets":{"fields":["field 1","field 2", ...], "mincount":1}}

        JSONStringer query = new JSONStringer();

        query.object().key("q").value(predicate);

        if (filterPredicate != null) {
            query.key("fq").value(filterPredicate);
        }

        if (sort != null) {
            query.key("sort").value(sort);
        }

        if ((facetField != null) || (facetQueries != null)) {
            query.key("facet").object();

            if (facetField != null) {
                query.key("field").value(facetField);
            }

            if (facetQueries != null) {
                query.key("query").array();
                for (String facetQuery : facetQueries) {
                    query.value(facetQuery);
                }
                query.endArray();
            }

            query.key("mincount").value(1);
            query.endObject();

        }

        query.endObject();

        LogHolder.LOGGER.debug("Query string built \n {}", query.toString());

        return query.toString();
    }

    default Iterable<Facet> resultSetToFacetList(ResultSet resultSet) {
        List<Facet> facets = new ArrayList<>();

        if (resultSet.isExhausted()) {
            return facets;
        }

        Row row = resultSet.one();
        ColumnDefinitions colDefs = row.getColumnDefinitions();

        JsonElement facetObject;
        if (colDefs.contains("facet_fields")) {
            facetObject = new JsonParser().parse(row.getString("facet_fields")).getAsJsonObject().entrySet().iterator()
                    .next().getValue();
        } else if (colDefs.contains("facet_queries")) {
            facetObject = new JsonParser().parse(row.getString("facet_queries"));
        } else {
            throw new RuntimeException("Unrecognised result set from facet query");
        }

        for (Map.Entry<String, JsonElement> entry : facetObject.getAsJsonObject().entrySet()) {
            facets.add(new Facet(entry));
        }

        return facets;
    }

}

/**
 * Use this pattern to allow logging in the default methods of the interface Not
 * really sold on it but seems like a reasonable way to go for now
 */
final class LogHolder { // not public
    static final Logger LOGGER = LoggerFactory.getLogger(AbstractSearchDAO.class);
}
