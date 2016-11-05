(function() {

    var LatestVideosController = function ($scope, $rootScope, $routeParams, $log, $location, videoFactory, commonFactory) {

        //Retrieves video info
        function init() {
            $log.log("Attempting to retrieve latest videos");
            videoFactory.getRecentVideos()
                .success(function(videos) {
                    $log.log("Latest videos retrieved successfully");
                    $scope.videos = videos;
                })
                .error(commonFactory.evaluateError);
            }
        init();
    };

    LatestVideosController.$inject = ['$scope', '$rootScope', '$routeParams', '$log', '$location', 'videoFactory', 'commonFactory'];

    angular.module('killrVideoApp')
        .controller('LatestVideosController', LatestVideosController);

}());