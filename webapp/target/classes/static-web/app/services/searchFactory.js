(function () {

    var API_PREFIX = '/api/killrvideo/';

    var searchFactory = function ($http, $location,$log) {
        
        var factory = {};

/*
        //Retrieve a list of all symbols with minimal info
        factory.getSymbols = function () {
            return $http.get(API_PREFIX+'companies');
        };

        //Retrieves company information based on the specified stock symbol
        factory.getCompanyProfile = function(symbol) {
            return $http.get(API_PREFIX+'companies/'+symbol);
        }
        //Account based purchase or sale of stock
        factory.placeOrder = function (userId, acctId, symbol, volume) {
            return $http.put(API_PREFIX+'transactions/'+userId+'/'+acctId+'/'+symbol+'/'+volume);
        };

        //Retrieve last known price of a specific stock symbol
        factory.getCurrentQuote = function (stockSymbol) {
            return $http.get(API_PREFIX+'quotes/'+stockSymbol);
        }
        //Creates a map of stock symbols for easy retrieval later
        factory.mapSymbolSetToMap = function(symbols) {
            var symbolMap = new Map();
            for (var i = 0, len = symbols.length; i < len; i++) {
                symbolMap.set(symbols[i].stockSymbol, symbols[i]);
            }
            return symbolMap;
        } */

        factory.searchAndFacetField = function(search, facetField) {
            return $http.get(API_PREFIX + 'videosearch/searchfacetfield/' +  encodeURIComponent(search) + '?field=' + facetField )
        }

        factory.searchWithQueryAndFacet = function(queryField, query, facetField) {
            return $http.get(API_PREFIX + 'videosearch/query/'+ queryField +'/'+  encodeURIComponent(query) + '?facetField=' + facetField )
        }

        factory.searchWithQueryFacetAndFilter = function(queryField, query, facetField, filter) {
            return $http.get(API_PREFIX + 'videosearch/query/'+ queryField +'/'+  encodeURIComponent(query) + '?facetField=' + facetField + '&filter='+ encodeURIComponent(filter));
        }

        return factory;
    };
    
    searchFactory.$inject = ['$http','$location','$log'];
    
    angular.module('killrVideoApp')
        .factory('searchFactory', searchFactory);
    
    
}());
