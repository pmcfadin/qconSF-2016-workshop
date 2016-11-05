(function() {
    
    var RegistrationController = function ($scope, $rootScope, $log, $location, $http, usersFactory) {
        //Takes a user object
        $scope.register = function(user) {
            usersFactory.register(user)
                .success(function(data, status, headers, config) {
                    $log.log('Successful registration');
                    $location.url('/login');
                })
                .error(function(data,status,headers, config) {
                    $log.log('Registration Failed!');
                    $location.url('/oops');

                });
        }

    };

    RegistrationController.$inject = ['$scope', '$rootScope', '$log', '$location', '$http', 'usersFactory'];

    angular.module('killrVideoApp')
      .controller('RegistrationController', RegistrationController);
    
}());