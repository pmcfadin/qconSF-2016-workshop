(function() {
    
    var AuthenticationController = function ($scope, $rootScope, $log, $location, $http, authenticationFactory) {

        $scope.ui = {};

        var path = $location.path();
        if(path === '/logout') {
            unsetAuthDetails();
            $location.url('/latest');
        }

        $scope.login = function (uname, password) {

            $log.log("login called....");

            authenticationFactory.login(uname,password)
                                .success(function(data, status, headers, config) {
                                    $log.log("login successful user=" + data);
                                    setAuthDetails(data, headers('authtoken'));
                                    $location.url("/home");
                                })
                                .error(function(data, status, headers, config) {
                                    $log.log(data.error + ' ' + status);
                                    switch (status) {
                                        case 403:
                                            $log.log("Authentication failed...");
                                            $scope.ui.errorMessage = "Incorrect username/password, please try again";
                                            break;
                                        default:
                                            $location.url('/oops');

                                    }
                                });
        };

        function setAuthDetails(user, authtoken) {
            $rootScope.authtoken = authtoken;
            $rootScope.user = user;
            $rootScope.loggedIn = true;
            $http.defaults.headers.common = { 'authtoken' : authtoken }
        }

        function unsetAuthDetails() {
            $rootScope.authtoken = undefined;
            $rootScope.user = undefined;
            $rootScope.loggedIn = undefined;
            $http.defaults.headers.common = { 'authtoken' : undefined }
        }

    };

    AuthenticationController.$inject = ['$scope', '$rootScope', '$log', '$location', '$http', 'authenticationFactory'];

    angular.module('killrVideoApp')
      .controller('AuthenticationController', AuthenticationController);
    
}());