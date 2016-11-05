/*
 * Common functions
 */
(function () {
    
    var commonFactory = function ($http, $log, $location, $window) {

        var factory = {};

        factory.evaluateError = function(data, status, headers, config) {
            switch (status) {
                case 403:
                    $log.log("Authentication failed...");
                    $location.url('/login');
                    break;
                default:
                    $log.log("Error encountered Status Code:"+status);
                    $location.url('/oops');
            }
        }

        factory.goBack = function() {
            $window.history.back();
        }

        return factory;
    };
    
    commonFactory.$inject = ['$http','$log','$location', '$window'];
    
    angular.module('killrVideoApp')
        .factory('commonFactory', commonFactory);

}());
