(function () {
    
    var authenticationFactory = function ($http, $log) {

        var factory = {};
        factory.login = function (uname, pwd) {
            return $http({
                method: 'POST',
                url: 'api/killrvideo/authenticate',
                data: $.param({username: uname, password : pwd}),
                headers: {'Content-Type': 'application/x-www-form-urlencoded'}
            });
        };
        return factory;
    };
    
    authenticationFactory.$inject = ['$http','$log'];
    
    angular.module('killrVideoApp')
        .factory('authenticationFactory', authenticationFactory);

}());
