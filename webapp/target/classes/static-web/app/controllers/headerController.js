(function() {

    var HeaderController = function($scope, $rootScope, $location, $log) {
        $scope.$on('$locationChangeSuccess', function(/* EDIT: remove params for jshint */) {
            $scope.headerUrl = ($rootScope.loggedIn === true) ? 'app/views/authenticated-header.html' : 'app/views/unauthenticated-header.html';
            $scope.loggedIn = $rootScope.loggedIn;
            $scope.user = $rootScope.user;
        });
    }

    HeaderController.$inject = ['$scope', '$rootScope', '$location', '$log'];

    angular.module('killrVideoApp')
        .controller('HeaderController', HeaderController);

}())

