(function() {

    var VideoSearchController = function ($scope, $rootScope, $routeParams, $log, $location, searchFactory, commonFactory) {

        //default
        $scope.userId = $routeParams.userId;
        $scope.acctId = $routeParams.acctId;
        $scope.facetField = "genres_facet";
        //Set the query field, defaults to all if not available
        var queryField = $scope.queryField = ($routeParams.queryField ? $routeParams.queryField : "all");
        var query = $routeParams.query;
        var queryParams = $location.search();

        searchWithQueryAndFacet = function(queryField, query, facetField) {
            searchFactory.searchWithQueryAndFacet(queryField, query, facetField)
                .success(setSearchResults)
                .error(function(data,status,headers, config) {
                    $log.log("Video Search Facets Failed")
                });
        }

        searchWithQueryFacetAndFilter = function(queryField, query, facetField, filterQuery) {
            searchFactory.searchWithQueryFacetAndFilter(queryField, query, facetField, filterQuery)
                .success(setSearchResults)
                .error(function(data,status,headers, config) {
                    $log.log("Video Search Facets Failed")
                });
        }

        function setSearchResults(searchAndFacets) {
            $scope.facets = searchAndFacets.facets;
            $scope.searchResults = searchAndFacets.searchResults;
            $scope.query = query;
            $scope.queryField = queryField;
        }

        //If a query has been specified then perform a search
        if(query) {
            if(queryParams.filter != undefined) {
                searchWithQueryFacetAndFilter(queryField, query, queryParams.field, queryParams.filter);
            } else {
                searchWithQueryAndFacet(queryField, query, queryParams.field);
            }
        }


    };

    VideoSearchController.$inject = ['$scope', '$rootScope', '$routeParams', '$log', '$location', 'searchFactory', 'commonFactory'];

    angular.module('killrVideoApp')
        .controller('VideoSearchController', VideoSearchController);

}());