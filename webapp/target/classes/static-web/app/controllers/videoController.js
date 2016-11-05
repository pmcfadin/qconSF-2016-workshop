(function() {
    
    var VideoController = function ($scope, $rootScope, $routeParams, $log, $location, $window, videoFactory, commonFactory) {

        var videoId = $routeParams.videoId;

        //Retrieves video info
        function init(s) {
            $log.log("Attempting to retrieve video metadata for " + s);
            videoFactory.getVideo(s)
                .success(function(video) {
                    $log.log("Video "+video.videoId+" retrieved successfully");
                    $scope.video = video;
                })
                .error(commonFactory.evaluateError);
        }
        init(videoId);

        $scope.back = function() {
            $window.history.back();
        }



    };
    
    VideoController.$inject = ['$scope', '$rootScope', '$routeParams', '$log', '$location', '$window', 'videoFactory', 'commonFactory'];

    angular.module('killrVideoApp').controller('VideoController', VideoController);
    
}());