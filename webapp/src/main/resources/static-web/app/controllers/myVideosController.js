(function() {
    
    var MyVideosController = function ($scope, $rootScope, $log, $location, videoFactory, commonFactory) {

        function retrieveVideos(userId) {
            videoFactory.getUserVideos(userId)
                .success(function(videos) {
                    $log.log("Retrieved user's videos successfully");
                    $rootScope.user.videos = $scope.videos = videos;
                })
                .error(commonFactory.evaluateError);
        }

        $scope.createVideo = function(video) {
            videoFactory.createVideo($rootScope.user.userId, video)
                .success(function(data, status, headers, config) {
                    $log.log('Successfully added video');
                    $location.url('/home');
                })
        }

        $scope.deleteVideo = function (videoId) {
            videoFactory.deleteVideo(videoId)
                .success(function(deletedId) {
                    $log.log("Deleted video " + deletedId + " successfully");
                    $location.url('/home');
                })
                .error(commonFactory.evaluateError);
        }

        if (typeof $rootScope.user !== "undefined")
            retrieveVideos($rootScope.user.userId);

    };
    
    MyVideosController.$inject = ['$scope', '$rootScope', '$log', '$location', 'videoFactory', 'commonFactory'];

    angular.module('killrVideoApp')
      .controller('MyVideosController', MyVideosController);
    
}());