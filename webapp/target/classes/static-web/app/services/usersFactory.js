(function () {

    var API_PREFIX = '/api/killrvideo/';

    var usersFactory = function ($http, $location,$log) {
        
        var factory = {};

        /* Registration */
        factory.register = function(user) {
            var userJson = JSON.stringify(user);
            $log.log("Registering user " + userJson);
            return $http.put(API_PREFIX+'users',userJson);
        }

        return factory;
    };
    
    usersFactory.$inject = ['$http','$location','$log'];
    
    angular.module('killrVideoApp')
        .factory('usersFactory', usersFactory);
    
    
}());
