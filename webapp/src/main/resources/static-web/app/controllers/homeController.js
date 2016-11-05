(function() {

    var HomeController = function ($scope, $rootScope, $log, commonFactory) {

        $scope.user = $rootScope.user;
        if (typeof $scope.user !== "undefined")
            $scope.userAddress = $scope.user.addresses.default;
        $scope.video = $rootScope.video;
    };

    HomeController.$inject = ['$scope', '$rootScope', '$log', 'commonFactory'];

    angular.module('killrVideoApp')
      .controller('HomeController', HomeController);

}());